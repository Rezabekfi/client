package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import Settings.Constants;

public class SquareUI extends JPanel {

    private char squareChar;
    private boolean lightedUp;

    public SquareUI(char currentChar) {
        this.squareChar = currentChar;
        this.setBackground(Constants.SQUARE_COLOR);
        this.setBorder(new LineBorder(Constants.OUTLINE_COLOR, 3));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Call the superclass method to ensure proper painting
        Graphics2D g2d = (Graphics2D) g;

        // Enable anti-aliasing for smooth rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (squareChar == Constants.EMPTY_SQUARE) {
            if (lightedUp) {
                lightUpSquare(g2d);
            }
            return;
        }

        // Set the color for the circle
        g2d.setColor(Constants.CHAR_TO_COLOR.get(squareChar)); // You can use a constant or dynamic color if needed

        // Draw the circle (as an oval with equal width and height)
        int diameter = Math.min(getWidth(), getHeight()) - 10;  // Circle size slightly smaller than the panel size
        int x = (getWidth() - diameter) / 2;  // Center the circle horizontally
        int y = (getHeight() - diameter) / 2; // Center the circle vertically
        g2d.fillOval(x, y, diameter, diameter);
    }

    private void lightUpSquare(Graphics2D g2d) {
        g2d.setColor(Constants.POSSIBLE_SQUARE_COLLOR);
        int diameter = Math.min(getWidth(), getHeight()) - 10;  // Circle size slightly smaller than the panel size
        int x = (getWidth() - diameter) / 2;  // Center the circle horizontally
        int y = (getHeight() - diameter) / 2; // Center the circle vertically
        g2d.fillOval(x, y, diameter, diameter);
    }

    public char getSquareChar() {
        return squareChar;
    }

    public void setSquareChar(char squareChar) {
        this.squareChar = squareChar;
    }

    public void setLightedUp(boolean lightedUp) {
        this.lightedUp = lightedUp;
    }

    public boolean isLightedUp() {
        return this.lightedUp;
    }
}
