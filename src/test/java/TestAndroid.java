import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestAndroid {

    public final static String DEVICE_IP = "10.10.0.102";
    public final static String logFileName = "logs_tshark_packages.txt";


    AndroidDriver<AndroidElement> driver;

    @Test
    public void test() throws IOException {

        String configUrl = "http://10.254.0.131/";
        String restrictionsApiHostAddress = "";
        String epgHostAddress = "";

        JSONObject json = Util.readJsonFromUrl("http://10.254.0.131/");
        String restrictionsApiUrl = json.getJSONObject("result").getJSONObject("sdk_config").get("restrictions_api_url").toString();

        try {
            InetAddress ia = InetAddress.getByName(new URL(restrictionsApiUrl).getHost());
            restrictionsApiHostAddress = ia.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        String epgApiUrl = json.getJSONObject("result").getJSONObject("sdk_config").get("epg_api_url").toString();

        try {
            InetAddress ia = InetAddress.getByName(new URL(restrictionsApiUrl).getHost());
            epgHostAddress = ia.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

//        Runtime.getRuntime().exec("sudo tshark -Y \"tcp.stream eq 3\" > /home/mitrixi/Local_C/IdeaProjects/VitrinaTV_Project/src/test/resources/logs_tshark_packages.txt");
        Runtime.getRuntime().exec("src/test/resources/tshark_script.sh");
//        Runtime.getRuntime().exec("touch /home/mitrixi/Local_C/IdeaProjects/VitrinaTV_Project/src/test/resources/logs_tshark_packages.txt");








        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "android");
        capabilities.setCapability("noReset", true);

        capabilities.setCapability("deviceName", "Xiaomi Redmi 7");
        capabilities.setCapability("udid", "a6eaa0e2");
        capabilities.setCapability("app", "/home/mitrixi/Local_C/IdeaProjects/untitled/src/main/resources/vitrina-app-debug.apk");
//        capabilities.setCapability("app","/opt/vitrina-app-debug.apk");


//        driver = new AndroidDriver<>(new URL("http://10.254.0.131:4723/wd/hub"), capabilities);   //for Jenkins
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);      //for local PC
//        driver = new AndroidDriver<>(new URL("http://192.168.99.103:4723/wd/hub"), capabilities);
//        driver = new AndroidDriver<>(new URL("http://192.168.99.100:2376/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);


        MobileElement inputField = driver.findElementById("ru.lyubimov.sdktestapp:id/configUrl");
        inputField.sendKeys(configUrl);

        MobileElement btnSubmit = driver.findElementById("ru.lyubimov.sdktestapp:id/submit");
        btnSubmit.click();



        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean isStreamStart = false;

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(this.getClass().getClassLoader().getResource(logFileName).getPath())));

        String fileString = "";
        while (reader.ready()) {
            fileString = reader.readLine();

            if (fileString.contains(DEVICE_IP) && fileString.contains(epgHostAddress) && fileString.contains("Client Hello")) {
                isStreamStart = true;
            }
        }


        assertThat("Отсутствие видеопотока", isStreamStart, equalTo(true));


    }


}

