package com.quoridor.UI.Windows;
import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.JSONObject;

import com.quoridor.Settings.FileManager;

import com.quoridor.Settings.Constants;
import com.quoridor.UI.Components.GameBoard;
import com.quoridor.GameLogic.*;

public class QuoridorApp extends JFrame {

    private JPanel mainPanel;

    private GamePanel gamePanel;

    private MenuPanel menuPanel;

    private SettingsPanel settingsPanel;

    private String playerName;
    
    public QuoridorApp() {
        createWindow(this, Constants.GAME_NAME, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        this.mainPanel = new JPanel();
        mainPanel.setLayout(new CardLayout());
        
         // Create two JPanels representing different game screens
        this.menuPanel = new MenuPanel(this);

        this.gamePanel = new GamePanel(this);

        this.settingsPanel = new SettingsPanel(this);

        // Add both panels to the frame (CardLayout will handle switching between them)
        mainPanel.add(this.menuPanel, Constants.MENU_CARD);
        mainPanel.add(this.gamePanel, Constants.GAME_ON_CARD);
        mainPanel.add(this.settingsPanel, Constants.SETTINGS_CARD);
        this.playerName = Constants.DEFAULT_PLAYER_NAME;
        this.add(this.mainPanel);
        this.setVisible(true);
    }

    public void showCard(String panel) {
        CardLayout cl = (CardLayout) this.mainPanel.getLayout();
        cl.show(this.mainPanel, panel);
    }

    public void startNewGame(int startingPlayer) {
        // Clean up any existing game
        gamePanel.cleanupGame();
        
        // Create new game components
        Board board = new Board();
        
        // Set up player names BEFORE creating GameBoard
        board.getPlayers()[0].setName(getPlayerName());
        board.getPlayers()[1].setName("Player1");
        
        GameBoard gb = new GameBoard(board, this);
        GameManager gm = new GameManager(board, gb);
        
        gb.setBoard(board);
        gamePanel.setGameBoard(gb);
        gamePanel.setGameManager(gm);
        gamePanel.updatePlayerPanels();
        gb.updateBoard();
        if (startingPlayer >= board.getNumberOfPlayers()) {
            startingPlayer = 0;
        }
        gm.setCurrentPlayer(board.getPlayers()[startingPlayer], startingPlayer);
        gm.gameLoop();
    }

    public void startMultiplayerGame(NetworkManager networkManager) {
        // Clean up any existing game
        gamePanel.cleanupGame();

        Board board = new Board();
        GameBoard gb = new GameBoard(board, this);
        MultiplayerGameManager gm = new MultiplayerGameManager(board, gb, networkManager);

        gb.setBoard(board);
        gamePanel.setGameBoard(gb);
        gamePanel.setGameManager(gm);
        gm.startNetworkListener();

    }

    public static void createWindow(JFrame window, String title, int width, int height) {
        window.setTitle(title);
        window.setSize(width, height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public MenuPanel getMenuPanel() {
        return menuPanel;
    }

    public SettingsPanel getSettingsPanel() {
        return settingsPanel;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void tryNewMultiplayerGame() {
        if (playerName.equals(Constants.DEFAULT_PLAYER_NAME)) {
            PopupWindow.showMessage("Please enter a name first. You can change it in the settings menu.");
            return;
        }
        JSONObject connectionSettings = FileManager.readJSONFromFile("connection_settings.txt");
        NetworkManager networkManager = new NetworkManager(connectionSettings.getString("address"), connectionSettings.getInt("port"));
        
        if (networkManager.connect()) {
            // Wait for initial messages
            GameMessage welcomeMsg = networkManager.receiveMessage();
            if (welcomeMsg != null && welcomeMsg.getType() == GameMessage.MessageType.WELCOME) {
                GameMessage nameRequest = networkManager.receiveMessage();
                if (nameRequest != null && nameRequest.getType() == GameMessage.MessageType.NAME_REQUEST) {
                    // Send name response
                    Map<String, Object> data = new HashMap<>();
                    data.put("name", playerName);
                    networkManager.sendMessage(new GameMessage(GameMessage.MessageType.NAME_RESPONSE, data));
                    
                    startMultiplayerGame(networkManager);
                    showCard(Constants.GAME_ON_CARD);
                }
            }
        } else {
            PopupWindow.showMessage("Failed to connect to server. Please check your connection settings.");
        }
    }
}

