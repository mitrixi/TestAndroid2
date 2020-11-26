package device;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AndroidDevice implements IDevice {

    public final static String ANDR_TSHARK_START_SCRIPT_FILE = "andr_tshark_start_script.sh";
    public final static String ANDR_TSHARK_BLACKOUT_SNIFFING = "andr_tshark_blackout_sniffing.sh";
    public final static String ANDR_TSHARK_STOP_SCRIPT_FILE = "andr_tshark_stop_script.sh";
    //    public final static String ANDR_TSHARK_KILL_SCRIPT = "sudo killall tshark";
    public final static String ANDR_DEVICE_IP = "10.10.0.102";
    public final static String ANDR_VITRINA_APP_APK_FILE = "vitrina-app-debug.apk";
    public final static String ANDR_BLACKOUTS_IP = "151.236.95.210";

    AppiumDriver<WebElement> driver;

    public AndroidDevice() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "android");
        capabilities.setCapability("noReset", true);
        capabilities.setCapability("deviceName", "Xiaomi Redmi 7");
        capabilities.setCapability("udid", "a6eaa0e2");
        capabilities.setCapability("app", "/home/mitrixi/Local_C/IdeaProjects/untitled/src/main/resources/vitrina-app-debug.apk");

        driver = new AndroidDriver<>(new URL("http://10.254.0.131:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Override
    public void stepToConfigUrl(String configFileUrl) {
        MobileElement inputField = (MobileElement) driver.findElementById("ru.lyubimov.sdktestapp:id/configUrl");
        inputField.sendKeys(configFileUrl);
    }

    @Override
    public void stepOk() {
        MobileElement btnSubmit = (MobileElement) driver.findElementById("ru.lyubimov.sdktestapp:id/submit");
        btnSubmit.click();
    }

    @Override
    public String getTsharkStartFilePath() {
        return this.getClass().getClassLoader().getResource(ANDR_TSHARK_START_SCRIPT_FILE).getPath();
    }

    @Override
    public String getTsharkStartBlackout(String configFileUrl) {
        return this.getClass().getClassLoader().getResource(ANDR_TSHARK_BLACKOUT_SNIFFING).getPath();
    }

    @Override
    public String getTsharkStopFilePath() {
        return this.getClass().getClassLoader().getResource(ANDR_TSHARK_STOP_SCRIPT_FILE).getPath();
    }

    @Override
    public String getDeviceIp() {
        return ANDR_DEVICE_IP;
    }
}
