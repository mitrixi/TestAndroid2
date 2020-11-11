import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class TestAndroid {

    AndroidDriver<AndroidElement> driver;

    @Test
    public void test() throws IOException {

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "android");
        capabilities.setCapability("noReset", true);

        capabilities.setCapability("deviceName", "Xiaomi Redmi 7");
        capabilities.setCapability("udid", "a6eaa0e2");
        capabilities.setCapability("app","/home/mitrixi/Local_C/IdeaProjects/untitled/src/main/resources/vitrina-app-debug.apk");
//        capabilities.setCapability("app","/opt/vitrina-app-debug.apk");


        driver = new AndroidDriver<>(new URL("http://10.254.0.131:4723/wd/hub"), capabilities);   //for Jenkins
//        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);      //for local PC
//        driver = new AndroidDriver<>(new URL("http://192.168.99.103:4723/wd/hub"), capabilities);
//        driver = new AndroidDriver<>(new URL("http://192.168.99.100:2376/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        MobileElement inputField = driver.findElementById("ru.lyubimov.sdktestapp:id/configUrl");
        inputField.sendKeys("http://10.254.0.131/");

        MobileElement btnSubmit = driver.findElementById("ru.lyubimov.sdktestapp:id/submit");
        btnSubmit.click();


        //смотрим, пришла ли json с https://stage.mediavitrina.ru/testdata/1223

    }


}