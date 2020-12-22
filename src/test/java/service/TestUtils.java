package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.deviceConfig.DeviceConfig;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.Charset;

public class TestUtils {
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static JSONObject readJsonFromFile(String path) throws IOException, JSONException {
        File file = new File(path);
        InputStream is = new FileInputStream(file);
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static DeviceConfig getPojoFromJsonFile(Class clazz, String path) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return (DeviceConfig) objectMapper.readValue(readJsonFromFile(path).toString(), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String readUsingBufferedReader(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public static void stopProcess(Process p) throws IOException {
        long pid = -1;

        try {
            if (p.getClass().getName().equals("java.lang.UNIXProcess")) {
                Field f = p.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                pid = f.getLong(p);
                f.setAccessible(false);
            }
        } catch (Exception e) {
            pid = -1;
        }
        Runtime.getRuntime().exec("kill -9 " + pid);
    }

    public static synchronized long getPidOfProcess(Process p) {
        long pid = -1;

        try {
            if (p.getClass().getName().equals("java.lang.UNIXProcess")) {
                Field f = p.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                pid = f.getLong(p);
                f.setAccessible(false);
            }
        } catch (Exception e) {
            pid = -1;
        }
        return pid;
    }

    public static int getSecFromBoStr(String s) {
        return Math.round(Float.parseFloat(s.split(" +")[1]));
    }

    public static boolean isExecOutputContainsMsg(BufferedReader bufferedReader, String msg) throws IOException {
        boolean isContains = false;
        String strStream;
        while (bufferedReader.ready()) {
            strStream = bufferedReader.readLine();
            System.out.println(strStream); // for test
            if (strStream.contains(msg)) {
                isContains = true;
                break;
            }
        }
        bufferedReader.close();
        return isContains;
    }

    public static String getSwipeDirection(int displayWidth, int mobileLocationY) {
        return displayWidth / 2 - mobileLocationY > 0 ? "down" : "up";
    }
}