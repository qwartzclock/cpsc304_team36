package NHLAnalyzer;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JOptionPane;


public class MainUI extends Frame implements WindowListener{

    private Label userinfo;

    private Label playerLabel;
    private Label teamLabel;
    private Label gameLabel;
    private Label inputLabel;
    private Label sensitivePlayerLabel;
    private Label sensitiveManagerLabel;
    private Label sensitiveTeamSearchLabel; // Admin only


    private TextField playerSearchField;
    private TextField teamSearchField;
    private TextField gameSearchField1;
    private TextField gameSearchField2;
    private TextField gameSearchField3;
    private TextField gameSearchField4;
    private TextField gameSearchField5;
    private TextField gameSearchField6;
    private TextField gameSearchField7;
    private TextField gameSearchField8;
    private TextField gameSearchField9;
    private TextField inputField1;
    private TextField inputField2;
    private TextField sensitiveTeamSearch;

    private Button playerButton;
    private Button teamButton;
    private Button gameButton;
    private Button inputButton;
    private Button sensitivePlayerButton;
    private Button sensitiveManagerButton;
    private Button sensitiveTeamSearchButton; // Admin only

    private Button gameButton2;
    private Button gameButton3;
    private Button gameButton4;
    private Button gameButton5;
    private Button gameButton6;

    private Account account;



    public MainUI(Account account){

        this.account=account;
        int y = 0;
        int x = 0;
        int rows = 4;
        if(account.getSecurityLevel()==1 || account.getSecurityLevel()==4) {
            rows += 2;
            y = 120;
            x = 400;
        }


        setLayout(new GridLayout(rows,1));

        setTitle("NHL Analyzer");
        setSize(400+x,400+y);

        Panel pnlText = new Panel();
        pnlText.setLayout(new FlowLayout());
        userinfo = new Label("Welcome "+account.username+"! Please select an option.");
        pnlText.add(userinfo);

        add(pnlText);


        Panel pnlPlayerSearch = new Panel();
        pnlPlayerSearch.setLayout(new FlowLayout());
        pnlPlayerSearch.add(new Label("Find player:"));
        playerSearchField = new TextField(30);
        pnlPlayerSearch.add(playerSearchField);
        playerButton = new Button("Search");
        playerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                playerPress();
            }
        });
        pnlPlayerSearch.add(playerButton);

        add(pnlPlayerSearch);

        Panel pnlTeamSearch = new Panel();
        pnlTeamSearch.setLayout(new FlowLayout());
        pnlTeamSearch.add(new Label("Find team:"));
        teamSearchField = new TextField(30);
        pnlTeamSearch.add(teamSearchField);
        teamButton = new Button("Search");
        teamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                teamPress();
            }
        });
        pnlTeamSearch.add(teamButton);

        add(pnlTeamSearch);

        Panel pnlGameSearch = new Panel();
        pnlGameSearch.setLayout(new FlowLayout());
        pnlGameSearch.add(new Label("Find game:"));
        gameSearchField1 = new TextField(30);
        pnlGameSearch.add(gameSearchField1);
        gameSearchField2 = new TextField(30);
        pnlGameSearch.add(gameSearchField2);
        gameButton = new Button("Search");
        gameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                gamePress();
            }
        });
        pnlGameSearch.add(gameButton);

        add(pnlGameSearch);

        // This is for statistcians- the add/delete game function
        if (account.getSecurityLevel() == 1 || account.getSecurityLevel() == 4) {

            Panel pnlGameAdd = new Panel();
            pnlGameAdd.setLayout(new FlowLayout());
            pnlGameAdd.add(new Label("Add game between:"));
            gameSearchField3 = new TextField(30);
            pnlGameAdd.add(gameSearchField3);
            gameSearchField4 = new TextField(30);
            pnlGameAdd.add(gameSearchField4);

            pnlGameAdd.add(new Label("Time (HH:MM):"));

            gameSearchField7 = new TextField(30);
            pnlGameAdd.add(gameSearchField7);
            pnlGameAdd.add(new Label("Date (YYYY-MM-DD):"));
            gameSearchField8 = new TextField(30);
            pnlGameAdd.add(gameSearchField8);
            pnlGameAdd.add(new Label("Location:"));
            gameSearchField9 = new TextField(30);
            pnlGameAdd.add(gameSearchField9);

            gameButton2 = new Button("Add");
            gameButton2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    gameAdd();
                }
            });
            pnlGameAdd.add(gameButton2);

            add(pnlGameAdd);


            Panel pnlGameRemove = new Panel();
            pnlGameRemove.setLayout(new FlowLayout());
            pnlGameRemove.add(new Label("Delete game between:"));
            gameSearchField5 = new TextField(30);
            pnlGameRemove.add(gameSearchField5);
            gameSearchField6 = new TextField(30);
            pnlGameRemove.add(gameSearchField6);
            gameButton3 = new Button("Delete");
            gameButton3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    gameRemove();
                }
            });
            pnlGameRemove.add(gameButton3);

            add(pnlGameRemove);

        }


        addWindowListener(this);

        setVisible(true);
    }

    public void playerPress(){
        Object[][] ary = Query.searchPlayers(playerSearchField.getText());
        if(ary.length==0){
            JOptionPane.showMessageDialog(this,
                    "Couldn't find any players by that name.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);

        } else {
            Main.openOptions(ary, account, 0);
        }
    }

    public void teamPress(){

        Object[][] ary = Query.searchTeams(teamSearchField.getText());
        if(ary.length==0){
            JOptionPane.showMessageDialog(this,
                    "Couldn't find any team by that name.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);

        } else {
            Main.openOptions(ary, account, 1);
        }
    }


    public void gamePress(){
        Object[][] ary = Query.searchGames(gameSearchField1.getText(), gameSearchField2.getText());
        if(ary.length==0){
            JOptionPane.showMessageDialog(this,
                    "Couldn't find any game by that name.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);

        } else {
            Main.openOptions(ary,account,2);

        }

    }

    public void gameAdd(){
        //TODO
        //Object[][] ary = Query.searchGames(gameSearchField1.getText(),gameSearchField2.getText());

        if(Query.getOneTeam(gameSearchField3.getText()) == null || Query.getOneTeam(gameSearchField4.getText()) == null){
            JOptionPane.showMessageDialog(this,
                    "One of those teams does not exist.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }else{
            Main.openAddGame(gameSearchField3.getText(),gameSearchField4.getText(),gameSearchField7.getText(),gameSearchField8.getText(),gameSearchField9.getText(),account);

        }


    }

    public void gameRemove(){
        //TODO
        Object[][] ary = Query.searchGames(gameSearchField5.getText(),gameSearchField6.getText());
        Main.openOptions(ary,account,3);

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