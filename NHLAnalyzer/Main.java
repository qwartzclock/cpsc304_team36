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
        mainFrame = new MainUI(account);
    }

    public static void openOptions(Object[][] objects,Account account,int waitingFor2){
        mainFrame.dispose();
        loginFrame.dispose();
        waitingFor=waitingFor2;
        optionsFrame = new UIOptions(objects,account,waitingFor);

    }

    public static void openAddGame(String name1, String name2, String time, String date, String loc, Account account){
        mainFrame.dispose();
        loginFrame.dispose();

        //TODO: need to have a thing that set time date and location

        Object[] data = new Object[]{time,date,loc,name1,name2,0,0,new Object[]{},new Object[][]{},new Object[][]{},new Object[]{}};
        dataFrame = new UIAddGame(data,account);
    }

    public static void openAddGameNew(Object[] data, Account account){
        mainFrame.dispose();
        loginFrame.dispose();
        dataFrame.dispose();
        dataFrame = new UIAddGame(data,account);
    }


    public static void openPlayerTeamGame(Object[] data, Account account, int waitingFor) {
        optionsFrame.dispose();
       if(dataFrame!=null)
           dataFrame.dispose();
        if (waitingFor == 0) {
            dataFrame = new UIPlayer(data, account);
        } else if (waitingFor == 1) {
            dataFrame = new UITeam(data, account);
        } else if (waitingFor == 2) {
            dataFrame = new UIGame(data, account);
        } else if (waitingFor == 3) {
            // Game deletion
            mainFrame = new MainUI(account);

    }

}

    public static void main(String[] args) {
        loginFrame = new LoginUI();
    }
}