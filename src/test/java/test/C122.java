package test;

import device.AndroidDevice;
import device.IDevice;
import device.IosDevice;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static test.TestUtils.getPidOfProcess;
import static test.TestUtils.readJsonFromUrl;

public class C122 {
    public final static String CONFIG_FILE_URL = "http://10.254.0.131/";
    public final static String START_STREAM_SERVER_MSG = "Server Hello";
    public final static String START_STREAM_CLIENT_MSG = "Client Hello";

    @Test(alwaysRun = true)
    public void C122() throws IOException, InterruptedException {
        IDevice device = "iPhone".equals(System.getenv("deviceType")) ? new IosDevice() : new AndroidDevice();
        JSONObject jsonConfigFile = readJsonFromUrl(CONFIG_FILE_URL);

        /******** Step 1 ********/

        device.restrictBlackout();

        String urlBlackout = jsonConfigFile.getJSONObject("result").getJSONObject("sdk_config").get("restrictions_api_url").toString();

        JSONObject json3 = readJsonFromUrl(urlBlackout);
        boolean broadcasting_allowed = (boolean) json3.getJSONArray("restrictions").getJSONObject(0).get("broadcasting_allowed");

        assertThat("C122_Step1 По ссылке в параметре конфига restrictions_api_url открывается jsonConfigFile-файл НЕ соответствующий описанию", broadcasting_allowed, equalTo(true));


        /******** Step 2 ********/

        device.stepToConfigUrl(CONFIG_FILE_URL);

        int restrictionsPeriodSec = (int) jsonConfigFile.getJSONObject("results").getJSONObject("sdk_config").get("restrictions_period_sec");

        Process tsharkProcessStream = Runtime.getRuntime().exec(device.getTsharkStartFilePath());
        BufferedReader tsharkProcessStreamReader = new BufferedReader(new InputStreamReader(tsharkProcessStream.getInputStream()));

        System.out.println("!!!!!!!!!!!!!!!!!!");

        device.stepOk();

        TimeUnit.SECONDS.sleep(restrictionsPeriodSec * 4);

        Runtime.getRuntime().exec("kill -9 " + getPidOfProcess(tsharkProcessStream));

        boolean isStreamStart = false;
        String strStream;
        while (tsharkProcessStreamReader.ready()) {
            strStream = tsharkProcessStreamReader.readLine();
            System.out.println(strStream);
            if (strStream.contains(START_STREAM_SERVER_MSG)) {
                isStreamStart = true;
                break;
            }
        }

//        Process tsharkProcessBlackout = Runtime.getRuntime().exec(device.getTsharkStartBlackout(CONFIG_FILE_URL));
//        BufferedReader tsharkProcessBlackoutReader = new BufferedReader(new InputStreamReader(tsharkProcessBlackout.getInputStream()));
//
//        boolean existBlackout = false;
//        if (tsharkProcessBlackoutReader.ready()) {
//            existBlackout = true;
//        }

        boolean seeBlackout = device.seeBlackout();

        assertThat("C122_Step2: Видеопоток отсутствует", isStreamStart, equalTo(true));
        assertThat("C122_Step2: Блэкаут НЕ виден", seeBlackout, equalTo(false));

        /******** Step 3 ********/

        device.allowBlackout();


    }
}
