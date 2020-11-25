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

public class StreamStartTest {
    public final static String START_STREAM_SERVER_MSG = "Server Hello";
    public final static String START_STREAM_CLIENT_MSG = "Client Hello";
    public final static String[] TEST_STREAM_IP = {"92.223.99.99", "178.176.158.69", "195.161.167.68"}; // СТС ToDo изменить на динамический
    public final static int SLEEP_TIME = 10;

    @Test
    public void isStreamStartTest() throws IOException, InterruptedException {
        // Флаг-заглушка ToDo: должен передаваться из Jenkins
        boolean isIos = true;

        IDevice device = isIos ? new IosDevice() : new AndroidDevice();

        device.stepToConfigUrl();

        // Запускаем tshark, читаем из консоли Stream
        Process tsharkProcess = Runtime.getRuntime().exec(this.getClass().getClassLoader().getResource(device.getTsharkStartFile()).getPath());
        BufferedReader tsharkInputStream = new BufferedReader(new InputStreamReader(tsharkProcess.getInputStream()));

        device.stepOk();

        tsharkProcess.waitFor(SLEEP_TIME, TimeUnit.SECONDS);

        Runtime.getRuntime().exec(this.getClass().getClassLoader().getResource(device.getTsharkStopFile()).getPath());

        boolean isStreamStart = false;
        while (tsharkInputStream.ready()) {
            String fileString = tsharkInputStream.readLine();
            System.out.println(fileString);
            if (fileString.contains(device.getDeviceId()) && Arrays.stream(TEST_STREAM_IP).anyMatch(fileString::contains) && fileString.contains(START_STREAM_SERVER_MSG)) {
                isStreamStart = true;
            }
        }

        assertThat("Видеопоток отсутствует", isStreamStart, equalTo(true));
    }

}
