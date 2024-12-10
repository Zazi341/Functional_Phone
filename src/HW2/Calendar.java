package HW2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

public class Calendar extends App {
    private List<Event> events; // List to store calendar events
    private PhoneBook phoneBook; // Reference to the PhoneBook for contact-related events

    public Calendar() {
        super("Calendar");
        this.phoneBook = null;
        events = new ArrayList<>(); // Initialize the list to store events
    }
    
    // Sets the PhoneBook reference
    public void setPhoneBook(PhoneBook phoneBook) {
        this.phoneBook = phoneBook;
    }

    // Adds an event after checking duration and contact existence
    public void addEvent(Date date, int duration, String description, String contactName) {
        if (duration < 1 || duration > 60) {
            JOptionPane.showMessageDialog(null, "Duration must be between 1 and 60 minutes.");
            return;
        }
        if (contactName != null && !phoneBook.contactExists(contactName)) {
            JOptionPane.showMessageDialog(null, "Error: Contact " + contactName + " does not exist in the phone book.");
            return;
        }
        Event event;
        if (contactName != null) {
            event = new MeetingEvent(date, duration, contactName);
        } else {
            event = new GeneralEvent(date, duration, description);
        }

        events.add(event);
        checkForOverlaps(); // Check for overlapping events
        Collections.sort(events); // Sorts events by date
    }

    // Checks for overlapping events and removes them
    private void checkForOverlaps() {
        Collections.sort(events);
        List<Event> toRemove = new ArrayList<>();
        Event previous = null;

        for (Event current : events) {
            if (previous != null) {
                Date previousEnd = new Date(previous.date.getTime() + previous.duration * 60000);
                if (!current.date.after(previousEnd)) { // Check if current starts before previous ends
                    toRemove.add(current); // Always remove the current event
                } else {
                    previous = current; // Update previous if no overlap
                }
            } else {
                previous = current; // Set the first event as previous
            }
        }

        events.removeAll(toRemove);
        if (!toRemove.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Overlapping events have been removed.");
        }
    }
    
    // Deletes an event by date
    public void deleteEvent(Date date) {
        boolean removed = events.removeIf(e -> e.date.equals(date));
        if (!removed) {
            JOptionPane.showMessageDialog(null, "No matching event found to delete.");
        }
    }

    // Prints events on a specific date
    public void printEventsOnDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        StringBuilder message = new StringBuilder("Events on " + dateFormat.format(date) + ":\n");

        boolean eventsFound = false;
        for (Event event : events) {
            // Check if the date matches the event's date (without time consideration)
            SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (eventDateFormat.format(event.date).equals(eventDateFormat.format(date))) {
                message.append(event.toString()).append("\n");
                eventsFound = true;
            }
        }

        if (!eventsFound) {
            message.append("No events found on this date.");
        }

