package UI;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {
    
    private JPanel mainPanel;

    public MenuPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        JButton playButton = new JButton("Play Game");
        playButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, "Game");  // Switch to Panel 1
            }
            
        });
        this.add(playButton);
    }
}
