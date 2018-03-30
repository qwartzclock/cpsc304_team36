package NHLAnalyzer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class UITeam extends UIData {

    private Button btnBack;
    private Button btnViewGame;
    private CheckboxGroup cbg;


    private Label infoBar;
    private Label infoBar1;
    private Label infoBar2;
    private Label infoBar3;
    private Label infoBar4;
    private ArrayList<Button> buttonList1;


    private Object[] list;

    private Account account;

    // Each of the external arrays represents one relation, each of the internal ones represents its features
    public UITeam(Object[] data, Account account){

        super(account);

        list = data;

        // Team Location Team Name
        // Manager: "manager name"
        // Win/Loss Ratio: xx/xx (xx wins)
        // Players
        // ....

        this.account=account;

        int rows = 12 + data.length;
        setLayout(new GridLayout(rows,1));

        setTitle("NHL Analyzer");
        setSize(400,40*rows);

        Panel pnlText = new Panel();
        pnlText.setLayout(new FlowLayout());
        infoBar = new Label(data[1]+" "+data[0]);
        pnlText.add(infoBar);

        add(pnlText);

        Panel pnlText1 = new Panel();
        pnlText1.setLayout(new FlowLayout());
        infoBar1 = new Label("Manager: "+data[2]);
        pnlText1.add(infoBar1);

        add(pnlText1);


        Panel pnlText3 = new Panel();
        pnlText3.setLayout(new FlowLayout());
        infoBar3 = new Label("Win/Loss Ratio: "+data[4]+" ("+data[3]+" wins)");
        pnlText3.add(infoBar3);

        add(pnlText3);


        Panel pnlText4 = new Panel();
        pnlText4.setLayout(new FlowLayout());
        infoBar4 = new Label("Players");
        pnlText4.add(infoBar4);

        add(pnlText4);

        buttonList1= new ArrayList<Button>();
        for(int i = 5; i < data.length; i++){
            Object[] player = (Object[])data[i];

            Panel pnlPlayer = new Panel();
            pnlPlayer.setLayout(new FlowLayout());
            Label infoBarP = new Label(player[0].toString());
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

        Object[] player = (Object[])list[playerIndex+5];

        Main.openPlayerTeamGame(Query.getOnePlayer((String)player[1],false),
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