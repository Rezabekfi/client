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

    public boolean connect() {
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
        try {
            String jsonString = message.toJSON();
            if (message.getType() != GameMessage.MessageType.ACK && message.getType() != GameMessage.MessageType.HEARTBEAT) {
                //System.out.println("Sending message: " + jsonString);
            }
            System.out.println("Sending message: " + jsonString);
            output.println(jsonString);
        } catch (Exception e) {
            System.err.println("Send error: " + e.getMessage());
        }
    }

    public GameMessage receiveMessage() {
        try {
            String jsonString = input.readLine();
            if (jsonString == null || jsonString.trim().isEmpty()) {
                GameMessage invalidMessage = new GameMessage("Recieved nothing from server (wierdge)"); // TODO: make profesional
                return invalidMessage;
            }
            
            // Validate that we have a proper JSON string
            jsonString = jsonString.trim();
            if (!jsonString.startsWith("{")) {
                GameMessage invalidMessage = new GameMessage("Invalid JSON format received: " + jsonString);
                System.err.println("Invalid JSON format received: " + jsonString);
                return invalidMessage;
            }
            if (GameMessage.fromJSON(jsonString).getType() != GameMessage.MessageType.ACK &&
            GameMessage.fromJSON(jsonString).getType() != GameMessage.MessageType.HEARTBEAT) System.out.println("Received message: " + jsonString);
            return GameMessage.fromJSON(jsonString);
        } catch (Exception e) {
            System.err.println("Receive error: " + e.getMessage());
            connected = false;
            return null;
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
}