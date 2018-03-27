package NHLAnalyzer;

import java.awt.*;
import java.awt.event.*;

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

    private Account account;



    public MainUI(Account account){

        this.account=account;

        int rows = 4;
        if(account.getSecurityLevel()!=1)
            rows += 1;
        if(account.getSecurityLevel()==4)
            rows += 1;

        setLayout(new GridLayout(rows,1));

        setTitle("NHL Analyzer");
        setSize(400,400);

        Panel pnlText = new Panel();
        pnlText.setLayout(new FlowLayout());
        userinfo = new Label("Welcome "+account.username+"! Please select and option.");
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

        addWindowListener(this);

        setVisible(true);
    }

    public void playerPress(){
        System.out.println("Player press");
        Object[][] ary = Query.searchPlayers(playerSearchField.getText());
        Main.openOptions(ary,account,0);
    }

    public void teamPress(){
        System.out.println("Team press");
        Object[][] ary = Query.searchTeams(teamSearchField.getText());
        Main.openOptions(ary,account,1);

    }


    public void gamePress(){
        System.out.println("Game press");
        Object[][] ary = Query.searchGames(gameSearchField1.getText(),gameSearchField2.getText());
        Main.openOptions(ary,account,2);

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