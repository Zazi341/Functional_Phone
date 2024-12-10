package HW2;

import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicReference;

public class BlackJack {
    private float bet;  // Stores the current bet amount
    private List<String> playerHand;  // List to store the player's hand
    private List<String> dealerHand;  // List to store the dealer's hand
    private List<String> deck;  // List to store the deck of cards
    private Random random;  // Random object for shuffling and drawing cards
    private boolean insuranceAvailable;  // Indicates if insurance is available
    private boolean dealerHitsOnSoft17;  // Indicates if the dealer hits on soft 17
    private int numDecks;  // Number of decks being used in the game

    public BlackJack(int numDecks, boolean dealerHitsOnSoft17, boolean insuranceAvailable) {
        this.numDecks = numDecks;  // Initialize the number of decks
        this.dealerHitsOnSoft17 = dealerHitsOnSoft17;  // Set dealer hitting behavior
        this.insuranceAvailable = insuranceAvailable;  // Set insurance availability
        this.random = new Random();  // Initialize the Random object
        initializeDeck();  // Initialize the deck of cards
    }

    private void initializeDeck() {
        deck = new ArrayList<>();  // Initialize the deck list
        String[] suits = {"♠", "♥", "♦", "♣"};  // Array of card suits
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};  // Array of card ranks
        for (int i = 0; i < numDecks; i++) {  // Loop through each deck
            for (String suit : suits) {  // Loop through each suit
                for (String rank : ranks) {  // Loop through each rank
                    deck.add(rank + suit);  // Add the card to the deck
                }
            }
        }
        shuffleDeck();  // Shuffle the deck
    }
    
    private void shuffleDeck() {
        // Shuffle the deck using the Fisher-Yates algorithm
        for (int i = deck.size() - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            String temp = deck.get(index);
            deck.set(index, deck.get(i));
            deck.set(i, temp);
        }
    }
    
    private String drawCard() {
        // Check if the deck needs to be reshuffled
        if (deck.size() < numDecks * 52 / 2) {
            JOptionPane.showMessageDialog(null,"Shuffling the deck...");
            initializeDeck();
        }
        // Draw a card from the deck
        return deck.remove(random.nextInt(deck.size()));
    }

    public float play(float betAmount) {
        bet = betAmount;  // Set the bet amount
        playerHand = new ArrayList<>();  // Initialize the player's hand
        dealerHand = new ArrayList<>();  // Initialize the dealer's hand

        // Initial deal
        playerHand.add(drawCard());  // Deal first card to player
        dealerHand.add(drawCard());  // Deal first card to dealer
        playerHand.add(drawCard());  // Deal second card to player
        dealerHand.add(drawCard());  // Deal second card to dealer

        displayHands(false);  // Display the hands without revealing the dealer's second card

        // Check for player Blackjack
        if (calculateHandValue(playerHand) == 21) {
            JOptionPane.showMessageDialog(null,"Blackjack! You win 1.5 times your bet!");
            return bet * 1.5f;
        }

        // Offer insurance if available and the dealer's upcard is an Ace
        if (insuranceAvailable && dealerHand.get(0).startsWith("A")) {
            if (offerInsurance()) {
                if (calculateHandValue(dealerHand) == 21) {
                    JOptionPane.showMessageDialog(null,"Dealer has Blackjack. Insurance pays 2:1.");
                    return bet / 2;
                } else {
                    JOptionPane.showMessageDialog(null,"Dealer does not have Blackjack. You lose your insurance bet.");
                    bet *= 1.5;
                }
            }
        }

        // Player's turn
        playerTurn();

        // Dealer's turn if player hasn't busted
        if (calculateHandValue(playerHand) <= 21) {
            dealerTurn();
        }

        // Determine the winner
        return determineWinner();
    }

    private void playerTurn() {
        boolean firstTurn = true;  // Flag to check if it's the player's first turn

        // Loop for the player's turn until they choose to stand or bust
        while (calculateHandValue(playerHand) < 21) {
            String choice;
            if (firstTurn) {
                choice = JOptionPane.showInputDialog(null,"Do you want to (H)it, (S)tand, or (D)ouble down?");
            } else {
                choice = JOptionPane.showInputDialog(null,"Do you want to (H)it or (S)tand?");
            }

            if (choice == null) {  // If the player doesn't make a choice, they stand by default
                JOptionPane.showMessageDialog(null,"Time's up! Standing by default.");
                break;
            }

            switch (choice.toUpperCase()) {
                case "H":
                    playerHand.add(drawCard());  // Player hits, draw another card
                    displayHands(false);
                    firstTurn = false;
                    break;
                case "S":
                    return;  // Player stands, end their turn
                case "D":
                    // Double down option available only on the first turn
                    if (firstTurn) {
                        bet *= 2;  // Double the bet
                        JOptionPane.showMessageDialog(null,"You've doubled down. Your bet is now $" + bet);

                        // Ask if the player wants to continue after doubling down
                        choice = JOptionPane.showInputDialog(null,"Do you want to (H)it or (S)tand?");

                        if (choice == null || choice.toUpperCase().equals("S")) {
                            JOptionPane.showMessageDialog(null, "Standing after double down.");
                            playerHand.add(drawCard());
                            displayHands(false);
                            return;
                        } else if (choice.toUpperCase().equals("H")) {
                            playerHand.add(drawCard());
                            playerHand.add(drawCard());
                            displayHands(false);
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid choice. Standing by default.");
                            playerHand.add(drawCard());
                            displayHands(false);
                            return;
                        }
                        firstTurn = false;
                    } else {
                        JOptionPane.showMessageDialog(null, "You can only double down on your first turn.");
                    }
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
            }
        }
    }

    private void dealerTurn() {
        // Dealer's turn: draw cards until the dealer should stand
        while (shouldDealerHit()) {
            dealerHand.add(drawCard());
        }
        displayHands(true);  // Display the dealer's full hand
    }

    private boolean shouldDealerHit() {
        // Determine if the dealer should hit or stand
        int value = calculateHandValue(dealerHand);
        return value < 17 || (dealerHitsOnSoft17 && value == 17 && containsAce(dealerHand));
    }

    private float determineWinner() {
        // Determine the winner of the round
        int playerValue = calculateHandValue(playerHand);
        int dealerValue = calculateHandValue(dealerHand);

        if (playerValue > 21) {
            JOptionPane.showMessageDialog(null, "You bust! Dealer wins.");
            return -bet;  // Player busts, they lose their bet
        } else if (dealerValue > 21) {
            JOptionPane.showMessageDialog(null, "Dealer busts! You win!");
            return bet;  // Dealer busts, player wins their bet
        } else if (playerValue > dealerValue) {
            JOptionPane.showMessageDialog(null, "You win!");
            return bet;  // Player's hand is better, they win their bet
        } else if (playerValue < dealerValue) {
            JOptionPane.showMessageDialog(null, "Dealer wins.");
            return -bet;  // Dealer's hand is better, player loses their bet
        } else {
            JOptionPane.showMessageDialog(null, "It's a tie!");
            return 0;  // Tie, no money is won or lost
        }
    }

    private int calculateHandValue(List<String> hand) {
        int value = 0;  // Initialize the hand value
        int aceCount = 0;  // Count the number of aces in the hand
        for (String card : hand) {
            String rank = card.substring(0, card.length() - 1);  // Extract the rank from the card
            if (rank.equals("A")) {
                aceCount++;  // Count the ace
            } else if (rank.equals("K") || rank.equals("Q") || rank.equals("J")) {
                value += 10;  // Face cards are worth 10
            } else {
                value += Integer.parseInt(rank);  // Add the numeric value of the card
            }
        }
        for (int i = 0; i < aceCount; i++) {
            if (value + 11 <= 21) {
                value += 11;  // Add 11 for an ace if it doesn't bust the hand
            } else {
                value += 1;  // Otherwise, add 1 for the ace
            }
        }
        return value;  // Return the calculated value of the hand
    }

    private void displayHands(boolean showDealerHand) {
        // Display the player's hand
        JOptionPane.showMessageDialog(null, "Your hand: " + playerHand + " (Value: " + calculateHandValue(playerHand) + ")");
        if (showDealerHand) {
            // Display the dealer's full hand
            JOptionPane.showMessageDialog(null, "Dealer's hand: " + dealerHand + " (Value: " + calculateHandValue(dealerHand) + ")");
        } else {
            // Hide the dealer's second card
            JOptionPane.showMessageDialog(null, "Dealer's hand: " + dealerHand.get(0) + " [Hidden]");
        }
    }

    public boolean offerInsurance() {
        // Offer insurance to the player when the dealer's up card is an Ace
        int option = JOptionPane.showConfirmDialog(
                null,
                "Dealer's up card is an Ace. Would you like to buy insurance?",
                "Insurance Offer",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        // Return true if the user selected "Yes", false if "No"
        return option == JOptionPane.YES_OPTION;
    }

    private boolean containsAce(List<String> hand) {
        // Check if the hand contains an Ace
        for (String card : hand) {
            if (card.startsWith("A")) {
                return true;
            }
        }
        return false;
    }

    private static final int TIME_LIMIT_SECONDS = 60; // Set the time limit for user input

    public String getTimedInput() {
        // Create a panel with a text field for input
        JPanel panel = new JPanel();
        new JLabel("Enter your choice (H/S):");
        JTextField textField = new JTextField(2);

        // Create an AtomicReference to store the user input
        AtomicReference<String> userInput = new AtomicReference<>(null);

        // Create a Timer to handle the timeout
        Timer timer = new Timer(TIME_LIMIT_SECONDS * 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Set input to null if time runs out
                if (userInput.get() == null) {
                    userInput.set(null);
                    textField.setEnabled(false);
                    JOptionPane.showMessageDialog(null, "Time's up! Default action will be taken.", "Timeout", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        timer.setRepeats(false);  // Timer should not repeat
        timer.start();  // Start the timer

        // Show the dialog
        int result = JOptionPane.showConfirmDialog(null, panel, "Timed Input", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        // Stop the timer if user responds
        timer.stop();

        // Handle the result based on user input or timeout
        if (result == JOptionPane.OK_OPTION) {
            userInput.set(textField.getText().trim());
        } else {
            // User closed the dialog or pressed cancel
            userInput.set(null);
        }

        // Return the user input or null if timed out
        return userInput.get();
    }

    public String getAdvice() {
        int playerValue = calculateHandValue(playerHand);  // Calculate the player's hand value
        String dealerUpCard = dealerHand.get(0).substring(0, dealerHand.get(0).length() - 1);  // Get the dealer's up card

        // Provide advice based on the player's hand value and the dealer's up card
        if (playerValue >= 17) return "Stand";
        if (playerValue <= 8) return "Hit";
        
        if (playerValue == 9) {
            if (dealerUpCard.equals("3") || dealerUpCard.equals("4") || dealerUpCard.equals("5") || dealerUpCard.equals("6")) {
                return "Double down if allowed, otherwise Hit";
            } else {
                return "Hit";
            }
        }
        
        if (playerValue == 10) {
            if (dealerUpCard.equals("10") || dealerUpCard.equals("J") || dealerUpCard.equals("Q") || dealerUpCard.equals("K") || dealerUpCard.equals("A")) {
                return "Hit";
            } else {
                return "Double down if allowed, otherwise Hit";
            }
        }
        
        if (playerValue == 11) return "Double down if allowed, otherwise Hit";
        
        if (playerValue == 12) {
            if (dealerUpCard.equals("4") || dealerUpCard.equals("5") || dealerUpCard.equals("6")) {
                return "Stand";
            } else {
                return "Hit";
            }
        }
        
        if (playerValue >= 13 && playerValue <= 16) {
            if (dealerUpCard.equals("2") || dealerUpCard.equals("3") || dealerUpCard.equals("4") || dealerUpCard.equals("5") || dealerUpCard.equals("6")) {
                return "Stand";
            } else {
                return "Hit";
            }
        }
        
        return "Stand";  // Default to Stand
    }
}
