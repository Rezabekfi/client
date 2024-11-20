package com.quoridor.UI.Windows;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.quoridor.Settings.Constants;

public class SettingsPanel extends JPanel {
    
    private QuoridorApp mainWindow;
    private JTextField playerNameField;
    private JTextField playerNameField2;

    public SettingsPanel(QuoridorApp mainWindow) {
        this.mainWindow = mainWindow;
        this.setLayout(new BorderLayout(10, 10));
        
        // Create title panel with some padding and styling
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        JLabel title = new JLabel("SETTINGS", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(title);
        this.add(titlePanel, BorderLayout.NORTH);

        // Create settings panel with better spacing
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
        
        // Add player name input with consistent sizing
        JPanel namePanel = createInputPanel("Player Name:", Constants.DEFAULT_PLAYER_NAME);
        playerNameField = (JTextField) namePanel.getComponent(1);
        settingsPanel.add(namePanel);
        
        // Add small vertical spacing
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // player2 name input
        JPanel namePanel2 = createInputPanel("Player 2 Name (solo play only):", Constants.DEFAULT_PLAYER_2_NAME);
        playerNameField2 = (JTextField) namePanel2.getComponent(1);
        settingsPanel.add(namePanel2);

        // Center the settings panel
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.add(settingsPanel);
        this.add(centerWrapper, BorderLayout.CENTER);

        // Create button panel with spacing
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        JButton saveButton = createStyledButton("Save Settings");
        JButton backButton = createStyledButton("Back to Menu");
        
        saveButton.addActionListener(e -> {
            saveSettings();
            mainWindow.showCard(Constants.MENU_CARD);
        });
        
        backButton.addActionListener(e -> mainWindow.showCard(Constants.MENU_CARD));
        
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createInputPanel(String labelText, String defaultValue) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(200, 25));
        
        JTextField field = new JTextField(15);
        field.setText(defaultValue);
        field.setPreferredSize(new Dimension(150, 25));
        
        panel.add(label);
        panel.add(field);
        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 30));
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        return button;
    }

    private void saveSettings() {
        String playerName = playerNameField.getText().trim();
        String playerName2 = playerNameField2.getText().trim();
        if (!playerName.isEmpty()) {
            mainWindow.setPlayerName(playerName);
            mainWindow.setPlayerName2(playerName2);
        }
    }

    public QuoridorApp getmainWindow() {
        return mainWindow;
    }
}
