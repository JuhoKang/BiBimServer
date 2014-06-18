package kr.re.ec.bibim.server.da;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import kr.re.ec.bibim.constants.Constants;
import kr.re.ec.bibim.util.LogUtil;
import kr.re.ec.bibim.vo.NoteData;

/**
 * DAO(Data Access Object) for controlling DB<br>
 * NoteData
 * 
 * @author Kang Juho
 * @version 1.0
 */

public class NoteDataController extends DataAccess {

	// for singleton
	private static NoteDataController instance = null;

	// for singleton
	static {
		try {
			instance = new NoteDataController();
		} catch (Exception e) {
			throw new RuntimeException("singleton instance intialize error");
		}
	}

	// for singleton
	private NoteDataController() {
		super(Constants.DataBaseConstantFrame.DB_NAME);
	}

	// for singleton
	public static NoteDataController getInstance() {
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
					+ Constants.NoteConstantFrame.TABLE_NAME + "'";
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
	public boolean createTable() {
		open();
		Connection c = getConnection();
		Statement stmt = null;
		try {
			stmt = c.createStatement();

			String query = "CREATE TABLE IF NOT EXISTS "
					+ Constants.NoteConstantFrame.TABLE_NAME + " ( "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTEID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTETITLE
					+ " TEXT , "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTECONTENT
					+ " TEXT , "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTEDATE
					+ " TEXT , "
					+ Constants.NoteConstantFrame.COLUMN_NAME_FOLDERID
					+ " INTEGER NOT NULL, "
					+ Constants.NoteConstantFrame.COLUMN_NAME_USERID
					+ " INTEGER NOT NULL, " + "FOREIGN KEY("
					+ Constants.NoteConstantFrame.COLUMN_NAME_FOLDERID + ")"
					+ "REFERENCES " + Constants.FolderConstantFrame.TABLE_NAME
					+ "(" + Constants.FolderConstantFrame.COLUMN_NAME_FOLDERID
					+ ")" + "FOREIGN KEY("
					+ Constants.NoteConstantFrame.COLUMN_NAME_USERID + ")"
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
					+ Constants.NoteConstantFrame.TABLE_NAME + ";";
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
	public ArrayList<NoteData> findAll() {
		ArrayList<NoteData> resultnotes = new ArrayList<NoteData>();

		open();
		Connection c = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = c.createStatement();
			String query = "SELECT "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTEID + ", "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTETITLE + ", "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTETITLE + ", "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTEDATE + ", "
					+ Constants.NoteConstantFrame.COLUMN_NAME_FOLDERID + ", "
					+ Constants.NoteConstantFrame.COLUMN_NAME_USERID + " FROM "
					+ Constants.UserConstantFrame.TABLE_NAME + ";";
			LogUtil.v("query: " + query);
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				NoteData notedata = new NoteData();
				notedata.setNoteid(rs
						.getInt(Constants.NoteConstantFrame.COLUMN_NAME_NOTEID));
				notedata.setTitle(rs
						.getString(Constants.NoteConstantFrame.COLUMN_NAME_NOTETITLE));
				notedata.setContent(rs
						.getString(Constants.NoteConstantFrame.COLUMN_NAME_NOTECONTENT));
				notedata.setDate(rs
						.getString(Constants.NoteConstantFrame.COLUMN_NAME_NOTEDATE));
				notedata.setFolderid(rs
						.getInt(Constants.NoteConstantFrame.COLUMN_NAME_FOLDERID));
				notedata.setUserid(rs
						.getInt(Constants.NoteConstantFrame.COLUMN_NAME_USERID));

				LogUtil.d("userdata :" + notedata.getNoteid() + "\t"
						+ notedata.getTitle() + "\t" + notedata.getContent()
						+ notedata.getDate() + "\t" + notedata.getUserid()
						+ "\t" + notedata.getFolderid());

				resultnotes.add(notedata);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return resultnotes;
	}

	public ArrayList<NoteData> findAllByFid(int id) {
		ArrayList<NoteData> resultnotes = new ArrayList<NoteData>();

		open();
		Connection c = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = c.createStatement();
			String query = "SELECT "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTEID + ", "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTETITLE + ", "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTECONTENT
					+ ", " + Constants.NoteConstantFrame.COLUMN_NAME_NOTEDATE
					+ ", " + Constants.NoteConstantFrame.COLUMN_NAME_FOLDERID
					+ ", " + Constants.NoteConstantFrame.COLUMN_NAME_USERID
					+ " FROM " + Constants.NoteConstantFrame.TABLE_NAME
					+ " WHERE "
					+ Constants.NoteConstantFrame.COLUMN_NAME_FOLDERID + " = "
					+ id + ";";
			LogUtil.v("query: " + query);
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				NoteData notedata = new NoteData();
				notedata.setNoteid(rs
						.getInt(Constants.NoteConstantFrame.COLUMN_NAME_NOTEID));
				notedata.setTitle(rs
						.getString(Constants.NoteConstantFrame.COLUMN_NAME_NOTETITLE));
				notedata.setContent(rs
						.getString(Constants.NoteConstantFrame.COLUMN_NAME_NOTECONTENT));
				notedata.setDate(rs
						.getString(Constants.NoteConstantFrame.COLUMN_NAME_NOTEDATE));
				notedata.setFolderid(rs
						.getInt(Constants.NoteConstantFrame.COLUMN_NAME_FOLDERID));
				notedata.setUserid(rs
						.getInt(Constants.NoteConstantFrame.COLUMN_NAME_USERID));

				LogUtil.d("userdata :" + notedata.getNoteid() + "\t"
						+ notedata.getTitle() + "\t" + notedata.getContent()
						+ notedata.getDate() + "\t" + notedata.getUserid()
						+ "\t" + notedata.getFolderid());

				resultnotes.add(notedata);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return resultnotes;
	}

	public int updateNote(NoteData notedata) {
		int result = 0;

		open();
		Connection c = getConnection();
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			String query = "UPDATE "
					+ Constants.NoteConstantFrame.TABLE_NAME + " SET "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTETITLE + "=" +"'"+ notedata.getTitle() + "', "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTECONTENT + "=" +"'" + notedata.getContent() + "', "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTEDATE + "="+"'" + notedata.getDate() + "', "
					+ Constants.NoteConstantFrame.COLUMN_NAME_USERID + "=" + notedata.getUserid() + ", "
					+ Constants.NoteConstantFrame.COLUMN_NAME_FOLDERID + "=" + notedata.getFolderid()
					+" WHERE "+Constants.NoteConstantFrame.COLUMN_NAME_NOTEID+"="+notedata.getNoteid()+";";
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

	public NoteData findById(int id) {
		NoteData note = new NoteData();

		open();
		Connection c = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = c.createStatement();
			String query = "SELECT "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTEID + ", "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTETITLE + ", "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTECONTENT
					+ ", " + Constants.NoteConstantFrame.COLUMN_NAME_NOTEDATE
					+ ", " + Constants.NoteConstantFrame.COLUMN_NAME_FOLDERID
					+ ", " + Constants.NoteConstantFrame.COLUMN_NAME_USERID
					+ " FROM " + Constants.NoteConstantFrame.TABLE_NAME
					+ " WHERE "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTEID + " = "
					+ id + ";";
			LogUtil.v("query: " + query);
			rs = stmt.executeQuery(query);

			while (rs.next()) {

				note.setNoteid(rs
						.getInt(Constants.NoteConstantFrame.COLUMN_NAME_NOTEID));
				note.setTitle(rs
						.getString(Constants.NoteConstantFrame.COLUMN_NAME_NOTETITLE));
				note.setContent(rs
						.getString(Constants.NoteConstantFrame.COLUMN_NAME_NOTECONTENT));
				note.setDate(rs
						.getString(Constants.NoteConstantFrame.COLUMN_NAME_NOTEDATE));
				note.setFolderid(rs
						.getInt(Constants.NoteConstantFrame.COLUMN_NAME_FOLDERID));
				note.setUserid(rs
						.getInt(Constants.NoteConstantFrame.COLUMN_NAME_USERID));

			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return note;
	}

	public int insert(NoteData notedata) {
		int result = 0;

		open();
		Connection c = getConnection();
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			String query = "INSERT INTO "
					+ Constants.NoteConstantFrame.TABLE_NAME + " ("
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTETITLE + ", "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTECONTENT
					+ ", " + Constants.NoteConstantFrame.COLUMN_NAME_NOTEDATE
					+ ", " + Constants.NoteConstantFrame.COLUMN_NAME_USERID
					+ ", " + Constants.NoteConstantFrame.COLUMN_NAME_FOLDERID
					+ ")" + " VALUES ('" + notedata.getTitle() + "','"
					+ notedata.getContent() + "','" + notedata.getDate()
					+ "','" + notedata.getUserid() + "','"
					+ notedata.getFolderid() + "');";
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
					+ Constants.NoteConstantFrame.TABLE_NAME + " WHERE "
					+ Constants.NoteConstantFrame.COLUMN_NAME_NOTEID + "=" + id
					+ ";";
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
