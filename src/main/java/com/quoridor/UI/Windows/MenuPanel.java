package com.quoridor.UI.Windows;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.quoridor.Settings.Constants;

/**
 * Panel for the main menu of the game. Allows the user to start a new game (singleplayer/multiplayer), change settings, or exit the game.
 */
public class MenuPanel extends JPanel {

    // reference to the main window
    private QuoridorApp mainWindow;

    // Constructor
    public MenuPanel(QuoridorApp mainWindow) {
        this.mainWindow = mainWindow;
        this.setLayout(new BorderLayout());

        setUpMenuTitle();        
        setUpMenuButtonPanel();
    }

    // Set up the title of the menu
    public void setUpMenuTitle(){
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel title = new JLabel(Constants.GAME_NAME, SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, mainWindow.getHeight()/4));
        titlePanel.add(title);
        this.add(titlePanel, BorderLayout.CENTER);
    }

    // Set up the buttons for the menu
    public void setUpMenuButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));
        
        // Singleplayer button
        JPanel playSoloPanel = new JPanel();
        JButton playSolButton = new JButton("Play Solo");
        playSoloPanel.add(playSolButton);
        playSolButton.addActionListener(playSoloAction());

        // Multiplayer button
        JPanel playMultiplayerPanel = new JPanel();
        JButton playMultiplayerButton = new JButton("Play Online");
        playMultiplayerButton.addActionListener(playMultiplayerAction());
        playMultiplayerPanel.add(playMultiplayerButton);

        // Settings button        
        JPanel settingsPanel = new JPanel();
        JButton settingsButton = new JButton("Player Settings");
        settingsButton.addActionListener(settingsAction());
        settingsPanel.add(settingsButton);

        // Exit button
        JPanel exitPanel = new JPanel();
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(exitAction());
        exitPanel.add(exitButton);

        buttonPanel.add(playSoloPanel);
        buttonPanel.add(playMultiplayerPanel);
        buttonPanel.add(settingsPanel);
        buttonPanel.add(exitPanel);

        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Action listeners for the buttons
    
    private ActionListener playSoloAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.startNewGame(0);
                mainWindow.showCard(Constants.GAME_ON_CARD);
            }
        };
    }

    private ActionListener settingsAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.showCard(Constants.SETTINGS_CARD);
            }
        };
    }

    private ActionListener playMultiplayerAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.tryNewMultiplayerGame();
            }
        };
    }

    private ActionListener exitAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(Constants.EXIT_SUCCES);
            }
        };
    }
}
