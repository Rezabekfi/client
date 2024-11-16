package com.quoridor.UI.Windows;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.quoridor.GameLogic.GameManager;
import com.quoridor.Settings.Constants;
import com.quoridor.UI.Components.GameBoard;

public class GamePanel extends JPanel {

    private QuoridorApp mainWindow;
    private GameBoard gb;
    private GameManager gm;
    private PlayerPanel p1;
    private PlayerPanel p2;

    public GamePanel(QuoridorApp mainWindow) {
        this.mainWindow = mainWindow;
        this.setLayout(null);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel title = new JLabel("GAME", SwingConstants.CENTER);
        titlePanel.add(title);
        titlePanel.setBounds(0, 0, this.mainWindow.getWidth(), this.mainWindow.getHeight()/12);
        titlePanel.setBackground(Color.GRAY);
        this.add(titlePanel);

        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back to menu.");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cleanupGame();
                mainWindow.showCard(Constants.MENU_CARD);
            }
        });
        buttonPanel.add(backButton);
        buttonPanel.setBounds(0, (int)(this.mainWindow.getHeight()*5.0/6), this.mainWindow.getWidth(), (int)(this.mainWindow.getHeight()/6.0));
        buttonPanel.setBackground(Color.GRAY);
        this.add(buttonPanel);
    }

    public void setUpTitle() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel title = new JLabel("GAME", SwingConstants.CENTER);
        titlePanel.add(title);
        titlePanel.setBounds(0, 0, this.mainWindow.getWidth(), this.mainWindow.getHeight()/12);
        titlePanel.setBackground(Color.GRAY);
        this.add(titlePanel);
    }
    
    @Override
    public void repaint() {
        super.repaint();

    }

    public void updatePlayerPanels() {
        if (p1 == null || p2 == null) {
            createPlayerPanels();
        } else {
            p1.updatePlayerInfo();
            p2.updatePlayerInfo();
        }
    }

    public void createPlayerPanels() {
        p1 = new PlayerPanel(gb.getBoard().getPlayers()[0], true);
        p2 = new PlayerPanel(gb.getBoard().getPlayers()[1], true);
        p1.setBounds(0,(int)((1.0/12.0)*this.mainWindow.getHeight()), 
                    (int)((this.mainWindow.getWidth()-(3.0/4.0)*this.mainWindow.getHeight())/2), 
                    (int)((3.0/4)*this.mainWindow.getHeight()));
        p2.setBounds((int)((this.mainWindow.getWidth() - (3.0/4)*this.mainWindow.getHeight())/2 + (3.0/4)*this.mainWindow.getHeight()),
                    (int)((1.0/12)*this.mainWindow.getHeight()), 
                    (int)((this.mainWindow.getWidth()-(3.0/4)*this.mainWindow.getHeight())/2), 
                    (int)((3.0/4)*this.mainWindow.getHeight()));
        this.add(p1);
        this.add(p2);
    }



    public QuoridorApp getmainWindow() {
        return mainWindow;
    }

    public GameBoard getGameBoard() {
        return gb;
    }

    public void setGameBoard(GameBoard gb) {
        this.gb = gb;
        this.add(gb);
        gb.setBounds((int)(this.mainWindow.getWidth()/2 - this.mainWindow.getHeight()*3.0/8.0), 
                     (int)(this.mainWindow.getHeight()/12), 
                     (int)(this.mainWindow.getHeight()*3.0/4), 
                     (int)(this.mainWindow.getHeight()*3.0/4));
    }

    public GameManager getGameManager() {
        return gm;
    }

    public void setGameManager(GameManager gm) {
        this.gm = gm;
    }

    public void cleanupGame() {
        if (p1 != null) remove(p1);
        if (p2 != null) remove(p2);
        if (gb != null) remove(gb);
        gb = null;
        gm = null;
        p1 = null;
        p2 = null;
        repaint();
    }
}
