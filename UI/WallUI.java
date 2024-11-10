package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import Settings.Constants;

public class WallUI extends JPanel {

    public boolean placed;
    public boolean isVertical;
    public boolean selected;
    public Color playerColor;

    public int row;
    public int col;

    public WallUI(int row, int col, boolean isVertical, boolean placed) {
        this.row = row;
        this.col = col;
        this.isVertical = isVertical;
        this.placed = placed;
        this.selected = false; 
        this.playerColor = Color.BLACK;
    }

    @Override
    public void paint(Graphics g) {
        if (isPlaced()) {
            this.setBackground(Color.BLACK);
            this.setBorder(new LineBorder(playerColor, 1));    
        } else if(isSelected()) {
            this.setBackground(Color.WHITE);
            this.setBorder(new LineBorder(playerColor, 2));    
        } else {
            this.setBackground(Color.WHITE);
            this.setBorder(new LineBorder(Color.WHITE, 1));
        }
        super.paint(g);
    }
    
    public boolean isPlaced() {
        return placed;
    }

    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

    public boolean isVertical() {
        return isVertical;
    }

    public void setVertical(boolean isVertical) {
        this.isVertical = isVertical;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }
}
