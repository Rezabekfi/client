package com.quoridor.GameLogic;

import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * NetworkManager class is responsible for handling the connection to the server, receiving and sending messages.
 */
public class NetworkManager {
    // Socket and I/O streams
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;

    // Connection status
    private boolean connected;

    // Server address and port
    private String serverAddress;
    private int serverPort;

    // Constructor
    public NetworkManager(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.connected = false;
    }

    // Connect to the server
    public synchronized boolean connect() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(serverAddress, serverPort), 5000); // 5 seconds timeout
            socket.setTcpNoDelay(true);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            connected = true;
            return true;
        } catch (SocketTimeoutException e) {
            System.err.println("Connection timed out: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
            return false;
        }
    }

    // Disconnect from the server
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

    // Send a message to the server
    public synchronized void sendMessage(String message) {
        if (!isConnected()) {
            System.err.println("Cannot send message, not connected to the server.");
            return;
        }

        try {
            output.println(message);
        } catch (Exception e) {
            System.err.println("Send error: " + e.getMessage());
        }
    }

    // Send a GameMessage to the server
    public synchronized void sendMessage(GameMessage message) {
        if (!isConnected()) {
            System.err.println("Cannot send message, not connected to the server.");
            return;
        }

        try {
            String jsonString = message.toMessageString();
            if (message.getType() != GameMessage.MessageType.ACK && message.getType() != GameMessage.MessageType.HEARTBEAT) {
                System.out.println("Sending message: " + jsonString);
            }
            output.println(jsonString);
        } catch (Exception e) {
            System.err.println("Send error: " + e.getMessage());
        }
    }

    // Receive a GameMessage from the server
    public GameMessage receiveMessage() {
        try {
            String jsonString = input.readLine();
            if (jsonString == null || jsonString.trim().isEmpty()) {
                GameMessage invalidMessage = new GameMessage("Received nothing from server");
                return invalidMessage;
            }

            if (GameMessage.fromMessageString(jsonString).getType() != GameMessage.MessageType.ACK &&
                GameMessage.fromMessageString(jsonString).getType() != GameMessage.MessageType.HEARTBEAT) {
                System.out.println("Received message: " + jsonString);
            }
            return GameMessage.fromMessageString(jsonString);
        } catch (Exception e) {
            System.err.println("Receive error: " + e.getMessage());
            connected = false;
            return GameMessage.createLostConnection();
        }
    }

    public boolean isConnected() {
        return connected;
    }

    // Validate network settings soft validation
    public static boolean validate_network_settings(String address, int port) {
        // I don't know how to validate an IP address, so I'll just check if it's not NULL and the port is in the valid range
        return address != null && !address.isEmpty() && port > 0 && port < 65536;
    }
}