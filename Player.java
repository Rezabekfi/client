import java.awt.*;

public class Player {

    private GoalDirection driection;
    private int numberOfWalls;
    private int x, y;
    private char color; 
    private Color color_2d;

    public Color getColor_2d() {
        return color_2d;
    }

    public Player(GoalDirection direction, int x, int y, char color){
        this.driection = direction;
        this.numberOfWalls = Constants.STARTING_NUMBER_OF_WALLS;
        this.x = x;
        this.y = y;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Player [driection=" + driection + ", numberOfWalls=" + numberOfWalls + ", x=" + x + ", y=" + y
                + ", color=" + color + "]";
    }


}
