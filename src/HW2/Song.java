package HW2;
import javax.swing.*;
public class Song extends MediaTypes {
    public Song(String name, int length) {
        super(name, length);
    }

    @Override
    public void play() {
        JOptionPane.showMessageDialog(null,"\"" + name + "\" song is now playing for " + length + " seconds.");
    }
}