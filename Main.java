import UI.*;
import GameLogic.*;

public class Main {
    
    public static void main(String[] args) {
        
        WindowHandlerer wh = new WindowHandlerer();
        Board board = wh.getGamePanel().getGameBoard().getBoard();

        char[][] bb = board.getBoard();
        bb[0][4] = 'X';
        board.printBoardDebugging();
        wh.getGamePanel().getGameBoard().updateBoard();
    }
}
