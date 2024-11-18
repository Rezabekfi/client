package com.quoridor.GameLogic;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.quoridor.UI.Components.GameBoard;
import com.quoridor.UI.Components.SquareUI;
import com.quoridor.UI.Components.WallUI;
import com.quoridor.UI.Windows.PopupWindow;

// would be better if class would extend another but because of my coding "style" I wont do it now (possible future improvment)

public class MultiplayerGameManager extends GameManager {
    private NetworkManager networkManager;
    private boolean isMyTurn;
    private String playerId;
    private Board board;
    private GameBoard gameBoardUI;
    private Player currentPlayer;
    private List<Player> players;
    private WallUI[] doubleWall;
    private volatile boolean running = true;

    public MultiplayerGameManager(Board board, GameBoard gameBoardUI, NetworkManager networkManager) {
        super(board, gameBoardUI);
        this.board = board;
        this.gameBoardUI = gameBoardUI;
        this.networkManager = networkManager;
        this.playerId = "";
        this.isMyTurn = false;
        this.doubleWall = new WallUI[2];
        initializeWalls();
    }

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

    public void stopNetworkListener() {
        running = false;
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
            default:
                System.out.println("Unknown message type: " + message.getType());
                break;
        }
    }

    private void handlePlayerReconnected(GameMessage message) {
        Player[] players = board.getPlayers();
        for (Player player : players) {
            if (player.getId().equals(message.getReconnectedPlayerId())) {
                player.setConnected(true);
                break;
            }
        }
        gameBoardUI.updateBoard();
    }

    private void handlePlayerDisconnected(GameMessage message) {
        Player[] players = board.getPlayers();
        for (Player player : players) {
            if (player.getId().equals(message.getDisconnectedPlayerId())) {
                player.setConnected(false);
                break;
            }
        }
        gameBoardUI.updateBoard();
    }

    private void handleNameRequest(GameMessage message) {
        sendNameResponse(gameBoardUI.getMainWindow().getPlayerName());
    }

    private void handleWelcome(GameMessage message) {
        sendAck();
    }

    private void handleWaiting(GameMessage message) {
        PopupWindow.showMessage("Waiting for other players to join...");
        sendAck();
    }

    private void handleGameStarted(GameMessage message) {
        findPlayerId(message);
        updateGameState(message);
        sendAck();
    }

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

    private void handleNextTurn(GameMessage message) {
        sendAck();
        setCurrentPlayer(message);
        updateGameState(message);
        if (isMyTurn(message)) {
            isMyTurn = true;
            highlightPossibleMoves();
        } else {
            isMyTurn = false;
            removeSelectedSquares();
        }
        gameBoardUI.updateBoard();
    }

    private void setCurrentPlayer(GameMessage message) {
        currentPlayer = message.getCurrentPlayer();
    }

    private void updateGameState(GameMessage message) {
        updatePlayers(message);
        updateBoard(message);
        updateWalls(message);
        gameBoardUI.updateBoard();
    }

    private boolean isMyTurn(GameMessage message) {
        return message.getPlayerId(currentPlayer).equals(playerId);
    }

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

    private void placeWallServer(Position wall, boolean isHorizontal) {
        board.placeWall(wall, !isHorizontal);
    }

    private void updatePlayers(GameMessage message) {
        players = message.getPlayers();
        Player[] playersArray = new Player[players.size()];
        for (int i = 0; i < players.size(); i++) {
            playersArray[i] = players.get(i);
        }
        board.setPlayers(playersArray);
    }

    private void updateBoard(GameMessage message) {
        String boardString = message.getBoardString();
        char[][] board = this.board.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = boardString.charAt(i * board.length + j);
            }
        }
    }

    private Player getPlayerById(String id) {
        for (Player player : players) {
            if (player.getId().equals(id)) {
                return player;
            }
        }
        return null;
    }

    private void handleGameEnd(GameMessage message) {
        Player winner = getPlayerById(message.getWinnerId());
        if (winner == null) {
            PopupWindow.showMessage("Game ended. Winner: unknown");
        } else {
            PopupWindow.showMessage("Game ended. Winner: " + winner.getName());
        }

        removeSelectedSquares();
        removeWallActionListener();
        gameBoardUI.updateBoard();
        stopNetworkListener();
        sendAck();
    }

    private void removeWallActionListener() {
        List<WallUI> walls = gameBoardUI.getAllWalls();
        for (WallUI wall : walls) {
            wall.removeMouseListener(wall.getMouseListener());
        }
    }

    private void handleError(GameMessage message) {
        // something went wrong ig end game and return to lobby
        //TODO: implement
    }

    private void handleWrongMessage(GameMessage message) {
        System.out.println("Wrong message type: " + message.getType());
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

    public void sendMove(Position position) {
        Map<String, Object> data = new HashMap<>();
        data.put("player_id", Integer.parseInt(playerId)-1);
        data.put("is_horizontal", false);
        
        // Create position array in format [[x,y]]
        int[][] positionArray = new int[][]{{position.getRow(), position.getCol()}};
        data.put("position", positionArray);
        
        GameMessage message = new GameMessage(GameMessage.MessageType.MOVE, data);
        networkManager.sendMessage(message);
    }
    
    public void sendWallPlacement(Position wall1, Position wall2, boolean isHorizontal) {
        Map<String, Object> data = new HashMap<>();
        data.put("player_id", Integer.parseInt(playerId)-1);
        data.put("is_horizontal", isHorizontal);
        
        // Create position array in format [[x1,y1], [x2,y2]]
        int[][] positionArray = new int[][]{
            {wall1.getRow(), wall1.getCol()},
            {wall2.getRow(), wall2.getCol()}
        };
        data.put("position", positionArray);
        
        GameMessage message = new GameMessage(GameMessage.MessageType.MOVE, data);
        networkManager.sendMessage(message);
    }
    
    public void sendNameResponse(String name) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        
        GameMessage message = new GameMessage(GameMessage.MessageType.NAME_RESPONSE, data);
        networkManager.sendMessage(message);
    }

    private void sendAck() {
        networkManager.sendMessage(GameMessage.createAckMessage());
    }

    // Override other necessary methods

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

    private void removeSelectedSquares() {
        List<SquareUI> squares = getAllSquares();
        if (squares == null) {
            return;
        }
        for (SquareUI square : squares) {
            square.removeMouseListener(square.getMouseListener());
            square.setLightedUp(false);
        }
    }

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

    private void setSquaresActionListener() {
        List<Position> list = board.possibleMoves(currentPlayer);
        List<SquareUI> squares = gameBoardUI.setUpPossibleSquares(list);
        for (SquareUI square : squares) {
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
} 