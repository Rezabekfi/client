package UI;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Settings.Constants;

public class QuoridorApp extends JFrame {

    private JPanel mainPanel;

    private GamePanel gamePanel;

    private MenuPanel menuPanel;

    private SettingsPanel settingsPanel;
    
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

        this.add(this.mainPanel);
        this.setVisible(true);
    }

    public void showCard(String panel) {
        CardLayout cl = (CardLayout) this.mainPanel.getLayout();
        cl.show(this.mainPanel, panel);
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

    
}

