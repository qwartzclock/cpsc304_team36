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
		}
		return myDriver;
	}

	public static void startConnection() throws SQLException{
		try {
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1522:ug", "ora_y1u0b", "a31618151");

		} catch (SQLException sqle) {
			System.out.println("Driver open connection error: " + sqle.toString());
			throw sqle;
		}
	}

	public static Object[][] makeQuery(String query) throws SQLException {
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);


			int nCol = rs.getMetaData().getColumnCount();
			Object[][] resultArr = new Object[rs.getFetchSize()][nCol];
			while( rs.next()) {
				for( int iCol = 1; iCol <= nCol; iCol++ ){
					Object obj = rs.getObject( iCol );
					if (obj == null) {
						resultArr[rs.getRow()][iCol-1] = null;
					} else {
						resultArr[rs.getRow()][iCol-1] = obj;
					}
				}
			}
			stmt.close();

			return resultArr;
		} catch (SQLException e) {
			System.out.println("Driver query error: " + e.toString());
			throw e;
		}
	}

	public static void closeConnection() {
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println("Driver close connection error: " + e.toString());
		}
	}

	// public static Connection getConnection(String url, String userid, String password) throws SQLException


}
