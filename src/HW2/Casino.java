package HW2;

import javax.swing.*;
import java.awt.Font;
import java.awt.Color;

public class Casino extends App {
    protected float casinobalance;
    private Wallet wallet;
    private BlackJack blackjack;
    private Slots slots;

    public Casino() {
        super("Casino");
        this.casinobalance = 0.0f;
        this.blackjack = new BlackJack(6, true, true); // 6 decks, dealer hits on soft 17, insurance available
        this.slots = new Slots();
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public void setBlackJack(BlackJack blackjack) {
        this.blackjack = blackjack;
    }

    public float getCasinoBalance() {
        return casinobalance;
    }

    public void withdraw(float amount) {
        if (amount < 0) {
            JOptionPane.showMessageDialog(null, "\nError, you can't withdraw a negative amount");
            return;
        }
        if (amount > casinobalance) {
            JOptionPane.showMessageDialog(null, "\nError, you can't withdraw more than your balance");
            return;
        }
        if (amount > 0) {
            wallet.balance += amount;
            casinobalance -= amount;
            JOptionPane.showMessageDialog(null, "Withdrew: $" + String.format("%.2f", amount));
        } else {
            JOptionPane.showMessageDialog(null, "Invalid amount. Must be positive.");
        }
    }

    @Override
    public void start(Window window) {
    	InvisibleButton bj = new InvisibleButton(0.3703, 0.6171, 250);
    	InvisibleButton slots = new InvisibleButton(0.3703, 0.2646, 250);
        JPanel casinopanel = window.getPanel();
        casinopanel.add(bj);
        casinopanel.add(slots);

        // Create a label to display the balance
        JLabel balanceLabel = new JLabel("Current Balance: $" + String.format("%.2f", casinobalance));
        balanceLabel.setFont(new Font("Calibre", Font.BOLD, 25));
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setBounds(380, 80, 400, 100); // (x, y, width, height)
        casinopanel.add(balanceLabel);

        // Create a pretty button for withdrawing
        JButton withdrawButton = new JButton("Withdraw money to your wallet");
        withdrawButton.setBounds(350, 150, 400, 50);
        withdrawButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        withdrawButton.setBackground(new Color(70, 130, 180)); // SteelBlue background
        withdrawButton.setForeground(Color.WHITE); // White text
        withdrawButton.setFocusPainted(false); // Remove focus border
        withdrawButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        casinopanel.add(withdrawButton);

        // Create a pretty button for exiting
        JButton exitButton = new JButton("Leave the Casino");
        exitButton.setBounds(350, 180, 400, 50);
        exitButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        exitButton.setBackground(new Color(70, 130, 180)); // SteelBlue background
        exitButton.setForeground(Color.WHITE); // White text
        exitButton.setFocusPainted(false); // Remove focus border
        exitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        casinopanel.add(exitButton);

        // Action listener for exit button
        exitButton.addActionListener(e -> {
            window.dispose();
        });

        // Action listener for Blackjack button
        bj.addActionListener(e -> {
            Window bjWin = new Window("bj.png", "BLACKJACK");
            bjWin.setVisible(true);
            playBlackjack();
            bjWin.dispose();
            balanceLabel.setText("Current Balance: $" + String.format("%.2f", casinobalance));
            casinopanel.revalidate();
            casinopanel.repaint();
        });

        // Action listener for Slots button
        slots.addActionListener(e -> {
            Window slotWin = new Window("slots.png", "SLOTS");
            slotWin.setVisible(true);
            playSlots(slotWin);
            slotWin.dispose();
            balanceLabel.setText("Current Balance: $" + String.format("%.2f", casinobalance));
            casinopanel.revalidate();
            casinopanel.repaint();
        });

        // Action listener for Withdraw button
        withdrawButton.addActionListener(e -> {
            String withdrawAmount = JOptionPane.showInputDialog(null, "Enter amount to Withdraw to the Wallet: ");
            if (withdrawAmount != null) {
                try {
                    float withdrawAmountFloat = Float.parseFloat(withdrawAmount);
                    withdraw(withdrawAmountFloat);
                    balanceLabel.setText("Current Balance: $" + String.format("%.2f", casinobalance));
                    casinopanel.revalidate();
                    casinopanel.repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a numeric value.");
                }
            }
        });
    }

    private void playSlots(Window window) {
        JOptionPane.showMessageDialog(null, "Current Jackpot: $" + String.format("%.2f", slots.getJackpot()));
        String bet = JOptionPane.showInputDialog(null, "Enter your bet amount: $");

        if (bet == null) {
            JOptionPane.showMessageDialog(null, "Betting cancelled.");
            return;
        }

        float betAmount;
        try {
            betAmount = Float.parseFloat(bet);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a numeric value.");
            return;
        }

        if (betAmount <= 0 || betAmount > casinobalance) {
            JOptionPane.showMessageDialog(null, "Invalid bet amount. Please try again.");
            return;
        }

        float result = slots.play(betAmount, window);
        casinobalance += result;

        JOptionPane.showMessageDialog(null, result >= 0 ? "You won $" + String.format("%.2f", result) : "You lost $" + String.format("%.2f", -result));
        JOptionPane.showMessageDialog(null, "Casino balance: $" + String.format("%.2f", casinobalance));
    }

    private void playBlackjack() {
        String bet = JOptionPane.showInputDialog(null, "Enter your bet amount: $");

        if (bet == null) {
            JOptionPane.showMessageDialog(null, "Betting cancelled.");
            return;
        }

        float betAmount;
        try {
            betAmount = Float.parseFloat(bet);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a numeric value.");
            return;
        }

        if (betAmount <= 0 || betAmount > casinobalance) {
            JOptionPane.showMessageDialog(null, "Invalid bet amount. Please try again.");
            return;
        }

        float result = blackjack.play(betAmount);
        casinobalance += result;
        JOptionPane.showMessageDialog(null, "Casino balance: $" + String.format("%.2f", casinobalance));

        JOptionPane.showMessageDialog(null, "Advice for next hand: " + blackjack.getAdvice());
    }

    @Override
    public void displayAllContents() {
        // Implementation for displaying all contents, if needed.
    }
}
