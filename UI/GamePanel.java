package UI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import GameLogic.Board;
import Settings.Constants;

public class GamePanel extends JPanel {

    private WindowHandlerer mainFrame;
    private GameBoard gb;

    public GamePanel(WindowHandlerer mainFrame) {
        this.mainFrame = mainFrame;
        this.setLayout(null);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel title = new JLabel("GAME", SwingConstants.CENTER);
        titlePanel.add(title);
        titlePanel.setBounds(0, 0, this.mainFrame.getWidth(), this.mainFrame.getHeight()/12);
        titlePanel.setBackground(Color.GRAY);
        this.add(titlePanel);

        
        this.gb = new GameBoard(new Board(), this.mainFrame);
        this.gb.setBounds(this.mainFrame.getWidth()/2 - this.mainFrame.getHeight()*3/8, this.mainFrame.getHeight()/12, this.mainFrame.getHeight()*3/4, this.mainFrame.getHeight()*3/4);;
        this.add(this.gb);




        // dont know about this button might
        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back to menu.");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showCard(Constants.MENU_CARD);
            }
        });
        buttonPanel.add(backButton);
        buttonPanel.setBounds(0, this.mainFrame.getHeight()*5/6, this.mainFrame.getWidth(), this.mainFrame.getHeight()/6);
        buttonPanel.setBackground(Color.GRAY);
        this.add(buttonPanel);
        
    }


    
    @Override
    public void repaint() {
        super.repaint();
    }



    public WindowHandlerer getMainFrame() {
        return mainFrame;
    }

    public GameBoard getGameBoard() {
        return gb;
    }

    public void setGb(GameBoard gb) {
        this.gb = gb;
    }
}
