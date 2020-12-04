package device;

import java.io.IOException;

public interface IDevice {

    void stepToConfigUrl(String configFileUrl);
    void stepOk();
    void stepCancelStream();

    boolean seeBlackout();
    boolean isBoOnScreenShot() throws IOException;

    String getTsharkStartFilePath();
    String getTsharkStartBlackout(String configFileUrl) throws IOException;
    String getTsharkStopFilePath();

//    String getDeviceIp();

    void restrictBlackout() throws IOException, InterruptedException;
    void allowBlackout() throws IOException, InterruptedException;

}
