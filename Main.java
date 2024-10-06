import UI.*;
import GameLogic.*;

public class Main {
    
    public static void main(String[] args) {
        
        WindowHandlerer wh = new WindowHandlerer();
        GameManager gm = wh.getGamePanel().getGameManager();

        gm.movePlayer(8, 5);
    }
}
