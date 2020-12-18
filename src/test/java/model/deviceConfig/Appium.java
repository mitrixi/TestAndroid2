package model.deviceConfig;

public class Appium {
    private Capabilities capabilities;

    private Driver driver;

    private Sniffer sniffer;

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

    public Sniffer getSniffer() {
        return sniffer;
    }

    public void setSniffer(Sniffer sniffer) {
        this.sniffer = sniffer;
    }
}
