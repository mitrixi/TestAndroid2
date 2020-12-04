package device;

import device.version.android.A9_V4_3_1;
import device.version.ios.I6_V4_2_5;

import java.util.function.Supplier;

public enum DeviceFactory {
    // deviceVersion енума (например: "I6_V4.2.5") должно соответствовать jenkins параметру DeviceVersion (см. General-"параметризованная сборка")
    I6_V425("I6_V4.2.5", I6_V4_2_5::getInstance),
    A9_V431("A9_V4.3.1", A9_V4_3_1::new);

    private String deviceVersion;
    private Supplier<IDevice> instantiator;

    public IDevice getInstance() {
        return instantiator.get();
    }

    DeviceFactory(String deviceVersion, Supplier<IDevice> instantiator) {
        this.deviceVersion = deviceVersion;
        this.instantiator = instantiator;
    }

    public static IDevice getIDeviceByDeviceVersion(String deviceVersion) {
        for (DeviceFactory deviceFactory : DeviceFactory.values()) {
            if (deviceFactory.deviceVersion.equals(deviceVersion)) {
                return deviceFactory.getInstance();
            }
        }
        return null;
    }
}
