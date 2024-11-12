import UI.*;
import GameLogic.*;

public class Main {
    
    public static void main(String[] args) {
        
        QuoridorApp wh = new QuoridorApp();
        GameManager gm = wh.getGamePanel().getGameManager();

        gm.gameLoop();
    }
}
