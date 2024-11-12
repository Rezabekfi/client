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

        Board board =new Board();
        this.gb = new GameBoard(board, this.mainWindow);
        this.gb.setBounds((int)(this.mainWindow.getWidth()/2 - this.mainWindow.getHeight()*3.0/8.0), (int)(this.mainWindow.getHeight()/12), (int)(this.mainWindow.getHeight()*3.0/4), (int)(this.mainWindow.getHeight()*3.0/4));;
        this.add(this.gb);

        this.gm = new GameManager(board, gb);

        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back to menu.");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.showCard(Constants.MENU_CARD);
            }
        });
        buttonPanel.add(backButton);
        buttonPanel.setBounds(0, (int)(this.mainWindow.getHeight()*5.0/6), this.mainWindow.getWidth(), (int)(this.mainWindow.getHeight()/6.0));
        buttonPanel.setBackground(Color.GRAY);
        this.add(buttonPanel);


        board.getPlayers()[0].setName("Franta");
        board.getPlayers()[1].setName("Jirka");
        updatePlayerPanels();
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
        p1 = new PlayerPanel(gb.getBoard().getPlayers()[0], true);
        p2 = new PlayerPanel(gb.getBoard().getPlayers()[1], true);
        p1.setBounds(0,(int)((1.0/12.0)*this.mainWindow.getHeight()), (int)((this.mainWindow.getWidth()-(3.0/4.0)*this.mainWindow.getHeight())/2), (int)((3.0/4)*this.mainWindow.getHeight()));
        p2.setBounds((int)((this.mainWindow.getWidth() - (3.0/4)*this.mainWindow.getHeight())/2 + (3.0/4)*this.mainWindow.getHeight()),(int)((1.0/12)*this.mainWindow.getHeight()), (int)((this.mainWindow.getWidth()-(3.0/4)*this.mainWindow.getHeight())/2), (int)((3.0/4)*this.mainWindow.getHeight()));
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
    }

    public GameManager getGameManager() {
        return gm;
    }
}
