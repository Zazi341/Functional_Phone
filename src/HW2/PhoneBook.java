package HW2;

import java.util.*;
import java.io.*;
import javax.swing.*;

//Define the Contact class
class Contact {
 String name;
 int phoneNumber;

 Contact(String name, int phoneNumber) {
     this.name = name;
     this.phoneNumber = phoneNumber;
 }

 // Override toString for easy printing
 @Override
 public String toString() {
     return name + ": 0" + phoneNumber;
 }
}

public class PhoneBook extends App {
    private List<Contact> contacts;
    private SMS smsApp; // Reference to SMS app
    private Calendar calendarApp; // Reference to SMS app

    public PhoneBook() {
        super("PhoneBook");
        this.smsApp = null;
        this.calendarApp = null;
        contacts = new ArrayList<>();
    }

    public void setSMSApp(SMS smsApp) {
        this.smsApp = smsApp;
    }

    public void setCalendarApp(Calendar calendarApp) {
        this.calendarApp = calendarApp;
    }

    // 1. Method to add a contact
    public void addContact(String name, int phoneNumber) {
        for (Contact contact : contacts) {
            if (contact.name.equals(name)) {
                JOptionPane.showMessageDialog(null, "\nError! Already have a contact with this name.");
                return;
            }
        }
        String phoneStr = String.valueOf(phoneNumber);
        
        // Check if the phone number has 10 digits and starts with "05"
        if (phoneStr.length() != 9 || !phoneStr.startsWith("5")) {
        	JOptionPane.showMessageDialog(null,"\nError! Invalid phone number. It must be 10 digits long and start with '05'.");
            return;
        }
        
        contacts.add(new Contact(name, phoneNumber));
        JOptionPane.showMessageDialog(null,"\nContact added successfully.");
    }

