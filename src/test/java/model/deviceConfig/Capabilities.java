package model.deviceConfig;

public class Capabilities {
    private String deviceName;

    private String xcodeOrgId;

    private String bundleId;

    private String agentPath;

    private String bootstrapPath;

    private boolean useNewWDA;

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setXcodeOrgId(String xcodeOrgId) {
        this.xcodeOrgId = xcodeOrgId;
    }

    public String getXcodeOrgId() {
        return this.xcodeOrgId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getBundleId() {
        return this.bundleId;
    }

    public void setAgentPath(String agentPath) {
        this.agentPath = agentPath;
    }

    public String getAgentPath() {
        return this.agentPath;
    }

    public void setBootstrapPath(String bootstrapPath) {
        this.bootstrapPath = bootstrapPath;
    }

    public String getBootstrapPath() {
        return this.bootstrapPath;
    }

    public void setUseNewWDA(boolean useNewWDA) {
        this.useNewWDA = useNewWDA;
    }

    public boolean getUseNewWDA() {
        return this.useNewWDA;
    }
}
