package HW2;

public abstract class App {
    protected String appName;

    public App(String name) {
        this.appName = name;
    }

    public abstract void start(Window window);
    public abstract void displayAllContents();
}
