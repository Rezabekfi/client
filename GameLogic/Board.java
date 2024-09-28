package GameLogic;

import Settings.Constants;

public class Board {
    private char[][] board;
    private boolean[][] horizontalWalls; 
    private boolean[][] verticalWalls;
    private int numberOfPlayers;
    private Player[] players;

    public Board(int numberOfPlayers) {
        if (numberOfPlayers > Constants.MAXIMUM_POSSIBLE_PLAYERS) {
            numberOfPlayers = Constants.MAXIMUM_POSSIBLE_PLAYERS;
        }
        this.numberOfPlayers = numberOfPlayers;
        this.horizontalWalls = new boolean[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
        this.verticalWalls = new boolean[Constants.BOARD_SIZE][Constants.BOARD_SIZE-1];
        this.board = new char[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
        this.players = new Player[numberOfPlayers];
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
            this.board[players[i].getY()][players[i].getX()] = players[i].getColor();
        }

    }

    // TODO: change color distribution to something else.
    // CAREFUL DIRECTION IS DIRECTION WHICH PLAYER WILL BE FACING AND TRYING TO GET TO NOT THE ONE WEHRE HE STARTS!
    private Player createPlayer(GoalDirection goalDirection) {
        int x, y;
        char color;
        switch (goalDirection) {
            case NORTH:
                x = this.board.length/2;
                y = this.board.length - 1;
                color = Constants.PLAYER_COLORS[0];
                break;
            case EAST:
                x = 0;
                y = this.board.length/2;
                color = Constants.PLAYER_COLORS[2];
                break;
            case SOUTH:
                x = this.board.length/2;
                y = 0;
                color = Constants.PLAYER_COLORS[1];
                break;
            case WEST:
                x = this.board.length - 1;
                y = this.board.length/2;
                color = Constants.PLAYER_COLORS[3];
                break;
            default:
                System.out.println("DISASTER!!! X Y SET TO 0 0");
                x = 0;
                y = 0;
                color = Constants.PLAYER_COLORS[4];
                break;
        }

        return new Player(goalDirection, x, y, color);
    }

    public char[][] getBoard() {
        return board;
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
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(this.board[i][j]);
            }
            System.out.println();
        }
    }
}
