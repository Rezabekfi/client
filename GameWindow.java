import javax.swing.*;
import java.awt.*;

public class GameWindow extends JPanel {

    private static final int BOARD_SIZE = Constants.BOARD_SIZE;
    private Board gameBoard;  // Your game logic Board

    // Constructor to initialize the game board and setup the GUI
    public GameWindow(Board gameBoard) {
        this.gameBoard = gameBoard;  // Reference to the game logic board
        this.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));

        // Set up the GUI for the board (visual representation)
        updateBoardDisplay();
    }

    // Method to refresh and display the current state of the board
    public void updateBoardDisplay() {
        this.removeAll();  // Clear the current GUI components
        
        char[][] boardState = gameBoard.getBoard();  // Get the game board state
        
        // Loop through the board array and visualize it
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                JPanel square = new JPanel(new BorderLayout());
                char currentCell = boardState[row][col];
                
                // Color based on empty square or player
                if (currentCell == Constants.EMPTY_SQUARE) {
                    square.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.BLACK);  // Chessboard color
                } else {
                    JPanel playerLabel = new CirclePanel(Constants.CHAR_TO_COLOR.get(currentCell));
                    square.add(playerLabel);
                }
                
                // Optional: set a border for better visibility
                square.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                this.add(square);
            }
        }
        
        this.revalidate();  // Refresh the display
        this.repaint();
    }
    
}
