package test;


import com.google.inject.Guice;
import device.AndroidDevice;
import device.IDevice;
import device.IosDevice;
import io.qameta.allure.Step;
import org.json.JSONObject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Singleton;
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
    IDevice device;

    @Step(value = "C122")
    @Test
    public void c122() throws IOException, InterruptedException {
//        device = "iPhone".equals(System.getenv("deviceType")) ? IosDevice.INSTANCE : AndroidDevice.INSTANCE;
        device = "iPhone".equals(System.getenv("deviceType")) ? new IosDevice() : new AndroidDevice();


        /******** Step 1 ********/
        device.restrictBlackout();

        //пауза для возможности отработать команде по ssh
        TimeUnit.SECONDS.sleep(5);

        JSONObject jsonConfigFile = readJsonFromUrl(CONFIG_FILE_URL);

        String urlBlackout = jsonConfigFile.getJSONObject("result").getJSONObject("sdk_config").get("restrictions_api_url").toString();

        JSONObject jsonBlackout = readJsonFromUrl(urlBlackout);
        boolean broadcasting_allowed = Boolean.parseBoolean(jsonBlackout.getJSONArray("restrictions").getJSONObject(0).get("broadcasting_allowed").toString());

        System.out.println(broadcasting_allowed);
        assertThat("C122_Step1 По ссылке в параметре конфига restrictions_api_url открывается jsonConfigFile-файл НЕ соответствующий описанию", broadcasting_allowed, equalTo(true));


        /******** Step 2 ********/

        device.stepToConfigUrl(CONFIG_FILE_URL);

        int restrictionsPeriodSec = Integer.parseInt(jsonConfigFile.getJSONObject("result").getJSONObject("sdk_config").get("restrictions_period_sec").toString());

        Process tsharkProcessStream = Runtime.getRuntime().exec(device.getTsharkStartFilePath());
        BufferedReader tsharkProcessStreamReader = new BufferedReader(new InputStreamReader(tsharkProcessStream.getInputStream()));

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

        if (!isStreamStart) System.out.println("C122_Step2: Видеопоток отсутствует");
        assertThat("C122_Step2: Видеопоток отсутствует", isStreamStart, equalTo(true));
        if (seeBlackout) System.out.println("C122_Step2: Блэкаут виден");
        assertThat("C122_Step2: Блэкаут виден", seeBlackout, equalTo(false));

        /******** Step 3 ********/

        device.allowBlackout();

        device.stepToConfigUrl(CONFIG_FILE_URL);

        int restrictionsPeriodSecStep3 = Integer.parseInt(jsonConfigFile.getJSONObject("result").getJSONObject("sdk_config").get("restrictions_period_sec").toString());

        Process tsharkProcessStreamStep3 = Runtime.getRuntime().exec(device.getTsharkStartFilePath());
        BufferedReader tsharkProcessStreamReaderStep3 = new BufferedReader(new InputStreamReader(tsharkProcessStreamStep3.getInputStream()));

        device.stepOk();

        TimeUnit.SECONDS.sleep(restrictionsPeriodSecStep3 * 2);

        boolean isBoOnScreenShot = device.isBoOnScreenShot(); // Блэкаут в приложении должен запуститься не позднее чем через <restrictions_period_sec>x2 секунд после перезапуска приложения.

        Runtime.getRuntime().exec("kill -9 " + getPidOfProcess(tsharkProcessStreamStep3));
        Runtime.getRuntime().exec(device.getTsharkStopFilePath());

        device.stepCancelStream();

        boolean isStreamStartStep3 = false;
        String strStreamStep3;
        while (tsharkProcessStreamReaderStep3.ready()) {
            strStreamStep3 = tsharkProcessStreamReaderStep3.readLine();
            System.out.println(strStreamStep3);
            if (strStreamStep3.contains(START_STREAM_SERVER_MSG)) {
                isStreamStartStep3 = true;
                break;
            }
        }

        if (!isStreamStartStep3) System.out.println("C122_Step3: Видеопоток отсутствует");
        assertThat("C122_Step3: Видеопоток отсутствует", isStreamStartStep3, equalTo(true));
        if (!isBoOnScreenShot) System.out.println("C122_Step3: Поверх видеотрансляции НЕ выводится заглушка блэкаута");
        assertThat("C122_Step3: Поверх видеотрансляции НЕ выводится заглушка блэкаута", isBoOnScreenShot, equalTo(true));
    }
}