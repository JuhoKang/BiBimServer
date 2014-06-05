package kr.re.ec.bibim.server.da;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import kr.re.ec.bibim.constants.Constants;
import kr.re.ec.bibim.util.LogUtil;
import kr.re.ec.bibim.vo.FolderData;
import kr.re.ec.bibim.vo.UserData;

/**
 * DAO(Data Access Object) for controlling DB<br>
 * FolderData
 * 
 * @author Kang Juho
 * @version 1.0
 */

public class FolderDataController extends DataAccess {
	// for singleton
	private static FolderDataController instance = null;

	// for singleton
	static {
		try {
			instance = new FolderDataController();
		} catch (Exception e) {
			throw new RuntimeException("singleton instance intialize error");
		}
	}

	// for singleton
	private FolderDataController() {
		super(Constants.DataBaseConstantFrame.DB_NAME);
	}

	// for singleton
	public static FolderDataController getInstance() {
		return instance;
	}

	public boolean isTableExists() {
		open();
		Connection c = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		int tableCnt = 0;

		try {
			stmt = c.createStatement();

			String query = "SELECT COUNT(name)" + " FROM sqlite_master"
					+ " WHERE type='table' AND name='"
					+ Constants.FolderConstantFrame.TABLE_NAME + "'";
			LogUtil.v("query: " + query);
			rs = stmt.executeQuery(query);

			if (rs.next()) {
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

		if (tableCnt == 0) { // no table
			return false;
		} else { // table already exist
			return true;
		}
	}

	/**
	 * Create table if not exists.
	 * 
	 * @return success
	 */
	
	//TODO delete UNIQUE in folder name, use folder name with selecting by id,and seperating by user id
	public boolean createTable() {
		open();
		Connection c = getConnection();
		Statement stmt = null;
		try {
			stmt = c.createStatement();

			String query = "CREATE TABLE IF NOT EXISTS "
					+ Constants.FolderConstantFrame.TABLE_NAME + " ( "
					+ Constants.FolderConstantFrame.COLUMN_NAME_FOLDERID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ Constants.FolderConstantFrame.COLUMN_NAME_FOLDERNAME
					+ " TEXT NOT NULL UNIQUE, "
					+ Constants.FolderConstantFrame.COLUMN_NAME_USERID
					+ " INTEGER NOT NULL, " + "FOREIGN KEY("
					+ Constants.FolderConstantFrame.COLUMN_NAME_USERID + ")"
					+ "REFERENCES " + Constants.UserConstantFrame.TABLE_NAME
					+ "(" + Constants.UserConstantFrame.COLUMN_NAME_USERID
					+ ")" + " );"; // should not be UNIQUE Deleted UNIQUE
			// this query depends on SQLite
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
	 * 
	 * @return success
	 */
	public boolean dropTable() {
		open();
		Connection c = getConnection();
		Statement stmt = null;
		try {
			stmt = c.createStatement();

			String query = "DROP TABLE "
					+ Constants.FolderConstantFrame.TABLE_NAME + ";";
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

	// find
	/**
	 * find all directories.
	 * 
	 * @return ArrayList<Directory>
	 */
	public ArrayList<FolderData> findAll() {
		ArrayList<FolderData> resultfolders = new ArrayList<FolderData>();

		open();
		Connection c = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = c.createStatement();
			String query = "SELECT "
					+ Constants.FolderConstantFrame.COLUMN_NAME_FOLDERID + ", "
					+ Constants.FolderConstantFrame.COLUMN_NAME_FOLDERNAME
					+ ", " + Constants.FolderConstantFrame.COLUMN_NAME_USERID
					+ " FROM " + Constants.FolderConstantFrame.TABLE_NAME + ";";
			LogUtil.v("query: " + query);
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				FolderData folderdata = new FolderData();
				folderdata
						.setFolderid(rs
								.getInt(Constants.FolderConstantFrame.COLUMN_NAME_FOLDERID));
				folderdata
						.setName(rs
								.getString(Constants.FolderConstantFrame.COLUMN_NAME_FOLDERNAME));
				folderdata
						.setUserid(rs
								.getInt(Constants.FolderConstantFrame.COLUMN_NAME_USERID));
				LogUtil.d("userdata :" + folderdata.getFolderid() + "\t"
						+ folderdata.getName() + "\t" + folderdata.getUserid());
				resultfolders.add(folderdata);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return resultfolders;
	}
	
	// find
		/**
		 * find all directories.
		 * 
		 * @return ArrayList<Directory>
		 */
		public ArrayList<FolderData> findFolderListById(int id) {
			ArrayList<FolderData> resultfolders = new ArrayList<FolderData>();

			open();
			Connection c = getConnection();
			Statement stmt = null;
			ResultSet rs = null;
			try {
				stmt = c.createStatement();
				String query = "SELECT "
						+ Constants.FolderConstantFrame.COLUMN_NAME_FOLDERID + ", "
						+ Constants.FolderConstantFrame.COLUMN_NAME_FOLDERNAME
						+ ", " + Constants.FolderConstantFrame.COLUMN_NAME_USERID
						+ " FROM " + Constants.FolderConstantFrame.TABLE_NAME
						+ " WHERE "+ Constants.FolderConstantFrame.COLUMN_NAME_USERID + "=" +"'"+ id +"'"+  ";";
				LogUtil.v("query: " + query);
				rs = stmt.executeQuery(query);

				while (rs.next()) {
					FolderData folderdata = new FolderData();
					folderdata
							.setFolderid(rs
									.getInt(Constants.FolderConstantFrame.COLUMN_NAME_FOLDERID));
					folderdata
							.setName(rs
									.getString(Constants.FolderConstantFrame.COLUMN_NAME_FOLDERNAME));
					folderdata
							.setUserid(rs
									.getInt(Constants.FolderConstantFrame.COLUMN_NAME_USERID));
					LogUtil.d("userdata :" + folderdata.getFolderid() + "\t"
							+ folderdata.getName() + "\t" + folderdata.getUserid());
					resultfolders.add(folderdata);
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close();
			}

			return resultfolders;
		}
	
	public FolderData findByName(String name) { 
		FolderData resultfolder = new FolderData();
		
		open();
		Connection c = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = c.createStatement();
			String query = "SELECT " 
					+ Constants.FolderConstantFrame.COLUMN_NAME_FOLDERID + ", "
					+ Constants.FolderConstantFrame.COLUMN_NAME_FOLDERNAME + ", "
					+ Constants.FolderConstantFrame.COLUMN_NAME_USERID 
					+" FROM " + Constants.FolderConstantFrame.TABLE_NAME
					+" WHERE "+ Constants.FolderConstantFrame.COLUMN_NAME_FOLDERNAME + "=" +"'"+ name +"'"+ ";";
			LogUtil.v("query: " + query);
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				resultfolder.setFolderid(rs.getInt(Constants.FolderConstantFrame.COLUMN_NAME_FOLDERID));
				resultfolder.setUserid(rs.getInt(Constants.FolderConstantFrame.COLUMN_NAME_FOLDERNAME));
				resultfolder.setName(rs.getString(Constants.FolderConstantFrame.COLUMN_NAME_USERID));
				
				LogUtil.d("userdata :"+ resultfolder.getFolderid()+"\t"+resultfolder.getName()+"\t"+resultfolder.getUserid());
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		} finally {
			close();
		}
		
		return resultfolder;
	}

	public int insert(FolderData folderdata) {
		int result = 0;

		open();
		Connection c = getConnection();
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			String query = "INSERT INTO "
					+ Constants.FolderConstantFrame.TABLE_NAME + " ("
					+ Constants.FolderConstantFrame.COLUMN_NAME_FOLDERNAME
					+ ", " + Constants.FolderConstantFrame.COLUMN_NAME_USERID
					+ ")" + " VALUES ('" + folderdata.getName() + "','"
					+ folderdata.getUserid() + "');";
			LogUtil.v("query: " + query);
			result = stmt.executeUpdate(query);
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
			String query = "DELETE FROM "
					+ Constants.FolderConstantFrame.TABLE_NAME + " WHERE "
					+ Constants.FolderConstantFrame.COLUMN_NAME_FOLDERID + "="
					+ id + ";";
			LogUtil.v("query: " + query);
			result = stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return result;
	}

}
