import javax.swing.*;
import java.awt.*;

public class Main {
    
    public static void main(String[] args) {
        // Initialize the game logic board
        Board board = new Board(Constants.MAXIMUM_POSSIBLE_PLAYERS);  // Create a board with 4 players (for example)
        board.setUpGame();  // Set up the game state
        
        // Create the main window (JFrame)
        JFrame frame = new JFrame("Game Board with GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);  // Set window size
        
        // Create an instance of GameWindow (our JPanel subclass) and pass the game board
        GameWindow gameWindow = new GameWindow(board);
        
        // Add the game window to the frame
        frame.add(gameWindow);
        
        // Make the window visible
        frame.setVisible(true);

        // Optionally, print the board for debugging
        board.printBoardDebugging();
    }
}
