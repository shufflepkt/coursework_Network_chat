package Network;

public interface ConnectionListener {
    void onConnectionReady(Connection connection);
    void onGettingMsg(Connection connection, String msg);
    void onException(Connection connection, Exception e);
}
