package com.quoridor.GameLogic;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.quoridor.Settings.Constants;

import com.quoridor.UI.Components.GameBoard;
import com.quoridor.UI.Components.SquareUI;
import com.quoridor.UI.Components.WallUI;
import com.quoridor.UI.Windows.PopupWindow;


/**
 * MultiplayerGameManager class is responsible for managing the game state and handling the game loop for Multiplayer mode
 * It extends GameManager class to handle the game loop for Multiplayer mode -> the extension is barely used in the code it is more for accesing polymorphised methods by UI classes
 */
public class MultiplayerGameManager extends GameManager {
    // NetworkManager object for handling network communication
    private NetworkManager networkManager;
    // boolean to check if it is the player's turn
    private boolean isMyTurn;
    // player id
    private String playerId;
    // Board representing the game state
    private Board board;
    // GameBoard representing the game UI
    private GameBoard gameBoardUI;
    
    private Player currentPlayer;
    private List<Player> players;
    private WallUI[] doubleWall;

    // boolean to check if the game is running and set to false when the game ends to stop threads.
    private volatile boolean running = true;

    // booleans to check if the setup messages has been sent and received
    private boolean nameSent = false;
    private boolean welcomeReceived = false;

    // time of the last message
    private long lastHeartbeat;

    // Player object representing the disconnected player
    Player disconectedPlayer;

    public MultiplayerGameManager(Board board, GameBoard gameBoardUI, NetworkManager networkManager) {
        super(board, gameBoardUI);
        this.board = board;
        this.gameBoardUI = gameBoardUI;
        this.networkManager = networkManager;
        this.playerId = "";
        this.isMyTurn = false;
        this.doubleWall = new WallUI[2];
        initializeWalls();
        startHeartbeatChecker();
        lastHeartbeat = System.currentTimeMillis();
    }

