package HW2;

import java.util.*;
import javax.swing.*;

public class SMS extends App {
    private Map<String, List<String>> conversations; // Stores SMS conversations per contact
    private PhoneBook phoneBook; // Reference to the PhoneBook app for contact verification
    
    public SMS() {
        super("SMS");
        this.phoneBook = null; 
        conversations = new HashMap<>(); // Initialize the map to store conversations
    }
    
    // Sets the PhoneBook reference
    public void setPhoneBook(PhoneBook phoneBook) {
        this.phoneBook = phoneBook;
    }

    // Sends an SMS to a contact if they exist in the phone book
    public void sendSMS(String name, String message) {
        if (!phoneBook.contactExists(name)) {
            JOptionPane.showMessageDialog(null, "Error: Contact " + name + " does not exist in the phone book.");
            return;
        }
        conversations.putIfAbsent(name, new ArrayList<>());
        conversations.get(name).add(message);
        JOptionPane.showMessageDialog(null, "Message sent to " + name);
    }

    // Deletes all conversations with a specific contact
    public void deleteConversation(String name) {
        if (conversations.remove(name) == null) {
            JOptionPane.showMessageDialog(null, "No conversation found with " + name);
        } else {
            JOptionPane.showMessageDialog(null, "Conversation with " + name + " deleted.");
        }
    }

    // Prints the conversation history with a given contact
    public void printConversation(String name) {
        if (conversations.containsKey(name)) {
            StringBuilder conversationLog = new StringBuilder("Conversation with " + name + ":\n");
            for (String msg : conversations.get(name)) {
                conversationLog.append(msg).append("\n");
            }
            JOptionPane.showMessageDialog(null, conversationLog.toString());
        } else {
            JOptionPane.showMessageDialog(null, "No conversation found with " + name);
        }
    }

    // Searches all messages for a specified keyword
    public void searchMessages(String keyword) {
        boolean found = false;
        JOptionPane.showMessageDialog(null, "Searching for keyword '" + keyword + "' in all conversations:");
        for (Map.Entry<String, List<String>> entry : conversations.entrySet()) {
            for (String msg : entry.getValue()) {
                if (msg.contains(keyword)) {
                    JOptionPane.showMessageDialog(null, "Found in conversation with " + entry.getKey() + ": " + msg);
                    found = true;
                }
            }
        }
        if (!found) {
            JOptionPane.showMessageDialog(null, "No messages found containing '" + keyword + "'.");
        }
    }

    // Displays all SMS conversations
    @Override
    public void displayAllContents() {
        for (String name : conversations.keySet()) {
            printConversation(name);
        }
    }
    
    // Main method handling SMS app operations
    @Override
    public void start(Window window) {
    	boolean running = true;

        while (running) {
            String[] options = {"Send SMS", "Delete Conversation", "Print Conversation", "Search Messages", "Print All Conversations", "Exit"};
            int choice = JOptionPane.showOptionDialog(null, "SMS Menu:", "Select an option",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (choice == -1) { // User closed the dialog
                JOptionPane.showMessageDialog(null, "Exiting SMS App.");
                running = false;
                window.dispose();
                break;
            }

            switch (choice) {
                case 0: // Send SMS
                    String name = JOptionPane.showInputDialog("Enter name:");
                    String message = JOptionPane.showInputDialog("Enter message:");
                    if (name != null && message != null) {
                        sendSMS(name, message);
                    }
                    break;
                case 1: // Delete Conversation
                    name = JOptionPane.showInputDialog("Enter name to delete conversation:");
                    if (name != null) {
                        deleteConversation(name);
                    }
                    break;
                case 2: // Print Conversation
                    name = JOptionPane.showInputDialog("Enter name to print conversation:");
                    if (name != null) {
                        printConversation(name);
                    }
                    break;
                case 3: // Search Messages
                    String keyword = JOptionPane.showInputDialog("Enter keyword to search:");
                    if (keyword != null) {
                        searchMessages(keyword);
                    }
                    break;
                case 4: // Print All Conversations
                    JOptionPane.showMessageDialog(null, "Printing all conversations:");
                    displayAllContents();
                    break;
                case 5: // Exit
                    window.dispose();
                    return; // Exits the SMS app
                default:
                    JOptionPane.showMessageDialog(null, "Invalid option. Please try again.");
            }
        }
    }
}
