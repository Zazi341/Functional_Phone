package HW2;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Media extends App {
    private List<MediaTypes> mediaList; // List to store different media types

    public Media() {
        super("Media");
        mediaList = new ArrayList<>(); // Initializes the media list
    }

    // Adds a media object to the list
    public void addMedia(MediaTypes media) {
        mediaList.add(media);
    }

    // Plays a media by its name, prints error if not found
    public void playMediaByName(String name) {
        for (MediaTypes media : mediaList) {
            if (media.getName().equalsIgnoreCase(name)) {
                media.play();
                return;
            }
        }
        JOptionPane.showMessageDialog(null,"Media not found: " + name);
    }

    // Helper to parse media length from string to seconds
    private int parseLength(String lengthStr) {
        try {
            String[] parts = lengthStr.split(":");
            if (parts.length == 2) {
                int minutes = Integer.parseInt(parts[0]);
                int seconds = Integer.parseInt(parts[1]);
                return minutes * 60 + seconds;
            } else if (parts.length == 1) {
                return Integer.parseInt(parts[0]);
            } else {
                return -1;
            }
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Main method handling the Media app operations
    @SuppressWarnings("finally")
	@Override
    public void start(Window window) {
        try {
            boolean running = true;
            while (running) {
                String[] options = {"Add Media", "Play Media by Name", "Play All Media", "Exit"};
                int choice = JOptionPane.showOptionDialog(null, "Media App Menu", "Select an option",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                if (choice == -1) { // User closed the dialog
                    JOptionPane.showMessageDialog(null, "Exiting Media App...", "Exit", JOptionPane.INFORMATION_MESSAGE);
                    running = false;
                    window.dispose();
                    break;
                }

                switch (choice) {
                    case 0: // Add Media
                        String type = JOptionPane.showInputDialog(null, "Enter media type (song/video):", "Add Media", JOptionPane.QUESTION_MESSAGE);
                        if (type == null) break; // Handle cancel

                        String name = JOptionPane.showInputDialog(null, "Enter media name:", "Add Media", JOptionPane.QUESTION_MESSAGE);
                        if (name == null) break; // Handle cancel

                        String lengthStr = JOptionPane.showInputDialog(null, "Enter media length (as 1:47 or 107):", "Add Media", JOptionPane.QUESTION_MESSAGE);
                        if (lengthStr == null) break; // Handle cancel

                        int length = parseLength(lengthStr);
                        if (length != -1) {
                            if (type.equalsIgnoreCase("song")) {
                                addMedia(new Song(name, length));
                            } else if (type.equalsIgnoreCase("video")) {
                                addMedia(new Video(name, length));
                            } else {
                                JOptionPane.showMessageDialog(null, "Unknown media type: " + type, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid length format. Please enter a valid format (as 1:47 or 107).", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        break;

                    case 1: // Play Media by Name
                        String nameToPlay = JOptionPane.showInputDialog(null, "Enter media name to play:", "Play Media", JOptionPane.QUESTION_MESSAGE);
                        if (nameToPlay != null) {
                            playMediaByName(nameToPlay);
                        }
                        break;

                    case 2: // Play All Media
                        JOptionPane.showMessageDialog(null, "Displaying all media contents:", "Play All Media", JOptionPane.INFORMATION_MESSAGE);
                        displayAllContents();
                        break;

                    case 3: // Exit
                        running = false;
                        window.dispose();
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        finally {
            return;
        }
    }

    // Displays all media in the list
    @Override
    public void displayAllContents() {
        if (mediaList.isEmpty()) {
            JOptionPane.showMessageDialog(null,"No media to display.");
        } else {
            for (MediaTypes media : mediaList) {
                media.play();
            }
        }
    }
}
