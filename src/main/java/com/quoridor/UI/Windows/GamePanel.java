package com.quoridor.UI.Windows;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.quoridor.GameLogic.GameManager;
import com.quoridor.GameLogic.MultiplayerGameManager;
import com.quoridor.GameLogic.GameMessage;
import com.quoridor.Settings.Constants;
import com.quoridor.UI.Components.GameBoard;
import com.quoridor.GameLogic.Player;
import com.quoridor.GameLogic.NetworkManager;

/**
 * Panel that displays the game board and player information. It is shown when the game is running.
 */
public class GamePanel extends JPanel {

    // Reference to the main window
    private QuoridorApp mainWindow;
    
    // Game board that will be shown in the panel (UI component)
    private GameBoard gb;

    // Game manager that will be used to run the game (singleplayer/multiplayer)
    private GameManager gm;

    // Player panels that will display information about the players next to the game board
    private PlayerPanel p1;
    private PlayerPanel p2;

    // Network manager that will be used to communicate with the server, if the game is multiplayer
    private NetworkManager networkManager;
    // Flag to check if the game is multiplayer
    private boolean isMultiplayerGame;

    // Constructor (setting up the graphical interface)
    public GamePanel(QuoridorApp mainWindow) {
        this.mainWindow = mainWindow;
        this.setLayout(null);
        this.isMultiplayerGame = false;

        // Set up the title of the game
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel title = new JLabel("GAME", SwingConstants.CENTER);
        titlePanel.add(title);
        titlePanel.setBounds(0, 0, this.mainWindow.getWidth(), this.mainWindow.getHeight()/12);
        titlePanel.setBackground(Color.GRAY);

        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Abandon Game");
        backButton.addActionListener(backToMenuAction());
        buttonPanel.add(backButton);
        buttonPanel.setBounds(0, (int)(this.mainWindow.getHeight()*5.0/6), this.mainWindow.getWidth(), (int)(this.mainWindow.getHeight()/6.0));
        buttonPanel.setBackground(Color.GRAY);
        this.add(buttonPanel);
    }

    // Action listener for the back button
    public ActionListener backToMenuAction() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isMultiplayerGame) {
                    cleanupGame();
                } else {
                    // send abandon message that will delete user from waiting players and also from game
                    // will act as a surrender button and an disconect button
                    networkManager.sendMessage(GameMessage.createAbandonMessage());
                    networkManager.disconnect();
                    // if gm is instance of multiplayerGameManager then stop the network listener
                    if (gm instanceof MultiplayerGameManager) ((MultiplayerGameManager) gm).stopNetworkListener();
                    cleanupGame();
                }
                mainWindow.showCard(Constants.MENU_CARD);
            }
        };
        return al;
    }
    
    @Override
    public void repaint() {
        super.repaint();

    }

    // Method to update the player panels with new player information
    public void updatePlayerPanels(List<Player> players) {
        if (p1 == null || p2 == null) {
            createPlayerPanels(players);
        } else {
            p1.setPlayer(players.get(0));
            p2.setPlayer(players.get(1));
            p1.updatePlayerInfo();
            p2.updatePlayerInfo();
        }
    }

    // Method to create the player panels
    public void createPlayerPanels(List<Player> players) {
        p1 = new PlayerPanel(players.get(0));
        p2 = new PlayerPanel(players.get(1));
        p1.setBounds(0,(int)((1.0/12.0)*this.mainWindow.getHeight()), 
                    (int)((this.mainWindow.getWidth()-(3.0/4.0)*this.mainWindow.getHeight())/2), 
                    (int)((3.0/4)*this.mainWindow.getHeight()));
        p2.setBounds((int)((this.mainWindow.getWidth() - (3.0/4)*this.mainWindow.getHeight())/2 + (3.0/4)*this.mainWindow.getHeight()),
                    (int)((1.0/12)*this.mainWindow.getHeight()), 
                    (int)((this.mainWindow.getWidth()-(3.0/4)*this.mainWindow.getHeight())/2), 
                    (int)((3.0/4)*this.mainWindow.getHeight()));
        this.add(p1);
        this.add(p2);
    }

    // Method to clean up the game components
    public void cleanupGame() {
        if (p1 != null) remove(p1);
        if (p2 != null) remove(p2);
        if (gb != null) remove(gb);
        gb = null;
        gm = null;
        p1 = null;
        p2 = null;
        networkManager = null;
        isMultiplayerGame = false;
        repaint();
    }

    // Getters and setters
    public QuoridorApp getmainWindow() {
        return mainWindow;
    }

    public GameBoard getGameBoard() {
        return gb;
    }

    public void setGameBoard(GameBoard gb) {
        this.gb = gb;
        this.add(gb);
        gb.setBounds((int)(this.mainWindow.getWidth()/2 - this.mainWindow.getHeight()*3.0/8.0), 
                     (int)(this.mainWindow.getHeight()/12), 
                     (int)(this.mainWindow.getHeight()*3.0/4), 
                     (int)(this.mainWindow.getHeight()*3.0/4));
    }

    public GameManager getGameManager() {
        return gm;
    }

    public void setGameManager(GameManager gm) {
        this.gm = gm;
    }

    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
        this.isMultiplayerGame = true;
    }
}
