package UI.Windows;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import GameLogic.Player;
import Settings.ColorUtils;

public class PlayerPanel extends JPanel {

    Player player;
    boolean connected;

    public PlayerPanel(Player p, boolean connected) {
        this.player = p;
        this.connected = connected;
        setPlayerPanel();
    }

    private void setPlayerPanel() {
        this.setLayout(new GridLayout(4, 1));
        if (player == null) {
            displayEmptyPlayer();
        } else {
            displayPlayer();
        }
    }

    private void displayEmptyPlayer() {
        this.add(new JLabel("No Player"), SwingConstants.CENTER);
        this.add(new JLabel("Walls: N/A"), SwingConstants.CENTER);
        this.add(new JLabel("Status: Not Connected"), SwingConstants.CENTER);
        this.add(new JLabel("Color: N/A"), SwingConstants.CENTER);
    }

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
        JLabel statusLabel = new JLabel("Status: " + (connected ? "Connected" : "Disconnected"), SwingConstants.CENTER);
        statusLabel.setBorder(new LineBorder(Color.BLACK, 1));
        this.add(statusLabel);

        // Display player color
        JLabel colorLabel = new JLabel("Color:" + ColorUtils.getColorName(player.getColor_2d()), SwingConstants.CENTER);
        colorLabel.setBorder(new LineBorder(Color.BLACK, 1));
        colorLabel.setForeground(player.getColor_2d()); // Assuming player has a getColor() method returning Color
        this.add(colorLabel);
    }

    public void updatePlayerInfo() {
        this.removeAll();  // Clear existing components
        displayPlayer();   // Redisplay with updated info
        this.revalidate();
        this.repaint();
    }

    
}
