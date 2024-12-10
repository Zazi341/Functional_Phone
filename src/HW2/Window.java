package HW2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Window extends JFrame {
    private BufferedImage backgroundImage;
    private JPanel mainPanel;
    private static final int WINDOW_WIDTH = 1100; 
    private static final int WINDOW_HEIGHT = 900; 


    public Window(String imagePath, String title) {
        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File(imagePath));    
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1); // Exit if the image can't be loaded
        }

        // Set up the JFrame
        setTitle(title);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setResizable(false); // Prevent window resizing

        // Create a JPanel with the background image
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the image scaled to fit the panel
                g.drawImage(backgroundImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, this);
                
            }
        };
        mainPanel = panel;
        panel.setLayout(null); // Allows for custom positioning of components

        // Add the panel to the frame
        setContentPane(panel);
    }


    public JPanel getPanel(){
        return this.mainPanel;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Window window = new Window("phone.png","Phone Main Menu");
            window.setVisible(true);
        });
    }
}
