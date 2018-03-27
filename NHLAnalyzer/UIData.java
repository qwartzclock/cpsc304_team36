package NHLAnalyzer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class UIData extends Frame implements WindowListener{

    private Account account;
    // Each of the external arrays represents one relation, each of the internal ones represents its features
    public UIData(Account account){
        this.account=account;
    }

    public void backPress(){
        Main.loginWith(account);

    }

    @Override
    public void windowClosing(WindowEvent evt) {
        System.exit(0);  // Terminate the program
    }

    // Not Used, but need to provide an empty body to compile.
    @Override public void windowOpened(WindowEvent evt) { }
    @Override public void windowClosed(WindowEvent evt) { }
    @Override public void windowIconified(WindowEvent evt) { }
    @Override public void windowDeiconified(WindowEvent evt) { }
    @Override public void windowActivated(WindowEvent evt) { }
    @Override public void windowDeactivated(WindowEvent evt) { }


}