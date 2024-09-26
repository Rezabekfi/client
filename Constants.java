import java.util.HashMap;
import java.awt.*;

public class Constants{
    public static int STARTING_NUMBER_OF_WALLS = 10; 
    public static int BOARD_SIZE = 9;
    public static int LEAST_POSSIBLE_PLAYERS = 2;
    public static int MAXIMUM_POSSIBLE_PLAYERS = 4;
    public static char EMPTY_SQUARE = 'X';
    public static char[] PLAYER_COLORS = {'b','r','g','y','p'};
    public static HashMap<Character, Color> CHAR_TO_COLOR = new HashMap<Character, Color>() {{
        put('b', Color.BLUE);
        put('r', Color.RED);
        put('g', Color.GREEN);
        put('y', Color.YELLOW);
        put('p', Color.PINK);
    }};
}