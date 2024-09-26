public class Player {

    private GoalDirection driection;
    private int numberOfWalls;
    private int x, y;
    private char color; 

    public Player(GoalDirection direction, int x, int y, char color){
        this.driection = direction;
        this.numberOfWalls = Constants.STARTING_NUMBER_OF_WALLS;
        this.x = x;
        this.y = y;
        this.color = color;
    }
}
