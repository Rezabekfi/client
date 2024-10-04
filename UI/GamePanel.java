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

    public GamePanel(WindowHandlerer mainFrame) {
        this.mainFrame = mainFrame;
        this.setLayout(null);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel title = new JLabel("GAME", SwingConstants.CENTER);
        titlePanel.add(title);
        titlePanel.setBounds(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT/12);
        titlePanel.setBackground(Color.GRAY);
        this.add(titlePanel);

        
        GameBoard gb = new GameBoard(new Board());
        gb.setBounds(Constants.WINDOW_WIDTH/2 - Constants.WINDOW_HEIGHT*3/8, Constants.WINDOW_HEIGHT/12, Constants.WINDOW_HEIGHT*3/4, Constants.WINDOW_HEIGHT*3/4);;
        this.add(gb);




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
        buttonPanel.setBounds(0, Constants.WINDOW_HEIGHT*5/6, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT/6);
        buttonPanel.setBackground(Color.GRAY);
        this.add(buttonPanel);
        
    }
}
