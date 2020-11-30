//package test;
//
//import io.appium.java_client.AppiumDriver;
//import io.appium.java_client.MobileElement;
//import io.appium.java_client.android.AndroidDriver;
//import io.appium.java_client.ios.IOSDriver;
//import org.json.JSONObject;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.InetAddress;
//import java.net.URL;
//import java.util.Arrays;
//import java.util.concurrent.TimeUnit;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.equalTo;
//import static test.TestUtils.readJsonFromUrl;
//
//public class TestDevice {
//    // Android
//    public final static String ANDR_TSHARK_START_SCRIPT_FILE = "andr_tshark_start_script.sh";
//    public final static String ANDR_TSHARK_STOP_SCRIPT_FILE = "andr_tshark_stop_script.sh";
////    public final static String ANDR_TSHARK_KILL_SCRIPT = "sudo killall tshark";
//    public final static String ANDR_DEVICE_IP = "10.10.0.102";
//    public final static String ANDR_CONFIG_FILE_URL = "http://10.254.0.131/";
//    public final static String ANDR_VITRINA_APP_APK_FILE = "vitrina-app-debug.apk";
////    public final static String ANDR_SSH_TSHARK = "ssh root@10.254.0.131 '/usr/bin/tshark'";
//
//    //iOS
//    public final static String IOS_TSHARK_START_SCRIPT_FILE = "tshark_start_script.sh"; // вывод для консоли
//    public final static String IOS_TSHARK_STOP_SCRIPT_FILE = "tshark_stop_script.sh";
////    public final static String IOS_TSHARK_KILL_SCRIPT = "killall tshark";
//    public final static String IOS_DEVICE_IP = "10.254.7.106";  // см. файл tshark_start_script.sh
////    public final static String IOS_STREAM_IP = "92.223.99.99";  // см. файл tshark_start_script.sh
//    public final static String IOS_CONFIG_FILE_URL = "https://stage.mediavitrina.ru/testdata/1227";
//
//    public final static String START_STREAM_SERVER_MSG = "Server Hello";
//    public final static String START_STREAM_CLIENT_MSG = "Client Hello";
//
//    public final static String[] TEST_STREAM_IP = {"92.223.99.99", "178.176.158.69", "195.161.167.68"}; // СТС ToDo изменить на динамический
//
//    public final static int SLEEP_TIME = 10;
//
//    private static String epgIP = "";
//    private static String restrictionsApiIP = "";
//
//    AppiumDriver<WebElement> driver;
//
//    @BeforeClass
//    public void preinstallations() throws IOException {
//        JSONObject json = readJsonFromUrl(ANDR_CONFIG_FILE_URL);
//
//        String restrictionsApiUrl = json.getJSONObject("result").getJSONObject("sdk_config").get("restrictions_api_url").toString();
//        InetAddress restrictionsInetAddress = InetAddress.getByName(new URL(restrictionsApiUrl).getHost());
//        restrictionsApiIP = restrictionsInetAddress.getHostAddress();
//
//        String epgApiUrl = json.getJSONObject("result").getJSONObject("sdk_config").get("epg_api_url").toString();
//        InetAddress epgInetAddress = InetAddress.getByName(new URL(epgApiUrl).getHost());
//        epgIP = epgInetAddress.getHostAddress();
//    }
//
//    @Test(enabled = false)
//    public void isStreamStartTest() throws IOException, InterruptedException {
//        // Флаг-заглушка ToDo: должен передаваться из Jenkins
//        boolean isIos = true;
//
//        initDriver(isIos);
//        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//
//        stepToConfigUrl(isIos);
//
//        // Запускаем tshark, читаем из консоли Stream
//        String tsharkStartScript = isIos ? IOS_TSHARK_START_SCRIPT_FILE : ANDR_TSHARK_START_SCRIPT_FILE;
//        Process tsharkProcess = Runtime.getRuntime().exec(this.getClass().getClassLoader().getResource(tsharkStartScript).getPath());
//        BufferedReader tsharkInputStream = new BufferedReader(new InputStreamReader(tsharkProcess.getInputStream()));
//
//        stepOk(isIos);
//
//        tsharkProcess.waitFor(SLEEP_TIME, TimeUnit.SECONDS);
//
//        String tsharkStopScript = isIos ? IOS_TSHARK_STOP_SCRIPT_FILE : ANDR_TSHARK_STOP_SCRIPT_FILE;
//        Runtime.getRuntime().exec(this.getClass().getClassLoader().getResource(tsharkStopScript).getPath());
//
////        boolean isStreamStart = false;
////        while (tsharkInputStream.ready()) {
////            String fileString = tsharkInputStream.readLine();
////            System.out.println(fileString);
////            if (isStreamStartCheck(isIos, fileString)) {
////                isStreamStart = true;
////            }
////        }
////
////        assertThat("Видеопоток отсутствует", isStreamStart, equalTo(true));
//
//
//    }
//
//    private void initDriver(boolean isIos) {
//        try {
//            if (isIos) {
//                driver = new IOSDriver<>(new URL("http://10.254.7.106:4723/wd/hub"), fillCapabilities(isIos));
//            } else {
//                driver = new AndroidDriver<>(new URL("http://10.254.0.131:4723/wd/hub"), fillCapabilities(isIos));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private DesiredCapabilities fillCapabilities(boolean isIos) {
//        DesiredCapabilities capabilities = new DesiredCapabilities();
//
//        if (isIos) {
//            capabilities.setCapability("platformName", "iOS");
//            capabilities.setCapability("deviceName", "iPhone (MMTR)");
//            capabilities.setCapability("udid", "auto");
//            capabilities.setCapability("xcodeOrgId", "L8RRJQRVFV");
//            capabilities.setCapability("bundleId", "com.apple.TestFlight");
//            capabilities.setCapability("xcodeSigningId", "iPhone Developer");
//            capabilities.setCapability("automationName", "XCUITest");
//            capabilities.setCapability("agentPath", "/Users/mmtr/.npm-packages/lib/node_modules/appium/node_modules/appium-webdriveragent/WebDriverAgent.xcodeproj");
//            capabilities.setCapability("bootstrapPath", "/Users/mmtr/.npm-packages/lib/node_modules/appium/node_modules/appium-webdriveragent");
//            capabilities.setCapability("useNewWDA", true);
//        }
//        else {
//            capabilities.setCapability("platformName", "android");
//            capabilities.setCapability("noReset", true);
//            capabilities.setCapability("deviceName", "Xiaomi Redmi 7");
//            capabilities.setCapability("udid", "a6eaa0e2");
//            capabilities.setCapability("app", "/home/mitrixi/Local_C/IdeaProjects/untitled/src/main/resources/vitrina-app-debug.apk");
//        }
//
//        return capabilities;
//    }
//
//    private void stepToConfigUrl(boolean isIos) {
//        if (isIos) {
//            MobileElement openVitrinaBtn = (MobileElement) driver.findElementByXPath("//XCUIElementTypeButton[@name=\"ОТКРЫТЬ\"]");
//            openVitrinaBtn.click();
//
//            MobileElement linkBtn = (MobileElement) driver.findElementByXPath("//XCUIElementTypeButton[@name=\"icon link\"]");
//            linkBtn.click();
//
//            MobileElement btnSubmit = (MobileElement) driver.findElementByXPath("//XCUIElementTypeAlert[@name=\"Адрес конфигурации компании\"]");
//            btnSubmit.sendKeys(IOS_CONFIG_FILE_URL);
//        }
//        else {
//            MobileElement inputField = (MobileElement) driver.findElementById("ru.lyubimov.sdktestapp:id/configUrl");
//            inputField.sendKeys(ANDR_CONFIG_FILE_URL);
//        }
//    }
//
//    private void stepOk(boolean isIos) {
//        if (isIos) {
//            MobileElement okBtn = (MobileElement) driver.findElementByXPath("//XCUIElementTypeButton[@name=\"ОК\"]");
//            okBtn.click();
//        }
//        else {
//            MobileElement btnSubmit = (MobileElement) driver.findElementById("ru.lyubimov.sdktestapp:id/submit");
//            btnSubmit.click();
//        }
//    }
//
//    private boolean isStreamStartCheck(boolean isIos, String fileString) {
//        if (isIos) {
//            return fileString.contains(IOS_DEVICE_IP) && Arrays.stream(TEST_STREAM_IP).anyMatch(fileString::contains) && fileString.contains(START_STREAM_SERVER_MSG);
//        }
//        else {
//            return fileString.contains(ANDR_DEVICE_IP) && Arrays.stream(TEST_STREAM_IP).anyMatch(fileString::contains) && fileString.contains(START_STREAM_SERVER_MSG);
//        }
//    }
//
//}
//
