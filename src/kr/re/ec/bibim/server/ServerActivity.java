package kr.re.ec.bibim.server;

import java.io.IOException;

import kr.re.ec.bibim.server.da.FolderDataController;
import kr.re.ec.bibim.server.da.NoteDataController;
import kr.re.ec.bibim.server.da.UserDataController;

public class ServerActivity {

	public void Start() {
		try {
			new ServerNetworkController().Start();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void initDB() {

		UserDataController.getInstance().createTable();
		FolderDataController.getInstance().createTable();
		NoteDataController.getInstance().createTable();

	}

	public void dropDB() {

		UserDataController.getInstance().dropTable();
		FolderDataController.getInstance().dropTable();
		NoteDataController.getInstance().dropTable();

	}

	public static void main(String[] args) {
		// to initDB
		// new ServerActivity().dropDB();
		// new ServerActivity().initDB();
		
		try {
			new ServerNetworkController().Start();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
