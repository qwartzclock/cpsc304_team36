package NHLAnalyzer;

import java.awt.*;
import java.awt.event.*;


public class Main {

    private static LoginUI loginFrame;
    private static MainUI mainFrame;
    private static UIOptions optionsFrame;
    private static UIData dataFrame;

    // 0 = player
    // 1 = team
    // 2 = game
    private static int waitingFor = -1;

    public static void loginWith(Account account){
        loginFrame.dispose();
        if(dataFrame!=null)
            dataFrame.dispose();
        if(optionsFrame!=null)
            optionsFrame.dispose();
        System.out.println("Logged in succesfully.");
        mainFrame = new MainUI(account);
    }

    public static void openOptions(Object[][] objects,Account account,int waitingFor2){
        mainFrame.dispose();
        loginFrame.dispose();
        waitingFor=waitingFor2;
        optionsFrame = new UIOptions(objects,account,waitingFor);

    }

    public static void openPlayerTeamGame(Object[] data, Account account, int waitingFor) {
        optionsFrame.dispose();
       if(dataFrame!=null)
           dataFrame.dispose();
        System.out.println("Logged in succesfully.");
        if (waitingFor == 0) {
            dataFrame = new UIPlayer(data, account);
        } else if (waitingFor == 1) {
            dataFrame = new UIPlayer(data, account);
        } else if (waitingFor == 2) {
            dataFrame = new UIPlayer(data, account);

        }
    }

    public static void main(String[] args) {
        loginFrame = new LoginUI();
    }
}