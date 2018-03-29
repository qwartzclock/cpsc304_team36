package NHLAnalyzer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class UIGame extends UIData {

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
    private ArrayList<Button> buttonList1;
    private ArrayList<Button> buttonList2;


    private Object[] list;

    private Account account;

    // Each of the external arrays represents one relation, each of the internal ones represents its features
    public UIGame(Object[] data, Account account){

        super(account);

        list = data;

        // "Team 1" vs "Team 2"
        // Winner: Team x
        // Score: x-x
        // Referees: x, x, x
        // Team 1
        // Player 1: 5 goals, 3 shots, 2 assists 3 fouls - CLICK TO PLAYER PROFILE
        // ...
        // Team 2
        // Player 1: 5 goals, 3 shots, 2 assists 3 fouls - CLICK TO PLAYER PROFILE

        this.account=account;

        int rows = 12 + data.length;
        setLayout(new GridLayout(rows,1));

        setTitle("NHL Analyzer");
        setSize(400,40*rows);

        Panel pnlText = new Panel();
        pnlText.setLayout(new FlowLayout());
        infoBar = new Label(data[3]+" vs. "+data[4]);
        pnlText.add(infoBar);

        add(pnlText);

        Panel pnlTextTeam = new Panel();
        pnlTextTeam.setLayout(new FlowLayout());
        Object winner = (int)data[5] > (int)data[6] ? data[3] : data[4];
        infoBarTeam = new Label("Winner:  "+winner);
        pnlTextTeam.add(infoBarTeam);

        add(pnlTextTeam);


        Panel pnlText1 = new Panel();
        pnlText1.setLayout(new FlowLayout());
        infoBar1 = new Label("Score: "+data[5] + "-"+data[6]);
        pnlText1.add(infoBar1);

        add(pnlText1);

        Object[] refAry = (Object[])data[7];
        String refStr = (String)refAry[0];

        for(int i = 1; i < refAry.length; i++){
            refStr += ", ";
            refStr += (String)refAry[i];
        }
        Panel pnlText2 = new Panel();
        pnlText2.setLayout(new FlowLayout());
        infoBar2 = new Label(data[3]+" Statistics");
        pnlText2.add(infoBar2);

        add(pnlText2);

        buttonList1= new ArrayList<Button>();
        for(int i = 0; i < ((Object[])data[8]).length; i++){
            Object[] player = ((Object[][])data[8])[i];

            Panel pnlPlayer = new Panel();
            pnlPlayer.setLayout(new FlowLayout());
            Label infoBarP = new Label(player[0]+": "+player[1]+" goals, "+player[2]+" assists, "+player[3]+" shots,"+player[4]+" fouls");
            pnlPlayer.add(infoBarP);

            Button btnPlayer = new Button("View");

            buttonList1.add(btnPlayer);
            btnPlayer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    int i = buttonList1.indexOf(evt.getSource());
                    playerPress(false,i);
                }
            });
            pnlPlayer.add(btnPlayer);


            add(pnlPlayer);

        }

        Panel pnlText3 = new Panel();
        pnlText3.setLayout(new FlowLayout());
        infoBar3 = new Label(data[4]+" Statistics");
        pnlText3.add(infoBar3);

        add(pnlText3);

        buttonList2= new ArrayList<Button>();
        for(int i = 0; i < ((Object[])data[9]).length; i++){
            Object[] player = ((Object[][])data[9])[i];

            Panel pnlPlayer = new Panel();
            pnlPlayer.setLayout(new FlowLayout());
            Label infoBarP = new Label(player[0]+": "+player[1]+" goals, "+player[2]+" assists, "+player[3]+" shots,"+player[4]+" fouls");
            pnlPlayer.add(infoBarP);

            Button btnPlayer = new Button("View");

            buttonList2.add(btnPlayer);
            btnPlayer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    int i = buttonList2.indexOf(evt.getSource());
                    playerPress(true,i);
                }
            });
            pnlPlayer.add(btnPlayer);


            add(pnlPlayer);

        }

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

    public void playerPress(boolean teamTwo, int playerIndex){

        Object[][] team1 = (Object[][])(this.list[8]);
        if(teamTwo)
            team1 = (Object[][])(this.list[9]);

        Main.openPlayerTeamGame(Query.getOnePlayer((String)team1[playerIndex][1],false),
                account,
                0);
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