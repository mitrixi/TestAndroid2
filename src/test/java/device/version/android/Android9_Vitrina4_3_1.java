package device.version.android;

import device.AndroidDevice;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.checkerframework.checker.units.qual.A;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import service.ImageCompare;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Android9_Vitrina4_3_1 extends AndroidDevice {

    public Android9_Vitrina4_3_1() {
        super();
    }

    public static class SingletonHolder {
        private final static Android9_Vitrina4_3_1 instance = new Android9_Vitrina4_3_1();
    }

    public static Android9_Vitrina4_3_1 getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public void stepToConfigUrl(String configFileUrl) {

        // КОСТЫЛЬ на время неполноценной версии приложения
        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
//            driver = new AndroidDriver<>(new URL("http://10.254.0.131:4723/wd/hub"), capabilities);
            driver = new AndroidDriver<>(new URL("http://172.19.0.2:4723/wd/hub"), capabilities);

        } catch (Exception e) {
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
    public boolean isBoOnScreenShot() throws IOException {
        File f = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        ImageCompare imageCompare = new ImageCompare();
        return imageCompare.compareBo(f, this.getClass().getClassLoader().getResource(ANDR_BO_SCR_FILE).getPath());
    }
}
