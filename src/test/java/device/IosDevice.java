package device;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static service.ImageCompare.compareBo;

public enum IosDevice implements IDevice {

    INSTANCE;

    public final static String IOS_DEVICE_IP = "10.254.7.106";
    public final static String SSH = "ssh -tt mmtr@10.254.7.106 ";
    public final static String IOS_TSHARK_START_SCRIPT_FILE = "tsharkScript/tshark_start_script.sh"; // вывод для консоли
    public final static String IOS_TSHARK_START_CMD_FOR_BO = SSH + "'/usr/local/bin/tshark -Y \"tls.handshake.session_id && ip.dst == " + IOS_DEVICE_IP + " && ip.src == {1}\"'";
    public final static String IOS_TSHARK_BLACKOUT_SNIFFING = "tsharkScript/tshark_blackout_sniffing.sh";
    public final static String IOS_TSHARK_STOP_SCRIPT_FILE = "tsharkScript/tshark_stop_script.sh";

    public final static String IOS_BO_SCR_FILE = "screenshot/iOsBoScr.jpg"; // вывод для консоли
    //    public final static String IOS_TSHARK_KILL_SCRIPT = "killall tshark";

    AppiumDriver<WebElement> driver;

    IosDevice() {
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

        try {
            driver = new IOSDriver<>(new URL("http://10.254.7.106:4723/wd/hub"), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Override
    public void stepToConfigUrl(String configFileUrl) {
        if (driver.findElements(By.xpath("//XCUIElementTypeButton[@name=\"icon link\"]")).size() == 0) {
            MobileElement openVitrinaBtn = (MobileElement) driver.findElementByXPath("//XCUIElementTypeButton[@name=\"ОТКРЫТЬ\"]");
            openVitrinaBtn.click();
        }

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
    public void restrictBlackout() throws IOException, InterruptedException {
        Process pr = Runtime.getRuntime().exec(this.getClass().getClassLoader().getResource("blackoutOnOffScript/allow_broadcasts.sh").getPath());
        pr.waitFor();
    }

    @Override
    public void allowBlackout() throws IOException, InterruptedException {
        Process pr = Runtime.getRuntime().exec(this.getClass().getClassLoader().getResource("blackoutOnOffScript/restrict_broadcasts.sh").getPath());
        pr.waitFor();
    }

    @Override
    public boolean seeBlackout() {
        return false;
    }

    @Override
    public boolean isBoOnScreenShot() throws IOException {
        File f = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        return compareBo(f, this.getClass().getClassLoader().getResource(IOS_BO_SCR_FILE).getPath());
    }
}
