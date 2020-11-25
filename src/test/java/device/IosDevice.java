package device;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class IosDevice implements IDevice {

    public final static String IOS_TSHARK_START_SCRIPT_FILE = "tshark_start_script.sh"; // вывод для консоли
    public final static String IOS_TSHARK_STOP_SCRIPT_FILE = "tshark_stop_script.sh";
    //    public final static String IOS_TSHARK_KILL_SCRIPT = "killall tshark";
    public final static String IOS_DEVICE_IP = "10.254.7.106";  // см. файл tshark_start_script.sh
    public final static String IOS_CONFIG_FILE_URL = "https://stage.mediavitrina.ru/testdata/1227";

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
    public void stepToConfigUrl() {
        MobileElement openVitrinaBtn = (MobileElement) driver.findElementByXPath("//XCUIElementTypeButton[@name=\"ОТКРЫТЬ\"]");
        openVitrinaBtn.click();

        MobileElement linkBtn = (MobileElement) driver.findElementByXPath("//XCUIElementTypeButton[@name=\"icon link\"]");
        linkBtn.click();

        MobileElement btnSubmit = (MobileElement) driver.findElementByXPath("//XCUIElementTypeAlert[@name=\"Адрес конфигурации компании\"]");
        btnSubmit.sendKeys(IOS_CONFIG_FILE_URL);
    }

    @Override
    public void stepOk() {
        MobileElement okBtn = (MobileElement) driver.findElementByXPath("//XCUIElementTypeButton[@name=\"ОК\"]");
        okBtn.click();
    }

    @Override
    public String getTsharkStartFile() {
        return IOS_TSHARK_START_SCRIPT_FILE;
    }

    @Override
    public String getTsharkStopFile() {
        return IOS_TSHARK_STOP_SCRIPT_FILE;
    }

    @Override
    public String getDeviceId() {
        return IOS_DEVICE_IP;
    }
}
