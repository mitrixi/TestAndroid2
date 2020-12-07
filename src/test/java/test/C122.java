package test;

import device.DeviceFactory;
import device.IDevice;
import io.qameta.allure.Step;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static service.ConfigUrl.getRestrictionsPeriodSec;
import static service.ConfigUrl.isBroadcastingAllowed;
import static service.TestUtils.isExecOutputContainsMsg;

public class C122 {
    // ToDo ссылка должна быть статичной, что-то вроде http://10.254.0.131/C122/Step1 и ещё одна http://10.254.0.131/C122/Step3
    //      методы device.restrictBlackout() и device.allowBlackout() НЕ НУЖНЫ
    //      ещё можно динамически генерировать конфиг передавая параметры, например http://10.254.0.131/C122/Step1?restrictionsPeriodSec=10
    public final static String CONFIG_FILE_URL = "http://10.254.0.131/";

    public final static String START_STREAM_SERVER_MSG = "Server Hello";
    public final static String START_STREAM_CLIENT_MSG = "Client Hello";

    @Step(value="C122")
    @Test(alwaysRun = true)
    public void c122() throws IOException, InterruptedException {
        IDevice device = DeviceFactory.getIDeviceByDeviceVersion(System.getenv("DeviceVersion"));

        /******** Step 1 ********/

        device.restrictBlackout();

        boolean isBroadcastingAllowed = isBroadcastingAllowed(CONFIG_FILE_URL);

        assertThat("C122_Step1 По ссылке в параметре конфига restrictions_api_url открывается jsonConfigFile-файл НЕ соответствующий описанию", isBroadcastingAllowed, equalTo(true));

        /******** Step 2 ********/

        device.stepToConfigUrl(CONFIG_FILE_URL);

        int restrictionsPeriodSec = getRestrictionsPeriodSec(CONFIG_FILE_URL);

        Process tsharkProcessStream = Runtime.getRuntime().exec(device.getTsharkStartFilePath());
        BufferedReader tsharkProcessStreamReader = new BufferedReader(new InputStreamReader(tsharkProcessStream.getInputStream()));

        device.stepOk();

        // Подождать <restrictions_period_sec>x4 секунд.
        TimeUnit.SECONDS.sleep(restrictionsPeriodSec * 2);
        boolean isBoOnScreenShotStep2 = device.isBoOnScreenShot(); // Блэкаут в приложении должен запуститься не позднее чем через <restrictions_period_sec>x2 секунд (после перезапуска приложения)
        TimeUnit.SECONDS.sleep(restrictionsPeriodSec * 2);

//        Runtime.getRuntime().exec("kill -9 " + getPidOfProcess(tsharkProcessStream));
        Runtime.getRuntime().exec(device.getTsharkStopFilePath());

        device.stepCancelStream();

        boolean isStreamStart = isExecOutputContainsMsg(tsharkProcessStreamReader, START_STREAM_SERVER_MSG);

        assertThat("C122_Step2: Видеопоток отсутствует", isStreamStart, equalTo(true));
        assertThat("C122_Step2: Блэкаут виден", isBoOnScreenShotStep2, equalTo(false));

        /******** Step 3 ********/

        device.allowBlackout();

        device.stepToConfigUrl(CONFIG_FILE_URL);

        int restrictionsPeriodSecStep3 = getRestrictionsPeriodSec(CONFIG_FILE_URL);

        Process tsharkProcessStreamStep3 = Runtime.getRuntime().exec(device.getTsharkStartFilePath());
        BufferedReader tsharkProcessStreamReaderStep3 = new BufferedReader(new InputStreamReader(tsharkProcessStreamStep3.getInputStream()));

        device.stepOk();

        TimeUnit.SECONDS.sleep(restrictionsPeriodSecStep3 * 2);

        boolean isBoOnScreenShotStep3 = device.isBoOnScreenShot(); // Блэкаут в приложении должен запуститься не позднее чем через <restrictions_period_sec>x2 секунд (после перезапуска приложения)

//        Runtime.getRuntime().exec("kill -9 " + getPidOfProcess(tsharkProcessStreamStep3));
        Runtime.getRuntime().exec(device.getTsharkStopFilePath());

        device.stepCancelStream();

        boolean isStreamStartStep3 = isExecOutputContainsMsg(tsharkProcessStreamReaderStep3, START_STREAM_SERVER_MSG);

        assertThat("C122_Step3: Видеопоток отсутствует", isStreamStartStep3, equalTo(true));
        assertThat("C122_Step3: Поверх видеотрансляции НЕ выводится заглушка блэкаута", isBoOnScreenShotStep3, equalTo(true));
    }
}
