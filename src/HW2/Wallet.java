package HW2;

import java.util.*;
import javax.swing.*;
import java.io.*;

public class Wallet extends App {
    protected float balance;  // Stores the current balance in the wallet
    private PhoneBook phoneBook;  // Reference to a PhoneBook object
    private Casino casino;  // Reference to a Casino object
    private List<Transfer> transfers;  // List to keep track of all transfers
    private Map<String, String> creditCards; // Map to store credit card numbers and CVVs
    private Map<String, Float> exchangeRates; // Map to store exchange rates for various currencies

    public Wallet() {
        super("Wallet");
        this.balance = 0.0f;  // Initialize balance to 0
        this.transfers = new ArrayList<>();  // Initialize the list of transfers
        this.creditCards = new HashMap<>();  // Initialize the map of credit cards
        this.exchangeRates = new HashMap<>();  // Initialize the map of exchange rates
        initializeCreditCards();  // Load or initialize credit cards
        initializeExchangeRates();  // Initialize exchange rates
    }
    
    private void initializeExchangeRates() {
        // Initialize exchange rates (1 USD to other currencies)
    	exchangeRates.put("ILS", 3.72f);
        exchangeRates.put("EUR", 0.85f);
        exchangeRates.put("GBP", 0.73f);
        exchangeRates.put("JPY", 110.0f);
    }
    
