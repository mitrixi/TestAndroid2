package test;

import device.AndroidDevice;
import device.IDevice;
import device.IosDevice;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static test.TestUtils.getPidOfProcess;

public class MainTests {
    public final static String CONFIG_FILE_URL = "http://10.254.0.131/";
    public final static String START_STREAM_SERVER_MSG = "Server Hello";
    public final static int SLEEP_TIME_STREAM = 10;
    public final static int SLEEP_TIME_BLACKOUT = 40;

    @Test
    public void C348() throws IOException, InterruptedException {
        boolean isIos = "iPhone".equals(System.getenv("deviceType")) ? true : false;

        IDevice device = isIos ? new IosDevice() : new AndroidDevice();

        /******** Step 1 ********/

        device.stepToConfigUrl(CONFIG_FILE_URL);

        // Запускаем tshark, читаем из консоли Stream
        Process tsharkProcessStream = Runtime.getRuntime().exec(device.getTsharkStartFilePath());
        BufferedReader tsharkProcessStreamReader = new BufferedReader(new InputStreamReader(tsharkProcessStream.getInputStream()));

//        Process tsharkProcessBlackout = Runtime.getRuntime().exec("ssh -tt mmtr@10.254.7.106 '/usr/local/bin/tshark -Y \"tls.handshake.session_id && ip.dst == 10.254.7.106 && (ip.src == 193.27.224.36 || ip.src == 193.27.224.37 || ip.src == 193.27.224.38)\"'");
        Process tsharkProcessBlackout = Runtime.getRuntime().exec(device.getTsharkStartBlackout(CONFIG_FILE_URL));
        BufferedReader tsharkProcessBlackoutReader = new BufferedReader(new InputStreamReader(tsharkProcessBlackout.getInputStream()));

        device.stepOk();

        TimeUnit.SECONDS.sleep(SLEEP_TIME_STREAM);

        Runtime.getRuntime().exec("kill -9 " + getPidOfProcess(tsharkProcessStream));

        TimeUnit.SECONDS.sleep(SLEEP_TIME_BLACKOUT);

//        Runtime.getRuntime().exec("kill -9 " + getPidOfProcess(tsharkProcessBlackout));
        Runtime.getRuntime().exec(device.getTsharkStopFilePath());

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

        boolean existBlackout = false;
        String timeFirstAppearanceBlackout;
        String strBlackout = "";
        while (tsharkProcessBlackoutReader.ready()) {
            strBlackout = tsharkProcessBlackoutReader.readLine();
            System.out.println(strBlackout);
            if (strBlackout.contains(START_STREAM_SERVER_MSG)) {
                existBlackout = true;
                timeFirstAppearanceBlackout = strBlackout.split(" ")[1].split("\\.")[0];
                break;
            }
        }

        assertThat("C348_Step1: Видеопоток отсутствует", isStreamStart, equalTo(true));
        assertThat("C348_Step1: Запрос на restrictions_api_url не отправляется (блэкауты)", existBlackout, equalTo(true));

        /******** Step 2 ********/

        boolean existBlackout2 = false;
        String s;
        int rerunBlackoutCount = 0;


        while (tsharkProcessBlackoutReader.ready()) {
            s = tsharkProcessBlackoutReader.readLine();
            System.out.println(s);
//            if (s == null) {
//                break;
//            } else if (s.contains(device.getDeviceIp()) && s.contains(BLACKOUTS_IP)) {
//
//
//            }
        }

//        assertThat("Отсутствие видеопотока", existBlackout2, equalTo(true));
//        assertThat("Отправка запросов каждые 15 секунд", finishTimeStream - startTimeStream, equalTo(30));
    }
}

