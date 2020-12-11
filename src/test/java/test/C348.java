package test;

import device.DeviceFactory;
import device.IDevice;
import io.qameta.allure.Step;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static service.ConfigUrl.getRestrictionsPeriodSec;
import static service.TestUtils.*;

public class C348 {
    // ToDo см С122
    public final static String CONFIG_FILE_URL = "http://10.254.0.131/";

    public final static String START_STREAM_SERVER_MSG = "Server Hello";
    public final static String START_STREAM_CLIENT_MSG = "Client Hello";
    public final static String[] TEST_STREAM_IP = {"92.223.99.99", "178.176.158.69", "195.161.167.68"}; // СТС ToDo изменить на динамический
    public final static int SLEEP_TIME_STREAM = 10;

//    @BeforeClass
//    public void preinstallations() throws IOException, InterruptedException {
//        IDevice device = DeviceEnumFactory.getIDeviceByDeviceVersion(System.getenv("deviceType"));
//    }

    @Step(value = "C348")
    @Test(alwaysRun = true)
    public void c348() throws IOException, InterruptedException {

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

//        Process tsharkProcessStream00 = Runtime.getRuntime().exec("/usr/local/bin/tshark");
//        /**/
//        BufferedReader tsharkProcessStreamReader00 = new BufferedReader(new InputStreamReader(tsharkProcessStream00.getInputStream()));
//
//        while (tsharkProcessStreamReader00.ready()) {
//            String strStream = tsharkProcessStreamReader00.readLine();
//            System.out.println(strStream); // for test
//        }
//        /**/

        Process tsharkProcessStream0 = Runtime.getRuntime().exec("ssh -tt mmtr@10.254.7.106 '/usr/local/bin/tshark -Y \"(ip.src != 92.223.99.99 || ip.src == 178.176.158.68 || ip.src == 178.176.158.69 || ip.src == 195.161.167.68 || ip.src == 195.161.167.69) && ip.dst == 10.254.7.106\"'");
        /**/
        BufferedReader tsharkProcessStreamReader0 = new BufferedReader(new InputStreamReader(tsharkProcessStream0.getInputStream()));

        TimeUnit.SECONDS.sleep(2);

        while (tsharkProcessStreamReader0.ready()) {
            String strStream = tsharkProcessStreamReader0.readLine();
            System.out.println(strStream); // for test
        }
        /**/


        Process tsharkProcessStream000 = Runtime.getRuntime().exec("ssh -tt mmtr@10.254.7.106 '/usr/local/bin/tshark -Y \"ip.dst == 10.254.7.106\"'");
        /**/
        BufferedReader tsharkProcessStreamReader000 = new BufferedReader(new InputStreamReader(tsharkProcessStream000.getInputStream()));

        TimeUnit.SECONDS.sleep(2);

        while (tsharkProcessStreamReader000.ready()) {
            String strStream = tsharkProcessStreamReader000.readLine();
            System.out.println(strStream); // for test
        }
        /**/


        Process tsharkProcessStream1 = Runtime.getRuntime().exec("ssh -tt mmtr@10.254.7.106 '/usr/local/bin/tshark'");
        /**/
        BufferedReader tsharkProcessStreamReader1 = new BufferedReader(new InputStreamReader(tsharkProcessStream1.getErrorStream()));

        TimeUnit.SECONDS.sleep(2);

        while (tsharkProcessStreamReader1.ready()) {
            String strStream = tsharkProcessStreamReader1.readLine();
            System.out.println(strStream); // for test
        }
        /**/

        Process tsharkProcessStream2 = Runtime.getRuntime().exec("ssh -t mmtr@10.254.7.106 '/usr/local/bin/tshark'");
        /**/
        BufferedReader tsharkProcessStreamReader2 = new BufferedReader(new InputStreamReader(tsharkProcessStream2.getErrorStream()));

        TimeUnit.SECONDS.sleep(2);

        while (tsharkProcessStreamReader2.ready()) {
            String strStream = tsharkProcessStreamReader2.readLine();
            System.out.println(strStream); // for test
        }
        /**/

        Process tsharkProcessStream3 = Runtime.getRuntime().exec("ssh mmtr@10.254.7.106 '/usr/local/bin/tshark'");
        /**/
        BufferedReader tsharkProcessStreamReader3 = new BufferedReader(new InputStreamReader(tsharkProcessStream3.getErrorStream()));

        TimeUnit.SECONDS.sleep(2);

        while (tsharkProcessStreamReader3.ready()) {
            String strStream = tsharkProcessStreamReader3.readLine();
            System.out.println(strStream); // for test
        }
        /**/

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");



        IDevice device = DeviceFactory.getIDeviceByDeviceVersion(System.getenv("DeviceVersion"));

        int boSuccessCount = 2;
        int secBoReqInterval = getRestrictionsPeriodSec(CONFIG_FILE_URL);
        int secBoReqLag = 2;

        /******** Step 1 ********/

        device.allowBlackout();

        device.stepToConfigUrl(CONFIG_FILE_URL);

        // Запускаем tshark, читаем из консоли Stream
        Process tsharkProcessStream = Runtime.getRuntime().exec(device.getTsharkStartFilePath());
//        BufferedReader tsharkProcessStreamReader = new BufferedReader(new InputStreamReader(tsharkProcessStream.getInputStream()));

        /**/
        BufferedReader tsharkProcessStreamReader = new BufferedReader(new InputStreamReader(tsharkProcessStream.getErrorStream()));

        while (tsharkProcessStreamReader.ready()) {
            String strStream = tsharkProcessStreamReader.readLine();
            System.out.println(strStream); // for test
        }
        /**/

        Process tsharkProcessBlackout = Runtime.getRuntime().exec(device.getTsharkStartBlackout(CONFIG_FILE_URL));
        BufferedReader tsharkProcessBlackoutReader = new BufferedReader(new InputStreamReader(tsharkProcessBlackout.getInputStream()));

        device.stepOk();

        TimeUnit.SECONDS.sleep(SLEEP_TIME_STREAM);

        Runtime.getRuntime().exec("kill -9 " + getPidOfProcess(tsharkProcessStream));

        TimeUnit.SECONDS.sleep(secBoReqInterval * 2 + secBoReqLag);

        Runtime.getRuntime().exec("kill -9 " + getPidOfProcess(tsharkProcessBlackout));
        Runtime.getRuntime().exec(device.getTsharkStopFilePath());

        device.stepCancelStream();

        boolean isStreamStart = isExecOutputContainsMsg(tsharkProcessStreamReader, START_STREAM_SERVER_MSG);

        List<String> blackoutList = new ArrayList<>(); // используется для Step 2
        String boLine;
        while (tsharkProcessBlackoutReader.ready()) {
            boLine = tsharkProcessBlackoutReader.readLine();
            if (boLine.contains(START_STREAM_SERVER_MSG)) {
                blackoutList.add(boLine.trim());
            }
        }

        tsharkProcessBlackoutReader.close();

        boolean existBlackout = !blackoutList.isEmpty();

        assertThat("C348_Step1: Видеопоток ОТСУТСТВУЕТ", isStreamStart, equalTo(true));
        assertThat("C348_Step1: Запрос на restrictions_api_url НЕ отправляется (блэкауты)", existBlackout, equalTo(true));

        /******** Step 2 ********/

        // tmp for test
        System.out.println("Блэкауты:");
        blackoutList.forEach(System.out::println);
        // tmp for test

        // 1) Должно быть еще 2 успешных запроса. (сам факт)
        boolean existTwoSuccessBoReq = false;
        // 2) Запросы отправляются каждые 15 секунд
        boolean isPeriodicityBoReq = false;

        if (!blackoutList.isEmpty()) {
            int firstSecBoReq = getSecFromBoStr(blackoutList.get(0));

            // Иногда пакет блэкаута "задваивается", поэтому последовательно сохранем в LinkedHashSet округляя до целых секунд (иначе считаем, что это два различных пакета)
            Set<Integer> secFromBoStrSet = new LinkedHashSet<>();
            blackoutList.forEach(boString -> {
                int secFromBoStr = getSecFromBoStr(boString);
                if ((secFromBoStr - firstSecBoReq) <= (boSuccessCount * secBoReqInterval + secBoReqLag))
                    secFromBoStrSet.add(secFromBoStr);
            });

            // 1) Анализ наличия еще 2 успешных запроса. (сам факт)
            if (secFromBoStrSet.size() > boSuccessCount)
                existTwoSuccessBoReq = true;

            // 2) Анализируем отправку только boSuccessCount запросов после первого, их периодичность должна быть равна (secBoReqInterval +- secBoReqLag) относительно firstSecBoReq
            isPeriodicityBoReq = true;
            int indexBoString = 0;
            for (int secFromBo : secFromBoStrSet) {
                if (indexBoString > boSuccessCount)
                    break;
                if ((indexBoString > 0 && indexBoString <= boSuccessCount) && !(secFromBo >= (firstSecBoReq + secBoReqInterval * indexBoString - secBoReqLag) && secFromBo <= (firstSecBoReq + secBoReqInterval * indexBoString + secBoReqLag))) {
                    isPeriodicityBoReq = false;
                }
                indexBoString++;
            }
        }

        assertThat("C348_Step2: Ещё двух запросов на блэкауты НЕТ", existTwoSuccessBoReq, equalTo(true));
        assertThat("C348_Step2: Периодичность отправки запросов НАРУШЕНА", isPeriodicityBoReq, equalTo(true));
    }
}

