package com.quoridor.GameLogic;

import java.util.HashMap;
import java.util.Map;

import com.quoridor.UI.Components.GameBoard;
import com.quoridor.UI.Windows.PopupWindow;

public class MultiplayerGameManager extends GameManager {
    private NetworkManager networkManager;
    private boolean isMyTurn;
    private String playerId;
    
    public MultiplayerGameManager(Board board, GameBoard gameBoardUI, NetworkManager networkManager) {
        super(board, gameBoardUI);
        this.networkManager = networkManager;
        playerId = ""; // emtpy value until we recieve a game started message
        startNetworkListener();
    }

    private void startNetworkListener() {
        new Thread(() -> {
            while (networkManager.isConnected()) {
                GameMessage message = networkManager.receiveMessage();
                handleNetworkMessage(message);
            }
        }).start();
    }

    private void handleNetworkMessage(GameMessage message) {
        if (message == null) return;
        
        switch (message.getType()) {
            case NEXT_TURN:
                handleNextTurn(message);
                break;
            case GAME_ENDED:
                handleGameEnd(message);
                break;
            case ERROR:
                handleError(message);
                break;
            case WRONG_MESSAGE:
                handleWrongMessage(message);
                break;
            case ACK:
                handleAck(message);
                break;
            case HEARTBEAT:
                handleHeartbeat(message);
                break;
            case NAME_RESPONSE:
                handleNameResponse(message);
                break;
            case WELCOME:
                handleWelcome(message);
                break;
            case WAITING:
                handleWaiting(message);
                break;
            case GAME_STARTED:
                handleGameStarted(message);
                break;
            default:
                System.out.println("Unknown message type: " + message.getType());
                break;
        }
    }

    private void handleWelcome(GameMessage message) {
        responseACK();
    }

    private void responseACK() {
        GameMessage message = new GameMessage(GameMessage.MessageType.ACK, new HashMap<>());
        networkManager.sendMessage(message);
    }

    private void handleWaiting(GameMessage message) {
        PopupWindow.showMessage("Waiting for other players to join...");
        // maybe add a timer
    }

    private void handleGameStarted(GameMessage message) {
        findPlayerId(message);
    }

    private void findPlayerId(GameMessage message) {
        // find player id in the message
        Board board = super.getGameBoardUI().getBoard();
        Player[] players = board.getPlayers();
        // find player name in the message that matches name setted in the main window
        String playerName = super.getGameBoardUI().getMainWindow().getPlayerName();
        
    }

    private void handleNextTurn(GameMessage message) {
        String currentPlayerId = message.getStringData("current_player_id");
        isMyTurn = playerId.equals(currentPlayerId);
        
        // Update board state
        updateGameState(message);
        super.nextTurn();
    }

    private void updateGameState(GameMessage message) {
        // Update board based on message data
        String boardString = message.getStringData("board");
        // Update walls
        // Update player positions
        // etc.
        
    }

    private void handleGameEnd(GameMessage message) {
        //TODO: implement
    }

    private void handleError(GameMessage message) {
        //TODO: implement
    }

    private void handleWrongMessage(GameMessage message) {
        //TODO: implement
    }

    private void handleAck(GameMessage message) {
        //TODO: implement
    }

    private void handleHeartbeat(GameMessage message) {
        //TODO: implement
    }

    private void handleNameResponse(GameMessage message) {
        //TODO: implement
    }

    @Override
    public void nextTurn() {
        super.nextTurn();
        isMyTurn = !isMyTurn;
    }

    public void sendMove(Position position) {
        Map<String, Object> data = new HashMap<>();
        data.put("player_id", playerId);
        data.put("is_horizontal", false);
        data.put("position", new Position[]{position});
        
        GameMessage message = new GameMessage(GameMessage.MessageType.MOVE, data);
        networkManager.sendMessage(message);
    }
    
    public void sendWallPlacement(Position wall1, Position wall2, boolean isHorizontal) {
        Map<String, Object> data = new HashMap<>();
        data.put("player_id", playerId);
        data.put("is_horizontal", isHorizontal);
        data.put("position", new Position[]{wall1, wall2});
        
        GameMessage message = new GameMessage(GameMessage.MessageType.MOVE, data);
        networkManager.sendMessage(message);
    }
    
    public void sendNameResponse(String name) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        
        GameMessage message = new GameMessage(GameMessage.MessageType.NAME_RESPONSE, data);
        networkManager.sendMessage(message);
    }

    @Override
    public boolean movePlayer(Position position) {
        if (!isMyTurn) return false;
        
        sendMove(position);
        return true;
    }

    @Override
    public boolean placeWall(int row1, int col1, int row2, int col2, boolean isVertical) {
        if (!isMyTurn) return false;
        
        Position wall1 = new Position(row1, col1);
        Position wall2 = new Position(row2, col2);
        sendWallPlacement(wall1, wall2, !isVertical); // Note: server uses horizontal instead of vertical
        return true;
    }

    // Override other necessary methods
} 