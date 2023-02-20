package Settings;

public class Main {
    public static void main(String[] args) {
        Settings settings = new Settings("localhost", "8080");
        settings.createSettingsFile();
    }
}
