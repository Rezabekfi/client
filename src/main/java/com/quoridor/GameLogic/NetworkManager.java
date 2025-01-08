package com.quoridor.GameLogic;

import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class NetworkManager {
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private boolean connected;
    private String serverAddress;
    private int serverPort;
    private boolean simulatedDisconnect = false;

    public NetworkManager(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.connected = false;
    }

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

    public synchronized void sendMessage(String message) {
        try {
            output.println(message);
        } catch (Exception e) {
            System.err.println("Send error: " + e.getMessage());
        }
    }

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
            System.out.println("yo");
            return GameMessage.createLostConnection();
        }
    }

    public boolean isConnected() {
        return connected && !simulatedDisconnect;
    }

    public void simulateDisconnect() {
        simulatedDisconnect = true;
        connected = false;
    }

    public void simulateReconnect() {
        if (simulatedDisconnect) {
            simulatedDisconnect = false;
            connected = true;
        }
    }

    public static boolean validate_network_settings(String address, int port) {
        // I don't know how to validate an IP address, so I'll just check if it's not empty
        return address != null && !address.isEmpty() && port > 0 && port < 65536;
    }
}