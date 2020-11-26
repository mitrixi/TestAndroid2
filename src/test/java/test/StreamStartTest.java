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

public class StreamStartTest {
    public final static String START_STREAM_SERVER_MSG = "Server Hello";
    public final static String START_STREAM_CLIENT_MSG = "Client Hello";
    public final static String[] TEST_STREAM_IP = {"92.223.99.99", "178.176.158.69", "195.161.167.68"}; // СТС ToDo изменить на динамический
    public final static int SLEEP_TIME = 10;
    public final static String BLACKOUTS_IP = "151.236.95.210";

    @Test
    public void isStreamStartTest() throws IOException, InterruptedException {
        // Флаг-заглушка ToDo: должен передаваться из Jenkins

        AndroidDevice device = new AndroidDevice();
//        IosDevice device = new IosDevice();

        device.stepToConfigUrl();

        // Запускаем tshark, читаем из консоли Stream
        Process tsharkProcessStream = Runtime.getRuntime().exec(device.getTsharkStartFilePath());
        BufferedReader tsharkProcessStreamReader = new BufferedReader(new InputStreamReader(tsharkProcessStream.getInputStream()));

        Process tsharkProcessBlackout = Runtime.getRuntime().exec(device.startBlackoutSniffing());
        BufferedReader tsharkProcessBlackoutReader = new BufferedReader(new InputStreamReader(tsharkProcessBlackout.getInputStream()));

        device.stepOk();

        TimeUnit.SECONDS.sleep(15);

        Runtime.getRuntime().exec("kill -9 " + getPidOfProcess(tsharkProcessStream));

        TimeUnit.SECONDS.sleep(10);


        Runtime.getRuntime().exec("kill -9 " + getPidOfProcess(tsharkProcessBlackout));
//        Runtime.getRuntime().exec(device.getTsharkStopFilePath());

        boolean isStreamStart = false;
        String strStream;
        while (tsharkProcessStreamReader.ready()) {
            System.out.println("Внутри while потока");
            strStream = tsharkProcessStreamReader.readLine();
            if (strStream.contains(device.getIP()) && Arrays.stream(TEST_STREAM_IP).anyMatch(strStream::contains) && strStream.contains(START_STREAM_SERVER_MSG)) {
                System.out.println(strStream);
                isStreamStart = true;
                break;
            }
        }

        boolean existBlackout = false;
        String strBlackout;
        while (tsharkProcessBlackoutReader.ready()) {
            System.out.println("Внутри while блэкаутов");
            strBlackout = tsharkProcessBlackoutReader.readLine();
            System.out.println(strBlackout);
            if (strBlackout.contains(device.getIP()) && strBlackout.contains(BLACKOUTS_IP) && strBlackout.contains(START_STREAM_SERVER_MSG)) {
                existBlackout = true;
                break;
            }
        }

        assertThat("Видеопоток отсутствует", isStreamStart, equalTo(true));
        assertThat("Блэкаут отсутствует", existBlackout, equalTo(true));

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

