package com.quoridor.GameLogic;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import com.quoridor.UI.Components.GameBoard;
import com.quoridor.UI.Components.SquareUI;
import com.quoridor.UI.Components.WallUI;
import com.quoridor.UI.Windows.PopupWindow;

public class GameManager {

    protected Board board;             // Game state (e.g., players, walls)
    protected GameBoard gameBoardUI;   // UI component for the game board
    protected Player currentPlayer;
    protected int playerIndex;
    protected List<SquareUI> squares;
    protected List<WallUI> walls;
    protected WallUI[] doubleWall;

    public GameManager(Board board, GameBoard gameBoardUI) {
        this.board = board;
        this.gameBoardUI = gameBoardUI;
        this.playerIndex = 0;
        this.currentPlayer = board.getPlayers()[playerIndex];
        this.doubleWall = new WallUI[2];
    }

    protected void initializeWalls() {
        this.walls = gameBoardUI.getAllWalls();
        setWallActionListener();
    }

    public void setWallActionListener() {
        for (WallUI wall : walls) {
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
                        wall.setPlayerColor(currentPlayer.getPlayerColor().getColor());
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
            board.placeWall(row_1, col_1, isVertical);  // Update the game logic
            board.placeWall(row_2, col_2, isVertical);
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
        //updateGameState -better
        removeSelectedWalls();
        removeSelectedSquares();
        gameBoardUI.updateBoard();
        if (getWinner() != null) {
            handleGameEnded();
            return;
        }
        getNextPlayer();
        highlightPossibleMoves();
    }

    private void getNextPlayer() {
        if (++playerIndex == board.getPlayers().length) {
            playerIndex = 0;
        }
        currentPlayer = board.getPlayers()[playerIndex];
    }

    private void handleGameEnded() {
        Player winner = getWinner();
        PopupWindow.showMessage("Game Over! Player " + winner.getName() + " wins!");
        getNextPlayer();
        gameBoardUI.setNewGame(playerIndex);
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

    private void highlightPossibleMoves() {
        List<Position> list = board.possibleMoves(currentPlayer);
        squares = gameBoardUI.setUpPossibleSquares(list);
        setSquaresActionListener();
    }

    public void gameLoop() {
        setUpFirstMove();
    }

    private void setUpFirstMove() {
        initializeWalls();
        gameBoardUI.updateBoard();
        highlightPossibleMoves();
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

    private void removeSelectedWalls() {
        for (WallUI wall : walls) {
            wall.setSelected(false);
        }
    }

    public void setCurrentPlayer(Player player, int playerIndex) {
        this.currentPlayer = player;
        this.playerIndex = playerIndex;
    }

    public GameBoard getGameBoardUI() {
        return gameBoardUI;
    }
}
