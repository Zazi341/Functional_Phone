package HW2;
public abstract class MediaTypes {
    protected String name;
    protected int length; // assuming length is in seconds

    public MediaTypes(String name, int length) {
        this.name = name;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public abstract void play();
}
