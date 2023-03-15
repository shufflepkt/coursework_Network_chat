package Server;

import Settings.Settings;

public class ServerLauncher {
    private static final String SERVER_NAME = "Server";

    public static void main(String[] args) {
        Settings settings = new Settings();
        settings.readSettingsFromFile();

        Server server = new Server(settings, SERVER_NAME);
        server.startServer();
    }
}
