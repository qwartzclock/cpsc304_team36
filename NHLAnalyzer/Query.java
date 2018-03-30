package NHLAnalyzer;

import javax.swing.*;
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
                JOptionPane.showMessageDialog(null, "No user matching " + username, "Log in Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            else if(answer.length != 1){
                JOptionPane.showMessageDialog(null, "Duplicate entry for user " + username, "Log in Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            else{
                if(password.equals((String)answer[0][1])){
                    String name = (answer[0][4] == null) ? null : answer[0][4].toString();
                    String team = (answer[0][3] == null) ? null : answer[0][3].toString();
                    return new Account(((BigDecimal)answer[0][2]).intValue(), name, team, answer[0][0].toString());
                }
                else{
                    JOptionPane.showMessageDialog(null, "Password not matching data for user " + username + ". Please retry", "Log in Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        catch(java.sql.SQLException e){
            JOptionPane.showMessageDialog(null, "Connection failed, details : " + e.toString(), "Log in Error / SQL Failure",
                    JOptionPane.ERROR_MESSAGE);
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

            JOptionPane.showMessageDialog(null, "Error with the query : " + e.toString(), "Querry error",
                    JOptionPane.ERROR_MESSAGE);
		}
        /*Object[][] o = {{"Jonathan Der","id5",},
                {"Miayaz Nahh","id6"},
              {"Dokidok Iredian","id7"},
              {"Natalia Dddder","id8"},
               {"Noncertified Miayana","id9"}};*/

        return result;
    }

    public static Object[] getOnePlayer(String playerID, boolean sensitive){

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
                JOptionPane.showMessageDialog(null, "No team with the name " + teamName + " was found in our records", "No results",
                        JOptionPane.ERROR_MESSAGE);
            }

            return answer;
        }
        catch(java.sql.SQLException e){

            JOptionPane.showMessageDialog(null, "Connection failed, details : " + e.toString(), "Log in Error / SQL Failure",
                    JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    public static Object[] getOneTeam(String teamName){

        try{
            String query = "SELECT teamName, city FROM team WHERE UPPER(teamName) = UPPER('" + teamName + "')";
            Object[][] answer = Driver.getInstance().makeQuery(query);

            if(answer.length == 0){
                JOptionPane.showMessageDialog(null, "No team with the name " + teamName + " was found in our records", "No results",
                        JOptionPane.ERROR_MESSAGE);
            }
            else if(answer.length != 1){
                JOptionPane.showMessageDialog(null, "Multiple values for " + teamName + " were found in our records", "SQL Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            else{
                String city = answer[0][1].toString();
                query = "SELECT mm.name FROM MANAGES m, team t, MANAGER mm WHERE m.MANAGERID = mm.MANAGERID AND t.teamName = m.teamName AND t.teamName = '" + teamName + "'";
                answer = Driver.getInstance().makeQuery(query);

                if(answer.length == 1) {
                    String manager = answer[0][0].toString();
                    query = "SELECT COUNT(*) FROM GAME G WHERE\n" +
                            "  (SELECT COUNT(*) FROM GOAL go\n" +
                            "    INNER JOIN EVENT e ON e.GAME_DATE = go.GAME_DATE AND e.LOCATION = go.LOCATION AND e.PLAYERID = go.PLAYERID AND e.GAMETIME = go.GAMETIME\n" +
                            "    INNER JOIN PLAYSFOR pf ON go.PLAYERID = pf.PLAYERID\n" +
                            "    INNER JOIN PLAYS p1 ON p1.LOCATION = go.LOCATION AND p1.GAME_DATE = go.GAME_DATE\n" +
                            "    INNER JOIN PLAYS p2 ON p2.LOCATION = go.LOCATION AND p2.GAME_DATE = go.GAME_DATE\n" +
                            "    INNER JOIN TEAM t1 ON t1.TEAMNAME = p1.TEAMNAME\n" +
                            "    INNER JOIN TEAM t2 ON t2.TEAMNAME = p2.TEAMNAME\n" +
                            "  WHERE t1.TEAMNAME = '" + teamName + "' and t2.TEAMNAME != t1.TEAMNAME AND t1.TEAMNAME = pf.TEAMNAME AND go.GAME_DATE = g.GAME_DATE AND go.LOCATION = g.LOCATION)\n" +
                            "   > ANY (SELECT COUNT(*) FROM GOAL go\n" +
                            "  INNER JOIN EVENT e ON e.GAME_DATE = go.GAME_DATE AND e.LOCATION = go.LOCATION AND e.PLAYERID = go.PLAYERID AND e.GAMETIME = go.GAMETIME\n" +
                            "  INNER JOIN PLAYSFOR pf ON go.PLAYERID = pf.PLAYERID\n" +
                            "  INNER JOIN PLAYS p1 ON p1.LOCATION = go.LOCATION AND p1.GAME_DATE = go.GAME_DATE\n" +
                            "  INNER JOIN PLAYS p2 ON p2.LOCATION = go.LOCATION AND p2.GAME_DATE = go.GAME_DATE\n" +
                            "  INNER JOIN TEAM t1 ON t1.TEAMNAME = p1.TEAMNAME\n" +
                            "  INNER JOIN TEAM t2 ON t2.TEAMNAME = p2.TEAMNAME\n" +
                            "  WHERE t1.TEAMNAME = '" + teamName + "' and t2.TEAMNAME != t1.TEAMNAME AND t2.TEAMNAME = pf.TEAMNAME AND go.GAME_DATE = g.GAME_DATE AND go.LOCATION = g.LOCATION);";
                    answer = Driver.getInstance().makeQuery(query);
                    int won = ((BigDecimal)answer[0][0]).intValue();

                    query = "SELECT COUNT(*) FROM GAME G WHERE\n" +
                            "  (SELECT COUNT(*) FROM GOAL go\n" +
                            "    INNER JOIN EVENT e ON e.GAME_DATE = go.GAME_DATE AND e.LOCATION = go.LOCATION AND e.PLAYERID = go.PLAYERID AND e.GAMETIME = go.GAMETIME\n" +
                            "    INNER JOIN PLAYSFOR pf ON go.PLAYERID = pf.PLAYERID\n" +
                            "    INNER JOIN PLAYS p1 ON p1.LOCATION = go.LOCATION AND p1.GAME_DATE = go.GAME_DATE\n" +
                            "    INNER JOIN PLAYS p2 ON p2.LOCATION = go.LOCATION AND p2.GAME_DATE = go.GAME_DATE\n" +
                            "    INNER JOIN TEAM t1 ON t1.TEAMNAME = p1.TEAMNAME\n" +
                            "    INNER JOIN TEAM t2 ON t2.TEAMNAME = p2.TEAMNAME\n" +
                            "  WHERE t1.TEAMNAME = '" + teamName + "' and t2.TEAMNAME != t1.TEAMNAME AND t1.TEAMNAME = pf.TEAMNAME AND go.GAME_DATE = g.GAME_DATE AND go.LOCATION = g.LOCATION)\n" +
                            "   < ANY (SELECT COUNT(*) FROM GOAL go\n" +
                            "  INNER JOIN EVENT e ON e.GAME_DATE = go.GAME_DATE AND e.LOCATION = go.LOCATION AND e.PLAYERID = go.PLAYERID AND e.GAMETIME = go.GAMETIME\n" +
                            "  INNER JOIN PLAYSFOR pf ON go.PLAYERID = pf.PLAYERID\n" +
                            "  INNER JOIN PLAYS p1 ON p1.LOCATION = go.LOCATION AND p1.GAME_DATE = go.GAME_DATE\n" +
                            "  INNER JOIN PLAYS p2 ON p2.LOCATION = go.LOCATION AND p2.GAME_DATE = go.GAME_DATE\n" +
                            "  INNER JOIN TEAM t1 ON t1.TEAMNAME = p1.TEAMNAME\n" +
                            "  INNER JOIN TEAM t2 ON t2.TEAMNAME = p2.TEAMNAME\n" +
                            "  WHERE t1.TEAMNAME = '" + teamName + "' and t2.TEAMNAME != t1.TEAMNAME AND t2.TEAMNAME = pf.TEAMNAME AND go.GAME_DATE = g.GAME_DATE AND go.LOCATION = g.LOCATION);";
                    answer = Driver.getInstance().makeQuery(query);
                    int lost = ((BigDecimal)answer[0][0]).intValue();

                    query = "SELECT p.NAME, p.PLAYERID FROM PLAYER p\n" +
                            "INNER JOIN PLAYSFOR pf ON p.PLAYERID = pf.PLAYERID\n" +
                            "INNER JOIN TEAM t ON pf.TEAMNAME = t.TEAMNAME\n" +
                            "WHERE t.TEAMNAME = 'Canucks'";
                    answer = Driver.getInstance().makeQuery(query);
                    Object[][] players = answer;

                    double ratio = 0;
                    if(lost == 0){
                        ratio = won;
                    }
                    else{
                        ratio = ((double)won)/lost;
                    }
                    Object ret[] = {teamName, city, manager, won, ratio, players};
                    return ret;

                }
                else{
                    JOptionPane.showMessageDialog(null, answer.length + " values for the manager were found instead of 1", "SQL Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }


            return answer;
        }
        catch(java.sql.SQLException e){

            JOptionPane.showMessageDialog(null, "Connection failed, details : " + e.toString(), "Log in Error / SQL Failure",
                    JOptionPane.ERROR_MESSAGE);
        }




        // 0 - team name
        // 1 - team location
        // 2 - manager name
        // 4 - wins
        // 5 - win/loss ratio
        // 6+ - arrays of players [] formatted as: [player name, player ID/key]

        Object[] o = {"Canucks","Vancouver","Tommy Thomaspon",5,1.5,new Object[]{"Jonathan Der","id5"}};
        return null;

        //
        //
        //TODO: REPLACE THIS WITH A QUERY
    }

    public static Object[][] searchGames(String team1, String team2){

        try{
            String query = "SELECT game_date,location FROM game g1 WHERE (g1.GAME_DATE,g1.LOCATION) IN"
                            +"(SELECT game_date, location FROM plays p1 WHERE UPPER(teamName) LIKE UPPER('%" +team1 +"%') AND (p1.GAME_DATE,p1.LOCATION) IN"
                            +"(SELECT game_date, location FROM plays p2 WHERE UPPER(teamName) LIKE UPPER('%" +team2+ "%') AND p1.game_date = p2.game_date AND p1.location = p2.location))";
            Object[][] answer = Driver.getInstance().makeQuery(query);

            if(answer.length == 0){
                JOptionPane.showMessageDialog(null, "No game between " + team1 + " and " + team2 + " were found in our records", "No results",
                        JOptionPane.ERROR_MESSAGE);
            }else {

                Object[][] ret = new Object[answer.length][answer[0].length + 2];
                for (int i = 0; i < answer.length; i++) {
                    ret[i][0] = team1;
                    ret[i][1] = team2;

                    for (int j = 0; j < answer[i].length; j++) {
                        ret[i][j + 2] = answer[i][j];
                    }

                }

                System.out.println(team1 + " " + team2 + "to return :" + Arrays.deepToString(ret));

                return ret;
            }
        }
        catch(java.sql.SQLException e){
            JOptionPane.showMessageDialog(null, "Connection failed, details : " + e.toString(), "Log in Error / SQL Failure",
                    JOptionPane.ERROR_MESSAGE);
        }

        return new Object[0][0];

    }

    public static Object[] getOneGame(String time, String date, String location){
        //TODO: REPLACE THIS WITH A QUERY
        //
        // Given the game's time, date, place, return all data about a game
        //

        //String q0_2 = "SELECT T.teamName, T.city, m.name Manager_Name FROM TEAM T INNER JOIN MANAGES ON T.teamName = manages.teamName INNER JOIN manager m ON manages.managerID = m.managerID";

        String q0_4 = "SELECT G.GAME_DATE, G.LOCATION, P.TEAMNAME, P2.TEAMNAME\n" +
                "FROM GAME G\n" +
                "  INNER JOIN PLAYS P ON G.GAME_DATE = P.GAME_DATE AND G.LOCATION = P.LOCATION\n" +
                "  INNER JOIN PLAYS P2 ON G.GAME_DATE = P2.GAME_DATE AND G.LOCATION = P2.LOCATION\n" +
                "WHERE P.TEAMNAME < P2.TEAMNAME AND G.GAME_DATE = TO_DATE('" + parseDate(time, date) + "', 'YYYY-MM-DD HH24:MI:SS') AND UPPER(G.LOCATION) = UPPER('" + location +"')";

        String q5 = "SELECT G.GAME_DATE, G.LOCATION, P.TEAMNAME, P2.TEAMNAME, COUNT(*)\n" +
                "  FROM GAME G\n" +
                "  INNER JOIN PLAYS P ON G.GAME_DATE = P.GAME_DATE AND G.LOCATION = P.LOCATION\n" +
                "  INNER JOIN PLAYS P2 ON G.GAME_DATE = P2.GAME_DATE AND G.LOCATION = P2.LOCATION\n" +
                "    INNER JOIN EVENT E ON G.GAME_DATE = E.GAME_DATE AND G.LOCATION = E.LOCATION\n" +
                "    INNER JOIN GOAL G2 ON E.GAMETIME = G2.GAMETIME AND E.PLAYERID = G2.PLAYERID AND E.GAME_DATE = G2.GAME_DATE AND E.LOCATION = G2.LOCATION\n" +
                "    INNER JOIN PLAYER P3 ON E.PLAYERID = P3.PLAYERID\n" +
                "    INNER JOIN PLAYSFOR P4 ON P3.PLAYERID = P4.PLAYERID\n" +
                "  WHERE P2.TEAMNAME != P.TEAMNAME AND P.TEAMNAME < P2.TEAMNAME\n" +
                "    AND P.TEAMNAME = P4.TEAMNAME AND G.GAME_DATE = TO_DATE('" + parseDate(time, date) + "', 'YYYY-MM-DD HH24:MI:SS') AND UPPER(G.LOCATION) = UPPER('" + location + "')\n" +
                "GROUP BY G.GAME_DATE, G.LOCATION, P.TEAMNAME, P2.TEAMNAME";

        String q6 = "SELECT G.GAME_DATE, G.LOCATION, P.TEAMNAME, P2.TEAMNAME, COUNT(*)\n" +
                "  FROM GAME G\n" +
                "  INNER JOIN PLAYS P ON G.GAME_DATE = P.GAME_DATE AND G.LOCATION = P.LOCATION\n" +
                "  INNER JOIN PLAYS P2 ON G.GAME_DATE = P2.GAME_DATE AND G.LOCATION = P2.LOCATION\n" +
                "    INNER JOIN EVENT E ON G.GAME_DATE = E.GAME_DATE AND G.LOCATION = E.LOCATION\n" +
                "    INNER JOIN GOAL G2 ON E.GAMETIME = G2.GAMETIME AND E.PLAYERID = G2.PLAYERID AND E.GAME_DATE = G2.GAME_DATE AND E.LOCATION = G2.LOCATION\n" +
                "    INNER JOIN PLAYER P3 ON E.PLAYERID = P3.PLAYERID\n" +
                "    INNER JOIN PLAYSFOR P4 ON P3.PLAYERID = P4.PLAYERID\n" +
                "  WHERE P2.TEAMNAME != P.TEAMNAME AND P.TEAMNAME < P2.TEAMNAME\n" +
                "    AND P2.TEAMNAME = P4.TEAMNAME AND G.GAME_DATE = TO_DATE('" + parseDate(time, date) + "', 'YYYY-MM-DD HH24:MI:SS') AND UPPER(G.LOCATION) = UPPER('" + location + "')\n" +
                "GROUP BY G.GAME_DATE, G.LOCATION, P.TEAMNAME, P2.TEAMNAME";

        String q7 = "SELECT R2.NAME\n" +
                "FROM GAME G\n" +
                "  INNER JOIN REFEREES R ON G.GAME_DATE = R.GAME_DATE AND G.LOCATION = R.LOCATION\n" +
                "  INNER JOIN REFEREE R2 ON R.REF_NUMBER = R2.REF_NUMBER\n" +
                "WHERE G.GAME_DATE = TO_DATE('" + parseDate(time, date) + "', 'YYYY-MM-DD HH24:MI:SS') AND UPPER(G.LOCATION) = UPPER('" + location + "')";

        String q8 = "  SELECT P4.NAME, P4.PLAYERID, COUNT(G2.PLAYERID), COUNT(A2.PLAYERID), COUNT(P5.PLAYERID)\n" +
                "  FROM GAME G\n" +
                "    INNER JOIN PLAYS P ON G.GAME_DATE = P.GAME_DATE AND G.LOCATION = P.LOCATION\n" +
                "    INNER JOIN PLAYS P2 ON G.GAME_DATE = P2.GAME_DATE AND G.LOCATION = P2.LOCATION\n" +
                "    INNER JOIN TEAM T ON P.TEAMNAME = T.TEAMNAME\n" +
                "    INNER JOIN PLAYSFOR P3 ON T.TEAMNAME = P3.TEAMNAME\n" +
                "    INNER JOIN PLAYER P4 ON P3.PLAYERID = P4.PLAYERID\n" +
                "    LEFT JOIN EVENT E ON P4.PLAYERID = E.PLAYERID\n" +
                "    LEFT JOIN GOAL G2 ON E.GAMETIME = G2.GAMETIME AND E.PLAYERID = G2.PLAYERID AND E.GAME_DATE = G2.GAME_DATE AND E.LOCATION = G2.LOCATION\n" +
                "    LEFT JOIN ASSIST A2 ON E.GAMETIME = A2.GAMETIME AND E.PLAYERID = A2.PLAYERID AND E.GAME_DATE = A2.GAME_DATE AND E.LOCATION = A2.LOCATION\n" +
                "    LEFT JOIN PENALTY P5 ON E.GAMETIME = P5.GAMETIME AND E.PLAYERID = P5.PLAYERID AND E.GAME_DATE = P5.GAME_DATE AND E.LOCATION = P5.LOCATION\n" +
                "  WHERE P.TEAMNAME < P2.TEAMNAME AND G.GAME_DATE = TO_DATE('" + parseDate(time, date) + "', 'YYYY-MM-DD HH24:MI:SS') AND UPPER(G.LOCATION) = UPPER('" + location + "')\n" +
                "      GROUP BY P4.NAME, P4.PLAYERID\n" +
                "ORDER BY P4.PLAYERID";

        String q9 = "SELECT P4.NAME, P4.PLAYERID\n" +
                "FROM GAME G\n" +
                "  INNER JOIN PLAYS P ON G.GAME_DATE = P.GAME_DATE AND G.LOCATION = P.LOCATION\n" +
                "  INNER JOIN PLAYS P2 ON G.GAME_DATE = P2.GAME_DATE AND G.LOCATION = P2.LOCATION\n" +
                "  INNER JOIN TEAM T ON P2.TEAMNAME = T.TEAMNAME\n" +
                "  INNER JOIN PLAYSFOR P3 ON T.TEAMNAME = P3.TEAMNAME\n" +
                "  INNER JOIN PLAYER P4 ON P3.PLAYERID = P4.PLAYERID\n" +
                "WHERE P.TEAMNAME < P2.TEAMNAME AND G.GAME_DATE = TO_DATE('" + parseDate(time, date) + "', 'YYYY-MM-DD HH24:MI:SS') AND UPPER(G.LOCATION) = UPPER('" + location + "')";

        Object[] rFinal = new Object[9];

        try {
            Object[] r0_4 = Driver.getInstance().makeQuery(q0_4)[0];
            rFinal[0] = r0_4[0];
            rFinal[1] = r0_4[1];
            rFinal[2] = r0_4[2];
            rFinal[3] = r0_4[3];
            rFinal[4] = r0_4[4];

            Object[][] r5a = Driver.getInstance().makeQuery(q5);
            int r5 = 0;
            if (r5a.length == 0) { r5 = 0; }
            else {r5 = ((BigDecimal)r5a[0][4]).intValue();}
            rFinal[5] = r0_4[5];

            Object[][] r6a = Driver.getInstance().makeQuery(q6);
            int r6 = 0;
            if (r6a.length == 0) { r6 = 0; }
            else {r6 = ((BigDecimal)r6a[0][4]).intValue();}
            rFinal[6] = r0_4[6];

            Object[][] r7a = Driver.getInstance().makeQuery(q7);
            Object[] r7 = new Object[r7a.length];
            for (int i = 0; i < r7a.length; i++) {
                r7[i] = r7a[i][0];
            }
            rFinal[7] = r0_4[7];

            Object[][] r8 = Driver.getInstance().makeQuery(q8);
            rFinal[8] = r0_4[8];
            Object[][] r9 = Driver.getInstance().makeQuery(q9);
            rFinal[9] = r0_4[9];

        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return rFinal;

        //String query = "SELECT t1.name, t2.name FROM "
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
            [player's name, players id/unique key, number of goals, number of assists, number of fouls]
         9 - [] <- List of team 2's players game stats formatted thusly:
            [player's name, players id/unique key, number of goals, number of assists, number of fouls]

        */
        /*Object game1 = new Object[] {0,0,"Rogers Arena", "Canucks","Oilers"};
        Object game2 = new Object[] {44,5,"Rogers Arena", "Flames","Canucks"};
        Object[] o = {"Jonathan Der","Canucks",2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,game1,game2};
        return o;
        */
        //
        //
        //TODO: REPLACE THIS WITH A QUERY
//        Object ret[] = {(Object)time, (Object)date, (Object)location,"",""};
//        return ret;
    }

    public static void deleteGame(String date, String location){

        try{
            String query = "DELETE FROM game WHERE game_date = TO_DATE('" + date.substring(0,date.length() - 2) + "','yyyy-mm-dd hh24:mi:ss') AND location = '"+ location + "'";
            Object[][] answer = Driver.getInstance().makeQuery(query);
            System.out.println(Arrays.deepToString(answer));
        }
        catch(java.sql.SQLException e){
            JOptionPane.showMessageDialog(null, "Connection failed, details : " + e.toString(), "Log in Error / SQL Failure",
                    JOptionPane.ERROR_MESSAGE);
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
            [gametime, player involved id, type]           type is 0, 1, 2, or 3, representing: goal, assist, shot, penalty (respecitvely)


         */

        /*Parse the data*/
        String dateTime = parseDate(data[1].toString(), data[0].toString());
        String location = data[2].toString();
        String team1 = data[3].toString();
        String team2 = data[4].toString();
        String[] refs = (String[]) data[7]; // Oh jesus please let this work
         if (false){
                try{
                    Driver.getInstance().executeUpdate("\n" +
                        "INSERT INTO GAME(GAME_DATE,LOCATION)\n" +
                        "VALUES ('" + dateTime + "', '" + location + "')");

                    Driver.getInstance().executeUpdate("INSERT INTO PLAYS(GAME_DATE,LOCATION,TEAMNAME) VALUES" +
                            " ('" + dateTime + "', '" + location + "','" + team1 + "'");

                    Driver.getInstance().executeUpdate("INSERT INTO PLAYS(GAME_DATE,LOCATION,TEAMNAME) VALUES" +
                            " ('" + dateTime + "', '" + location + "','" + team2 + "'");

                    Driver.getInstance().executeUpdate("\n" +
                            "INSERT INTO REFEREES(GAME_DATE, LOCATION, REF_NUMBER)\n" +
                            "VALUES ('" + dateTime + "', '" + location + "', 7);");
                } catch (SQLException e) {
                    System.out.println(e.toString());
                }
        }
    }

    private static String parseDate(String time, String date){
        return date + " " + time;
    }


}