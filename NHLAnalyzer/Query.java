package NHLAnalyzer;

import java.math.BigDecimal;
import java.sql.SQLException;

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
                    return new Account(((BigDecimal)answer[0][2]).intValue(), (String)answer[0][3], (String)answer[0][4], (String)answer[0][0]);
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
        // TODO: REPLACE THIS WITH A QUERY
        //
        //
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
        //
        //
        // TODO: REPLACE ABOVE WITH A QUERY
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
        //TODO: REPLACE THIS WITH A QUERY
        //
        // Given the player ID, return all data about a player
        //


        String q0_4 = "SELECT PLAYER.NAME, T.TEAMNAME, PLAYER.HEIGHT, PLAYER.WEIGHT, PLAYER.SALARY" +
                "  FROM PLAYER" +
                "    INNER JOIN PLAYSFOR P ON PLAYER.PLAYERID = P.PLAYERID" +
                "    INNER JOIN TEAM T ON P.TEAMNAME = T.TEAMNAME" +
                " WHERE PLAYER.PLAYERID = " + playerID;

        String q11 = "SELECT count(ASSIST.PLAYERID)" +
                "  FROM ASSIST" +
                " WHERE ASSIST.PLAYERID = " + playerID;

        // TODO: THIS PROBABLY DOESN'T WORK
        String q12 = "SELECT COUNT(A.PLAYERID)" +
                "  FROM ASSIST A" +
                "    INNER JOIN EVENT E ON A.GAMETIME = E.GAMETIME AND A.PLAYERID = E.PLAYERID AND A.GAME_DATE = E.GAME_DATE AND A.LOCATION = E.LOCATION" +
                "    INNER JOIN GAME G ON E.GAME_DATE = G.GAME_DATE AND E.LOCATION = G.LOCATION" +
                "  WHERE A.PLAYERID = " + playerID +
                "    GROUP BY G.GAME_DATE, G.LOCATION";

        String q7 = "SELECT COUNT(G.PLAYERID)" +
                "  FROM GOAL G " +
                "WHERE G.PLAYERID = " + playerID;

        String q8 = "SELECT COUNT(GO.PLAYERID)" +
                "  FROM GOAL A" +
                "    INNER JOIN EVENT E ON GO.GAMETIME = E.GAMETIME AND GO.PLAYERID = E.PLAYERID AND GO.GAME_DATE = E.GAME_DATE AND GO.LOCATION = E.LOCATION" +
                "    INNER JOIN GAME G ON E.GAME_DATE = G.GAME_DATE AND E.LOCATION = G.LOCATION" +
                "  WHERE GO.PLAYERID = " + playerID +
                "    GROUP BY G.GAME_DATE, G.LOCATION";

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
         [Time, Date, Location, Team 1, Team 2]

        */

        Object[] playerInfo = null;
        try{
            playerInfo = Driver.getInstance().makeQuery("SELECT PLAYER.NAME, TEAM.TEAMNAME, PLAYER.HEIGHT, PLAYER.WEIGHT, PLAYER.SALARY\n" +
		        "  FROM PLAYER, TEAM " +
		        "WHERE PLAYER.PLAYERID = " + playerID)[0];
        } catch (SQLException e) {
        	// TODO: DO THE ERROR THING
        }
        Object game1 = new Object[] {0,0,"Rogers Arena", "Canucks","Oilers"};
        Object game2 = new Object[] {44,5,"Rogers Arena", "Flames","Canucks"};
        Object[] o = {"Jonathan Der","Canucks",2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,game1,game2};
        return o;

        //
        //
        //TODO: REPLACE THIS WITH A QUERY
    }

    public static Object[][] searchTeams(String teamName){

        //TODO: REPLACE THIS WITH A QUERY
        //
        //
        Object[][] o = {{"Canucks","Vancouver",312,51,42,3,1},
        {"Maple Leafs","Toronto",51,143,6143,5,4},
        {"Senators","Ottawa",1,5,5,6,1},
        {"Oilers","Edmonton",5,1,5,3,2},
        {"Flames","Calgary",16,3,66,22,4}};
        return o;

        //
        //
        //TODO: REPLACE THIS WITH A QUERY
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
        //TODO
        //
        // Replace this with a query that deletes a the game with the local variables as keys
        // This has cascade deletion- when you delete a game, all the associated events with it are deleted too
        //
        // TODO
    }

    public static void addGame(Object[] data){
        //TODO
        //
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
        //
        // TODO
    }


}