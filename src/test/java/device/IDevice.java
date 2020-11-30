package device;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;

public interface IDevice {
    void stepToConfigUrl(String configFileUrl);
    void stepOk();
    void stepCancelStream();
    String getTsharkStartFilePath();
    String getTsharkStartBlackout(String configFileUrl) throws IOException;
    String getTsharkStopFilePath();
    String getDeviceIp();
    void restrictBlackout() throws IOException;
    void allowBlackout() throws IOException;

    boolean seeBlackout();

    public AppiumDriver<WebElement> getDriver();
}
