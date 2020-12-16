package service;

import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;

import static service.TestUtils.readJsonFromUrl;

public class TvConfig {
    public static String getUrlBlackout(String configUrl) throws IOException {
        JSONObject jsonConfigFile = readJsonFromUrl(configUrl);
        return jsonConfigFile.getJSONObject("result").getJSONObject("sdk_config").getString("restrictions_api_url");
    }

    public static int getRestrictionsPeriodSec(String configUrl) throws IOException {
        JSONObject jsonConfigFile = readJsonFromUrl(configUrl);
        return jsonConfigFile.getJSONObject("result").getJSONObject("sdk_config").getInt("restrictions_period_sec");
    }

    public static boolean isBroadcastingAllowed(String configUrl) throws IOException {
        JSONObject jsonBlackout = readJsonFromUrl(getUrlBlackout(configUrl));
        return jsonBlackout.getJSONArray("restrictions").getJSONObject(0).getBoolean("broadcasting_allowed");
    }

    public static String getRestrictionsApiIP(String configFileUrl) throws IOException {
        String restrictionsApiUrl = getUrlBlackout(configFileUrl);
        InetAddress restrictionsInetAddress = InetAddress.getByName(new URL(restrictionsApiUrl).getHost());
        return restrictionsInetAddress.getHostAddress();
    }
}
