package com.quoridor.Settings;
import java.util.HashMap;
import java.awt.*;

public class Constants{
    public static String GAME_NAME = "Quoridor";
    public static int STARTING_NUMBER_OF_WALLS = 10; 
    public static int BOARD_SIZE = 9;
    public static int LEAST_POSSIBLE_PLAYERS = 2;
    public static int MAXIMUM_POSSIBLE_PLAYERS = 4;
    public static char EMPTY_SQUARE = 'X';
    public static char PLAYER_1_CHAR = '1';
    public static char PLAYER_2_CHAR = '2';
    public static char PLAYER_3_CHAR = '3';
    public static char PLAYER_4_CHAR = '4';
    public static char[] PLAYER_COLORS = {'b','r','g','y','p'};
    public static HashMap<Character, Color> CHAR_TO_COLOR = new HashMap<Character, Color>() {{
        put('1', Color.BLUE);
        put('2', Color.RED);
        put('3', Color.GREEN);
        put('4', Color.YELLOW);
        put('5', Color.PINK);
    }};

    public static Color SQUARE_COLOR = Color.WHITE;
    public static Color OUTLINE_COLOR = Color.LIGHT_GRAY;
    public static Color WALL_COLOR = Color.GREEN;
    public static Color POSSIBLE_SQUARE_COLLOR = Color.GRAY;

    //WINDOW SETTINGS 
    public static int WINDOW_WIDTH = 1920/2;
    public static int WINDOW_HEIGHT = 1080/2;

    public static String MENU_CARD = "Menu";
    public static String GAME_ON_CARD = "Game";
    public static String GAME_OFF_CARD = "Game";
    public static String SETTINGS_CARD = "Settings";

    public static String DEFAULT_PLAYER_NAME = "Player";

    public static int EXIT_SUCCES = 0;
}