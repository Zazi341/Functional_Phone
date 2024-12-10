package HW2;

// class to start the phone menu
public class TestMobilePhone {
    public static void main(String[] args) {
        Phone mobilePhone = new Phone();
        Window window = new Window("phone.png","Phone Main Menu");
        mobilePhone.start(window); // Start the entire phone system
    }
}
