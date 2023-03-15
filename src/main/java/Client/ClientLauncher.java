package Client;

import Settings.Settings;

import java.util.Scanner;

public class ClientLauncher {
    public static void main(String[] args) {
        Settings settings = new Settings();
        settings.readSettingsFromFile();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите свое имя");
        String clientName = scanner.nextLine();

        Client client = new Client(settings, clientName);
        client.startClient();
    }
}
