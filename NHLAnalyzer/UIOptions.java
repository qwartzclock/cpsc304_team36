package NHLAnalyzer;

import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

public class UIOptions extends Frame implements WindowListener{

    private Button btnBack;
    private Button btnSubmit;
    private CheckboxGroup cbg;
    private ArrayList<Checkbox> cbList;
    private Label infoBar;

    private Account account;

    private Object[][] list;
    private int waitingFor;

    // Each of the external arrays represents one relation, each of the internal ones represents its features
    public UIOptions(Object[][] list, Account account, int waitingFor){


        this.account=account;
        this.list=list;

        int rows = 3 + list.length;
        setLayout(new GridLayout(rows,1));

        setTitle("NHL Analyzer");
        setSize(400,40*rows);

        Panel pnlText = new Panel();
        pnlText.setLayout(new FlowLayout());
        infoBar = new Label("Please select an option.");
        pnlText.add(infoBar);

        add(pnlText);

        cbg = new CheckboxGroup();
        cbList = new ArrayList<Checkbox>();
        for(Object[] o : list){
            Checkbox cb = new Checkbox(objectToString(o), cbg, true);
            add(cb);
            cbList.add(cb);
        }

        btnSubmit = new Button("Submit");
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                submitPress();
            }
        });
        add(btnSubmit);

        btnBack = new Button("Back");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                backPress();
            }
        });
        add(btnBack);

        addWindowListener(this);

        setVisible(true);
    }

    public String objectToString(Object[] obj){

        //TODO:
        // replace this
        // with an actual parser

        return obj[0].toString();
    }

    public void submitPress(){

        // TODO vvvvvv

        boolean allowedSensitive = true;

        //TODO ^^^^^^^

        Main.openPlayerTeamGame(Query.getOnePlayer((String)
                list[cbList.indexOf(cbg.getSelectedCheckbox())][1],allowedSensitive),
                account,
                waitingFor);
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