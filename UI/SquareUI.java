package UI;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import Settings.Constants;

public class SquareUI extends JPanel {

    public SquareUI() {
        this.setBackground(Constants.squareColor);
        this.setBorder(new LineBorder(Constants.outlineColor, 3));
    }
    
}
