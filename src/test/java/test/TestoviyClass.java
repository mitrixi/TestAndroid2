package test;

import device.AndroidDevice;
import device.IDevice;
import device.IosDevice;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static test.TestUtils.readJsonFromUrl;

public class TestoviyClass {
    @Test
    public void test() throws IOException, InterruptedException {


        IDevice device = "iPhone".equals(System.getenv("deviceType")) ? IosDevice.INSTANCE : AndroidDevice.INSTANCE;

        JSONObject jsonConfigFile = readJsonFromUrl("http://10.254.0.131/");

        String urlBlackout = jsonConfigFile.getJSONObject("result").getJSONObject("sdk_config").get("restrictions_api_url").toString();
        JSONObject jsonBlackout = readJsonFromUrl(urlBlackout);
        boolean broadcasting_allowed = Boolean.parseBoolean(jsonBlackout.getJSONArray("restrictions").getJSONObject(0).get("broadcasting_allowed").toString());
        System.out.println(broadcasting_allowed);

        device.allowBlackout(); // ToDo
        TimeUnit.SECONDS.sleep(10);


        JSONObject jsonConfigFile2 = readJsonFromUrl("http://10.254.0.131/");
        String urlBlackout2 = jsonConfigFile2.getJSONObject("result").getJSONObject("sdk_config").get("restrictions_api_url").toString();
        JSONObject jsonBlackout2 = readJsonFromUrl(urlBlackout2);
        boolean broadcasting_allowed2 = Boolean.parseBoolean(jsonBlackout2.getJSONArray("restrictions").getJSONObject(0).get("broadcasting_allowed").toString());
        System.out.println(broadcasting_allowed2);

        device.restrictBlackout(); // ToDo


        JSONObject jsonConfigFile3 = readJsonFromUrl("http://10.254.0.131/");
        String urlBlackout3 = jsonConfigFile3.getJSONObject("result").getJSONObject("sdk_config").get("restrictions_api_url").toString();
        JSONObject jsonBlackout3 = readJsonFromUrl(urlBlackout3);
        boolean broadcasting_allowed3 = Boolean.parseBoolean(jsonBlackout3.getJSONArray("restrictions").getJSONObject(0).get("broadcasting_allowed").toString());
        System.out.println(broadcasting_allowed3);

        device.allowBlackout(); // ToDo

        JSONObject jsonConfigFile4 = readJsonFromUrl("http://10.254.0.131/");
        String urlBlackout4 = jsonConfigFile4.getJSONObject("result").getJSONObject("sdk_config").get("restrictions_api_url").toString();
        JSONObject jsonBlackout4 = readJsonFromUrl(urlBlackout4);
        boolean broadcasting_allowed4 = Boolean.parseBoolean(jsonBlackout4.getJSONArray("restrictions").getJSONObject(0).get("broadcasting_allowed").toString());
        System.out.println(broadcasting_allowed4);

    }
}
