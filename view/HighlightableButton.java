package view;

import javax.swing.*;
import java.awt.*;

public class HighlightableButton extends JButton {
    private boolean isHighlighted = false;
    private Color highlightColour = null;

    HighlightableButton() {
        super();
        setContentAreaFilled(false); // Disable default background painting
        setFocusPainted(false);      // Remove focus painting
    }

    public void setHighlight(boolean highlighted, Color colour) {
        this.isHighlighted = highlighted;
        this.highlightColour = colour;
        repaint(); // Repaint button to apply highlight changes
    }

    public boolean getIsHighlighted(){
        return isHighlighted;
    }

    public Color getSelectedHighlight(){
        return new Color(0, 255, 0, 100); // green
    }

    public Color getBlueHighlight(){
        return new Color(0, 0, 200, 200); // blue
    }

    public Color getRedHighlight(){
        return new Color(200, 0, 0, 200); // red
    }

    public void resetHighlight(){
        setHighlight(false, null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Paint default button appearance

        if (isHighlighted) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(highlightColour); // Green overlay with transparency
            g2d.fillRect(0, 0, getWidth(), getHeight()); // Fill the button
            g2d.dispose();
        }
    }
}
