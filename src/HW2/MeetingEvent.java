package HW2;

import javax.swing.*;
import java.util.Date;

public class MeetingEvent extends Event { // Event when user meets with someone
    private String contactName;

    public MeetingEvent(Date date, int duration, String contactName) {
        super(date, duration);
        this.contactName = contactName;
    }

    public String getContactName() {
        return contactName;
    }
    
    @Override
    public void printEventDetails() {
        JOptionPane.showMessageDialog(null, "Meeting with " + contactName + " on " + date + " for " + duration + " minutes.");
    }

    @Override
    public String toString() {
        return "MeetingEvent{" +
               "date=" + date +
               ", duration=" + duration +
               ", contactName='" + contactName + '\'' +
               '}';
    }
}
