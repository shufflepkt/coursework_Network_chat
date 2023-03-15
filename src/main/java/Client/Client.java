package Client;

import Logger.Log;
import Network.Connection;
import Network.ConnectionListener;
import Settings.Settings;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Client implements ConnectionListener {
    private Thread writeThread;

    private final String clientName;

    private final Log logger;
    private final Settings settings;

    private static final String EXIT_MSG = "/exit";

    public Client(Settings settings, String clientName) {
        this.settings = settings;
        this.clientName = clientName;
        this.logger = new Log(clientName, settings.getLogFile());
    }

    public void startClient() {
        Connection connection;
        try {
            connection = new Connection(new Socket(settings.getIp(), Integer.parseInt(settings.getPort())), this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        writeThread = new Thread(() -> {
            while (!writeThread.isInterrupted()) {
                Scanner scanner = new Scanner(System.in);
                String msg = scanner.nextLine();
                if (EXIT_MSG.equals(msg)) {
                    disconnectClient(connection);
                    break;
                }
                String fullMsg = "[" + clientName + ", " + getDateTime() + "]: " + msg;
                connection.writeLine(fullMsg);
                logger.log(fullMsg);
            }
        });
        writeThread.start();
    }

    public void disconnectClient(Connection connection) {
        System.out.println("До свидания, " + clientName + "!");

        String msg = "[" + getDateTime() + "]: " + clientName + " покинул чат. IP: " +
                connection.getSocket().getInetAddress().getHostAddress() + ", Port: " +
                connection.getSocket().getLocalPort();
        connection.writeLine(msg);
        logger.log(msg);

        connection.writeLine(EXIT_MSG);

        writeThread.interrupt();
        connection.disconnect();
    }

    private String getDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy, HH:mm:ss"));
    }

    @Override
    public void onConnectionReady(Connection connection) {
        String msg = "[" + getDateTime() + "]: " + clientName + " вошел в чат. IP: " +
                connection.getSocket().getInetAddress().getHostAddress() + ", Port: " +
                connection.getSocket().getLocalPort();
        connection.writeLine(msg);
        logger.log(msg);

        System.out.println(clientName + ", добро пожаловать в чат!");
    }

    @Override
    public void onGettingMsg(Connection connection, String msg) {
        if (msg != null) {
            System.out.println(msg);
            logger.log(msg);
        }
    }

    @Override
    public void onException(Connection connection, Exception e) {
        String msg = "[" + getDateTime() + "]: " + " Сервер перестал отвечать";
        logger.log(msg);
        System.out.println(msg);

        e.printStackTrace();
    }
}
