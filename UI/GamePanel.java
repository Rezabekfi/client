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
