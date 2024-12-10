package HW2;

import javax.swing.*;

public class Video extends MediaTypes {
    public Video(String name, int length) {
        super(name, length);
    }

    @Override
    public void play() {
        JOptionPane.showMessageDialog(null,"\"" + name + "\" video is now playing for " + length + " seconds.");
    }
}