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

public class MenuPanel extends JPanel {

    private QuoridorApp mainWindow;

    public MenuPanel(QuoridorApp mainWindow) {
        this.mainWindow = mainWindow;
        this.setLayout(new BorderLayout());

        setUpMenuTitle();        
        setUpMenuButtonPanel();
    }

    public void setUpMenuTitle(){
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel title = new JLabel(Constants.GAME_NAME, SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, mainWindow.getHeight()/4));
        titlePanel.add(title);
        this.add(titlePanel, BorderLayout.CENTER);
    }

    public void setUpMenuButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));
        
        JPanel playSoloPanel = new JPanel();
        JButton playSolButton = new JButton("Play Solo");
        playSoloPanel.add(playSolButton);
        playSolButton.addActionListener(playSoloAction());

        JPanel playMultiplayerPanel = new JPanel();
        JButton playMultiplayerButton = new JButton("Play Online");
        playMultiplayerButton.addActionListener(playMultiplayerAction());
        playMultiplayerPanel.add(playMultiplayerButton);

        
        JPanel settingsPanel = new JPanel();
        JButton settingsButton = new JButton("Player Settings");
        settingsButton.addActionListener(settingsAction());
        settingsPanel.add(settingsButton);

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
