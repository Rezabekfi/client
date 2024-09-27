import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    public Menu() {
        // 
        WindowHandlerer.createWindow(this, "Quoridor", 1000, 600);
        
        // Create the main panel with BorderLayout
        JPanel menuPanel = new JPanel(new BorderLayout());

        // Create another panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 1, 1));  // 4 rows, 1 column, 10px vertical spacing
        // Create buttons
        JButton playSoloButton = new JButton("Play Offline");
        JButton playMultiplayerButton = new JButton("Play Multiplayer");
        JButton settingsButton = new JButton("Settings");
        JButton exitButton = new JButton("Exit");

        // Add buttons to the button panel
        buttonPanel.add(playSoloButton);
        buttonPanel.add(playMultiplayerButton);
        buttonPanel.add(settingsButton);
        buttonPanel.add(exitButton);
        menuPanel.add(buttonPanel, BorderLayout.SOUTH);   // Buttons panel placed at the bottom (south)

        // Add the main panel to the frame
        add(menuPanel);
    }
}
