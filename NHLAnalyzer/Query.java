package NHLAnalyzer;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Query {

    public Query (String query) {

    }

    public static Account tryLogin(String username, String password){
        System.out.println("Attempting to log in with "+username+" and "+password);

        if(username.equals("")){
            return new Account(0,"","","Guest");
        }

        try{
            String query = "SELECT * FROM users WHERE userName = '" + username + "'";
            Object[][] answer = Driver.getInstance().makeQuery(query);

            if(answer.length == 0){
                System.out.println("No user matching " + username);
            }
            else if(answer.length != 1){
                System.out.println("Duplicate entry for user " + username);
            }
            else{
                if(password.equals((String)answer[0][1])){
                    return new Account(((BigDecimal)answer[0][2]).intValue(), (answer[0][3]).toString(), answer[0][4].toString(), answer[0][0].toString());
                }
                else{
                    System.out.println("Password not matching data for user " + username);
                }
            }
        }
        catch(java.sql.SQLException e){
            System.out.println("Connection failed");
        }

        /*
        if(username.equals("stats")){
            return new Account(1,"","",username);
        }
        if(username.equals("player")){
            return new Account(2,"Gretzky","",username);
        }
        if(username.equals("manager")){
            return new Account(3,"","Oilers",username);
        }
        if(username.equals("admin")){
            return new Account(4,"","",username);
        }
        if(username.equals("")){
            return new Account(0,"","","Guest");
        }
        */
        return null;
    }

    public static Object[][] searchPlayers(String playerName){
        //TODO: REPLACE THIS WITH A QUERY
        //
        // Return all players + their ID's
        //
        // The array is formatted as follows
        // 0 - player name
        // 1 - player ID (key)
        // 2
		Object[][] result = null;
		try {
			result = Driver.getInstance().makeQuery("SELECT PLAYER.NAME, PLAYER.PLAYERID" +
					" FROM PLAYER " +
					"WHERE UPPER(PLAYER.name) LIKE UPPER('%" + playerName + "%')");
		} catch (SQLException e) {
			System.out.println("Error with the query");
		}
        /*Object[][] o = {{"Jonathan Der","id5",},
                {"Miayaz Nahh","id6"},
              {"Dokidok Iredian","id7"},
              {"Natalia Dddder","id8"},
               {"Noncertified Miayana","id9"}};*/

        return result;

        //
        //
        //TODO: REPLACE THIS WITH A QUERY
    }

    public static Object[] getOnePlayer(String playerID, boolean sensitive){
        //TODOde: REPLACE THIS WITH A QUERY
        //
        // Given the player ID, return all data about a player
        //

        String q0_4 = "SELECT PLAYER.NAME, T.TEAMNAME, PLAYER.HEIGHT, PLAYER.WEIGHT, PLAYER.SALARY" +
                "  FROM PLAYER" +
                "    INNER JOIN PLAYSFOR P ON PLAYER.PLAYERID = P.PLAYERID" +
                "    INNER JOIN TEAM T ON P.TEAMNAME = T.TEAMNAME" +
                " WHERE PLAYER.PLAYERID = " + playerID;

        String qtotalAssists =
                "SELECT COUNT(A.PLAYERID)" +
                        "  FROM Player P" +
                        "    LEFT JOIN ASSIST A ON P.PLAYERID = A.PLAYERID" +
                        "    WHERE P.PLAYERID = " + playerID +
                        "  GROUP BY P.PLAYERID";

        String qtotalGoals =
                "SELECT COUNT(A.PLAYERID)" +
                        "  FROM Player P" +
                        "    LEFT JOIN GOAL A ON P.PLAYERID = A.PLAYERID" +
                        "    WHERE P.PLAYERID = " + playerID +
                        "  GROUP BY P.PLAYERID";

        String qtotalPenalty =
                "SELECT COUNT(A.PLAYERID)" +
                        "  FROM Player P" +
                        "    LEFT JOIN PENALTY A ON P.PLAYERID = A.PLAYERID" +
                        "    WHERE P.PLAYERID = " + playerID +
                        "  GROUP BY P.PLAYERID";

        String qtotalGames =
                "SELECT COUNT(*) " +
                        "  FROM ( " +
                        "    SELECT DISTINCT G.GAME_DATE, G.LOCATION " +
                        "      FROM PLAYER P " +
                        "      INNER JOIN PLAYSFOR PF ON P.PLAYERID = PF.PLAYERID " +
                        "      INNER JOIN TEAM T ON PF.TEAMNAME = T.TEAMNAME " +
                        "      INNER JOIN PLAYS P2 ON T.TEAMNAME = P2.TEAMNAME " +
                        "      INNER JOIN GAME G ON P2.GAME_DATE = G.GAME_DATE AND P2.LOCATION = G.LOCATION " +
                        "      WHERE P.PLAYERID = " + playerID +
                        "  )";

        String qGames =
                "SELECT G.GAME_DATE, G.LOCATION, P2.TEAMNAME, P3.TEAMNAME " +
                        "    FROM PLAYER P " +
                        "    INNER JOIN PLAYSFOR PF ON P.PLAYERID = PF.PLAYERID " +
                        "    INNER JOIN TEAM T ON PF.TEAMNAME = T.TEAMNAME " +
                        "    INNER JOIN PLAYS P2 ON T.TEAMNAME = P2.TEAMNAME " +
                        "    INNER JOIN GAME G ON P2.GAME_DATE = G.GAME_DATE AND P2.LOCATION = G.LOCATION " +
                        "      INNER JOIN PLAYS P3 ON G.GAME_DATE = P3.GAME_DATE AND G.LOCATION = P3.LOCATION " +
                        "      WHERE P.PLAYERID = " + playerID + " AND NOT P3.TEAMNAME = P2.TEAMNAME";

        List<Object> rFinalList = new ArrayList<>();
        try {
            Object[][] r0_4 = Driver.getInstance().makeQuery(q0_4);
            for (int i = 0; i < r0_4[0].length; i++) {
                rFinalList.add(r0_4[0][i]);
            }

            int rtotAssist = ((BigDecimal) Driver.getInstance().makeQuery(qtotalAssists)[0][0]).intValue();
            int rtotGoals = ((BigDecimal) Driver.getInstance().makeQuery(qtotalGoals)[0][0]).intValue();
            int rtotPenalty = ((BigDecimal) Driver.getInstance().makeQuery(qtotalPenalty)[0][0]).intValue();
            int rtotGames = ((BigDecimal) Driver.getInstance().makeQuery(qtotalGoals)[0][0]).intValue();
            Object[][] rGames = Driver.getInstance().makeQuery(qGames);

            int points = rtotAssist + rtotGoals*2;
            rFinalList.add(points);
            rFinalList.add(((float) points) / rtotGoals);
            rFinalList.add(rtotGoals); // 7
            rFinalList.add(((float) rtotGoals) / rtotGames); // 8
            rFinalList.add(null); // 9
            rFinalList.add(null); // 10
            rFinalList.add(rtotAssist); // 11
            rFinalList.add(((float)rtotAssist) / rtotGames); // 12
            rFinalList.add(rtotPenalty); // 13
            rFinalList.add(((float) rtotPenalty) / rtotGames); // 14

            if (sensitive) {
                rFinalList.add(null); // 15
                rFinalList.add(null); // 16
            } else {
                rFinalList.add(null); // 15
                rFinalList.add(null); // 16
            }

            for (int i = 0; i < rGames.length; i++) {
                rFinalList.add(rGames[i]);
            }

        } catch (SQLException e) {
            System.out.println("Exception getting player data: " + e);
        }

        Object[] rFinalArray = rFinalList.toArray();

        /*
         0 - player name
         1 - team name
         2 - height
         3 - weight
         4 - salary
         5 - total scored points
         6 - avg scored points per game
         7 - total scored goals
         8 - avg scored goals per game
         9 - total shots
         10 - avg goals per shot
         11 - total assists
         12 - avg assists per game
         13 - total fouls
         14 - avg fouls per game

         IF SENSITIVE DATA IS ALLOWED

         15 - sin
         16 - phone

         REGARDLESS

         17 + will be the games they have played in, formatted like:
         [DateTime, Location, Team 1, Team 2]

        */

        Object game1 = new Object[] {0,0,"Rogers Arena", "Canucks","Oilers"};
        Object game2 = new Object[] {44,5,"Rogers Arena", "Flames","Canucks"};
        Object[] o = {"Jonathan Der","Canucks",2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,game1,game2};
        return rFinalArray;

        //
        //
        //TODOne: REPLACE THIS WITH A QUERY
    }

    public static Object[][] searchTeams(String teamName){

        try{
            String query = "SELECT teamName, city FROM team WHERE UPPER(teamName) like UPPER('%" + teamName + "%')";
            Object[][] answer = Driver.getInstance().makeQuery(query);

            if(answer.length == 0){
                System.out.println("No team matching " + teamName);
            }

            return answer;
        }
        catch(java.sql.SQLException e){
            System.out.println("Connection failed");
        }

        return null;
    }

    public static Object[] getOneTeam(String teamName){

        //TODO: REPLACE THIS WITH A QUERY
        //
        //


        // 0 - team name
        // 1 - team location
        // 2 - manager name
        // 4 - wins
        // 5 - win/loss ratio
        // 6+ - arrays of players [] formatted as: [player name, player ID/key]

        Object[] o = {"Canucks","Vancouver","Tommy Thomaspon",5,1.5,new Object[]{"Jonathan Der","id5"}};
        return o;

        //
        //
        //TODO: REPLACE THIS WITH A QUERY
    }

    public static Object[][] searchGames(String team1, String team2){

        try{
            String query = "SELECT game_date,location FROM game g1 WHERE EXISTS " +
                    "(SELECT game_date, location FROM plays p1 WHERE UPPER(teamName) LIKE UPPER('%"+team1+"%') AND EXISTS" +
                    "(SELECT game_date, location FROM plays p2 WHERE UPPER(teamName) LIKE UPPER('%"+team2+"%') AND p1.game_date = p2.game_date AND p1.location = p2.location))";
            Object[][] answer = Driver.getInstance().makeQuery(query);

            if(answer.length == 0){
                System.out.println("No game matching between " + team1 + " and " + team2);
            }

            Object[][] ret = new Object[answer.length][answer[0].length + 2];
            for(int i = 0; i <answer.length; i++){
                ret[i][0] = team1;
                ret[i][1] = team2;

                for(int j = 0; j < answer[i].length; j ++){
                    ret[i][j+2] = answer[i][j];
                }

            }

            //System.out.println("to return :" + Arrays.deepToString(ret));

            return ret;
        }
        catch(java.sql.SQLException e){
            System.out.println("Connection failed");
        }

        return null;

        /*
        return null;
        //TODO: REPLACE THIS WITH A QUERY
        //
        // This just searches the games for ones matching them (and then one of them will be selected for more data
        // Input the two teams, returns the two teams as well as the time/date/loc (the key)

        // Format:
        // 0 - team 1
        // 1 - team 2
        // 3 - time
        // 4 - date
        // 5 - loc

        Object[][] o = {{"Canucks","Flames",312,51,"Rogers Arena"},
        {"Maple Leafs","Senators",51,143,"Saddledome"},
        {"Senators","Oilers",1,5,5,6,"Hell Itself"},
        {"Canucks","Flames",5,1,5,3,"Candyland"},
        {"Oilers","Senators",16,3,66,22,"DMP 310"}};
        return o;

        //
        //
        //TODO: REPLACE THIS WITH A QUERY
        */
    }

    public static Object[] getOneGame(String time, String date, String location){
        //TODO: REPLACE THIS WITH A QUERY
        //
        // Given the game's time, date, place, return all data about a game
        //

        String q0_2 = "SELECT T.teamName, T.city, m.name Manager_Name FROM TEAM T INNER JOIN MANAGES ON T.teamName = manages.teamName INNER JOIN manager m ON manages.managerID = m.managerID";

        /*
         0 - Time
         1 - Date
         2 - Location
         3 - Team 1
         4 - Team 2
         5 - Team 1 goals
         6 - Team 2 goals
         7 - [] <- list of referee's names
         8 - [] <- List of team 1's players game stats formatted thusly:
            [player's name, players id/unique key, number of goals, number of assists, number of shots, number of fouls]
         9 - [] <- List of team 2's players game stats formatted thusly:
            [player's name, players id/unique key, number of goals, number of assists, number of shots, number of fouls]
         10 - avg goals per shot
         11 - total assists
         12 - avg assists per game
         13 - total fouls
         14 - avg fouls per game

         IF SENSITIVE DATA IS ALLOWED

         15 - sin
         16 - phone

         REGARDLESS

         17 + will be the games they have played in, formatted like:
         [Time, Date, Location, Team 1, Team 2]

        */
        Object game1 = new Object[] {0,0,"Rogers Arena", "Canucks","Oilers"};
        Object game2 = new Object[] {44,5,"Rogers Arena", "Flames","Canucks"};
        Object[] o = {"Jonathan Der","Canucks",2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,game1,game2};
        return o;

        //
        //
        //TODO: REPLACE THIS WITH A QUERY
    }

    public static void deleteGame(String time, String date, String location){

        try{
            String query = "DELETE FROM game WHERE game_date = '" + parseDate(time,date) + "' AND location = '"+ location + "'";
            Object[][] answer = Driver.getInstance().makeQuery(query);
            System.out.println(Arrays.deepToString(answer));
        }
        catch(java.sql.SQLException e){
            System.out.println("Connection failed");
        }
    }

    public static void addGame(Object[] data){
        // Replace this with a query that adds a game
        // The "data" variable is structured as follows:

        /*
         0 - Time
         1 - Date
         2 - Location
         3 - Team 1
         4 - Team 2
         5 - Team 1 goals
         6 - Team 2 goals
         7 - [] <- list of referee's names
         8 - [] <- List of events  formatted thusly:
            [gametime, player involved, type]           type is 0, 1, 2, or 3, representing: goal, assist, shot, penalty (respecitvely)


         */

        /*Parse the data*/
        String dateTime = parseDate(data[1].toString(), data[0].toString());
        String location = data[2].toString();
        String team1 = data[3].toString();
        String team2 = data[4].toString();
    }

    private static String parseDate(String time, String date){
        return date + " " + time;
    }


}