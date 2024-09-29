package UI;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Settings.Constants;

public class MenuPanel extends JPanel {

    private WindowHandlerer mainFrame;

    public MenuPanel(WindowHandlerer mainFrame) {
        this.mainFrame = mainFrame;
        this.setLayout(new BorderLayout());

        setUpMenuTitle();        
        setUpMenuButtonPanel();
    }

    public void setUpMenuTitle(){
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel title = new JLabel(Constants.GAME_NAME, SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, mainFrame.getHeight()/4));
        titlePanel.add(title);
        this.add(titlePanel, BorderLayout.CENTER);
    }

    public void setUpMenuButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));
        
        JPanel playSoloPanel = new JPanel();
        JButton playSolButton = new JButton("Play Solo");
        playSoloPanel.add(playSolButton);

        JPanel playMultiplayerPanel = new JPanel();
        JButton playMultiplayerButton = new JButton("Play Online");
        playMultiplayerPanel.add(playMultiplayerButton);

        JPanel settingsPanel = new JPanel();
        JButton settingsButton = new JButton("Settings");
        settingsPanel.add(settingsButton);

        JPanel exitPanel = new JPanel();
        JButton exitButton = new JButton("Exit");
        exitPanel.add(exitButton);

        buttonPanel.add(playSoloPanel);
        buttonPanel.add(playMultiplayerPanel);
        buttonPanel.add(settingsPanel);
        buttonPanel.add(exitPanel);

        this.add(buttonPanel, BorderLayout.SOUTH);
    }
}
