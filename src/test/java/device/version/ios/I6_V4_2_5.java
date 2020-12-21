package device.version.ios;

import device.IosDevice;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static service.ImageCompare.compareBo;
import static service.ios.scrollToElementByXpaths;

public class I6_V4_2_5 extends IosDevice {

    I6_V4_2_5() {
        super();
    }

    public static class SingletonHolder {
        private final static I6_V4_2_5 instance = new I6_V4_2_5();
    }

    public static I6_V4_2_5 getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public void stepToConfigUrl(String configFileUrl) {
        if (driver.findElements(By.xpath("//XCUIElementTypeButton[@name=\"icon link\"]")).size() == 0) {
            scrollToElementByXpaths(driver, Arrays.asList("//XCUIElementTypeCell[@name=\"vitrina.tv.demo\"]", "//XCUIElementTypeButton[@name=\"ОТКРЫТЬ\"]"));

            MobileElement openVitrinaBtn = ((MobileElement) driver
                    .findElementByXPath("//XCUIElementTypeCell[@name=\"vitrina.tv.demo\"]"))
                    .findElementByXPath("//XCUIElementTypeButton[@name=\"ОТКРЫТЬ\"]");
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
    public boolean isBoOnScreenShot() throws IOException {
        File f = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        return compareBo(f, this.getClass().getClassLoader().getResource(IOS_BO_SCR_FILE).getPath());
    }
}
