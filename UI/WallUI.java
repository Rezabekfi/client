package UI;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import Settings.Constants;

public class WallUI extends JPanel {

    public WallUI() {
        this.setBackground(Color.BLACK);
        this.setBorder(new LineBorder(Color.BLACK, 1));
    }
    
}
