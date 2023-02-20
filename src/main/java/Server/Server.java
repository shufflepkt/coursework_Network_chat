package Server;

import Logger.Log;
import Network.Connection;
import Network.ConnectionListener;
import Settings.Settings;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Server implements ConnectionListener {
    private final String serverName = "Server";

    private static final String EXIT_MSG = "/exit";
    private static final ArrayList<Connection> connections = new ArrayList<>();
    private Log logger;

    public static void main(String[] args) {
        new Server();
    }

    private Server() {
        Settings settings = Settings.getSettings();
        logger = new Log(serverName, settings.getLogFile());

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            String msg = "[" + serverName + ", " + getDateTime() + "]: " + "Server started...";
            System.out.println(msg);
            logger.log(msg);

            while (true) {
                new Connection(serverSocket.accept(), this);
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
        if (msg.equals(null)) {
            return;
        } else if (msg.equals(EXIT_MSG)) {
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
