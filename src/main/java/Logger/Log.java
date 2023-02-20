package Logger;

import java.io.FileWriter;
import java.io.IOException;

public class Log {
    private final String userName;
    private final String logFile;

    public Log(String userName, String logFile) {
        this.userName = userName;
        this.logFile = userName + "_" + logFile;
    }

    public void log(String msg) {
        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write(msg + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
