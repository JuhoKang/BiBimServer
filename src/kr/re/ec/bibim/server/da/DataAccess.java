package kr.re.ec.bibim.server.da;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.sqlite.SQLiteConfig;

/**
 * Data Access Object for SQLite 
 * @author Kim Taehee
 * @since 140504
 */
public class DataAccess {
	private Connection connection;
	private String dbFileName;
	private boolean isOpened = false;

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch(Exception e) { 
			e.printStackTrace(); 
		}
	}

	protected DataAccess(String databaseFileName) {
		this.dbFileName = databaseFileName;
	}

	protected boolean open() {
		//LogUtil.v("open called"); 
		try {
			SQLiteConfig config = new SQLiteConfig();
			config.setReadOnly(false);
			connection = DriverManager.getConnection("jdbc:sqlite:./db/" + this.dbFileName, config.toProperties());
		} catch(SQLException e) { 
			e.printStackTrace();
			return false;
		}

		isOpened = true;
		return true;
	}


	protected boolean close() {
		//LogUtil.v("close called");
		if(this.isOpened == false) {
			return true;
		}

		try {
			connection.close();
		} catch(SQLException e) { 
			e.printStackTrace(); return false; 
		}
		return true;
	}
	
	protected Connection getConnection() {
		return connection;
	}
}
