package Settings;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Settings {
    private String ip;
    private String port;

    private final static String IP = "ip";
    private final static String PORT = "port";
    private final static String SETTINGS_FILE_NAME = "settings.json";
    private final static String LOG_FILE = "file.log";

    public static Settings getSettings() {
        Settings settings = null;
        try {
            settings = new ObjectMapper().readValue(new File(SETTINGS_FILE_NAME), new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return settings;
    }

    public Settings() {}
    public Settings(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getLogFile() {
        return LOG_FILE;
    }

    public void createSettingsFile() {
        JSONObject obj = new JSONObject();
        obj.put(IP, this.ip);
        obj.put(PORT, this.port);

        try (FileWriter file = new FileWriter(SETTINGS_FILE_NAME)) {
            file.write(obj.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
