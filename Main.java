import UI.*;
import GameLogic.*;

public class Main {
    
    public static void main(String[] args) {
        
        WindowHandlerer wh = new WindowHandlerer();
        GameManager gm = wh.getGamePanel().getGameManager();

        gm.movePlayer(7, 5);
        gm.placeWall(0, 3, true);

        gm.debbugRand();
        gm.nextTurn();
        gm.nextTurn();

    }
}
