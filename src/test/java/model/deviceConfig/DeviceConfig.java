package model.deviceConfig;

public class DeviceConfig {
    private Appium appium;

    private Device device;

    public void setAppium(Appium appium) {
        this.appium = appium;
    }

    public Appium getAppium() {
        return this.appium;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return this.device;
    }
}
