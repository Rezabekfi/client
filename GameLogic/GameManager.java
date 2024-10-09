package GameLogic;

import UI.GameBoard;
import UI.SquareUI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import Settings.Constants;

public class GameManager {

    private Board board;             // Game state (e.g., players, walls)
    private GameBoard gameBoardUI;   // UI component for the game board
    private Player currentPlayer;
    private int playerIndex;
    private List<SquareUI> squares;

    public GameManager(Board board, GameBoard gameBoardUI) {
        this.board = board;
        this.gameBoardUI = gameBoardUI;
        this.playerIndex = 0;
        this.currentPlayer = board.getPlayers()[playerIndex];
    }

    // Handle placing a wall
    public boolean placeWall(int row, int col, boolean isVertical) {
        if (board.canPlaceWall(currentPlayer, row, col, isVertical)) {
            board.placeWall(currentPlayer, row, col, isVertical);  // Update the game logic
            gameBoardUI.updateBoard();             // Refresh the UI
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
        removeSelectedSquares();
        gameBoardUI.updateBoard();
        if (++playerIndex == board.getPlayers().length) {
            playerIndex = 0;
        }
        currentPlayer = board.getPlayers()[playerIndex];
        highlightPossibleMoves();
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
        setActionListener();
    }

    private void setActionListener() {
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
}
