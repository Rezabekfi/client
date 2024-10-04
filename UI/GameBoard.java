package UI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import GameLogic.*;
import Settings.Constants;

public class GameBoard extends JPanel {
    
    private Board board;

    public GameBoard(Board board) {
        this.board = board;
        this.setLayout(new BorderLayout());
        
        JPanel boardPanel = setUpGameBoardUI();
        this.add(boardPanel, BorderLayout.CENTER);
        
        
    }

    private JPanel setUpGameBoardUI() {
        JPanel boardPanel = new JPanel(new GridLayout(this.board.getBoardSize(), this.board.getBoardSize()));
        char[][] gamePosition = this.board.getBoard();
        for (int i = 0; i < this.board.getBoardSize(); i++) {
            for (int j = 0; j < this.board.getBoardSize(); j++) {
                char currentChar = gamePosition[i][j];
                SquareUI currentSquare = new SquareUI();
                boardPanel.add(currentSquare);
            }
        }
        return boardPanel;
    }

}
