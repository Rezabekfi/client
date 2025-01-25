package com.quoridor.UI.Components;

import com.quoridor.GameLogic.Position;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

//import Settings.Constants;


/**
 * Panel that represents a wall on the game board. It is used in the GameBoard panel.
 */
public class WallUI extends JPanel {
    // Flag to indicate if the wall is placed
    public boolean placed;
    // Flag to indicate if the wall is vertical
    public boolean isVertical;
    // Flag to indicate if the wall is selected (highlighted)
    public boolean selected;

    // Color of the player that placed the wall (set to black by default)
    public Color playerColor;

    // Position of the wall on the board
    public int row;
    public int col;

    // Mouse listener for the wall
    private MouseListener mouseListener;

    // Constructor
    public WallUI(int row, int col, boolean isVertical, boolean placed) {
        this.row = row;
        this.col = col;
        this.isVertical = isVertical;
        this.placed = placed;
        this.selected = false; 
        this.playerColor = Color.BLACK;
    }

    // Method to paint the wall
    @Override
    public void paint(Graphics g) {
        if (isPlaced()) {
            this.setBackground(Color.BLACK); 
            this.setBorder(new LineBorder(Color.BLACK, 3));
        } else if(isSelected()) {
            this.setBackground(Color.WHITE);
            this.setBorder(new LineBorder(playerColor, 2));    
        } else {
            this.setBackground(Color.WHITE);
            this.setBorder(new LineBorder(Color.WHITE, 1));
        }
        super.paint(g);
    }
    
    // Getters and setters
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

    public Position getPosition() {
        return new Position(row, col);
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

    public void setMouseListener(MouseListener listener) {
        this.mouseListener = listener;
    }

    public MouseListener getMouseListener() {
        return mouseListener;
    }
}
