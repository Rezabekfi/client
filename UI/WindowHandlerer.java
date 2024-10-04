package UI;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Settings.Constants;

public class WindowHandlerer extends JFrame {

    private JPanel mainPanel;
    
    public WindowHandlerer() {
        createWindow(this, Constants.GAME_NAME, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        this.mainPanel = new JPanel();
        mainPanel.setLayout(new CardLayout());
        
         // Create two JPanels representing different game screens
        JPanel menuPanel = new MenuPanel(this);

        JPanel gamePanel = new GamePanel(this);

        JPanel settingsPanel = new SettingsPanel(this);

        // Add both panels to the frame (CardLayout will handle switching between them)
        mainPanel.add(menuPanel, Constants.MENU_CARD);
        mainPanel.add(gamePanel, Constants.GAME_ON_CARD);
        mainPanel.add(settingsPanel, Constants.SETTINGS_CARD);

        this.add(mainPanel);
        this.setVisible(true);
    }

    public void showCard(String panel) {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, panel);
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
}
