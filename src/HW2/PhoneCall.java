package HW2;

import java.util.*;
import javax.swing.*;

public class PhoneCall extends App {
    private PhoneBook phoneBook;
    private Map<String, List<String>> calls;

    public PhoneCall() {
        super("PhoneCall");
        this.phoneBook = null;
        this.calls = new HashMap<>();
    }

    public void setPhoneBook(PhoneBook phoneBook) {
        this.phoneBook = phoneBook;
    }

    public void callContact(String contactName) {
        if (!phoneBook.contactExists(contactName)) {
            JOptionPane.showMessageDialog(null, "\nContact does not exist!");
            return;
        }

        Random random = new Random();
        boolean answered = random.nextBoolean(); // 50% chance of answering
        int minutes = answered ? random.nextInt(59) + 1 : 0; // Minutes between 1 and 59 if answered
        int seconds = answered ? random.nextInt(59) + 1 : 0; // Seconds between 1 and 59 if answered
        String duration = Integer.toString(minutes) + ":" + Integer.toString(seconds);
        
        String callDetail = "Answered: " + answered + ", Duration: " + duration + " min";
        calls.computeIfAbsent(contactName, k -> new ArrayList<>()).add(callDetail);

        JOptionPane.showMessageDialog(null, "\nCalling " + contactName + "...");
        JOptionPane.showMessageDialog(null, callDetail);
    }

    public void printCallsWithContact(String contactName) {
    	if (!phoneBook.contactExists(contactName)) {
            JOptionPane.showMessageDialog(null, "\nContact does not exist!");
            return;
    	}
    	
        List<String> details = calls.getOrDefault(contactName, Collections.emptyList());
        if (details.isEmpty()) {
            JOptionPane.showMessageDialog(null, "\nNo calls with " + contactName);
            return;
        }

        StringBuilder callHistory = new StringBuilder("\nCall history with " + contactName + ":\n");
        for (String detail : details) {
            callHistory.append(detail).append("\n");
        }
        JOptionPane.showMessageDialog(null, callHistory.toString());
    }

    @Override
    public void displayAllContents() {
        for (Map.Entry<String, List<String>> entry : calls.entrySet()) {
            printCallsWithContact(entry.getKey());
            }
        }
    

    @Override
    public void start(Window window) {
        boolean running = true;
        while (running) {
            String[] options = {
                    "Make a Call",
                    "View Call History with a Contact",
                    "View All Call Histories",
                    "Exit"
            };

            int choice = JOptionPane.showOptionDialog(null,
                    "PhoneCall Menu",
                    "Choose an option",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            // Handle cancellation (choice is -1 when user cancels or closes the dialog)
            if (choice == -1) {
                JOptionPane.showMessageDialog(null, "Exiting PhoneCall app...");
                running = false;
                window.dispose();
                break;
            }

            switch (choice) {
                case 0:
                    String contactName = JOptionPane.showInputDialog(null, "Enter contact name to call:", "Make a Call", JOptionPane.QUESTION_MESSAGE);
                    if (contactName != null) {
                        callContact(contactName);
                    }
                    break;

                case 1:
                    contactName = JOptionPane.showInputDialog(null, "Enter contact name to view history:", "View Call History", JOptionPane.QUESTION_MESSAGE);
                    if (contactName != null) {
                        printCallsWithContact(contactName);
                    }
                    break;

                case 2:
                    displayAllContents();
                    break;

                case 3:
                    running = false;
                    window.dispose();
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Invalid option. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
