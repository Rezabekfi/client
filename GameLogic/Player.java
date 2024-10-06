package GameLogic;
import java.awt.*;

import Settings.Constants;

public class Player {

    private GoalDirection driection;
    private int numberOfWalls;
    private int row, col;
    private char color; 
    private Color color_2d;

    public Color getColor_2d() {
        return color_2d;
    }

    public Player(GoalDirection direction, int row, int col, char color){
        this.driection = direction;
        this.numberOfWalls = Constants.STARTING_NUMBER_OF_WALLS;
        this.row = row;
        this.col = col;
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
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Player [driection=" + driection + ", numberOfWalls=" + numberOfWalls + ", row=" + row + ", col=" + col
                + ", color=" + color + "]";
    }


}
