package HW2;
import java.awt.Dimension;
import javax.swing.*;
import java.awt.*;


public class InvisibleButton extends JButton {

    public InvisibleButton(double xRatio, double yRatio, int BUTTON_SIZE) {
        // Set the position and size of the button
    	
    	// Get the current screen size

        // Calculate the actual position and size
        int x = (int) (1100 * xRatio);
        int y = (int) (900 * yRatio);

        // Set the position and size of the button
        setBounds(x, y, BUTTON_SIZE, BUTTON_SIZE);
        // Make the button invisible
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setMargin(new Insets(0, 0, 0, 0));  // Remove margins
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // Ensure superclass painting behavior

        if (getIcon() != null) {
            Icon icon = getIcon();
            int x = (getWidth() - icon.getIconWidth())/2;  // Adjust 10 pixels to the right
            int y = (getHeight() - icon.getIconHeight())/2;
            icon.paintIcon(this, g, x, y);  // Draw the icon
        }
    }



    
    
    @Override
    protected void paintBorder(Graphics g) {
        // Do nothing to keep the border invisible
    }
}
