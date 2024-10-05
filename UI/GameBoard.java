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
    private WindowHandlerer mainFrame;
    private SquareUI[][] squares = new SquareUI[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
    private WallUI[][] verticalWalls = new WallUI[Constants.BOARD_SIZE][Constants.BOARD_SIZE-1];
    private WallUI[][] horizontalWalls = new WallUI[Constants.BOARD_SIZE-1][Constants.BOARD_SIZE];

    public GameBoard(Board board, WindowHandlerer mainFrame) {
        this.board = board;
        this.mainFrame = mainFrame;
        this.setLayout(new BorderLayout());
        
        JPanel boardPanel = setUpGameBoardUI();
        this.add(boardPanel, BorderLayout.CENTER);
        
        
    }

    private JPanel setUpGameBoardUI() {
        JPanel boardPanel = new JPanel(null);
        char[][] gamePosition = this.board.getBoard();
        boolean[][] verticalWalls = this.board.getVerticalWalls();
        boolean[][] horizontalWalls = this.board.getHorizontalWalls();
        
        
        int wallGap = 5;
        int squareSize = mainFrame.getHeight()*3/4 /9 - wallGap;

        for (int i = 0; i < this.board.getBoardSize(); i++) {
            for (int j = 0; j < this.board.getBoardSize(); j++) {
                char currentChar = gamePosition[i][j];
                SquareUI currentSquare = new SquareUI(currentChar);
                squares[i][j] = currentSquare;
                currentSquare.setBounds(j*squareSize + j*wallGap, i*squareSize + i*wallGap, squareSize, squareSize);
                boardPanel.add(currentSquare);
            }
        }
        
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE-1; j++) {
                if (verticalWalls[i][j]) {
                    WallUI wall = new WallUI();
                    wall.setBounds((j+1)*squareSize + j*wallGap, i*squareSize + i*wallGap, wallGap, squareSize);
                    boardPanel.add(wall);
                }
            }
        }

        for (int i = 0; i < Constants.BOARD_SIZE-1; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                if (horizontalWalls[i][j]) {
                    WallUI wall = new WallUI();
                    wall.setBounds(j*squareSize + j*wallGap, (i+1)*squareSize + i*wallGap, squareSize, wallGap);
                    boardPanel.add(wall);
                }
            }
        }
        
        return boardPanel;
    }

    @Override
    public void repaint() {
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j  = 0; j < Constants.BOARD_SIZE; j++) {
                squares[i][j].repaint();
            }
        }
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public WindowHandlerer getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(WindowHandlerer mainFrame) {
        this.mainFrame = mainFrame;
    }

    public SquareUI[][] getSquares() {
        return squares;
    }

    public void setSquares(SquareUI[][] squares) {
        this.squares = squares;
    }

    public WallUI[][] getVerticalWalls() {
        return verticalWalls;
    }

    public void setVerticalWalls(WallUI[][] verticalWalls) {
        this.verticalWalls = verticalWalls;
    }

    public WallUI[][] getHorizontalWalls() {
        return horizontalWalls;
    }

    public void setHorizontalWalls(WallUI[][] horizontalWalls) {
        this.horizontalWalls = horizontalWalls;
    }

    
}
