package kr.re.ec.bibim.server.da;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import kr.re.ec.bibim.constants.Constants;
import kr.re.ec.bibim.util.LogUtil;
import kr.re.ec.bibim.vo.UserData;

/**
 * DAO(Data Access Object) for controlling DB<br>
 * UserData
 * 
 * @author Kang Juho
 * @version 1.0
 */

public class UserDataController extends DataAccess{
	
	//for singleton
	private static UserDataController instance = null;
	
	//for singleton
	static{
		try{
			instance = new UserDataController();
		} catch(Exception e){
			throw new RuntimeException("singleton instance intialize error");
		}
	}
	
	//for singleton
	private UserDataController(){
		super(Constants.DataBaseConstantFrame.DB_NAME);
	}
	
	//for singleton
	public static UserDataController getInstance(){
		return instance;
	}
	
	public boolean isTableExists() {
		open();
		Connection c = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		int tableCnt=0;

		try {
			stmt = c.createStatement();

			String query = "SELECT COUNT(name)" +
					" FROM sqlite_master" + 
					" WHERE type='table' AND name='" 
					+ Constants.UserConstantFrame.TABLE_NAME + "'";
			LogUtil.v("query: " + query);
			rs = stmt.executeQuery(query);
			
			
			if(rs.next()) {
				tableCnt = rs.getInt(1);
				LogUtil.v("tableCnt:" + tableCnt);
			}
			
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();			
		}
		
		if(tableCnt==0) { //no table
			return false;
		} else { //table already exist
			return true;
		}
	}
	
	/**
	 * Create table if not exists.
	 * @return success
	 */
	public boolean createTable() { 
		open();
		Connection c = getConnection();
		Statement stmt = null;
		try {
			stmt = c.createStatement();

			String query = "CREATE TABLE IF NOT EXISTS " 
					+ Constants.UserConstantFrame.TABLE_NAME  + " ( " 
					+ Constants.UserConstantFrame.COLUMN_NAME_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
					+ Constants.UserConstantFrame.COLUMN_NAME_USERNAME + " TEXT NOT NULL UNIQUE, " 
					+ Constants.UserConstantFrame.COLUMN_NAME_USERPWD + " TEXT );"; //should not be UNIQUE Deleted UNIQUE
			//this query depends on SQLite
			LogUtil.v("query: " + query);
						
			stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			close();
		}

		return true;
		
	}

	/**
	 * Drop table
	 * @return success
	 */
	public boolean dropTable() { 
		open();
		Connection c = getConnection();
		Statement stmt = null;
		try {
			stmt = c.createStatement();

			String query = "DROP TABLE " + Constants.UserConstantFrame.TABLE_NAME + ";";
			LogUtil.v("query: " + query);
			stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			close();			
		}
		
		return true;
	}
	
	//find
	/**
	 * find all directories.
	 * @return ArrayList<Directory> 
	 */
	public ArrayList<UserData> findAll() { 
		ArrayList<UserData> resultusers = new ArrayList<UserData>();
		
		open();
		Connection c = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = c.createStatement();
			String query = "SELECT " 
					+ Constants.UserConstantFrame.COLUMN_NAME_USERID + ", "
					+ Constants.UserConstantFrame.COLUMN_NAME_USERNAME + ", "
					+ Constants.UserConstantFrame.COLUMN_NAME_USERPWD 
					+" FROM " + Constants.UserConstantFrame.TABLE_NAME + ";";
			LogUtil.v("query: " + query);
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				UserData userdata = new UserData();
				userdata.setUserid(rs.getInt(Constants.UserConstantFrame.COLUMN_NAME_USERID));
				userdata.setName(rs.getString(Constants.UserConstantFrame.COLUMN_NAME_USERNAME));
				userdata.setPassword(rs.getString(Constants.UserConstantFrame.COLUMN_NAME_USERPWD));
				LogUtil.d("userdata :"+ userdata.getUserid()+"\t"+userdata.getName()+"\t"+userdata.getPassword());
				resultusers.add(userdata);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return resultusers;
	}
	
	public int insert(UserData userdata) { 
		int result = 0;
		
		open();
		Connection c = getConnection();
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			String query = "INSERT INTO " 
					+ Constants.UserConstantFrame.TABLE_NAME 
					+ " (" + Constants.UserConstantFrame.COLUMN_NAME_USERNAME + ", "
					+ Constants.UserConstantFrame.COLUMN_NAME_USERPWD + ")" 
					+ " VALUES ('" + userdata.getName() + "','" + userdata.getPassword() + "');";
			LogUtil.v("query: " + query);
			result=stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}
	
	public int deleteByID(int id) { 
		int result = 0;
		
		open();
		Connection c = getConnection();
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			String query = "DELETE FROM " + Constants.UserConstantFrame.TABLE_NAME 
					+ " WHERE " 
					+ Constants.UserConstantFrame.COLUMN_NAME_USERID + "=" + id + ";";
			LogUtil.v("query: " + query);
			result=stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}
		
}