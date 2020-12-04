package device.version.android;

import device.AndroidDevice;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import service.ImageCompare;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AndroidX_VitrinaX extends AndroidDevice {

    public AndroidX_VitrinaX() {
        super();
    }
//
//    public static class SingletonHolder {
//        private final static AndroidX_VitrinaX instance = new AndroidX_VitrinaX();
//    }
//
//    public static AndroidX_VitrinaX getInstance() {
//        return SingletonHolder.instance;
//    }

    @Override
    public void stepToConfigUrl(String configFileUrl) {

        // КОСТЫЛЬ на время неполноценной версии приложения
        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            driver = new AndroidDriver<>(new URL("http://10.254.0.131:4723/wd/hub"), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        MobileElement inputField = (MobileElement) driver.findElementById("ru.lyubimov.sdktestapp:id/configUrl");
        inputField.sendKeys(configFileUrl);
    }

    @Override
    public void stepOk() {
        MobileElement btnSubmit = (MobileElement) driver.findElementById("ru.lyubimov.sdktestapp:id/submit");
        btnSubmit.click();
    }

    @Override
    public void stepCancelStream() {
        // Закрывается сам через 1мин
    }

    @Override
    public boolean seeBlackout() {
        return false; // ToDo это будет isBoOnScreenShot
    }

    @Override
    public boolean isBoOnScreenShot() throws IOException {
        File f = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        ImageCompare imageCompare = new ImageCompare();
        return imageCompare.compareBo(f, this.getClass().getClassLoader().getResource(ANDR_BO_SCR_FILE).getPath());
    }
}
