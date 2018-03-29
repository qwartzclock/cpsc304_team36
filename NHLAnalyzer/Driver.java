package NHLAnalyzer;

import java.sql.*;

/**
 * Created by Nathan on 2018-03-26.
 */
public class Driver {

	private static Connection con;
	private static Driver myDriver;

	private Driver() {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException sqle) {
			System.out.println("Driver constructor error: " + sqle.toString());
		}
	}

	public static Driver getInstance() {
		if (myDriver == null) {
			myDriver = new Driver();
			try {
				myDriver.startConnection();
			} catch (SQLException e) {System.out.println(e.toString());}
		}
		return myDriver;
	}

	public void startConnection() throws SQLException{
		try {

			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:ug", "ora_k3o1b", "a30971196");
		} catch (SQLException sqle) {
			System.out.println("Driver open connection error: " + sqle.toString());
			throw sqle;
		}
	}

	// For queries only.
	// Returns a 2D Object array. For Object[i][j], i is the row, j is the column. Beware nulls and data types
	public Object[][] makeQuery(String query) throws SQLException {
		if (query.endsWith(";")){
			query = query.substring(0, query.length() - 1);
		}
		try {
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery(query);



			int nCol = rs.getMetaData().getColumnCount();
			int count = 0;
			while (rs.next()) {
				count++;
			}
			Object[][] resultArr = new Object[count][nCol];
			if (count == 0) {
				return resultArr;
			}
			rs.first();
			do {
				for( int iCol = 1; iCol <= nCol; iCol++ ){
					Object obj = rs.getObject( iCol );
					if (obj == null) {
						resultArr[rs.getRow()-1][iCol-1] = null;
					} else {
						resultArr[rs.getRow()-1][iCol-1] = obj;
					}
				}
			} while( rs.next());
			stmt.close();

			return resultArr;
		} catch (SQLException e) {
			System.out.println("Driver query error: " + e.toString());
			throw e;
		}
	}

	// For CREATE, INSERT, UPDATE, DELETE only.
	public void executeUpdate(String update) throws SQLException {
		if (update.endsWith(";")){
			update = update.substring(0, update.length() - 1);
		}
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(update);
			System.out.println("Successfully updated");
		} catch (SQLException e) {
			System.out.println("Driver update error: " + e.toString());
			throw e;
		}
	}

	public void closeConnection() {
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println("Driver close connection error: " + e.toString());
		}
	}

	// public static Connection getConnection(String url, String userid, String password) throws SQLException


}
