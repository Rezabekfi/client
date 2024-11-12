package GameLogic;
import java.awt.*;

import Settings.Constants;

public class Player {

    private GoalDirection driection;
    private int numberOfWalls;
    private Position position;
    private char color; 
    private Color color_2d;
    private String playerName;

    public Player(String playerName, GoalDirection direction, Position position, char color){
        this.playerName = playerName;
        this.driection = direction;
        this.numberOfWalls = Constants.STARTING_NUMBER_OF_WALLS;
        this.position = position;
        this.color = color;
        this.color_2d = Constants.CHAR_TO_COLOR.get(color);
    }

    public GoalDirection getDriection() {
        return driection;
    }

    public void setDriection(GoalDirection driection) {
        this.driection = driection;
    }

    public int getNumberOfWalls() {
        return numberOfWalls;
    }

    public void setNumberOfWalls(int numberOfWalls) {
        this.numberOfWalls = numberOfWalls;
    }

    public int getRow() {
        return position.getRow();
    }

    public void setRow(int row) {
        this.position.setRow(row);
    }

    public int getCol() {
        return position.getCol();
    }

    public void setCol(int col) {
        this.position.setCol(col);
    }

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Player [driection=" + driection + ", numberOfWalls=" + numberOfWalls + ", row=" + getRow() + ", col=" + getCol()
                + ", color=" + color + "]";
    }

    public void placedWall() {
        numberOfWalls--;
    }

    public String getName() {
        return playerName;
    }

    public void setName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isPlayerFinished() {
        switch (driection) {
            case NORTH:
                if (this.getRow() == 0) return true;
                break;
            case EAST:
                if (this.getCol() == Constants.BOARD_SIZE-1) return true;
                break;
            case SOUTH:
                if (this.getRow() == Constants.BOARD_SIZE-1) return true;
                break;
            case WEST:
                if (this.getCol() == 0) return true;
                break;
            default:
                return false;
        }
        return false;
    }

    public Color getColor_2d() {
        return color_2d;
    }   
}
