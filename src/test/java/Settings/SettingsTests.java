package Settings;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class SettingsTests {
    @Test
    public void getIpTest() {
        String expectedIp = "localhost";
        Settings settings = new Settings();
        settings.readSettingsFromFile();

        String resultIp = settings.getIp();

        Assertions.assertEquals(expectedIp, resultIp);
    }

    @Test
    public void getPortTest() {
        String expectedPort = "8080";
        Settings settings = new Settings();
        settings.readSettingsFromFile();

        String resultPort = settings.getPort();

        Assertions.assertEquals(expectedPort, resultPort);
    }
}
