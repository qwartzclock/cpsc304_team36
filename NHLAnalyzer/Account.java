package NHLAnalyzer;

public class Account{

    // 0 is a guest account: can only access public data
    // 1 is an statiscian account: can only access publid data, but can add info re: games and such
    // 2 is a player: same permissions as 0 except can also read/write to sensitive data ABOUT THEM
    // 3 is a manager: same as 0 but can read/write to sensitive data about their team, and semi-public data about other teams (phone # etc)
    // 4 is an administrator, can read/write everything
    private int securityLevel;

    // NULL if this is a guest account, statiscian, or administrator
    // the team name of a player or manager
    private String relatedTeam;


    private String playerName;

    public String username;

    public Account (int level, String name, String team, String user) {
        securityLevel=level;
        relatedTeam=team;
        playerName=name;
        username = user;

    }

    // This account can access
    public boolean canAccessPlayerData(String teamName, String playerName){
        return (securityLevel == 2 && playerName==this.playerName) || (securityLevel == 3 && relatedTeam==teamName) || securityLevel == 4;
    }

    public int getSecurityLevel(){
        return securityLevel;
    }
    public String getPlayerName(){
        return playerName;
    }
    public String getRelatedTeam(){
        return relatedTeam;
    }

}