    // 2. Method to delete a contact by name
    public void deleteContact(String name) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).name.equals(name)) {
                contacts.remove(i);
                calendarApp.deleteMeetingsByName(name);
                smsApp.deleteConversation(name); // Delete conversation when contact is deleted
                JOptionPane.showMessageDialog(null, "\nContact, conversation and meetings with " + name + " deleted.");
                return; // Exit the loop after removing the first match
            }
        }
        JOptionPane.showMessageDialog(null, "\nContact not found.");
    }

    // 3. Method to print all contacts
    public void printContacts() {
        if (contacts.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No contacts to display.", "Contacts", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder contactsList = new StringBuilder("Contacts List:\n");

        for (Contact contact : contacts) {
            contactsList.append(contact).append("\n");
        }

        JOptionPane.showMessageDialog(null, contactsList.toString(), "Contacts", JOptionPane.INFORMATION_MESSAGE);
    }


    @Override
    public void displayAllContents() {
        printContacts();
    }

    // 4. Method to search for a contact by name
    public void searchContact(String name) {
        for (Contact contact : contacts) {
            if (contact.name.equals(name)) {
            	JOptionPane.showMessageDialog(null,contact);
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "\nContact not found!");
    }

    public boolean contactExists(String name) {
        for (Contact contact : contacts) {
            if (contact.name.equals(name)) {
                return true; // Return true if the contact is found
            }
        }
        return false; // Return false if no contact with the given name exists
    }
    
    //5.  Method to sort contacts by name
    public void sortByName() {
        contacts.sort(Comparator.comparing(contact -> contact.name));
    }


    //6. Method to sort contacts by phone number
    public void sortByPhoneNumber() {
        contacts.sort(Comparator.comparing(contact -> contact.phoneNumber));
        reverseOrder();
    }

    //7. Method to remove duplicates
    public void removeDuplicates() {
        for (int i = 0; i < contacts.size(); i++) {
        	for (int j = i + 1; j < contacts.size(); j++) {
        		if (contacts.get(i).name.equals(contacts.get(j).name)) {
        			if(contacts.get(i).phoneNumber == (contacts.get(j).phoneNumber)) {
        				contacts.remove(j);
        			}
        		}
        	}
        }
    }

    //8. Method to reverse the order
    public void reverseOrder() {
        Collections.reverse(contacts);
    }

    //9. Method to save contacts to a file
    public void saveToFile(String filename) throws IOException {
    	if (!filename.endsWith(".txt")) { // make sure file will be saved as .txt
            filename += ".txt";
        }
    	
    	String relativePath = "src/HW2/" + filename; // so file will be saved inside of 'hw1' package
        try (PrintWriter writer = new PrintWriter(new FileWriter(relativePath))) {
            for (Contact contact : contacts) {
                writer.println(contact.name + ",0" + contact.phoneNumber);
            }
        }
        JOptionPane.showMessageDialog(null, "Contacts saved successfully to " + filename);
    }
    

    //10. Method to load contacts from a .txt file
    public void loadFromFile(String filename) throws IOException {
        if (!filename.endsWith(".txt")) {  // Ensure the file has the correct extension and path
            filename += ".txt";
        }
        String relativePath = "src/HW2/" + filename;

        try (BufferedReader reader = new BufferedReader(new FileReader(relativePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",0");
                if (parts.length == 2) {
                    String name = parts[0];
                    int phoneNumber = Integer.parseInt(parts[1].trim());  // Convert String to int and handle potential whitespace
                    addContact(name, phoneNumber);
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Contacts loaded successfully from " + filename);
    }
    
    public static int convertStringToNumber(String input) {
        try {
            // Try to parse as an integer first
            int intValue = Integer.parseInt(input);
            return intValue;
        } catch (NumberFormatException e) {
        	//System.out.println("Error: The number is too large to be stored as an integer.");
        	return 0;
        }
    }

    @Override
    public void start(Window window) {
        boolean running = true;

        while (running) {
            String[] options = {
                    "Add Contact", "Delete Contact", "Print All Contacts", "Search Contact",
                    "Sort by Name", "Sort by Phone Number", "Remove Duplicates",
                    "Reverse Order", "Save to File", "Load from File", "Exit"
            };

            int choice = JOptionPane.showOptionDialog(null, "Contact Manager Menu", "Select an option",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (choice == -1) { // User closed the dialog
                JOptionPane.showMessageDialog(null, "Goodbye!", "Exit", JOptionPane.INFORMATION_MESSAGE);
                running = false;
                window.dispose();
                break;
            }
            int phoneNumber;
            String name, phoneNumberStr;
            switch (choice) {
                case 0: // Add Contact
                    name = JOptionPane.showInputDialog(null, "Enter name:", "Add Contact", JOptionPane.QUESTION_MESSAGE);
                    if (name == null) break; // Handle cancel

                    phoneNumberStr = JOptionPane.showInputDialog(null, "Enter phone number:", "Add Contact", JOptionPane.QUESTION_MESSAGE);
                    if (phoneNumberStr == null) break; // Handle cancel
                    phoneNumber = convertStringToNumber(phoneNumberStr);
                    try {
                        addContact(name, phoneNumber);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid phone number. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 1: // Delete Contact
                    name = JOptionPane.showInputDialog(null, "Enter name to delete:", "Delete Contact", JOptionPane.QUESTION_MESSAGE);
                    if (name != null) {
                        deleteContact(name);
                    }
                    break;

                case 2: // Print All Contacts
                    printContacts();
                    break;

                case 3: // Search Contact
                    name = JOptionPane.showInputDialog(null, "Enter name to search:", "Search Contact", JOptionPane.QUESTION_MESSAGE);
                    if (name != null) {
                        searchContact(name);
                    }
                    break;

                case 4: // Sort by Name
                    sortByName();
                    break;

                case 5: // Sort by Phone Number
                    sortByPhoneNumber();
                    break;

                case 6: // Remove Duplicates
                    removeDuplicates();
                    break;

                case 7: // Reverse Order
                    reverseOrder();
                    break;

                case 8: // Save to File
                    String saveFilename = JOptionPane.showInputDialog(null, "Enter filename to save:", "Save to File", JOptionPane.QUESTION_MESSAGE);
                    if (saveFilename != null) {
                        try {
                            saveToFile(saveFilename);
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(null, "Error saving to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    break;

                case 9: // Load from File
                    String loadFilename = JOptionPane.showInputDialog(null, "Enter filename to load:", "Load from File", JOptionPane.QUESTION_MESSAGE);
                    if (loadFilename != null) {
                        try {
                            loadFromFile(loadFilename);
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(null, "Error loading from file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    break;

                case 10: // Exit
                    running = false;
                    window.dispose();
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Invalid option. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}