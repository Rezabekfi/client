package GameLogic;

import UI.GameBoard;
import UI.SquareUI;
import UI.WallUI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import Settings.Constants;
import UI.PopupWindow;
// import Settings.Constants;

public class GameManager {

    private Board board;             // Game state (e.g., players, walls)
    private GameBoard gameBoardUI;   // UI component for the game board
    private Player currentPlayer;
    private int playerIndex;
    private List<SquareUI> squares;
    private List<WallUI> walls;
    private WallUI[] doubleWall;

    public GameManager(Board board, GameBoard gameBoardUI) {
        this.board = board;
        this.gameBoardUI = gameBoardUI;
        this.playerIndex = 0;
        this.currentPlayer = board.getPlayers()[playerIndex];
        this.walls = gameBoardUI.getAllWalls();
        this.doubleWall = new WallUI[2];
        setWallActionListener();
    }

    public void setWallActionListener() {
        for (WallUI wall : walls) {
            if (wall.isPlaced()) continue;
            wall.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (doubleWall[0] == null) {
                        doubleWall[0] = wall;
                    } else if (doubleWall[1] == null) {
                        doubleWall[1] = wall;
                    }

                    if (possibleDoubleWall()) {
                        placeWall(doubleWall[0].getRow(), doubleWall[0].getCol(), doubleWall[1].getRow(), doubleWall[1].getCol(), doubleWall[0].isVertical());
                        doubleWall[0].setSelected(false);
                        doubleWall[1].setSelected(false);
                        doubleWall[0] = null;
                        doubleWall[1] = null;
                        nextTurn();
                    } else if (doubleWall[0] != null && doubleWall[1]!= null) {
                        doubleWall[0].setSelected(false);
                        doubleWall[1].setSelected(false);
                        doubleWall[0] = null;
                        doubleWall[1] = null;
                    }

                }
    
                @Override
                public void mousePressed(MouseEvent e) {
                }
    
                @Override
                public void mouseReleased(MouseEvent e) {
                }
    
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!wall.isPlaced()) {
                        wall.setPlayerColor(currentPlayer.getColor_2d());
                        wall.setSelected(true);
                    }
                }
    
                @Override
                public void mouseExited(MouseEvent e) {
                    wall.setSelected(false);
                    if ((doubleWall[0] == null && doubleWall[1] != null && doubleWall[1].equals(wall)) || (doubleWall[0] != null && doubleWall[1] == null && doubleWall[0].equals(wall))
                    || (doubleWall[0] != null && doubleWall[1] != null && doubleWall[0].equals(wall)) || (doubleWall[0] != null && doubleWall[1] != null && doubleWall[1].equals(wall))) {
                        wall.setSelected(true);
                    }
                }
                
            });
        }
    }

    public boolean possibleDoubleWall() {
        if (doubleWall[0] == null || doubleWall[1] == null) return false;
    
        // Check that both walls exist, aren't the same, and are both unplaced
        if (doubleWall[0] != null && doubleWall[1] != null 
            && !doubleWall[0].equals(doubleWall[1]) 
            && doubleWall[0].isVertical() == doubleWall[1].isVertical()
            && !doubleWall[0].isPlaced() && !doubleWall[1].isPlaced()) {
            
            if (doubleWall[0].isVertical()) {
                return Math.abs(doubleWall[0].getRow() - doubleWall[1].getRow()) == 1 
                       && doubleWall[0].getCol() == doubleWall[1].getCol();
            }
            else {
                return Math.abs(doubleWall[0].getCol() - doubleWall[1].getCol()) == 1 
                       && doubleWall[0].getRow() == doubleWall[1].getRow();
            }
        }
        return false;
    }
    

    public boolean placeWall(int row_1, int col_1, int row_2, int col_2, boolean isVertical) {
        if (board.canPlaceWall(currentPlayer, row_1, col_1, isVertical) && board.canPlaceWall(currentPlayer, row_2, col_2, isVertical)) {
            board.placeWall(currentPlayer, row_1, col_1, isVertical);  // Update the game logic
            board.placeWall(currentPlayer, row_2, col_2, isVertical);
            if (isVertical) {
                gameBoardUI.getVerticalWalls()[row_1][col_1].setPlaced(true);
                gameBoardUI.getVerticalWalls()[row_2][col_2].setPlaced(true);

            } else {
                gameBoardUI.getHorizontalWalls()[row_1][col_1].setPlaced(true);
                gameBoardUI.getHorizontalWalls()[row_2][col_2].setPlaced(true);
            }
            currentPlayer.placedWall();
            gameBoardUI.updateBoard();          // Refresh the UI
            return true;
        }
        return false;
    }

    // Handle moving a player
    public boolean movePlayer(Position position) {
        if (position == null) {
            return false;
        }

        if (board.canMovePlayer(currentPlayer,position.getRow(), position.getCol())) {
            board.movePlayer(currentPlayer, position.getRow(), position.getCol());  // Update player position in game logic
            gameBoardUI.updateBoard();  
            return true;
        }
        return false;
    }

    // Optional: Handling turns
    public void nextTurn() {
        //updatePlayerPosition();
        //updateGameState -bettet
        removeSelectedSquares();
        gameBoardUI.updateBoard();
        if (getWinner() != null) {
            handleGameEnded();
            return;
        }
        if (++playerIndex == board.getPlayers().length) {
            playerIndex = 0;
        }
        currentPlayer = board.getPlayers()[playerIndex];
        highlightPossibleMoves();
    }

    // currently not used but might be usefull later
    // TODO: most likely delete
    private void updatePlayerPosition() {
        char[][] board = this.board.getBoard();
        Player[] players = this.board.getPlayers();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != Constants.EMPTY_SQUARE) {
                    for (int k = 0; k < players.length; k++) {
                        if (players[k].getColor() == board[i][j]) {
                            players[k].setRow(i);
                            players[k].setCol(j);
                        }
                    }
                }
            }
        }
    }

    private void handleGameEnded() {
        PopupWindow.showMessage("Game Over! Player " + getWinner().getName() + " wins!");
        board = new Board(board.getNumberOfPlayers());
        gameBoardUI.setBoard(board);
        playerIndex = 0;
        currentPlayer = board.getPlayers()[playerIndex];
        
        // Reset walls
        walls = gameBoardUI.getAllWalls();
        doubleWall = new WallUI[2];
        
        // Reset UI
        gameBoardUI.updateBoard();
        setWallActionListener();

        gameLoop();
    }

    private void removeSelectedSquares() {
        if (squares == null) {
            return;
        }
        for (SquareUI square : squares) {
            square.removeMouseListener(square.getMouseListener());
            square.setLightedUp(false);
        }
        squares = null;
    }

    public void gameLoop() {
        setUpFirstMove();
    }

    private void setUpFirstMove() {
        gameBoardUI.updateBoard();
        highlightPossibleMoves();
    }

    private void highlightPossibleMoves() {
        List<Position> list = board.possibleMoves(currentPlayer);
        squares = gameBoardUI.setUpPossibleSquares(list);
        setSquaresActionListener();
    }

    private void setSquaresActionListener() {
        for (SquareUI square : squares) {
            MouseListener listener = new MouseListener() {
    
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (movePlayer(square.getPosition())) {
                        nextTurn();
                    }
                }
    
                @Override
                public void mousePressed(MouseEvent e) {
                }
    
                @Override
                public void mouseReleased(MouseEvent e) {
                }
    
                @Override
                public void mouseEntered(MouseEvent e) {
                }
    
                @Override
                public void mouseExited(MouseEvent e) {
                }
            };
            square.addMouseListener(listener);
            square.setMouseListener(listener);  // Store the listener in SquareUI
        }
    }
    

    // for debugging - current development tested here
    public void debbugRand() {
        System.out.println("debbuging - nothing");
    }

    public Player getWinner() {
        Player[] players = this.board.getPlayers();
        
        for (int i = 0; i < players.length; i++) {
            if (players[i].isPlayerFinished()) {
                return players[i];
            }
        }
        return null;
    }
}
