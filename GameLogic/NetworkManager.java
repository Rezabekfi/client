package GameLogic;

import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NetworkManager {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private boolean connected;
    private String serverAddress;
    private int serverPort;

    public NetworkManager(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.connected = false;
    }

    public boolean connect() {
        try {
            socket = new Socket(serverAddress, serverPort);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            connected = true;
            return true;
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        try {
            if (socket != null) socket.close();
            if (input != null) input.close();
            if (output != null) output.close();
            connected = false;
        } catch (Exception e) {
            System.err.println("Disconnect error: " + e.getMessage());
        }
    }

    public void sendMessage(GameMessage message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (Exception e) {
            System.err.println("Send error: " + e.getMessage());
        }
    }

    public GameMessage receiveMessage() {
        try {
            return (GameMessage) input.readObject();
        } catch (Exception e) {
            System.err.println("Receive error: " + e.getMessage());
            return null;
        }
    }

    public boolean isConnected() {
        return connected;
    }
} 