package UI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import GameLogic.*;
import Settings.Constants;

public class GameBoard extends JPanel {
    
    private Board board;
    private WindowHandlerer mainFrame;
    private JPanel boardPanel;
    private SquareUI[][] squares;
    private WallUI[][] verticalWalls = new WallUI[Constants.BOARD_SIZE][Constants.BOARD_SIZE-1];
    private WallUI[][] horizontalWalls = new WallUI[Constants.BOARD_SIZE-1][Constants.BOARD_SIZE];

    private Position newPosition;

    public GameBoard(Board board, WindowHandlerer mainFrame) {
        this.board = board;
        this.mainFrame = mainFrame;
        this.setLayout(new BorderLayout());
        this.squares = new SquareUI[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
        
        JPanel boardPanel = setUpGameBoardUI();
        this.add(boardPanel, BorderLayout.CENTER);
        
        
    }

    private JPanel setUpGameBoardUI() {
        this.boardPanel = new JPanel(null);
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
                this.boardPanel.add(currentSquare);
            }
        }
        
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE-1; j++) {
                if (verticalWalls[i][j]) {
                    WallUI wall = new WallUI();
                    wall.setBounds((j+1)*squareSize + j*wallGap, i*squareSize + i*wallGap, wallGap, squareSize);
                    this.boardPanel.add(wall);
                }
            }
        }

        for (int i = 0; i < Constants.BOARD_SIZE-1; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                if (horizontalWalls[i][j]) {
                    WallUI wall = new WallUI();
                    wall.setBounds(j*squareSize + j*wallGap, (i+1)*squareSize + i*wallGap, squareSize, wallGap);
                    this.boardPanel.add(wall);
                }
            }
        }
        
        return this.boardPanel;
    }

    // Method to update board UI dynamically
    public void updateBoard() {
        // Check for player movement and repaint the affected squares
        updateSquares();
        
        // Check for wall placement and repaint affected walls
        updateWalls();
        
        revalidate(); // Ensure layout is recalculated
        repaint();    // Redraw the updated components
    }

    private void updateSquares() {
        char[][] gamePosition = this.board.getBoard();
        for (int i = 0; i < this.board.getBoardSize(); i++) {
            for (int j = 0; j < this.board.getBoardSize(); j++) {
                SquareUI currentSquare = squares[i][j];
                
                if (currentSquare.isLightedUp()) {
                    currentSquare.setLightedUp(false);
                }

                // If the square has changed (e.g., a player moved), update it
                if (currentSquare.getSquareChar() != gamePosition[i][j]) {
                    currentSquare.setSquareChar(gamePosition[i][j]);
                    currentSquare.repaint();  // Repaint only this square


                }
            }
        }
    }

    private void updateWalls() {

        // TODO: zkrášlit metodu

        boolean[][] verticalWallState = this.board.getVerticalWalls();
        boolean[][] horizontalWallState = this.board.getHorizontalWalls();

        // Check and repaint vertical walls
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE - 1; j++) {
                if (verticalWallState[i][j] && verticalWalls[i][j] == null) {
                    WallUI wall = new WallUI();  // Create the new wall
                    int wallGap = 5;
                    int squareSize = mainFrame.getHeight() * 3 / 4 / 9 - wallGap;
                    wall.setBounds((j + 1) * squareSize + j * wallGap, i * squareSize + i * wallGap, wallGap, squareSize);
                    verticalWalls[i][j] = wall;
                    this.boardPanel.add(wall);  // Add the new wall to the panel
                    wall.repaint();
                }
            }
        }

        // Check and repaint horizontal walls
        for (int i = 0; i < Constants.BOARD_SIZE - 1; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                if (horizontalWallState[i][j] && horizontalWalls[i][j] == null) {
                    WallUI wall = new WallUI();  // Create the new wall
                    int wallGap = 5;
                    int squareSize = mainFrame.getHeight() * 3 / 4 / 9 - wallGap;
                    wall.setBounds(j * squareSize + j * wallGap, (i + 1) * squareSize + i * wallGap, squareSize, wallGap);
                    horizontalWalls[i][j] = wall;
                    this.boardPanel.add(wall);  // Add the new wall to the panel
                    wall.repaint();
                }
            }
        }
        boardPanel.repaint();
        this.repaint();
    }

    public void lightUpSquares(List<Position> list) {
        for (Position position : list) {
            squares[position.getRow()][position.getCol()].setLightedUp(true);
            squares[position.getRow()][position.getCol()].repaint();
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

    public Position getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(Position newPosition) {
        this.newPosition = newPosition;
    }
    
}
