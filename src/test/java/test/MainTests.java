package test;

import device.AndroidDevice;
import device.IDevice;
import device.IosDevice;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static test.TestUtils.getPidOfProcess;

public class MainTests {
    public final static String CONFIG_FILE_URL = "http://10.254.0.131/";
    public final static String START_STREAM_SERVER_MSG = "Server Hello";
    public final static String START_STREAM_CLIENT_MSG = "Client Hello";
    public final static String[] TEST_STREAM_IP = {"92.223.99.99", "178.176.158.69", "195.161.167.68"}; // СТС ToDo изменить на динамический
    public final static int SLEEP_TIME_STREAM = 10;
    public final static int SLEEP_TIME_BLACKOUT = 40;

    @Test
    public void C348() throws IOException, InterruptedException {
        IDevice device = "iPhone".equals(System.getenv("deviceType")) ? new IosDevice() : new AndroidDevice();

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

        Runtime.getRuntime().exec("kill -9 " + getPidOfProcess(tsharkProcessBlackout));
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

        List<String> blackoutList = new ArrayList<>(); // используется для Step 2
        while (tsharkProcessBlackoutReader.ready()) {
            blackoutList.add(tsharkProcessBlackoutReader.readLine());
        }

        boolean existBlackout = false;
        for (String bo: blackoutList) {
            if (bo.contains(START_STREAM_SERVER_MSG)) {
                existBlackout = true;
            }
        }

        assertThat("C348_Step1: Видеопоток отсутствует", isStreamStart, equalTo(true));
        assertThat("C348_Step1: Запрос на restrictions_api_url не отправляется (блэкауты)", existBlackout, equalTo(true));

        /******** Step 2 ********/

//        String patternServerHello = "\\s*(\\d+)\\s(\\d+\\.\\d{9}).+(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}).+(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}).*\\bServer Hello\\b.*";
//        String blackouts = "\\\\\\\\";

//        boolean existBlackout = false;
//        String s = "";
//        long startTimeStream = 0;
//        long finishTimeStream = 0;
//        int rerunBlackoutCount = 0;
//        while (rerunBlackoutCount < 4) {
//            s = tsharkProcessStreamReader.readLine();
//            if (s == null)
//                break;
//            else if (s.contains(device.getIP()) && Arrays.stream(TEST_STREAM_IP).anyMatch(s::contains) && s.contains(START_STREAM_SERVER_MSG)) {
//                existBlackout = true;
//            } else if (s.contains(device.getIP()) && s.contains(BLACKOUTS_IP)) {
//                rerunBlackoutCount++;
//                if (rerunBlackoutCount < 1)
//                    startTimeStream = System.nanoTime() / 1000000000;
//                else if (rerunBlackoutCount == 3)
//                    finishTimeStream = System.nanoTime() / 1000000000;
//            }
//        }

//        assertThat("Отсутствие видеопотока", existBlackout, equalTo(true));
//        assertThat("Отправка запросов каждые 15 секунд", finishTimeStream - startTimeStream, equalTo(30));
    }
}

