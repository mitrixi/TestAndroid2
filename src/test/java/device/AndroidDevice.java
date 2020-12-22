package device;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public abstract class AndroidDevice implements IDevice {

    public final static String ANDR_TSHARK_START_SCRIPT_FILE = "tsharkScript/andr_tshark_start_script.sh";
    public final static String ANDR_TSHARK_BLACKOUT_SNIFFING = "tsharkScript/andr_tshark_blackout_sniffing.sh";
    public final static String ANDR_TSHARK_STOP_SCRIPT_FILE = "tsharkScript/andr_tshark_stop_script.sh";
    //    public final static String ANDR_TSHARK_KILL_SCRIPT = "sudo killall tshark";
//    public final static String ANDR_DEVICE_IP = "10.10.0.102";
//    public final static String ANDR_VITRINA_APP_APK_FILE = "vitrina-app-debug.apk";
//    public final static String ANDR_BLACKOUTS_IP = "151.236.95.210";

    public final static String ANDR_BO_SCR_FILE = "screenshot/andrBoScr.jpg"; // вывод для консоли

    public AndroidDriver<WebElement> driver;
    public DesiredCapabilities capabilities;

    public AndroidDevice() {
        capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "android");
        capabilities.setCapability("noReset", true);
        capabilities.setCapability("deviceName", "Xiaomi Redmi 7");
        capabilities.setCapability("udid", "a6eaa0e2");
//        capabilities.setCapability("app", "/home/mitrixi/Local_C/IdeaProjects/untitled/src/main/resources/vitrina-app-debug.apk");    //адрес на хосте
//        capabilities.setCapability("app", "/var/jenkins_home/workspace/TestAndroid/src/main/resources/vitrina-app-debug.apk");    //адрес в контейнере дженкинса
        capabilities.setCapability("appPackage", "ru.lyubimov.sdktestapp");
        capabilities.setCapability("appActivity", "ru.lyubimov.sdktestapp.MainActivity");
//        capabilities.setCapability("app", "/tmp/dir-apk-file-android/vitrina-app-debug.apk");   //адрес в контейнере аппиума

        try {
//            driver = new AndroidDriver<>(new URL("http://10.254.0.131:4723/wd/hub"), capabilities);
            driver = new AndroidDriver<>(new URL("http://172.19.0.2:4723/wd/hub"), capabilities);
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
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

//    @Override
//    public String getDeviceIp() {
//        return ANDR_DEVICE_IP;
//    }

    @Override
    public void restrictBlackout() throws IOException, InterruptedException {
        Process pr = Runtime.getRuntime().exec(this.getClass().getClassLoader().getResource("blackoutOnOffScript/allow_broadcasts.sh").getPath());
//        TimeUnit.SECONDS.sleep(10);
        pr.waitFor();
    }

    @Override
    public void allowBlackout() throws IOException, InterruptedException {
        Process pr = Runtime.getRuntime().exec(this.getClass().getClassLoader().getResource("blackoutOnOffScript/restrict_broadcasts.sh").getPath());
//        TimeUnit.SECONDS.sleep(10);
        pr.waitFor();
    }
}
