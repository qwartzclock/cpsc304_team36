package NHLAnalyzer;

import java.awt.*;
import java.awt.event.*;

public class LoginUI extends Frame implements ActionListener, WindowListener{

    private Button btnSubmit;
    private TextField userTextField;
    private TextField passTextField;

    private Label infoBar;

    public LoginUI(){


        setLayout(new GridLayout(5,1));

        setTitle("NHL Analyzer");
        setSize(270,300);

        Panel pnlText = new Panel();
        pnlText.setLayout(new FlowLayout());
        infoBar = new Label("Please enter your login information.");
        pnlText.add(infoBar);

        add(pnlText);

        Panel pnlText2 = new Panel();
        pnlText2.setLayout(new FlowLayout());
        pnlText2.add(new Label("(Leave blank to enter as Guest)"));

        add(pnlText2);


        Panel pnlUsername = new Panel();
        pnlUsername.setLayout(new FlowLayout());
        pnlUsername.add(new Label("Username:"));
        userTextField = new TextField(30);
        pnlUsername.add(userTextField);

        add(pnlUsername);

        Panel pnlPassword = new Panel();
        pnlPassword.setLayout(new FlowLayout());
        pnlPassword.add(new Label("Password:"));
        passTextField = new TextField(30);
        pnlPassword.add(passTextField);

        add(pnlPassword);

        btnSubmit = new Button("Submit");
        btnSubmit.addActionListener(this);
        add(btnSubmit);

        addWindowListener(this);

        setVisible(true);
    }


    // ActionEvent handler - Called back upon button-click.
    @Override
    public void actionPerformed(ActionEvent evt) {
        Account accnt =  Query.tryLogin(userTextField.getText(),passTextField.getText());
        if (accnt != null){
            Main.loginWith(accnt);
        } else {
            infoBar.setText("Incorrect username/password.");
        }
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