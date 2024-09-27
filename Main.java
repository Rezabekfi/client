import javax.swing.*;
import java.awt.*;

public class Main {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Menu game = new Menu();
            game.setVisible(true);
        });
    }
}
