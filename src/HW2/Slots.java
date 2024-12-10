package HW2;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.Font;
import javax.swing.Timer;


public class Slots {
    private static final String[] SYMBOLS = {"🍒", "🍋", "🍊", "🍇", "🔔", "💎", "❼", "🎰"};
    private static final int NUM_REELS = 5;
    private static final int NUM_ROWS = 3;
    private float jackpot;
    private Random rng;

    public Slots() {
        this.jackpot = 1000.0f; // Starting jackpot
        this.rng = new Random();
    }

    public float play(float betAmount, Window window) {
        String[][] result = spin(window);
        return calculateWinnings(result, betAmount, window);
    }
    private void displayFrame(String[][] frame, JPanel panel, JLabel label) {
        SwingUtilities.invokeLater(() -> {
            // Build the text to display
            StringBuilder sb = new StringBuilder();
            sb.append("╔════════════════╗\n");
            for (int i = 0; i < NUM_ROWS; i++) {
                sb.append("║ ");
                for (int j = 0; j < NUM_REELS; j++) {
                    sb.append(frame[i][j]).append(" ");
                }
                sb.append("║\n");
            }
            sb.append("╚════════════════╝");

            label.setText("<html><pre>" + sb.toString() + "</pre></html>");
            label.setFont(new Font("Calibre", Font.BOLD, 40)); // Ensure font size is set
            label.setOpaque(true); // Make the label opaque
            label.setBackground(Color.WHITE);
            label.setBounds(700,150,380,200);
            // Clear existing contents and add the new JLabel
            panel.removeAll();
            panel.add(label);
            // Revalidate and repaint to update the panel
            panel.revalidate();
        });
    }

    private String[][] spin(Window window) {
        String[][] result = new String[NUM_ROWS][NUM_REELS];
        int[] speeds = new int[NUM_REELS];
        int[] positions = new int[NUM_REELS];
        final int[] frameCount = {50}; // Use an array to wrap the frameCount

        // Initialize different speeds and starting positions for each reel
        for (int i = 0; i < NUM_REELS; i++) {
            speeds[i] = rng.nextInt(3) + 1; // Speed 1-3
            positions[i] = rng.nextInt(SYMBOLS.length);
        }

        // Create a JLabel with initial empty text
        JLabel label = new JLabel("");
        label.setFont(new Font("Calibre", Font.BOLD, 40));

        // Timer to update the slot frames
        Timer timer = new Timer(100, e -> {
            if (frameCount[0]-- <= 0) { // Access frameCount via the array
                ((Timer)e.getSource()).stop(); // Stop the timer when done
                return;
            }

            String[][] currentFrame = new String[NUM_ROWS][NUM_REELS];
            for (int col = 0; col < NUM_REELS; col++) {
                for (int row = 0; row < NUM_ROWS; row++) {
                    int symbolIndex = (positions[col] + row) % SYMBOLS.length;
                    currentFrame[row][col] = SYMBOLS[symbolIndex];
                }
                positions[col] = (positions[col] + speeds[col]) % SYMBOLS.length;
            }
            displayFrame(currentFrame, window.getPanel(), label);
            window.repaint();
        });
        timer.start(); // Start the timer

        // Final result after spinning
        for (int col = 0; col < NUM_REELS; col++) {
            for (int row = 0; row < NUM_ROWS; row++) {
                int symbolIndex = (positions[col] + row) % SYMBOLS.length;
                result[row][col] = SYMBOLS[symbolIndex];
            }
        }
        displayFrame(result, window.getPanel(), label);
        return result;
    }



    private float calculateWinnings(String[][] result, float betAmount, Window window) {
        float winnings = 0;

        // Check for horizontal lines
        for (int i = 0; i < NUM_ROWS; i++) {
            winnings += checkLine(result[i], betAmount);
        }

        // Check for vertical lines
        for (int j = 0; j < NUM_REELS; j++) {
            String[] column = new String[NUM_ROWS];
            for (int i = 0; i < NUM_ROWS; i++) {
                column[i] = result[i][j];
            }
            winnings += checkLine(column, betAmount);
        }

     // Check for diagonals
        // Top-left to bottom-right
        String[] diagonal1 = new String[NUM_ROWS];
        for (int i = 0; i < NUM_ROWS; i++) {
            diagonal1[i] = result[i][i];
        }
        winnings += checkLine(diagonal1, betAmount);

        // Top-right to bottom-left
        String[] diagonal2 = new String[NUM_ROWS];
        for (int i = 0; i < NUM_ROWS; i++) {
            diagonal2[i] = result[i][NUM_REELS - 1 - i];
        }
        winnings += checkLine(diagonal2, betAmount);


        // Progressive jackpot
        if (winnings == 0) {
            jackpot += betAmount * 0.1f;
        }

        // Bonus level
        if (rng.nextFloat() < 0.05) { // 5% chance of bonus level
            JOptionPane.showMessageDialog(null, "Bonus level activated!");
            winnings *= 2;
        }

        // Jackpot
        if (rng.nextFloat() < 0.001) { // 0.1% chance of jackpot
            JOptionPane.showMessageDialog(null, "JACKPOT!");
            winnings += jackpot;
            jackpot = 1000.0f; // Reset jackpot
        }

        return winnings - betAmount;
    }

    private float checkLine(String[] line, float betAmount) {
        if (Arrays.stream(line).distinct().count() == 1) {
            // All symbols in the line are the same
            return betAmount * 10;
        } else if (Arrays.stream(line).filter(s -> s.equals("🃏")).count() >= 3) {
            // Wild symbol bonus
            return betAmount * 5;
        }
        return 0;
    }

    public float getJackpot() {
        return jackpot;
    }
}