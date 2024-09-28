package UI;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Settings.Constants;

public class WindowHandlerer extends JFrame {

    private CardLayout cl;
    
    public WindowHandlerer() {
        createWindow(this, Constants.GAME_NAME, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new CardLayout());
        
         // Create two JPanels representing different game screens
        JPanel menuPanel = new MenuPanel(mainPanel);

        JPanel gamePanel = new GamePanel(mainPanel);
        JLabel gameLabel = new JLabel("Game is running...");
        gamePanel.add(gameLabel);

        JPanel settingsPanel = new SettingsPanel(mainPanel);

        // Add both panels to the frame (CardLayout will handle switching between them)
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(gamePanel, "Game");

        this.add(mainPanel);


        this.setVisible(true);

    }



    public static void createWindow(JFrame window, String title, int width, int height) {
        window.setTitle(title);
        window.setSize(width, height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
    }
}
