package model.deviceConfig;

public class Driver {
    private String url;

    private int implicitlyWait;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setImplicitlyWait(int implicitlyWait) {
        this.implicitlyWait = implicitlyWait;
    }

    public int getImplicitlyWait() {
        return this.implicitlyWait;
    }
}
