package com.quoridor.UI.Windows;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.quoridor.GameLogic.Player;

/**
 * Panel that displays information about a player. It is shown in GamePanel next to the game board.
*/
public class PlayerPanel extends JPanel {
    // Player object to display information about
    Player player;

    // Constructor
    public PlayerPanel(Player p) {
        this.player = p;
        setPlayerPanel();
    }

    // Set up the panel to display player information
    private void setPlayerPanel() {
        this.setLayout(new GridLayout(4, 1));
        if (player == null) {
            displayEmptyPlayer();
        } else {
            displayPlayer();
        }
    }

    // Display default message when no player is set
    private void displayEmptyPlayer() {
        this.add(new JLabel("No Player"), SwingConstants.CENTER);
        this.add(new JLabel("Walls: N/A"), SwingConstants.CENTER);
        this.add(new JLabel("Status: Not Connected"), SwingConstants.CENTER);
        this.add(new JLabel("Color: N/A"), SwingConstants.CENTER);
    }

    // Display player information
    private void displayPlayer() {
        // Display player name
        JLabel nameLabel = new JLabel("Player: " + player.getName(), SwingConstants.CENTER);
        nameLabel.setBorder(new LineBorder(Color.BLACK, 1));
        this.add(nameLabel);

        // Display number of walls
        JLabel wallsLabel = new JLabel("Walls: " + player.getNumberOfWalls(), SwingConstants.CENTER);
        wallsLabel.setBorder(new LineBorder(Color.BLACK, 1));
        this.add(wallsLabel);

        // Display connection status
        // if disconected change color to red
        JLabel statusLabel = new JLabel("Status: " + (player.isConnected() ? "Connected" : "Disconnected"), SwingConstants.CENTER);
        statusLabel.setBorder(new LineBorder(Color.BLACK, 1));
        if (!player.isConnected()) {
            statusLabel.setForeground(Color.RED);
        }
        this.add(statusLabel);

        // Display player color
        JLabel colorLabel = new JLabel("Color:" + player.getPlayerColor().getColorName(), SwingConstants.CENTER);
        colorLabel.setBorder(new LineBorder(Color.BLACK, 1));
        colorLabel.setForeground(player.getPlayerColor().getColor()); // Assuming player has a getColor() method returning Color
        this.add(colorLabel);
    }

    // Update player information displayed in the panel
    // called when player info changes
    public void updatePlayerInfo() {
        this.removeAll();  // Clear existing components
        displayPlayer();   // Redisplay with updated info
        this.revalidate();
        this.repaint();
    }

    // Set the player to display information about
    public void setPlayer(Player p) {
        this.player = p;
    }

    
}
