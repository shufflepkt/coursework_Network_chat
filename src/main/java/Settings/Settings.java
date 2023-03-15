package Settings;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Settings {
    private String ip;
    private String port;

    private final static String SETTINGS_FILE_NAME = "settings.json";
    private final static String LOG_FILE = "file.log";

    public void readSettingsFromFile() {
        try {
            Settings settings = new ObjectMapper().readValue(new File(SETTINGS_FILE_NAME), new TypeReference<>() {
            });
            this.ip = settings.getIp();
            this.port = settings.getPort();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
