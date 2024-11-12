package UI.Components;
import java.awt.BorderLayout;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JPanel;

import GameLogic.*;
import Settings.Constants;
import UI.Windows.QuoridorApp;

public class GameBoard extends JPanel {
    
    private Board board;
    private QuoridorApp mainWindow;
    private JPanel boardPanel;
    private SquareUI[][] squares;
    private WallUI[][] verticalWalls = new WallUI[Constants.BOARD_SIZE][Constants.BOARD_SIZE-1];
    private WallUI[][] horizontalWalls = new WallUI[Constants.BOARD_SIZE-1][Constants.BOARD_SIZE];

    private Position newPosition;

    public GameBoard(Board board, QuoridorApp mainWindow) {
        this.board = board;
        this.mainWindow = mainWindow;
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
        int squareSize = mainWindow.getHeight()*3/4 /9 - wallGap;

        for (int i = 0; i < this.board.getBoardSize(); i++) {
            for (int j = 0; j < this.board.getBoardSize(); j++) {
                char currentChar = gamePosition[i][j];
                SquareUI currentSquare = new SquareUI(currentChar, new Position(i, j));
                squares[i][j] = currentSquare;
                currentSquare.setBounds(j*squareSize + j*wallGap, i*squareSize + i*wallGap, squareSize, squareSize);
                this.boardPanel.add(currentSquare);
            }
        }
        
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE-1; j++) {
                WallUI wall = new WallUI(i,j,true, verticalWalls[i][j]);
                wall.setBounds((j+1)*squareSize + j*wallGap, i*squareSize + i*wallGap, wallGap, squareSize);    
                this.boardPanel.add(wall);
                this.verticalWalls[i][j] = wall;
                wall.repaint();
            }
        }

        for (int i = 0; i < Constants.BOARD_SIZE-1; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                WallUI wall = new WallUI(i, j, false, horizontalWalls[i][j]);
                wall.setBounds(j*squareSize + j*wallGap, (i+1)*squareSize + i*wallGap, squareSize, wallGap);
                this.boardPanel.add(wall);
                this.horizontalWalls[i][j] = wall;
                wall.repaint();
            }
        }
        
        return this.boardPanel;
    }

    // Method to update board UI dynamically
    public void updateBoard() {
        // Check for player movement and repaint the affected squares
        updateSquares();
        mainWindow.getGamePanel().updatePlayerPanels();
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

                // If the square has changed (e.g., a player moved), update it
                if (currentSquare.getSquareChar() != gamePosition[i][j]) {
                    currentSquare.setSquareChar(gamePosition[i][j]);
                    currentSquare.repaint();
                }
            }
        }
    }

    public void clearAllWalls() {
        for (WallUI wall : this.getAllWalls()) {
            wall.setPlaced(false);
            wall.repaint();
        }
    }

    private void updateWalls() {

        // TODO: zkrášlit metodu

        boolean[][] verticalWallState = this.board.getVerticalWalls();
        boolean[][] horizontalWallState = this.board.getHorizontalWalls();
        int wallGap = 5;
        // Check and repaint vertical walls
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE - 1; j++) {
                if (verticalWallState[i][j] && !verticalWalls[i][j].isPlaced()) {
                    verticalWalls[i][j].setPlaced(true);  // Create the new wall
                    int squareSize = mainWindow.getHeight() * 3 / 4 / 9 - wallGap;
                    verticalWalls[i][j].setBounds((j + 1) * squareSize + j * wallGap, i * squareSize + i * wallGap, wallGap, squareSize);
                    verticalWalls[i][j].repaint();
                }
            }
        }

        // Check and repaint horizontal walls
        for (int i = 0; i < Constants.BOARD_SIZE - 1; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                if (horizontalWallState[i][j] && !horizontalWalls[i][j].isPlaced()) {
                    horizontalWalls[i][j].setPlaced(true);
                    int squareSize = mainWindow.getHeight() * 3 / 4 / 9 - wallGap;
                    horizontalWalls[i][j].setBounds(j * squareSize + j * wallGap, (i + 1) * squareSize + i * wallGap, squareSize, wallGap);
                    horizontalWalls[i][j].repaint();
                }
            }
        }
        boardPanel.repaint();
        this.repaint();
    }

    public List<SquareUI> setUpPossibleSquares(List<Position> list) {
        List<SquareUI> res = new ArrayList<SquareUI>();
        for (Position position : list) {
            SquareUI currentSquare = squares[position.getRow()][position.getCol()];
            currentSquare.setLightedUp(true);
            currentSquare.repaint();
            res.add(currentSquare);
        }
        return res;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public QuoridorApp getmainWindow() {
        return mainWindow;
    }

    public void setmainWindow(QuoridorApp mainWindow) {
        this.mainWindow = mainWindow;
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

    public List<WallUI> getAllWalls() {
        List<WallUI> res = new ArrayList<WallUI>();
        for (int i = 0; i < verticalWalls.length; i++) {
            for (int j = 0; j < verticalWalls[0].length; j++) {
                res.add(verticalWalls[i][j]);
            }
        }
        for (int i = 0; i < horizontalWalls.length; i++) {
            for (int j = 0; j < horizontalWalls[0].length; j++) {
                res.add(horizontalWalls[i][j]);
            }
        }
        return res;
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
