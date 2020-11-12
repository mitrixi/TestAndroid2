import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestDevice {

    public final static String DEVICE_IP = "10.10.0.102";
    public final static String SNIFFED_PACKAGES_LOG_FILE = "logs_tshark_packages.txt"; // см. файл tshark_script.sh
    public final static String TSHARK_SCRIPT_FILE = "tshark_script.sh";
    public final static String VITRINA_APP_APK_FILE = "vitrina-app-debug.apk";
    public final static String CONFIG_FILE_URL = "http://10.254.0.131/";

    public final static int SLEEP_TIME = 10;

    private static String epgIP = "";
    private static String restrictionsApiIP = "";

    AndroidDriver<AndroidElement> driver;

    @BeforeClass
    public void preinstallations() throws IOException {

        JSONObject json = UtilTestDevice.readJsonFromUrl(CONFIG_FILE_URL);

        String restrictionsApiUrl = json.getJSONObject("result").getJSONObject("sdk_config").get("restrictions_api_url").toString();
        InetAddress restrictionsInetAddress = InetAddress.getByName(new URL(restrictionsApiUrl).getHost());
        restrictionsApiIP = restrictionsInetAddress.getHostAddress();

        String epgApiUrl = json.getJSONObject("result").getJSONObject("sdk_config").get("epg_api_url").toString();
        InetAddress epgInetAddress = InetAddress.getByName(new URL(epgApiUrl).getHost());
        epgIP = epgInetAddress.getHostAddress();

    }

    @Test
    public void test() throws IOException {
        Runtime.getRuntime().exec(this.getClass().getClassLoader().getResource(TSHARK_SCRIPT_FILE).getPath());

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "android");
        capabilities.setCapability("noReset", true);

        capabilities.setCapability("deviceName", "Xiaomi Redmi 7");
        capabilities.setCapability("udid", "a6eaa0e2");
        capabilities.setCapability("app", "/home/mitrixi/Local_C/IdeaProjects/untitled/src/main/resources/vitrina-app-debug.apk");


        driver = new AndroidDriver<>(new URL("http://10.254.0.131:4723/wd/hub"), capabilities);   //for Jenkins
//        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);      //for local PC
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);


        MobileElement inputField = driver.findElementById("ru.lyubimov.sdktestapp:id/configUrl");
        inputField.sendKeys(CONFIG_FILE_URL);

        MobileElement btnSubmit = driver.findElementById("ru.lyubimov.sdktestapp:id/submit");
        btnSubmit.click();

        try {
            TimeUnit.SECONDS.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(this.getClass().getClassLoader().getResource(SNIFFED_PACKAGES_LOG_FILE).getPath())));

        boolean isStreamStart = false;
        while (reader.ready()) {
            String fileString = reader.readLine();

            if (fileString.contains(DEVICE_IP) && fileString.contains(epgIP) && fileString.contains("Client Hello")) {
                isStreamStart = true;
            }
        }

        assertThat("Отсутствие видеопотока", isStreamStart, equalTo(true));


        String test =
                "    1 0.000000000  10.10.0.102 → 192.168.0.4  DNS 83 Standard query 0x1f3f A youtubei.googleapis.com\n" +
                "    2 2.255402522  10.10.0.102 → 192.168.0.4  DNS 89 Standard query 0x3a3f A connectivitycheck.gstatic.com\n" +
                "    3 2.263425351  10.10.0.102 → 64.233.161.95 TCP 74 38402 → 443 [SYN] Seq=0 Win=65535 Len=0 MSS=1460 SACK_PERM=1 TSval=74902848 TSecr=0 WS=256";
    }

    Pattern pattern = Pattern.compile("");

//    Matcher matcher = pattern.matcher()
}

