package com.quoridor.UI.Windows;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.quoridor.GameLogic.GameManager;
import com.quoridor.Settings.Constants;
import com.quoridor.UI.Components.GameBoard;
import com.quoridor.GameLogic.Player;
import com.quoridor.GameLogic.NetworkManager;

public class GamePanel extends JPanel {

    private QuoridorApp mainWindow;
    private GameBoard gb;
    private GameManager gm;
    private PlayerPanel p1;
    private PlayerPanel p2;
    private NetworkManager networkManager;
    private boolean isMultiplayerGame;

    public GamePanel(QuoridorApp mainWindow) {
        this.mainWindow = mainWindow;
        this.setLayout(null);
        this.isMultiplayerGame = false;

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel title = new JLabel("GAME", SwingConstants.CENTER);
        titlePanel.add(title);
        titlePanel.setBounds(0, 0, this.mainWindow.getWidth(), this.mainWindow.getHeight()/12);
        titlePanel.setBackground(Color.GRAY);

        // Adding the key listener to the title panel -> only for testing in "production" should be removed
        titlePanel.setFocusable(true);
        titlePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!isMultiplayerGame) return;
                
                if (e.getKeyCode() == KeyEvent.VK_D) { // 'D' for disconnect
                    simulateDisconnect();
                } else if (e.getKeyCode() == KeyEvent.VK_C) { // 'C' for connect
                    simulateReconnect();
                }
            }
        });
        this.add(titlePanel);

        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back to menu.");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cleanupGame();
                mainWindow.showCard(Constants.MENU_CARD);
            }
        });
        buttonPanel.add(backButton);
        buttonPanel.setBounds(0, (int)(this.mainWindow.getHeight()*5.0/6), this.mainWindow.getWidth(), (int)(this.mainWindow.getHeight()/6.0));
        buttonPanel.setBackground(Color.GRAY);
        this.add(buttonPanel);
    }

    public void setUpTitle() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel title = new JLabel("GAME", SwingConstants.CENTER);
        titlePanel.add(title);
        titlePanel.setBounds(0, 0, this.mainWindow.getWidth(), this.mainWindow.getHeight()/12);
        titlePanel.setBackground(Color.GRAY);
        this.add(titlePanel);
    }
    
    @Override
    public void repaint() {
        super.repaint();

    }

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

    public void cleanupGame() {
        if (p1 != null) remove(p1);
        if (p2 != null) remove(p2);
        if (gb != null) remove(gb);
        gb = null;
        gm = null;
        p1 = null;
        p2 = null;
        repaint();
    }

    private void simulateDisconnect() {
        if (networkManager != null) {
            SwingUtilities.invokeLater(() -> {
                networkManager.simulateDisconnect();
                PopupWindow.showMessage("Simulated Disconnect");
                if (p1 != null) p1.updatePlayerInfo();
                if (p2 != null) p2.updatePlayerInfo();
                revalidate();
                repaint();
            });
        }
    }

    private void simulateReconnect() {
        if (networkManager != null) {
            SwingUtilities.invokeLater(() -> {
                networkManager.simulateReconnect();
                PopupWindow.showMessage("Simulated Reconnect");
                if (p1 != null) p1.updatePlayerInfo();
                if (p2 != null) p2.updatePlayerInfo();
                revalidate();
                repaint();
            });
        }
    }

    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
        this.isMultiplayerGame = true;
    }
}
