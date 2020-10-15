import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class TestAndroid {

    @Test
    public static void test() throws MalformedURLException, InterruptedException {
        AndroidDriver<AndroidElement> driver;
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "android");
        capabilities.setCapability("noReset", "true");

        capabilities.setCapability("deviceName", "Galaxy S6");
        capabilities.setCapability("udid", "0915f9e4326d0805");
        capabilities.setCapability("appPackage", "com.sec.android.app.popupcalculator");
        capabilities.setCapability("appActivity", "com.sec.android.app.popupcalculator.Calculator");

//        capabilities.setCapability("deviceName", "Xiaomi Redmi 7");
//        capabilities.setCapability("udid", "a6eaa0e2");
//        capabilities.setCapability("appPackage", "com.miui.calculator");
//        capabilities.setCapability("appActivity", "com.miui.calculator.cal.CalculatorActivity");

        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        MobileElement buttonDigit5 = driver.findElementById("com.sec.android.app.popupcalculator:id/bt_05");
        MobileElement buttonMultiplication = driver.findElementById("com.sec.android.app.popupcalculator:id/bt_mul");
        MobileElement buttonEquals = driver.findElementById("com.sec.android.app.popupcalculator:id/bt_equal");
        MobileElement turnDisplay = driver.findElementById("com.sec.android.app.popupcalculator:id/rotation_button");
        MobileElement txtCalc = driver.findElementById("com.sec.android.app.popupcalculator:id/txtCalc");
        MobileElement btnBackspace = driver.findElementById("com.sec.android.app.popupcalculator:id/bt_backspace");


//        MobileElement buttonDigit5 = driver.findElementById("com.miui.calculator:id/btn_5_s");
//        MobileElement buttonMultiplication = driver.findElementById("com.miui.calculator:id/btn_mul_s");
//        MobileElement buttonEquals = driver.findElementById("com.miui.calculator:id/btn_equal_s");
//        MobileElement tabFinance = driver.findElementById("com.miui.calculator:id/iv_tab_finance");
//        MobileElement btnInvestment = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.support.v4.view.ViewPager/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.ScrollView/android.view.ViewGroup/android.widget.LinearLayout[1]");

        txtCalc.sendKeys("10");
        buttonMultiplication.click();
        txtCalc.sendKeys("10");
        buttonEquals.click();

        new Actions(driver).clickAndHold(btnBackspace).perform();

        buttonDigit5.click();
        buttonMultiplication.click();
        buttonDigit5.click();
        buttonEquals.click();
        turnDisplay.click();
        Thread.sleep(2000);
        turnDisplay.click();
    }
}