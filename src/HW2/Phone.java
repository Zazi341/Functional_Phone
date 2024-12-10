package HW2;

import javax.swing.*;


public class Phone {

	private PhoneBook phoneBookApp;
    private SMS smsApp;
    private Calendar calendarApp;
    private Media mediaApp;
    private PhoneCall phoneCallApp;
    private Wallet walletApp;
    private Casino casinoApp;
    
    public Phone() {
    	phoneBookApp = new PhoneBook(); 
        smsApp = new SMS(); 
        calendarApp = new Calendar();
        mediaApp = new Media();
        phoneCallApp = new PhoneCall();
        walletApp = new Wallet();
        casinoApp = new Casino();
        phoneBookApp.setSMSApp(smsApp);
        phoneBookApp.setCalendarApp(calendarApp);
        smsApp.setPhoneBook(phoneBookApp);
        calendarApp.setPhoneBook(phoneBookApp);
        phoneCallApp.setPhoneBook(phoneBookApp);
        walletApp.setPhoneBook(phoneBookApp);
        walletApp.setCasino(casinoApp);
        casinoApp.setWallet(walletApp);
    }
    
    public void start(Window window) {
        SwingUtilities.invokeLater(() -> {
            window.setVisible(true);
            JPanel panel = window.getPanel();
            // Create an invisible button at position
            InvisibleButton wallet = new InvisibleButton(0.3110, 0.1295, 110);
            // Add the button to the panel
            panel.add(wallet);

            // Add an action listener to the button
            wallet.addActionListener(e -> {
                Window walletWin = new Window("wallet.png","Wallet App");
                walletWin.setVisible(true);
                walletApp.start(walletWin);
            });

            // Create an invisible button at position 
            InvisibleButton video = new InvisibleButton(0.4499, 0.1325, 110);
            // Add the button to the panel
            panel.add(video);

            // Add an action listener to the button
            video.addActionListener(e -> {
                Window videoWin = new Window("video.png","Media App");
                videoWin.setVisible(true);
                mediaApp.start(videoWin);
            });

            // Create an invisible button at position (100, 100)
            InvisibleButton contacts = new InvisibleButton(0.5980, 0.1295, 100);
            // Add the button to the panel
            panel.add(contacts);

            // Add an action listener to the button
            contacts.addActionListener(e -> {
                Window contactWin = new Window("contacts.png","Phone Book App");
                contactWin.setVisible(true);
                phoneBookApp.start(contactWin);
            });

            // Create an invisible button at position 
            InvisibleButton casino = new InvisibleButton(0.5980, 0.2604, 105);
            // Add the button to the panel
            panel.add(casino);

            // Add an action listener to the button
            casino.addActionListener(e -> {
                Window casinoWin = new Window("casino.png","CASINO App");
                casinoWin.setVisible(true);
                casinoApp.start(casinoWin);
            });

            // Create an invisible button at position 
            InvisibleButton sms = new InvisibleButton(0.3180, 0.777, 100);
            // Add the button to the panel
            panel.add(sms);

            // Add an action listener to the button
            sms.addActionListener(e -> {
                Window smsWin = new Window("sms.png","Messages App");
                smsWin.setVisible(true);
                smsApp.start(smsWin);
            });

            // Create an invisible button at position 
            InvisibleButton phone = new InvisibleButton(0.4540, 0.777, 100);
            // Add the button to the panel
            panel.add(phone);

            // Add an action listener to the button
            phone.addActionListener(e -> {
                Window phoneWin = new Window("phoneapp.png","Phone Calls App");
                phoneWin.setVisible(true);
                phoneCallApp.start(phoneWin);
            });

            // Create an invisible button at position 
            InvisibleButton calendar = new InvisibleButton(0.5930, 0.777, 100);
            // Add the button to the panel
            panel.add(calendar);

            // Add an action listener to the button
            calendar.addActionListener(e -> {
                Window calWin = new Window("cal.png","Calendar App");
                calWin.setVisible(true);
                calendarApp.start(calWin);
            });
        });
    }
}