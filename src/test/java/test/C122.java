package test;

import device.AndroidDevice;
import device.IDevice;
import device.IosDevice;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    public void c122() throws IOException, InterruptedException {
        IDevice device = "iPhone".equals(System.getenv("deviceType")) ? IosDevice.INSTANCE : AndroidDevice.INSTANCE;
        JSONObject jsonConfigFile = readJsonFromUrl(CONFIG_FILE_URL);

        /******** Step 1 ********/

        device.restrictBlackout();

        String urlBlackout = jsonConfigFile.getJSONObject("result").getJSONObject("sdk_config").get("restrictions_api_url").toString();

        JSONObject jsonBlackout = readJsonFromUrl(urlBlackout);
        boolean broadcasting_allowed = Boolean.parseBoolean(jsonBlackout.getJSONArray("restrictions").getJSONObject(0).get("broadcasting_allowed").toString());

        assertThat("C122_Step1 По ссылке в параметре конфига restrictions_api_url открывается jsonConfigFile-файл НЕ соответствующий описанию", broadcasting_allowed, equalTo(true));


        /******** Step 2 ********/

        device.stepToConfigUrl(CONFIG_FILE_URL);

        int restrictionsPeriodSec = Integer.parseInt(jsonConfigFile.getJSONObject("result").getJSONObject("sdk_config").get("restrictions_period_sec").toString());

        Process tsharkProcessStream = Runtime.getRuntime().exec(device.getTsharkStartFilePath());
        BufferedReader tsharkProcessStreamReader = new BufferedReader(new InputStreamReader(tsharkProcessStream.getInputStream()));

        device.stepOk();

        TimeUnit.SECONDS.sleep(restrictionsPeriodSec * 4);

        Runtime.getRuntime().exec("kill -9 " + getPidOfProcess(tsharkProcessStream));
        Runtime.getRuntime().exec(device.getTsharkStopFilePath());

        device.stepCancelStream();

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

        device.stepToConfigUrl(CONFIG_FILE_URL);

        restrictionsPeriodSec = Integer.parseInt(jsonConfigFile.getJSONObject("result").getJSONObject("sdk_config").get("restrictions_period_sec").toString());

        tsharkProcessStream = Runtime.getRuntime().exec(device.getTsharkStartFilePath());
        tsharkProcessStreamReader = new BufferedReader(new InputStreamReader(tsharkProcessStream.getInputStream()));

        device.stepOk();

        TimeUnit.SECONDS.sleep(restrictionsPeriodSec * 2);

        boolean isBoOnScreenShot = device.isBoOnScreenShot(); // Блэкаут в приложении должен запуститься не позднее чем через <restrictions_period_sec>x2 секунд после перезапуска приложения.

        Runtime.getRuntime().exec("kill -9 " + getPidOfProcess(tsharkProcessStream));
        Runtime.getRuntime().exec(device.getTsharkStopFilePath());

        device.stepCancelStream();

        isStreamStart = false;
        while (tsharkProcessStreamReader.ready()) {
            strStream = tsharkProcessStreamReader.readLine();
            System.out.println(strStream);
            if (strStream.contains(START_STREAM_SERVER_MSG)) {
                isStreamStart = true;
                break;
            }
        }

        assertThat("C122_Step3: Видеопоток отсутствует", isStreamStart, equalTo(true));
        assertThat("C122_Step3: Поверх видеотрансляции НЕ выводится заглушка блэкаута", isBoOnScreenShot, equalTo(true));
    }
}