    public void saveToFile(String filename) throws IOException {
        // Append .txt if the filename doesn't end with it
        if (!filename.endsWith(".txt")) {
            filename += ".txt";
        }

        String relativePath = "src/HW2/" + filename;
        // Save credit card details to a file
        try (PrintWriter writer = new PrintWriter(new FileWriter(relativePath))) {
            for (Map.Entry<String, String> entry : creditCards.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue());
            }
        }
        // Notify the user that the cards have been saved
        JOptionPane.showMessageDialog(null,"Credit cards saved successfully to " + filename);
    }

    public void loadFromFile(String filename) throws IOException {
        // Append .txt if the filename doesn't end with it
        if (!filename.endsWith(".txt")) {
            filename += ".txt";
        }
        String relativePath = "src/HW2/" + filename;

        // Load credit card details from a file
        try (BufferedReader reader = new BufferedReader(new FileReader(relativePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    creditCards.put(parts[0], parts[1]);
                }
            }
        }
        // Notify the user that the cards have been loaded
        JOptionPane.showMessageDialog(null,"Credit cards loaded successfully from " + filename);
    }
    
    private void initializeCreditCards() {
        // Attempt to load credit cards from file
        try {
            loadFromFile("credit_cards.txt");
        } catch (IOException e) {
        	// If file not found, create a new one with example cards
        	JOptionPane.showMessageDialog(null,"No existing credit cards file found. Creating new file with example cards.");
            creditCards.put("1234-5678-9012-3456", "123");
            creditCards.put("9876-5432-1098-7654", "456");
            try {
                saveToFile("credit_cards.txt");
            } catch (IOException ex) {
            	JOptionPane.showMessageDialog(null,"Error saving credit cards to file: " + ex.getMessage());
            }
        }
    }

    public void setPhoneBook(PhoneBook phoneBook) {
        // Set the phoneBook object reference
        this.phoneBook = phoneBook;
    }

    public void setCasino (Casino casino) {
        // Set the casino object reference
        this.casino = casino; 
    }
    
    private boolean isValidCreditCard(String cardNumber, String cvv) {
        // Validate the format of the card number
        if (!cardNumber.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}")) {
        	JOptionPane.showMessageDialog(null,"Error! Invalid card number format. Use format: aaaa-bbbb-cccc-dddd");
            return false;
        }

        // Validate the format of the CVV
        if (!cvv.matches("\\d{3}")) {
        	JOptionPane.showMessageDialog(null,"Error! Invalid CVV. Must be 3 digits.");
            return false;
        }

        return true;  // Card number and CVV are valid
    }

    public void deposit(float amount, String cardNumber, String cvv) {
        // Check if the deposit amount is positive
        if (amount <= 0) {
        	JOptionPane.showMessageDialog(null,"Invalid amount. Must be positive.");
            return;
        }

        // Validate the credit card details
        if (!isValidCreditCard(cardNumber, cvv)) {
            return;
        }

        // Add the deposit amount to the balance
        balance += amount;
        // Notify the user that the deposit was successful
        JOptionPane.showMessageDialog(null,"Deposited: $" + String.format("%.2f", amount));
    }

    public float getBalance() {
        // Return the current balance
        return balance;
    }

    public void transferMoney(String recipient, float amount) {
        // Check if the transfer amount is valid
        if (amount <= 0) {
        	JOptionPane.showMessageDialog(null,"Invalid transaction amount. Must be positive.");
            return;
        }
        else if (amount > balance) {
        	JOptionPane.showMessageDialog(null,"Invalid transaction amount. You don't have enough balance in your wallet.");
        	return;
        }
        
        // Check if the recipient exists in the phonebook
        if ((phoneBook.contactExists(recipient))) {
            balance -= amount;  // Deduct the amount from the balance
            transfers.add(new Transfer(recipient, amount));  // Add the transfer to the list
            JOptionPane.showMessageDialog(null,"Transferred $" + String.format("%.2f", amount) + " to " + recipient);
        } else {
            // Special case: if the recipient is the casino
            if ((recipient.equalsIgnoreCase("casino"))) {
                balance -= amount;  // Deduct the amount from the balance
                casino.casinobalance += amount;  // Add the amount to the casino balance
                transfers.add(new Transfer(recipient, amount));  // Add the transfer to the list
                JOptionPane.showMessageDialog(null,"Transferred $" + String.format("%.2f", amount) + " to " + recipient);
            } else {
            	JOptionPane.showMessageDialog(null,"Transfer failed. Invalid recipient.");
            }
        }
    }

    @Override
    public void displayAllContents() {
        // Check if there are any transfers
        if (transfers.isEmpty()) {
        	JOptionPane.showMessageDialog(null,"\nNo transfers made yet.");
            return;
        }
        // Display the transfer history
        StringBuilder transferHistory = new StringBuilder("\nTransfers history:\n");
        for (Transfer transfer : transfers) {
            transferHistory.append(transfer).append("\n");
        }
        JOptionPane.showMessageDialog(null, transferHistory.toString());
    }

    public void addCreditCard(String cardNumber, String cvv) {
        // Validate the credit card details
        if (!isValidCreditCard(cardNumber, cvv)) {
            return;
        }

        // Check if the card already exists
        if (creditCards.containsKey(cardNumber)) {
        	JOptionPane.showMessageDialog(null,"This card already exists.");
            return;
        }

        // Add the card to the credit cards map
        creditCards.put(cardNumber, cvv);
        JOptionPane.showMessageDialog(null,"Credit card added successfully.");
        try {
            saveToFile("credit_cards.txt");
        } catch (IOException e) {
        	JOptionPane.showMessageDialog(null,"Error saving credit cards to file: " + e.getMessage());
        }
    }
    
    public void deleteCreditCard(String cardNumber) {
        // Check if the card exists
        if (creditCards.containsKey(cardNumber)) {
            creditCards.remove(cardNumber);  // Remove the card from the map
            JOptionPane.showMessageDialog(null,"Credit card deleted successfully.");
            try {
                saveToFile("credit_cards.txt");
            } catch (IOException e) {
            	JOptionPane.showMessageDialog(null,"Error saving credit cards to file: " + e.getMessage());
            }
        } else {
        	JOptionPane.showMessageDialog(null,"Card not found. Deletion failed.");
        }
    }

    public void displayCreditCards() {
        // Check if there are any credit cards stored
        if (creditCards.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No credit cards stored.");
        } else {
            // Display the stored credit cards
            StringBuilder cardList = new StringBuilder("Stored Credit Cards:\n");
            for (String cardNumber : creditCards.keySet()) {
                cardList.append(cardNumber).append("\n");
            }
            JOptionPane.showMessageDialog(null, cardList.toString());
        }
    }
    
    public static float convertStringToFloat(String input) {
        try {
        	// Attempt to convert the input string to a float
        	float floatValue = Float.parseFloat(input);
            return floatValue;
        } catch (NumberFormatException e) {
        	JOptionPane.showMessageDialog(null,"Error! Invalid input. cannot be converted to a float.");
            return 0;  // Return 0 if conversion fails
        }
    }
    
    @Override
    public void start(Window window) {
        boolean running = true;
        while (running)  {
            // Define the menu options
            String[] options = {
                    "View Current Balance", "Deposit Money", "Transfer Money",
                    "View Transfers History", "Add New Credit Card", "Delete Credit Card",
                    "View Credit Cards", "Exit"
            };

            // Display the menu and get the user's choice
            int choice = JOptionPane.showOptionDialog(null, "Wallet Menu", "Select an option",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (choice == -1) { // User closed the dialog
                JOptionPane.showMessageDialog(null, "Exiting Wallet app...");

                running = false;  // Stop the loop
                window.dispose();  // Close the window
                break;
            }

            // Handle the user's choice
            switch (choice) {
            case 0:
            	JOptionPane.showMessageDialog(null,"Current Balance: $" + String.format("%.2f", getBalance()));
                break;
            case 1:
                // Handle deposit
                String da = JOptionPane.showInputDialog(null, "Enter amount to deposit:");
                if(da==null)
                	break;
                float depositAmount = convertStringToFloat(da);
                if(depositAmount == 0) {
                	JOptionPane.showMessageDialog(null,"Current Balance: $" + String.format("%.2f", getBalance()));
                	break;
                }
                String cardNumber = JOptionPane.showInputDialog(null,"Enter credit card number:[aaaa-bbbb-cccc-dddd] ");
                String cvv = JOptionPane.showInputDialog(null,"Enter CVV: ");
                deposit(depositAmount, cardNumber, cvv);
                break;
            case 2:
                // Handle money transfer
                String recipient = JOptionPane.showInputDialog(null,"Enter recipient's name or 'casino' to transfer: ");
                if(recipient==null)
                	break;
                String input = JOptionPane.showInputDialog(null, "Enter amount to transfer:");
                if(input==null)
                	break;
                float transferAmount = convertStringToFloat(input);
                transferMoney(recipient, transferAmount);
                JOptionPane.showMessageDialog(null,"Current Balance: $" + String.format("%.2f", getBalance()));
                break;
            case 3:
                displayAllContents();  // Display the transfer history
                break;
            case 4:
                // Handle adding a new credit card
                String newCardNumber = JOptionPane.showInputDialog(null,"Enter new credit card number:[aaaa-bbbb-cccc-dddd] ");
                if(newCardNumber==null)
                	break;
                String newCvv = JOptionPane.showInputDialog(null,"Enter CVV: ");
                if(newCvv==null)
                	break;
                
                addCreditCard(newCardNumber, newCvv);
                break;
            case 5:
                // Handle deleting a credit card
                String cardToDelete = JOptionPane.showInputDialog(null,"Enter credit card number to delete:[aaaa-bbbb-cccc-dddd] ");
                if(cardToDelete==null)
                	break;
                deleteCreditCard(cardToDelete);
                break;
            case 6:
                displayCreditCards();  // Display the stored credit cards
                break;

            case 7: // Exit
                running = false;  // Stop the loop
                window.dispose();  // Close the window
                break;

            default:
                JOptionPane.showMessageDialog(null, "Invalid option. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
