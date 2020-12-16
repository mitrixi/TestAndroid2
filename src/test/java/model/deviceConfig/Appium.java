package model.deviceConfig;

public class Appium {
    private Capabilities capabilities;

    private Driver driver;

    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public Capabilities getCapabilities() {
        return this.capabilities;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return this.driver;
    }
}
