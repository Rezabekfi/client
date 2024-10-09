package GameLogic;

import UI.GameBoard;

import java.util.List;

import Settings.Constants;

public class GameManager {

    private Board board;             // Game state (e.g., players, walls)
    private GameBoard gameBoardUI;   // UI component for the game board
    private Player currentPlayer;
    private int playerIndex;

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
    public boolean movePlayer(int newRow, int newCol) {
        if (board.canMovePlayer(currentPlayer,newRow, newCol)) {
            board.movePlayer(currentPlayer, newRow, newCol);  // Update player position in game logic
            gameBoardUI.updateBoard();                   // Refresh the UI
            return true;
        }
        return false;
    }

    // Optional: Handling turns
    public void nextTurn() {
        gameBoardUI.updateBoard();
        if (++playerIndex == board.getPlayers().length) {
            playerIndex = 0;
        }
        currentPlayer = board.getPlayers()[playerIndex];
        highlightPossibleMoves();
    }

    private void highlightPossibleMoves() {
        List<Position> list = board.possibleMoves(currentPlayer);
        gameBoardUI.lightUpSquares(list);
    }

    // for debugging - current development tested here
    public void debbugRand() {
        System.out.println("debbuging - nothing");
    }
}
