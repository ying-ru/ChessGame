package server.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {
	private String user_token;
	private int win;
	private int lose;
	private Connection connection = null;
	private Statement stmt;

	public DataBase() {
		try {
			// Step 1: 載入JDBC驅動程式: 請將正確值填入 ""中
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// Step 2: 取得資料庫連結: 請將正確值填入 ""中
			String url = "jdbc:mysql://sqadb.cuxcf7jbbgaj.ap-northeast-1.rds.amazonaws.com/"
					+ "game_server1?user=d0030683&password=d0030683&useUnicode=true"
					+ "&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false";
			connection = DriverManager.getConnection(url);
//			createTable(); //if NEVER createTable
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void createTable() {
		String createSQL = "CREATE TABLE player_info (" + "user_token char(100), "
				+ "win int(11), " + "lose int(11), " + "PRIMARY KEY (user_token))";
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(createSQL);
		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}
	}
	
	public void insert(String userToken) {
		this.user_token = userToken;
		String insert = "INSERT INTO player_info VALUES ("
				+ "'" + user_token + "', 0, 0)";
		try {
			stmt = connection.createStatement();
			int result = stmt.executeUpdate(insert);
			System.out.println(result + " data was insert");
		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}
	}
	
	public void update(String userToken, int win, int lose) {
		this.user_token = userToken;
		this.win = win;
		this.lose = lose;
		String update = "UPDATE player_info "
				+ " SET win = " + win + ", lose = " + lose
				+ " WHERE user_token = '" + user_token + "'";
		try {
			stmt = connection.createStatement();
			int result = stmt.executeUpdate(update);
			System.out.println(result + " data was update");
		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}
	}
	
	public void delete(String userToken) {
		this.user_token = userToken;
		String delete = "DELETE FROM player_info "
				+ "WHERE user_token = '" + user_token + "'";
		try {
			stmt = connection.createStatement();
			int result = stmt.executeUpdate(delete);
			System.out.println(result + " data was delete");
		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}
	}
	
	//if NOT found userID in database, it will insert this user
	public boolean selectUser(String userToken) { 
		this.user_token = userToken;
		String selectUser = "SELECT user_token FROM player_info "
				+ "WHERE user_token = '" + user_token + "'";
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(selectUser);
			rs.next();
			if (rs.getString(1) == null) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			insert(userToken);
			System.out.println("CreateDB Exception :" + e.toString());
			return false;
		}
		
	}
	
	public int selectWin(String userToken) {
		this.user_token = userToken;
		String selectWin = "SELECT win FROM player_info "
				+ "WHERE user_token = '" + user_token + "'";
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(selectWin);
			rs.next();
			win = Integer.parseInt(rs.getString(1));
			System.out.println(userToken.toString() + " win: " + win);
		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}
		return win;
	}
	
	public int selectLose(String userToken) { 
		this.user_token = userToken;
		String selectLose = "SELECT lose FROM player_info "
				+ "WHERE user_token = '" + user_token + "'";
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(selectLose);
			rs.next();
			lose = Integer.parseInt(rs.getString(1));
			System.out.println(userToken.toString() + " lose: " + rs.getString(1));
		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}
		return lose;
	}
	
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
		}
	}
	
	public static void main(String[] args) {
		DataBase db = new DataBase();
//		db.delete("123");
//		db.delete(null);
//		db.update("123", 0, 1);
//		System.out.println(db.selectUser("123"));
//		db.insert("123");
		db.selectWin("123");
//		db.close();
	}
	
}