    // Start the network listener thread
    // This thread listens for incoming messages from the server and handles them
    public void startNetworkListener() {
        new Thread(() -> {
            while (running) {
                if (networkManager.isConnected()) {
                    GameMessage message = networkManager.receiveMessage();
                    handleNetworkMessage(message);
                } else {
                    // Add small sleep to prevent CPU spinning when disconnected
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }).start();
    }

    // Stop the network listener thread
    public void stopNetworkListener() {
        running = false;
    }
    
    // Handle incoming network messages
    // This method is called by the network listener thread and is very important for the game to work
    private void handleNetworkMessage(GameMessage message) {
        if (message == null) return;

        // Update the last message time
        lastHeartbeat = System.currentTimeMillis();

        if (message.getType() == GameMessage.MessageType.ACK) { // Ignore ACK messages
            return;
        }

        if (!welcomeReceived) {
            if (message.getType() == GameMessage.MessageType.WELCOME) { // Check if the first message is WELCOME
                welcomeReceived = true;
            } else {
                handleError(GameMessage.createErrorMessage("Expected WELCOME message"));
                return;
            }
        } else if (!nameSent) {
            if (message.getType() == GameMessage.MessageType.NAME_REQUEST) { // Check if the second message is NAME_REQUEST
                handleNameRequest(message);
                return;
            } else {
                handleError(GameMessage.createErrorMessage("Expected NAME_REQUEST message"));
                return;
            }
        }
        
        // Handle the message based on its type
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
            case NAME_REQUEST:
                handleNameRequest(message);
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
            case PLAYER_DISCONNECTED:
                handlePlayerDisconnected(message);
                break;
            case PLAYER_RECONNECTED:
                handlePlayerReconnected(message);
                break;
            case LOST_CONNECTION:
                handleConnectionLoss(); // message isnt inmportant here
                break;
            default:
                System.out.println("Unknown message type: " + message.getType());
                break;
        }
    }

    // Handle connection loss by starting the reconnection loop in a new thread
    private void handleConnectionLoss() {
        if (!running) return;
        startReconnectionLoop();
    }

    // Start the reconnection loop in a new thread
    private void startReconnectionLoop() {
        new Thread(() -> {
            while (!networkManager.isConnected()) {
                try {
                    System.out.println("Attempting to reconnect...");
                    if (networkManager.connect()) {
                        System.out.println("Reconnected to the server.");
                        PopupWindow.showMessage("Reconnected to the server!");
                        startHeartbeatChecker();
                        break;
                    }
                    Thread.sleep(5000); // Wait for 5 seconds before retrying
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    // Handle message type PLAYER_RECONNECTED
    private void handlePlayerReconnected(GameMessage message) {
        Player[] players = board.getPlayers();
        for (Player player : players) {
            if (player.getId().equals(message.getReconnectedPlayerId())) {
                player.setConnected(true);
                disconectedPlayer = null;
                break;
            }
        }
        gameBoardUI.updateBoard();
    }

    // Handle message type PLAYER_DISCONNECTED
    private void handlePlayerDisconnected(GameMessage message) {
        Player[] players = board.getPlayers();
        for (Player player : players) {
            if (player.getId().equals(message.getDisconnectedPlayerId())) {
                player.setConnected(false);
                disconectedPlayer = player;
                break;
            }
        }
        gameBoardUI.updateBoard();
    }

    // Handle message type NAME_REQUEST
    private void handleNameRequest(GameMessage message) {
        nameSent = true;
        sendNameResponse(gameBoardUI.getMainWindow().getPlayerName());
    }

    // Handle message type WELCOME
    private void handleWelcome(GameMessage message) {
        sendAck();
    }

    // Handle message type WAITING
    private void handleWaiting(GameMessage message) {
        PopupWindow.showMessage("Waiting for other players to join...");
        sendAck();
    }

    // Handle message type GAME_STARTED
    private void handleGameStarted(GameMessage message) {
        findPlayerId(message); // Find player id based on the player name (this client)
        updateGameState(message); // Update the game state based on the message (should be starting position)
        sendAck();
    }

    // Find the player id based on the player name
    private void findPlayerId(GameMessage message) {
        String playerName = gameBoardUI.getMainWindow().getPlayerName();
        List<Player> players = message.getPlayers();
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                player.setName(playerName);
                playerId = message.getPlayerId(player);
                break;
            }
        }
    }

    // Handle the next turn message
    private void handleNextTurn(GameMessage message) {
        sendAck();
        setCurrentPlayer(message); // Set the current player based on the message
        updateGameState(message); // Update the game state
        if (isMyTurn(message)) {
            isMyTurn = true;
            highlightPossibleMoves(); // Highlight possible moves if it is the player's turn
        } else {
            isMyTurn = false;
            removeSelectedSquares(); // Remove selected squares if it is not the player's turn
            removeSelectedWalls(); // Remove selected walls if it is not the player's turn (this is not necessary, but it is nicer visualy)
        }
        gameBoardUI.updateBoard(); // Update the game board so it reflects the new game state
    }

    // Removes selected walls that are not placed (when user clicks a wall it is selected, but if the user doesn't place it, it should be unselected, after he makes another move)
    private void removeSelectedWalls() {
        List<WallUI> walls = gameBoardUI.getAllWalls();
        for (WallUI wall : walls) {
            if (wall.selected && !wall.placed) {
                wall.selected = false;
            }
        }
    }

    // Set the current player based on the message
    private void setCurrentPlayer(GameMessage message) {
        currentPlayer = message.getCurrentPlayer();
    }

    // Update the game state based on the message
    private void updateGameState(GameMessage message) {
        updatePlayers(message);
        updateBoard(message);
        updateWalls(message);
        gameBoardUI.updateBoard();
    }

    // Check if it is the player's turn based on the message
    private boolean isMyTurn(GameMessage message) {
        return message.getPlayerId(currentPlayer).equals(playerId);
    }

    // Place walls based on the new game state
    private void updateWalls(GameMessage message) {
        List<Position> horizontalWalls = message.getHorizontalWalls();
        List<Position> verticalWalls = message.getVerticalWalls();
        for (Position wall : horizontalWalls) {
            placeWallServer(wall, true);
        }
        for (Position wall : verticalWalls) {
            placeWallServer(wall, false);
        }
    }

    // Place a wall, but without checking if it is possible, because server is king
    private void placeWallServer(Position wall, boolean isHorizontal) {
        board.placeWall(wall, !isHorizontal);
    }

    // Update player objects
    private void updatePlayers(GameMessage message) {
        players = message.getPlayers();
        Player[] playersArray = new Player[players.size()];
        for (int i = 0; i < players.size(); i++) {
            playersArray[i] = players.get(i);
            if (disconectedPlayer != null && players.get(i).getName().equals(disconectedPlayer.getName())) {
                playersArray[i].setConnected(false);
            }
        }
        board.setPlayers(playersArray);
    }

    // Update the board based on the message
    private void updateBoard(GameMessage message) {
        String boardString = message.getBoardString();
        char[][] board = this.board.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = boardString.charAt(i * board.length + j);
            }
        }
    }

    // Find a player based on the id
    private Player getPlayerById(String id) {
        for (Player player : players) {
            if (player.getId().equals(id)) {
                return player;
            }
        }
        return null;
    }

    // Handle the end of the game
    private void handleGameEnd(GameMessage message) {
        Player winner = getPlayerById(message.getWinnerId());
        // Show who won
        if (winner == null) {
            PopupWindow.showMessage("Game ended. Winner: unknown");
        } else {
            PopupWindow.showMessage("Game ended. Winner: " + winner.getName());
        }
        updateBoard(message); // update board so the oponent can see the last move
        // clean up
        removeSelectedSquares();
        removeWallActionListener();
        gameBoardUI.updateBoard();
        stopNetworkListener(); // stop the network listener
        sendAck();

        // disconnect from the server
        networkManager.sendMessage(GameMessage.createAbandonMessage());
        networkManager.disconnect();
    }

    // Remove the action listener from the walls
    private void removeWallActionListener() {
        List<WallUI> walls = gameBoardUI.getAllWalls();
        for (WallUI wall : walls) {
            wall.removeMouseListener(wall.getMouseListener());
        }
    }
    // Handle the error message -> show the error message and disconnect from the server
    private void handleError(GameMessage message) {
        message.setMessage("Server sent error: " + message.getMessage());
        handleWrongMessage(message);
    }
    // Handle wrong message
    // clean up game and disconnect from the server + show the error message
    synchronized private void handleWrongMessage(GameMessage message) {
        stopNetworkListener();
        networkManager.disconnect();
        gameBoardUI.getMainWindow().getGamePanel().cleanupGame();
        gameBoardUI.getMainWindow().showCard(Constants.MENU_CARD);
        PopupWindow.showMessage(message.getMessage() + "\nDisconnecting from the server.");
    }

    private void handleAck(GameMessage message) {
        // pass
    }

    private void handleHeartbeat(GameMessage message) {
        sendAck();
    }

    private void handleNameResponse(GameMessage message) {
        sendAck();
    }

    public void nextTurn() {
        isMyTurn = !isMyTurn;
        if (!isMyTurn) {
            removeSelectedSquares();
        }
    }

    // Send the move to the server
    public void sendMove(Position position) {
        Map<String, Object> data = new HashMap<>();
        data.put("player_id", Integer.parseInt(playerId)-1);
        data.put("is_horizontal", false);
        
        data.put("position", "[" + position.getRow() + "," + position.getCol() + "]");
        
        GameMessage message = new GameMessage(GameMessage.MessageType.MOVE, data);
        networkManager.sendMessage(message);
    }
    
    // Send the wall placement to the server
    public void sendWallPlacement(Position wall1, Position wall2, boolean isHorizontal) {
        Map<String, Object> data = new HashMap<>();
        data.put("player_id", Integer.parseInt(playerId)-1);
        data.put("is_horizontal", isHorizontal);
        
        data.put("position", "[" + wall1.getRow() + "," + wall1.getCol() + "],[" + wall2.getRow() + "," + wall2.getCol() + "]");
        
        GameMessage message = new GameMessage(GameMessage.MessageType.MOVE, data);
        networkManager.sendMessage(message);
    }
    
    // Send the name response to the server
    public void sendNameResponse(String name) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        
        GameMessage message = new GameMessage(GameMessage.MessageType.NAME_RESPONSE, data);
        networkManager.sendMessage(message);
    }        

    // Send ACK message
    private void sendAck() {
        networkManager.sendMessage(GameMessage.createAckMessage());
    }

    // Set the wall action listener to work with the UI.
    public void setWallActionListener() {
        List<WallUI> walls = gameBoardUI.getAllWalls();
        for (WallUI wall : walls) {
            MouseListener listener = new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!isMyTurn) return;
                    if (doubleWall[0] == null) {
                        doubleWall[0] = wall;
                    } else if (doubleWall[1] == null) {
                        doubleWall[1] = wall;
                    }

                    if (possibleDoubleWall()) {
                        sendWallPlacement(doubleWall[0].getPosition(), doubleWall[1].getPosition(), !doubleWall[0].isVertical());
                        doubleWall[0].setSelected(false);
                        doubleWall[1].setSelected(false);
                        doubleWall[0] = null;
                        doubleWall[1] = null;
                    } else if (doubleWall[0] != null && doubleWall[1]!= null) {
                        doubleWall[0].setSelected(false);
                        doubleWall[1].setSelected(false);
                        doubleWall[0] = null;
                        doubleWall[1] = null;
                    }

                }
    
                @Override
                public void mousePressed(MouseEvent e) {
                }
    
                @Override
                public void mouseReleased(MouseEvent e) {
                }
    
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!isMyTurn) return;
                    if (!wall.isPlaced()) {
                        wall.setPlayerColor(currentPlayer.getPlayerColor().getColor());
                        wall.setSelected(true);
                    }
                }
    
                @Override
                public void mouseExited(MouseEvent e) {
                    if (!isMyTurn) return;
                    wall.setSelected(false);
                    if ((doubleWall[0] == null && doubleWall[1] != null && doubleWall[1].equals(wall)) || (doubleWall[0] != null && doubleWall[1] == null && doubleWall[0].equals(wall))
                    || (doubleWall[0] != null && doubleWall[1] != null && doubleWall[0].equals(wall)) || (doubleWall[0] != null && doubleWall[1] != null && doubleWall[1].equals(wall))) {
                        wall.setSelected(true);
                    }
                }
                
            };
            wall.addMouseListener(listener);
            wall.setMouseListener(listener);  // Store the listener
        }
    }
    
    // Check if the player can place a wall
    public boolean possibleDoubleWall() {
        if (doubleWall[0] == null || doubleWall[1] == null) return false;
    
        // Check that both walls exist, aren't the same, and are both unplaced
        if (doubleWall[0] != null && doubleWall[1] != null 
            && !doubleWall[0].equals(doubleWall[1]) 
            && doubleWall[0].isVertical() == doubleWall[1].isVertical()
            && !doubleWall[0].isPlaced() && !doubleWall[1].isPlaced()) {
            
            if (doubleWall[0].isVertical()) {
                return Math.abs(doubleWall[0].getRow() - doubleWall[1].getRow()) == 1 
                       && doubleWall[0].getCol() == doubleWall[1].getCol();
            }
            else {
                return Math.abs(doubleWall[0].getCol() - doubleWall[1].getCol()) == 1 
                       && doubleWall[0].getRow() == doubleWall[1].getRow();
            }
        }
        return false;
    }

    // Remove selected squares
    private void removeSelectedSquares() {
        List<SquareUI> squares = getAllSquares();
        if (squares == null) {
            return;
        }
        for (SquareUI square : squares) {
            square.removeMouseListener(square.getMouseListener());
            square.setMouseListener(null);
            square.setLightedUp(false);
        }
    }

    // Highlight possible moves
    private void highlightPossibleMoves() {
        if (!isMyTurn) {
            removeSelectedSquares();
            return;
        }
        setSquaresActionListener();
    }

    private List<SquareUI> getAllSquares() {
        SquareUI[][] squares = gameBoardUI.getSquares();
        List<SquareUI> allSquares = new ArrayList<>();
        for (SquareUI[] row : squares) {
            allSquares.addAll(Arrays.asList(row));
        }
        return allSquares;
    }

    public void cleanupGame() {
        stopNetworkListener();
        networkManager.disconnect();
    }

    // Set the action listener for the squares
    private void setSquaresActionListener() {
        List<Position> list = board.possibleMoves(currentPlayer);
        List<SquareUI> squares = gameBoardUI.setUpPossibleSquares(list);
        for (SquareUI square : squares) {
            if (square == null || square.getMouseListener() != null) { // this is a work around a current bug
                continue;
            }
            MouseListener listener = new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    sendMove(square.getPosition());
                }
        
                @Override
                public void mousePressed(MouseEvent e) {
                }
        
                @Override
                public void mouseReleased(MouseEvent e) {
                }
        
                @Override
                public void mouseEntered(MouseEvent e) {
                }
        
                @Override
                public void mouseExited(MouseEvent e) {
                }
            };
            square.addMouseListener(listener);
            square.setMouseListener(listener);  // Store the listener in SquareUI
        }
    }

    @Override
    public void gameLoop() {
        // Network game doesn't need local game loop
    }

    @Override
    public boolean movePlayer(Position position) {
        // Movement is handled by network messages
        return false;
    }

    @Override
    public boolean placeWall(int row1, int col1, int row2, int col2, boolean isHorizontal) {
        // Wall placement is handled by network messages
        return false;
    }

    // Start the heartbeat checker in a new thread
    private void startHeartbeatChecker() {
        new Thread(() -> {
            while (running) {
                if (networkManager.isConnected()) {
                    networkManager.sendMessage(GameMessage.createHeartbeatMessage());
                }
                if (networkManager.isConnected() && (System.currentTimeMillis() - lastHeartbeat > 10000)) {
                    PopupWindow.showMessage("Connection lost!");
                    handlePlayerDisconnected(GameMessage.createPlayerDisconnected(playerId));
                    break;
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }
}