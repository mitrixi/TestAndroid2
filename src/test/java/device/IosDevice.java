package device;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static test.TestUtils.readJsonFromUrl;

public class IosDevice implements IDevice {

    public final static String IOS_DEVICE_IP = "10.254.7.106";
    public final static String SSH = "ssh -tt mmtr@10.254.7.106 ";
    public final static String IOS_TSHARK_START_SCRIPT_FILE = "tshark_start_script.sh"; // вывод для консоли
    public final static String IOS_TSHARK_START_CMD_FOR_BO = SSH + "'/usr/local/bin/tshark -Y \"tls.handshake.session_id && ip.dst == " + IOS_DEVICE_IP + " && ip.src == {1}\"'";
    public final static String IOS_TSHARK_BLACKOUT_SNIFFING = "tshark_blackout_sniffing.sh";
    public final static String IOS_TSHARK_STOP_SCRIPT_FILE = "tshark_stop_script.sh";
    //    public final static String IOS_TSHARK_KILL_SCRIPT = "killall tshark";

    AppiumDriver<WebElement> driver;

    public IosDevice() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("deviceName", "iPhone (MMTR)");
        capabilities.setCapability("udid", "auto");
        capabilities.setCapability("xcodeOrgId", "L8RRJQRVFV");
        capabilities.setCapability("bundleId", "com.apple.TestFlight");
        capabilities.setCapability("xcodeSigningId", "iPhone Developer");
        capabilities.setCapability("automationName", "XCUITest");
        capabilities.setCapability("agentPath", "/Users/mmtr/.npm-packages/lib/node_modules/appium/node_modules/appium-webdriveragent/WebDriverAgent.xcodeproj");
        capabilities.setCapability("bootstrapPath", "/Users/mmtr/.npm-packages/lib/node_modules/appium/node_modules/appium-webdriveragent");
        capabilities.setCapability("useNewWDA", true);

        driver = new IOSDriver<>(new URL("http://10.254.7.106:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Override
    public void stepToConfigUrl(String configFileUrl) {
        MobileElement openVitrinaBtn = (MobileElement) driver.findElementByXPath("//XCUIElementTypeButton[@name=\"ОТКРЫТЬ\"]");
        openVitrinaBtn.click();

        MobileElement linkBtn = (MobileElement) driver.findElementByXPath("//XCUIElementTypeButton[@name=\"icon link\"]");
        linkBtn.click();

        MobileElement btnSubmit = (MobileElement) driver.findElementByXPath("//XCUIElementTypeAlert[@name=\"Адрес конфигурации компании\"]");
        btnSubmit.sendKeys(configFileUrl);
    }

    @Override
    public void stepOk() {
        MobileElement okBtn = (MobileElement) driver.findElementByXPath("//XCUIElementTypeButton[@name=\"ОК\"]");
        okBtn.click();
    }

    @Override
    public void stepCancelStream() {
        MobileElement okBtn = (MobileElement) driver.findElementByXPath("//XCUIElementTypeButton[@name=\"Закрыть\"]");
        okBtn.click();
    }

    @Override
    public String getTsharkStartFilePath() {
        return this.getClass().getClassLoader().getResource(IOS_TSHARK_START_SCRIPT_FILE).getPath();
    }

    @Override
    public String getTsharkStartBlackout(String configFileUrl) throws IOException {
        return this.getClass().getClassLoader().getResource(IOS_TSHARK_BLACKOUT_SNIFFING).getPath();
//        return IOS_TSHARK_START_CMD_FOR_BO.replace("{1}", getRestrictionsApiIP(configFileUrl));
    }

    @Override
    public String getTsharkStopFilePath() {
        return this.getClass().getClassLoader().getResource(IOS_TSHARK_STOP_SCRIPT_FILE).getPath();
    }

    @Override
    public String getDeviceIp() {
        return IOS_DEVICE_IP;
    }

    @Override
    public void restrictBlackout() throws IOException {

    }

    @Override
    public void allowBlackout() throws IOException {

    }

    private CharSequence getRestrictionsApiIP(String configFileUrl) throws IOException {
        JSONObject json = readJsonFromUrl(configFileUrl);

        String restrictionsApiUrl = json.getJSONObject("result").getJSONObject("sdk_config").get("restrictions_api_url").toString();
        InetAddress restrictionsInetAddress = InetAddress.getByName(new URL(restrictionsApiUrl).getHost());
        return restrictionsInetAddress.getHostAddress();
    }
}
