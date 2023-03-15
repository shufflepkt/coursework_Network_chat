package Server;

import Logger.Log;
import Network.Connection;
import Network.ConnectionListener;
import Settings.Settings;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Server implements ConnectionListener {
    private static final String EXIT_MSG = "/exit";
    private static final ArrayList<Connection> connections = new ArrayList<>();
    private final Settings settings;
    private final Log logger;
    private final String serverName;

    public Server(Settings settings, String serverName) {
        this.settings = settings;
        this.serverName = serverName;
        this.logger = new Log(serverName, settings.getLogFile());
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(settings.getPort()))) {
            String msg = "[" + serverName + ", " + getDateTime() + "]: " + "Server started...";
            System.out.println(msg);
            logger.log(msg);

            while (true) {
//              Почему если создание сокета ниже засунуть в try с ресурсами, чтобы он автоматически закрывался,
//              работа клиента падает при отправке первого сообщения серверу?
                Socket socket = serverSocket.accept();
                new Connection(socket, this);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void sendToAllExcept(String msg, Connection exceptConnection) {
        for (Connection connection : connections) {
            if (connection != exceptConnection) {
                connection.writeLine(msg);
            }
        }
    }

    private void disconnectClient(Connection connection) {
        synchronized (connections) {
            connections.remove(connection);
        }
    }

    private String getDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy, HH:mm:ss"));
    }

    @Override
    public void onConnectionReady(Connection connection) {
        synchronized (connections) {
            connections.add(connection);
        }
    }

    @Override
    public void onGettingMsg(Connection connection, String msg) {
        if (EXIT_MSG.equals(msg)) {
            disconnectClient(connection);
        } else {
            System.out.println(msg);
            logger.log(msg);
            sendToAllExcept(msg, connection);
        }
    }

    @Override
    public void onException(Connection connection, Exception e) {
        String msg = "[" + getDateTime() + "]: " + " Клиент перестал отвечать";
        logger.log(msg);
        System.out.println(msg);

        e.printStackTrace();
    }
}