        JOptionPane.showMessageDialog(null, message.toString(), "Events on Date", JOptionPane.INFORMATION_MESSAGE);
    }

    
    // Prints events associated with a specific contact
    public void printEventsForContact(String contactName) {
        if (!phoneBook.contactExists(contactName)) {
            JOptionPane.showMessageDialog(null, "Contact " + contactName + " does not exist.");
            return;
        }

        StringBuilder message = new StringBuilder("Events for " + contactName + ":\n");

        boolean eventsFound = false;
        for (Event event : events) {
            if (event instanceof MeetingEvent && contactName.equals(((MeetingEvent) event).getContactName())) {
                message.append(event.toString()).append("\n");
                eventsFound = true;
            }
        }

        if (!eventsFound) {
            message.append("No events found for this contact.");
        }

        JOptionPane.showMessageDialog(null, message.toString(), "Events for Contact", JOptionPane.INFORMATION_MESSAGE);
    }

    // Removes meetings associated with a specific contact
    public void deleteMeetingsByName(String contactName) {
        List<Event> meetingsToDelete = new ArrayList<>();
        
        for (Event event : events) {
            if (event instanceof MeetingEvent && contactName.equals(((MeetingEvent) event).getContactName())) {
                meetingsToDelete.add(event);
            }
        }

        if (meetingsToDelete.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No meetings found with " + contactName + ".");
        } 
        else {
            // Remove the meetings
            events.removeAll(meetingsToDelete);
            JOptionPane.showMessageDialog(null, "All meetings with " + contactName + " have been deleted.");
        }
    }

    // Main method handling Calendar app operations
    @Override
    public void start(Window window) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        boolean running = true;

        while (running) {
            String[] options = {
                "Add Event",
                "Delete Event",
                "View Events on Date",
                "View Events for Contact",
                "List All Events",
                "Exit"
            };

            int choice = JOptionPane.showOptionDialog(null,
                "Calendar Menu",
                "Choose an option",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

            if (choice == -1) { // Handle cancellation (input is -1 when user cancels or closes the dialog)
                JOptionPane.showMessageDialog(null, "Exiting Calendar App...");
                running = false;
                window.dispose();
                break;

            }

            try {
                switch (choice) {
                    case 0: // Add Event
                        String dateStr = JOptionPane.showInputDialog(null, "Enter event date and time (yyyy-MM-dd HH:mm):", "Add Event", JOptionPane.QUESTION_MESSAGE);
                        if (dateStr == null) {
                            JOptionPane.showMessageDialog(null, "Event addition cancelled.");
                            break;
                        }
                        Date date = dateFormat.parse(dateStr);

                        String durationStr = JOptionPane.showInputDialog(null, "Enter event duration (minutes):", "Add Event", JOptionPane.QUESTION_MESSAGE);
                        if (durationStr == null) {
                            JOptionPane.showMessageDialog(null, "Event addition cancelled.");
                            break;
                        }
                        int duration = Integer.parseInt(durationStr);

                        String detail = JOptionPane.showInputDialog(null, "Enter description (for general events) or contact name (for meetings):", "Add Event", JOptionPane.QUESTION_MESSAGE);
                        if (detail == null) {
                            JOptionPane.showMessageDialog(null, "Event addition cancelled.");
                            break;
                        }

                        String contactName = null;
                        String description = null;
                        if (phoneBook.contactExists(detail)) {
                            contactName = detail; // Treat as contact name if exists
                        } else {
                            description = detail; // Otherwise, treat as description
                        }

                        addEvent(date, duration, description, contactName);
                        break;

                    case 1: // Delete Event
                        dateStr = JOptionPane.showInputDialog(null, "Enter the date and time of the event to delete (yyyy-MM-dd HH:mm):", "Delete Event", JOptionPane.QUESTION_MESSAGE);
                        if (dateStr == null) {
                            JOptionPane.showMessageDialog(null, "Event deletion cancelled.");
                            break;
                        }
                        date = dateFormat.parse(dateStr);
                        deleteEvent(date);
                        break;

                    case 2: // View Events on Date
                        dateStr = JOptionPane.showInputDialog(null, "Enter the date to view events (yyyy-MM-dd):", "View Events on Date", JOptionPane.QUESTION_MESSAGE);
                        if (dateStr == null) {
                            JOptionPane.showMessageDialog(null, "View events cancelled.");
                            break;
                        }
                        date = dateFormat.parse(dateStr + " 00:00");
                        printEventsOnDate(date);
                        break;

                    case 3: // View Events for Contact
                        String contactNameToView = JOptionPane.showInputDialog(null, "Enter contact name to view their events:", "View Events for Contact", JOptionPane.QUESTION_MESSAGE);
                        if (contactNameToView == null) {
                            JOptionPane.showMessageDialog(null, "View events for contact cancelled.");
                            break;
                        }
                        printEventsForContact(contactNameToView);
                        break;

                    case 4: // List All Events
                        displayAllContents();
                        break;

                    case 5: // Exit
                        running = false;
                        window.dispose();
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Invalid date format. Please use yyyy-MM-dd HH:mm.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid number format. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Displays all events in the calendar
    @Override
    public void displayAllContents() {
        if (events.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No events to display.", "No Events", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create a StringBuilder to construct the message
        StringBuilder message = new StringBuilder("All Events:\n");
        for (Event event : events) {
            message.append(event.toString()).append("\n");
        }

        // Show the message in a JOptionPane
        JOptionPane.showMessageDialog(null, message.toString(), "Event List", JOptionPane.INFORMATION_MESSAGE);
    }
}
