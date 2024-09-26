import javax.swing.*;
import java.awt.*;

public class CirclePanel extends JPanel {

    private Color circleColor;

    // Constructor to set the circle color
    public CirclePanel(Color circleColor) {
        this.circleColor = circleColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;  // Cast to Graphics2D for better control

        // Anti-aliasing for smoother edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set the color for the circle
        g2d.setColor(circleColor);

        // Calculate size and position for the circle
        int diameter = Math.min(getWidth(), getHeight()) - 10;  // Leave a small margin
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;

        // Draw the circle
        g2d.fillOval(x, y, diameter, diameter);
    }
}
