package Network;

import java.io.*;
import java.net.Socket;

public class Connection {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    private Thread readThread;

    private ConnectionListener listener;

    public Connection(Socket socket, ConnectionListener listener) {
        try {
            this.socket = socket;
            this.reader = createReader();
            this.writer = createWriter();
            this.listener = listener;

            this.listener.onConnectionReady(Connection.this);

            readThread = new Thread(() -> {
                while (!readThread.isInterrupted()) {
                    try {
                        if (reader.ready()) {
                            listener.onGettingMsg(Connection.this, reader.readLine());
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            readThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLine(String msg) {
        try {
            writer.write(msg);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            listener.onException(this, e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        readThread.interrupt();
        try {
            socket.close();
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedReader createReader() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private BufferedWriter createWriter() throws IOException {
        return new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public Socket getSocket() {
        return socket;
    }
}
