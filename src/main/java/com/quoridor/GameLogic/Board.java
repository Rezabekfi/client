package com.quoridor.GameLogic;

import java.util.*;

import com.quoridor.Settings.Constants;
import com.quoridor.Settings.PlayerColor;

public class Board {
    private char[][] board;
    private boolean[][] horizontalWalls; 
    private boolean[][] verticalWalls;
    private int numberOfPlayers;
    private Player[] players;
    private int boardSize;

    public Board(int numberOfPlayers) {
        if (numberOfPlayers > Constants.MAXIMUM_POSSIBLE_PLAYERS) {
            numberOfPlayers = Constants.MAXIMUM_POSSIBLE_PLAYERS;
        }
        this.boardSize = Constants.BOARD_SIZE;
        this.numberOfPlayers = numberOfPlayers;
        this.horizontalWalls = new boolean[boardSize][boardSize];
        this.verticalWalls = new boolean[boardSize][boardSize-1];
        this.board = new char[boardSize][boardSize];
        this.players = new Player[numberOfPlayers];

        setUpGame();
    }

    public Board() {
        this(Constants.LEAST_POSSIBLE_PLAYERS);
    }

    public void setUpGame(){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                this.board[i][j] = Constants.EMPTY_SQUARE;
            }
        }
        GoalDirection[] dirs = GoalDirection.values();
        for(int i = 0; i < this.numberOfPlayers; i++) {
            this.players[i] = createPlayer(dirs[i]);
            this.board[players[i].getRow()][players[i].getCol()] = players[i].getColorSymbol();
        }

    }

    private Player createPlayer(GoalDirection goalDirection) {
        int row, col;
        PlayerColor playerColor;
        
        switch (goalDirection) {
            case NORTH:
                col = this.board.length/2;
                row = this.board.length - 1;
                playerColor = PlayerColor.BLUE;
                break;
            case EAST:
                col = 0;
                row = this.board.length/2;
                playerColor = PlayerColor.GREEN;
                break;
            case SOUTH:
                col = this.board.length/2;
                row = 0;
                playerColor = PlayerColor.RED;
                break;
            case WEST:
                col = this.board.length - 1;
                row = this.board.length/2;
                playerColor = PlayerColor.YELLOW;
                break;
            default:
                throw new IllegalStateException("Invalid direction");
        }

        return new Player("XXX", goalDirection, new Position(row, col), playerColor);
    }

    public char[][] getBoard() {
        return board;
    }

    public int getBoardSize() {
        return this.boardSize;
    }

    public boolean[][] getHorizontalWalls() {
        return horizontalWalls;
    }

    public boolean[][] getVerticalWalls() {
        return verticalWalls;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void printBoardDebugging() {
        System.out.println("----------BOARD---------");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(this.board[i][j]);
            }
            System.out.println();
        }
    }

    public boolean canPlaceWall(Player player, int row, int col, boolean isVertical) {
        if (!isInsideArray(row, col, isVertical) || player.getNumberOfWalls() < 1 || !checkEscape() || (isVertical && verticalWalls[row][col]) || (!isVertical && horizontalWalls[row][col])) {
            return false;
        }
        return true;
    }

    private boolean isInsideArray(int row, int col, boolean isVertical) {
        if (isVertical) {
            return !(row < 0 || col < 0 || col >= verticalWalls[0].length || row >= verticalWalls.length);
        } else {
            return !(row < 0 || col < 0 || col >= horizontalWalls[0].length || row >= horizontalWalls.length);
        }
    }

    private boolean checkEscape() {
        return true;
    }

    public void placeWall(int row, int col, boolean isVertical) {
        if (isVertical) {
            verticalWalls[row][col] = true;
        } else {
            horizontalWalls[row][col] = true;
        }
    }

    public void placeWall(Position wall, boolean isVertical) {
        placeWall(wall.getRow(), wall.getCol(), isVertical);
    }

    public boolean canMovePlayer(Player currentPlayer, int newRow, int newCol) {
        int currentRow = currentPlayer.getRow();
        int currentCol = currentPlayer.getCol();

        if (!insideBoard(newRow, newCol) || wallBetween(currentRow, currentCol, newRow, newCol)) {
            return false;
        }

        return true;
    }

    private boolean wallBetween(int currentRow, int currentCol, int newRow, int newCol) {
        if (currentRow - newRow == 0) {
            return verticalWalls[currentRow][Math.min(currentCol, newCol)];
        } else {
            return horizontalWalls[Math.min(newRow, currentRow)][currentCol];
        }
    }

    private boolean insideBoard(int newRow, int newCol) {
        return !(newRow < 0 || newCol < 0 || newRow >= getBoardSize() || newCol >= getBoardSize());
    }

    public void movePlayer(Player player, int newRow, int newCol) {
        if (board[newRow][newCol] != Constants.EMPTY_SQUARE) {
            handlePlayerToPlayerMove(player, newRow, newCol);
        }

        board[newRow][newCol] = player.getColorSymbol();
        board[player.getRow()][player.getCol()] = Constants.EMPTY_SQUARE;
        player.setCol(newCol);
        player.setRow(newRow);
    }

    private void handlePlayerToPlayerMove(Player player, int newRow, int newCol) {
        char kickedOutPlayer = board[newRow][newCol];
        for (int i = 0; i < players.length; i++) {
            if(players[i].getColorSymbol() == kickedOutPlayer) {
                players[i] = createPlayer(players[i].getDirection()); 
                board[players[i].getRow()][players[i].getCol()] = players[i].getColorSymbol();
            }
        }
    }

    public List<Position> possibleMoves(Player player) {
        List<Position> list = new ArrayList<Position>();
        int row = player.getRow();
        int col = player.getCol();
        int newRow, newCol;
        for (int i = 0; i < 2; i++) {
            for (int j = -1; j < 2; j+=2) {
                if (i == 0) {
                    newCol = col + j;
                    newRow = row;
                } else {
                    newRow = row + j;
                    newCol = col;
                }
                if(canMovePlayer(player, newRow, newCol)) {
                    list.add(new Position(newRow, newCol));
                }
            }
        }
        return list;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }
}
