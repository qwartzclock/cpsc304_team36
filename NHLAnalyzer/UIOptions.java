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

        this.waitingFor=waitingFor;

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
        if(waitingFor==2){
            return obj[0].toString()+" vs. "+obj[1].toString()+" ("+obj[2]+", "+obj[3]+", "+obj[4]+")";

        }
        if(waitingFor==0){
            return obj[0].toString()+" ("+obj[1]+")";

        }
        return obj[0].toString();
    }

    public void submitPress(){

        // TODO vvvvvv

        boolean allowedSensitive = true;

        //TODO ^^^^^^^

       // Main.openPlayerTeamGame(Query.getOnePlayer((String)
//                        list[cbList.indexOf(cbg.getSelectedCheckbox())][1],allowedSensitive),
  //              account,
    //            waitingFor);
        Object[] waiting;
        if(waitingFor==0){
            waiting = Query.getOnePlayer((String)list[cbList.indexOf(cbg.getSelectedCheckbox())][1],allowedSensitive);

        } else if(waitingFor==1){
            waiting = Query.getOneTeam((String)
                    list[cbList.indexOf(cbg.getSelectedCheckbox())][1]);
        } else if(waitingFor==2) {
            Object[] w = list[cbList.indexOf(cbg.getSelectedCheckbox())];
            String time;
            String date;
            String loc;

            if (w[2] instanceof Integer) {
                time = ((Integer) w[2]).toString();
            } else {
                time = (String) w[2];
            }
            if (w[3] instanceof Integer) {
                date = ((Integer) w[2]).toString();
            } else {
                date = (String) w[3];
            }
            if (w[4] instanceof Integer) {
                loc = ((Integer) w[4]).toString();
            } else {
                loc = (String) w[4];
            }
            waiting = Query.getOneGame(time, date, loc);
            Main.openPlayerTeamGame(waiting,
                    account,
                    waitingFor);
            return;

        } else {
            Object[] w =list[cbList.indexOf(cbg.getSelectedCheckbox())];

            Query.deleteGame(((Integer)w[2]).toString(),((Integer)w[3]).toString(),(String)w[4]);
            Main.openPlayerTeamGame(null,account,waitingFor);
            return;
        }

        Main.openPlayerTeamGame(waiting,account,waitingFor);
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