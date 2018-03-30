package NHLAnalyzer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.JOptionPane;


public class UIAddGame extends UIData {

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
    public UIAddGame(Object[] data, Account account){

        super(account);

        list = data;

        // "Team 1" vs "Team 2"
        // Winner: Team x  (calculated from scores)
        // Score: x-x + button to type in scores
        // Referees: x, x, x + "add"
        // Team 1
        // Player 1: 5 goals, 3 shots, 2 assists 3 fouls - CLICK TO PLAYER PROFILE
        // empty typespace + "add player data"
        // ...
        // Team 2
        // Player 1: 5 goals, 3 shots, 2 assists 3 fouls - CLICK TO PLAYER PROFILE
        // empty typespace + "add player data"

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

        Panel pnlText1 = new Panel();
        pnlText1.setLayout(new FlowLayout());
        infoBar1 = new Label("Score: "+data[5] + "-"+data[6]);
        pnlText1.add(infoBar1);

        Button btnScore = new Button("Edit");

        btnScore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                scorePress();
            }
        });
        pnlText1.add(btnScore);


        add(pnlText1);

        Object[] refAry = (Object[])data[7];
        String refStr;
        if(refAry.length == 0){
            refStr = "";

        } else {
            refStr = (String) refAry[0];
        }

        for(int i = 1; i < refAry.length; i++){
            refStr += ", ";
            refStr += (String)refAry[i];
        }
        Panel pnlText2 = new Panel();
        pnlText2.setLayout(new FlowLayout());
        infoBar2 = new Label(data[3]+" Players");
        pnlText2.add(infoBar2);

        Button btnAddPlayer = new Button("Add");
        btnAddPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                addPlayerPress(false);
            }
        });
        pnlText2.add(btnAddPlayer);
        add(pnlText2);

        buttonList1= new ArrayList<Button>();
        for(int i = 0; i < ((Object[])data[8]).length; i++){
            Object[] player = ((Object[][])data[8])[i];

            Panel pnlPlayer = new Panel();
            pnlPlayer.setLayout(new FlowLayout());
            Label infoBarP = new Label(player[0]+": "+player[2]+" goals, "+player[3]+" assists, "+player[4]+" shots,"+player[5]+" fouls");
            pnlPlayer.add(infoBarP);

            add(pnlPlayer);

        }

        Panel pnlText3 = new Panel();
        pnlText3.setLayout(new FlowLayout());
        infoBar3 = new Label(data[4]+" Players");
        pnlText3.add(infoBar3);

        Button btnAddPlayer2 = new Button("Add");
        btnAddPlayer2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                addPlayerPress(true);
            }
        });
        pnlText3.add(btnAddPlayer2);


        add(pnlText3);

        buttonList2= new ArrayList<Button>();
        for(int i = 0; i < ((Object[])data[9]).length; i++){
            Object[] player = ((Object[][])data[9])[i];

            Panel pnlPlayer = new Panel();
            pnlPlayer.setLayout(new FlowLayout());
            Label infoBarP = new Label(player[0]+": "+player[1]+" goals, "+player[2]+" assists, "+player[3]+" shots,"+player[4]+" fouls");
            pnlPlayer.add(infoBarP);

            add(pnlPlayer);

        }


        Button event;
        event = new Button("Add Event");
        event.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                    addEventPress();
            }
        });
        add(event);


        Button btnSubmit;
        btnSubmit = new Button("Submit");
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                submitPress();
            }
        });
        add(btnSubmit);

        Button btnBack;
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


    public void addPlayerPress(boolean team2){
        int score1 = 0;
        int score2 = 0;

        while (1==1){
            String s = (String)JOptionPane.showInputDialog(
                    this,
                    "What the player's name?",
                    "Customized Dialog",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null);

            if(s != null ){
                //Checks to make sure there's one on the team
                boolean isInTeam=false;
                Object[] team = Query.getOneTeam((String)list[3]);//team1
                if(team2)
                    team=Query.getOneTeam((String)list[4]);
                for(int ij = 5; ij < team.length; ij++){
                    Object[] player = (Object[])team[ij];
                    if(((String)player[0]).equals(s)){
                        isInTeam=true;
                    }

                }
                if(!isInTeam){
                    JOptionPane.showMessageDialog(this,
                            "No player of that name on the team.",
                            "Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }




                Object[][] playerList = Query.searchPlayers(s);
                String str = "";
                Object[] player = playerList[0];
                if(playerList.length > 1){
                    for(Object[] playerTemp : playerList){
                        str+=playerTemp[1];
                        str+=" ";
                    }
                    outerloop:
                    while(3==3){
                        String t = (String)JOptionPane.showInputDialog(
                                this,
                                "Multiple players of this name. Select an ID from "+str,
                                "Customized Dialog",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                null,
                                null);
                        int j = 0;
                        for(Object[] playerTemp : playerList){
                            j += 1;
                            if(playerTemp[1].equals(t)){
                                player = playerList[j-1];
                                break outerloop;
                            }
                        }

                    }

                }

                if(!team2){
                    Object[][] newAry = new Object[((Object[])list[8]).length+1][];
                    System.arraycopy((Object[])list[8],0,newAry,0,((Object[])list[8]).length);
                    newAry[((Object[])list[8]).length] = new Object[]{player[0],player[1],0,0,0,0,new Object[][]{}};
                    list[8]=(Object[][])newAry;
                } else {
                    Object[][] newAry = new Object[((Object[])list[9]).length+1][];
                        System.arraycopy((Object[])list[9],0,newAry,0,((Object[])list[9]).length);
                    newAry[((Object[])list[9]).length] = new Object[]{player[0],player[1],0,0,0,0,new Object[][]{}};
                    list[9]=(Object[][])newAry;
                }

                break;
            }
        }



        Main.openAddGameNew(list,account);

    }

    public void addEventPress(){

        if(((Object[][])list[9]).length == 0 && ((Object[][])list[8]).length == 0) {
            JOptionPane.showMessageDialog(this,
                    "You must have a player to assign the event to.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
            outermost:
        while (1==1){
            String str = "";

            Object[][] team1List = (Object[][])list[8];
            Object[][] team2List = (Object[][])list[9];

            for(Object[] playerTemp : team1List){
                str+=playerTemp[1];
                str+=" ";
            }

            for(Object[] playerTemp : team2List){
                str+=playerTemp[1];
                str+=" ";
            }

            String s = (String)JOptionPane.showInputDialog(
                    this,
                    "Which player? Select an ID from: "+str,
                    "Clarification",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null);

            if(s != null ) {
                Object[][] playerList = (Object[][])addAll(team1List,team2List);
                int j = 0;
                outerloop:
                for (Object[] playerTemp : playerList) {
                    if (playerTemp[1].equals(s)) {
                        Object[] player = playerList[j];

                        //Custom button text
                        Object[] options = {"Goal",
                                "Assist",
                                "Shot",
                                "Penalty"};
                        int n = JOptionPane.showOptionDialog(this,
                                "What sort of event happened?",
                                "Clarification",
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[2]);


                        //TODO
                        String gametime = (String)JOptionPane.showInputDialog(
                                this,
                                "What was the gametime of the event?",
                                "Clarification",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                null,
                                null);

                        Object[][] newEvents = new Object[((Object[])list[10]).length+1][];
                        System.arraycopy(list[10],0,newEvents,0,((Object[])list[10]).length);
                        newEvents[((Object[])list[10]).length]=new Object[]{gametime, s, n};
                        list[10]=newEvents;


                        //Adds 1 to the player parts of this event
                        int tempVal = 0;
                        switch(n) {
                            case 0:
                                tempVal = 2;
                                break;
                            case 1:
                                tempVal = 3;
                                break;
                            case 2:
                                tempVal = 4;
                                break;
                            case 3:
                                tempVal = 5;
                                break;
                        }

                        if(j>=team1List.length){
                            j -= team1List.length;
                            ((Object[])((Object[])list[9])[j])[tempVal] = (Integer)((Object[])((Object[])list[9])[j])[tempVal]+ 1;
                        }else {
                            ((Object[])((Object[])list[8])[j])[tempVal] = (Integer)((Object[])((Object[])list[8])[j])[tempVal]+ 1;
                        }

                        break outermost;
                    }
                    j += 1;

                }
            }

        }



        Main.openAddGameNew(list,account);

    }

    public Object[][] addAll(Object[][] a, Object[][] b){
        Object[][] c = new Object[a.length+b.length][];
        System.arraycopy(a,0,c,0,a.length);
        System.arraycopy(b,0,c,a.length,b.length);
        return c;
    }


    public void submitPress(){

        Object[] submitData = new Object[]{list[0],list[1],list[2],list[3],list[4],list[5],list[6],list[7],list[10]};
/*
        8/list[10] - [] <- List of events  formatted thusly:
            [gametime, player involved, type]           type is 0, 1, 2, or 3, representing: goal, assist, shot, penalty (respecitvely)
*/

        System.out.println(submitData);
        Query.addGame(submitData);
        Main.loginWith(account);

    }

    public void scorePress(){
        int score1 = 0;
        int score2 = 0;
        while (1==1){
            String s = (String)JOptionPane.showInputDialog(
                    this,
                    "What is the first teams score?",
                    "Customized Dialog",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null);

            if(s != null && s.matches("\\d+")){
                score1 = Integer.parseInt(s);
                break;
            }
        }

        while(2==2){
            String s = (String)JOptionPane.showInputDialog(
                    this,
                    "What is the second teams score?",
                    "Customized Dialog",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null);
            System.out.println(s);

            if(s != null && s.matches("\\d+")){
                score2 = Integer.parseInt(s);
                break;
            }

        }
        list[5]=score1;
        list[6]=score2;


        Main.openAddGameNew(list,account);
    }


    public String objectToString(Object ob){

        // This is for games
        Object[] obj = (Object[])ob;

        return obj[3].toString()+" vs. "+obj[4].toString()+" ("+obj[2].toString()+")";
    }


}