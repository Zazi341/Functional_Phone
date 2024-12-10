package HW2;

import javax.swing.*;
import java.util.Date;

public class GeneralEvent extends Event { // Event without meeting with someone
    private String description;

    public GeneralEvent(Date date, int duration, String description) {
        super(date, duration);
        this.description = description;
    }

    @Override
    public void printEventDetails() {
        JOptionPane.showMessageDialog(null, "Event on " + date + ": " + description + " lasting for " + duration + " minutes.");
    }

    @Override
    public String toString() {
        return "GeneralEvent{" +
               "date=" + date +
               ", duration=" + duration +
               ", description='" + description + '\'' +
               '}';
    }
}

