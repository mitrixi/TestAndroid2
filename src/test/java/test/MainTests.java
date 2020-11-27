package test;

import device.AndroidDevice;
import device.IDevice;
import device.IosDevice;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
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

        List<String> blackoutList = new ArrayList<>(); // используется для Step 2
        while (tsharkProcessBlackoutReader.ready()) {
            blackoutList.add(tsharkProcessBlackoutReader.readLine());
        }

        boolean existBlackout = false;
        if (!blackoutList.isEmpty()) {
            for (String boString : blackoutList) {
                if (boString.contains(START_STREAM_SERVER_MSG)) {
                    existBlackout = true;
                    break;
                }
            }
        }

        assertThat("C348_Step1: Видеопоток отсутствует", isStreamStart, equalTo(true));
        assertThat("C348_Step1: Запрос на restrictions_api_url НЕ отправляется (блэкауты)", existBlackout, equalTo(true));

        /******** Step 2 ********/

        // Должно быть еще 2 успешных запроса. (сам факт)
        boolean existTwoSuccessBoReq = false;

        int boSuccessCount = 2;
        int secBoReqInterval = 15;
        int secBoReqLag = 2;

        if (!blackoutList.isEmpty()) {
            int firstSecBoReq = getSecFromBoStr(blackoutList.get(0));
            Set<Integer> secFromBoStrSet = new HashSet<Integer>();
            for (String boString : blackoutList) {
                int secFromBoStr = getSecFromBoStr(boString);
                if ((secFromBoStr - firstSecBoReq) <= (boSuccessCount * secBoReqInterval + secBoReqLag))
                    secFromBoStrSet.add(secFromBoStr);
            }
            if (secFromBoStrSet.size() > 2)
                existTwoSuccessBoReq = true;
        }

        assertThat("C348_Step2: Ещё двух запросов на блэкауты НЕТ", existTwoSuccessBoReq, equalTo(true));
    }

    private int getSecFromBoStr(String s) {
        return Math.round(Float.parseFloat(s.split(" +")[1]));
    }
}

