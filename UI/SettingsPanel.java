package UI;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Settings.Constants;

public class SettingsPanel extends JPanel {
    
    private QuoridorApp mainWindow;

    public SettingsPanel(QuoridorApp mainWindow) {
        this.mainWindow = mainWindow;
        this.setLayout(new BorderLayout());
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel title = new JLabel("SETTINGS", SwingConstants.CENTER);
        titlePanel.add(title);

        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back to menu.");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.showCard(Constants.MENU_CARD);
            }
        });
        buttonPanel.add(backButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    public QuoridorApp getmainWindow() {
        return mainWindow;
    }
}
