package com.quoridor.UI.Windows;
import java.awt.CardLayout;
import java.util.List;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.quoridor.Settings.Constants;
import com.quoridor.UI.Components.GameBoard;
import com.quoridor.GameLogic.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class QuoridorApp extends JFrame {

    private JPanel mainPanel;

    private GamePanel gamePanel;

    private MenuPanel menuPanel;

    private SettingsPanel settingsPanel;

    private String playerName;

    private String playerName2;

    private boolean isConnecting = false;
    
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
        this.playerName2 = Constants.DEFAULT_PLAYER_2_NAME;
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
        board.getPlayers()[1].setName(getPlayerName2());
        
        GameBoard gb = new GameBoard(board, this);
        GameManager gm = new GameManager(board, gb);
        
        gb.setBoard(board);
        gamePanel.setGameBoard(gb);
        gamePanel.setGameManager(gm);
        Player[] players = board.getPlayers();
        List<Player> playerList = Arrays.asList(players);
        gamePanel.updatePlayerPanels(playerList);
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
        gamePanel.setNetworkManager(networkManager);
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

    public String getPlayerName2() {
        return playerName2;
    }

    public void setPlayerName2(String playerName2) {
        this.playerName2 = playerName2;
    }

    public void tryNewMultiplayerGame() {
        if (isConnecting) {
            PopupWindow.showMessage("Already trying to connect. Please wait.");
            return;
        }

        if (playerName.equals(Constants.DEFAULT_PLAYER_NAME)) {
            PopupWindow.showMessage("Please enter a name first. You can change it in the settings menu.");
            return;
        }

        isConnecting = true;
        new Thread(() -> {
            String address = "";
            int port = 0;

            try (BufferedReader br = new BufferedReader(new FileReader("../connection_settings.txt"))) {
                address = br.readLine().trim();
                port = Integer.parseInt(br.readLine().trim());
            } catch (IOException e) {
                PopupWindow.showMessage("Could not read connection settings. Please check your settings file.");
                return;
            }
            if (!NetworkManager.validate_network_settings(address, port)) {
                PopupWindow.showMessage("Invalid connection settings. Please check your connection settings file.");
                isConnecting = false;
                return;
            }
            NetworkManager networkManager = new NetworkManager(address, port);

            // check connection
            if (!networkManager.connect()) {
                PopupWindow.showMessage("Could not connect to server. Please check your connection settings.");
                isConnecting = false;
                return;
            }

            startMultiplayerGame(networkManager);
            showCard(Constants.GAME_ON_CARD);
            isConnecting = false;
        }).start();
    }
}

