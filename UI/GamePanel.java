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

public class GamePanel extends JPanel {

    private WindowHandlerer mainFrame;

    public GamePanel(WindowHandlerer mainFrame) {
        this.mainFrame = mainFrame;
        this.setLayout(new BorderLayout());
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel title = new JLabel("GAME", SwingConstants.CENTER);
        titlePanel.add(title);
        this.add(titlePanel, BorderLayout.CENTER);

        /*  RANDOM pokus - nic duleziteho
        JPanel centerPanel = new JPanel();
        this.add(centerPanel, BorderLayout.CENTER);
        JPanel centerPanel1 = new JPanel();
        centerPanel.add(centerPanel1, BorderLayout.NORTH);
        JPanel centerPanel2 = new JPanel();
        centerPanel1.add(centerPanel2, BorderLayout.WEST);
        JPanel centerPanel3 = new JPanel();
        centerPanel2.add(centerPanel3, BorderLayout.NORTH);
        JPanel centerPanel4 = new JPanel();
        JLabel titleCenter = new JLabel("GAME");
        centerPanel4.add(titleCenter);
        centerPanel3.add(centerPanel4, BorderLayout.WEST);
        */

        // TODO: add Game board middle sides maybe names + walls number 



        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back to menu.");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showCard(Constants.MENU_CARD);
            }
        });
        buttonPanel.add(backButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
        
    }
}
