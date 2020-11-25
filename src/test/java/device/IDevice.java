package device;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;

public interface IDevice {
    void stepToConfigUrl();
    void stepOk();
    String getTsharkStartFilePath();
    String getTsharkStopFilePath();
    String getIP();
    String startBlackoutSniffing();
}
