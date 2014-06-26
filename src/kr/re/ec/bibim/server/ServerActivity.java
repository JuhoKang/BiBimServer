package kr.re.ec.bibim.server;

import kr.re.ec.bibim.server.da.FolderDataController;
import kr.re.ec.bibim.server.da.NoteDataController;
import kr.re.ec.bibim.server.da.UserDataController;

public class ServerActivity {

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

		new ServerNetworkController().run();
	}

}
