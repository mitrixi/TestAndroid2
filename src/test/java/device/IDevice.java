package device;

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
}
