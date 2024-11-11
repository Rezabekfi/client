package UI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import GameLogic.Board;
import GameLogic.GameManager;
import Settings.Constants;

public class GamePanel extends JPanel {

    private WindowHandlerer mainFrame;
    private GameBoard gb;
    private GameManager gm;

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

        Board board =new Board();
        this.gb = new GameBoard(board, this.mainFrame);
        this.gb.setBounds((int)(this.mainFrame.getWidth()/2 - this.mainFrame.getHeight()*3.0/8.0), (int)(this.mainFrame.getHeight()/12), (int)(this.mainFrame.getHeight()*3.0/4), (int)(this.mainFrame.getHeight()*3.0/4));;
        this.add(this.gb);

        this.gm = new GameManager(board, gb);




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
        buttonPanel.setBounds(0, (int)(this.mainFrame.getHeight()*5.0/6), this.mainFrame.getWidth(), (int)(this.mainFrame.getHeight()/6.0));
        buttonPanel.setBackground(Color.GRAY);
        this.add(buttonPanel);


        board.getPlayers()[0].setName("Franta");
        board.getPlayers()[1].setName("Jirka");
        PlayerPanel p1 = new PlayerPanel(board.getPlayers()[0], true);
        PlayerPanel p2 = new PlayerPanel(board.getPlayers()[1], true);
        p1.setBounds(0,(int)((1.0/12.0)*this.mainFrame.getHeight()), (int)((this.mainFrame.getWidth()-(3.0/4.0)*this.mainFrame.getHeight())/2), (int)((3.0/4)*this.mainFrame.getHeight()));
        p2.setBounds((int)((this.mainFrame.getWidth() - (3.0/4)*this.mainFrame.getHeight())/2 + (3.0/4)*this.mainFrame.getHeight()),(int)((1.0/12)*this.mainFrame.getHeight()), (int)((this.mainFrame.getWidth()-(3.0/4)*this.mainFrame.getHeight())/2), (int)((3.0/4)*this.mainFrame.getHeight()));
        this.add(p1);
        this.add(p2);
    }

    public void setUpTitle() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel title = new JLabel("GAME", SwingConstants.CENTER);
        titlePanel.add(title);
        titlePanel.setBounds(0, 0, this.mainFrame.getWidth(), this.mainFrame.getHeight()/12);
        titlePanel.setBackground(Color.GRAY);
        this.add(titlePanel);
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

    public void setGameBoard(GameBoard gb) {
        this.gb = gb;
    }

    public GameManager getGameManager() {
        return gm;
    }
}
