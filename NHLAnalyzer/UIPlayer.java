package NHLAnalyzer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class UIPlayer extends UIData {

    private Button btnBack;
    private Button btnViewGame;
    private CheckboxGroup cbg;


    private Label infoBar;
    private Label infoBarTeam;
    private Label infoBar1;
    private Label infoBar2;
    private Label infoBar3;
    private Label infoBar4;
    private Label infoBar5;
    private Label infoBar6;
    private Label infoBar7;
    private Label infoBar8;
    private ArrayList<Checkbox> cbList;

    private Object[] list;

    private Account account;

    // Each of the external arrays represents one relation, each of the internal ones represents its features
    public UIPlayer(Object[] data, Account account){

        super(account);

        list = data;

        // Playername

        // Team he plays for + VIEW TEAM
        // Height
        // Weight
        // Salary

        // Stats title
        // Points in history + AVG PER GAME
        // Goals in history + AVG PER GAME
        // Shots in history + goals per shot
        // Assists in history + AVG PER GAME
        // Fouls in history + AVG PER GAME

        // Sensitive data title
        // SENSITIVE INFO (IF THIS IS HIM VIEWING INFO ABOUT HIMSELF)
        // SIN
        // PHONE #

        // Title name
        // List of games they participate in + VIEW GAME

        this.account=account;

        int rows = 12 + data.length;
        setLayout(new GridLayout(rows,1));

        setTitle("NHL Analyzer");
        setSize(400,40*rows);

        Panel pnlText = new Panel();
        pnlText.setLayout(new FlowLayout());
        infoBar = new Label("Player: "+data[0]);
        pnlText.add(infoBar);

        add(pnlText);

        Panel pnlTextTeam = new Panel();
        pnlTextTeam.setLayout(new FlowLayout());
        infoBarTeam = new Label("Team: "+data[1]);
        pnlTextTeam.add(infoBarTeam);

        add(pnlTextTeam);


        Panel pnlText1 = new Panel();
        pnlText1.setLayout(new FlowLayout());
        infoBar1 = new Label("Total Points: "+data[2] + " (Avg per game: "+data[3]+")");
        pnlText1.add(infoBar1);

        add(pnlText1);

        Panel pnlText2 = new Panel();
        pnlText2.setLayout(new FlowLayout());
        infoBar2 = new Label("Total Goals: "+data[4] + " (Avg per game: "+data[5]+")");
        pnlText2.add(infoBar2);

        add(pnlText2);

        Panel pnlText3 = new Panel();
        pnlText3.setLayout(new FlowLayout());
        infoBar3 = new Label("Total Shots: "+data[6] + " (Goals per shot: "+data[7]+")");
        pnlText3.add(infoBar3);

        add(pnlText3);

        Panel pnlText4 = new Panel();
        pnlText4.setLayout(new FlowLayout());
        infoBar4 = new Label("Total Assists: "+data[8] + " (Avg per game: "+data[9]+")");
        pnlText4.add(infoBar4);

        add(pnlText4);

        Panel pnlText5 = new Panel();
        pnlText5.setLayout(new FlowLayout());
        infoBar5 = new Label("Total Fouls: "+data[10] + " (Avg per game: "+data[11]+")");
        pnlText5.add(infoBar5);

        add(pnlText5);

        if ((account.getSecurityLevel()==2 && account.getPlayerName()==data[0]) ||
                (account.getSecurityLevel()==3 && account.getRelatedTeam()==data[1]) ||
                account.getSecurityLevel()==5){

            //Sensitive data

            Panel pnlText6 = new Panel();
            pnlText6.setLayout(new FlowLayout());
            infoBar6 = new Label("SIN/SSN: "+data[12]);
            pnlText6.add(infoBar6);

            add(pnlText6);


            Panel pnlText7 = new Panel();
            pnlText7.setLayout(new FlowLayout());
            infoBar7 = new Label("Phone Number:"+data[13]);
            pnlText7.add(infoBar7);

            add(pnlText7);


        }
        Panel pnlText8 = new Panel();
        pnlText8.setLayout(new FlowLayout());
        infoBar8 = new Label("Games Played:");
        pnlText8.add(infoBar8);

        add(pnlText8);


        cbg = new CheckboxGroup();
        cbList = new ArrayList<Checkbox>();

        for(Object o : Arrays.copyOfRange(data,17,data.length)){
            Checkbox cb = new Checkbox(objectToString(o), cbg, true);
            add(cb);
            cbList.add(cb);

//            add(new Checkbox(objectToString(o), cbg, true));
        }

        btnViewGame = new Button("View Game");
        btnViewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                viewGamePress();
            }
        });
        add(btnViewGame);

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

    public void viewGamePress(){


        Main.openPlayerTeamGame(Query.getOnePlayer((String)
                        this.list[cbList.indexOf(cbg.getSelectedCheckbox())],false),
                account,
                2);
    }

    public void backPress(){
        Main.loginWith(account);

    }


    public String objectToString(Object ob){

        // This is for games
        Object[] obj = (Object[])ob;

        return obj[3].toString()+" vs. "+obj[4].toString()+" ("+obj[2].toString()+")";
    }


}