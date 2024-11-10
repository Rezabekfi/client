package GameLogic;
import java.awt.*;

import Settings.Constants;

public class Player {

    private GoalDirection driection;
    private int numberOfWalls;
    private Position position;
    private char color; 
    private Color color_2d;

    public Color getColor_2d() {
        return color_2d;
    }

    public Player(GoalDirection direction, Position position, char color){
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
}
