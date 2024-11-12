package UI;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Settings.Constants;

public class SettingsPanel extends JPanel {
    
    private QuoridorApp mainWindow;
    private JTextField playerNameField;

    public SettingsPanel(QuoridorApp mainWindow) {
        this.mainWindow = mainWindow;
        this.setLayout(new BorderLayout());
        
        // Create title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel title = new JLabel("SETTINGS", SwingConstants.CENTER);
        titlePanel.add(title);
        this.add(titlePanel, BorderLayout.NORTH);

        // Create settings panel
        JPanel settingsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        
        // Add player name input
        JPanel namePanel = new JPanel();
        JLabel nameLabel = new JLabel("Player Name: ");
        playerNameField = new JTextField(15);
        playerNameField.setText(Constants.DEFAULT_PLAYER_NAME);
        namePanel.add(nameLabel);
        namePanel.add(playerNameField);
        settingsPanel.add(namePanel);
        
        this.add(settingsPanel, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save Settings");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSettings();
                mainWindow.showCard(Constants.MENU_CARD);
            }
        });
        
        JButton backButton = new JButton("Back to menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.showCard(Constants.MENU_CARD);
            }
        });
        
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveSettings() {
        String playerName = playerNameField.getText().trim();
        if (!playerName.isEmpty()) {
            mainWindow.setPlayerName(playerName);
        }
    }

    public QuoridorApp getmainWindow() {
        return mainWindow;
    }
}
