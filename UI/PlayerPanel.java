package UI;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import GameLogic.Player;

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
        this.add(nameLabel);

        // Display number of walls
        JLabel wallsLabel = new JLabel("Walls: " + player.getNumberOfWalls(), SwingConstants.CENTER);
        this.add(wallsLabel);

        // Display connection status
        JLabel statusLabel = new JLabel("Status: " + (connected ? "Connected" : "Disconnected"), SwingConstants.CENTER);
        this.add(statusLabel);

        // Display player color
        JLabel colorLabel = new JLabel("Color:", SwingConstants.CENTER);
        colorLabel.setForeground(player.getColor_2d()); // Assuming player has a getColor() method returning Color
        this.add(colorLabel);
    }
}
