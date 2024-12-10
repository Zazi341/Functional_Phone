package HW2;

import java.util.Date;

public abstract class Event implements Comparable<Event> {
    protected Date date;
    protected int duration;

    public Event(Date date, int duration) {
        this.date = date;
        this.duration = duration;
    }

    @Override
    public int compareTo(Event other) { // Overriding `compareTo` method to compare between events
        return this.date.compareTo(other.date);
    }

    public abstract void printEventDetails();
}
