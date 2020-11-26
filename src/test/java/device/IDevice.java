package device;

import java.io.IOException;

public interface IDevice {
    void stepToConfigUrl(String configFileUrl);
    void stepOk();
    String getTsharkStartFilePath();
    String getTsharkStartBlackout(String configFileUrl) throws IOException;
    String getTsharkStopFilePath();
    String getDeviceIp();
}
