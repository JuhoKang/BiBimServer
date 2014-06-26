package kr.re.ec.bibim.server.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import kr.re.ec.bibim.constants.Constants;
import kr.re.ec.bibim.server.da.FolderDataController;
import kr.re.ec.bibim.server.da.NoteDataController;
import kr.re.ec.bibim.util.LogUtil;
import kr.re.ec.bibim.vo.FolderData;
import kr.re.ec.bibim.vo.NoteData;
import kr.re.ec.bibim.vowrapper.FolderDataWrapper;
import kr.re.ec.bibim.vowrapper.WrappedClassOpener;

public class FolderStream implements StreamLike {

	private static ServerSocket subserver;

	@Override
	public void Activate(StreamContext context) throws IOException, ClassNotFoundException{

		boolean isreturnfolder = true;
		FolderDataWrapper fdw = new FolderDataWrapper();
		FolderData folder = new FolderData();
		// create the socket server object
		subserver = new ServerSocket(Constants.NetworkConstantFrame.SUBPORT);
		// keep listens indefinitely until receives 'exit' call or program
		// terminates

		/*
		 * UserDataController.getInstance().createTable();
		 * 
		 * for(int i=4;i<10;i++){ loginuser.setName("juho"+i);
		 * loginuser.setPassword("aaa"+i+i);
		 * UserDataController.getInstance().insert(loginuser); }
		 */

		LogUtil.d("Waiting for client folder request");
		// creating socket and waiting for client connection
		Socket socket = subserver.accept();

		// read from socket to ObjectInputStream object
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

		// convert ObjectInputStream object to String
		fdw = (FolderDataWrapper) ois.readObject();
		LogUtil.d("fdw is: " + fdw.getFolderid() + "\t" + fdw.getName() + "\t"
				+ fdw.getUserid());

		if (fdw.getQueryHeader().equals(
				Constants.QueryHeaderConstantFrame.INSERT)) {
			LogUtil.d("QueryHeader is "
					+ Constants.QueryHeaderConstantFrame.INSERT);
			folder = WrappedClassOpener.getInstance()
					.OpenFolderDataWrapper(fdw);

			LogUtil.d("openedclass: " + folder.getFolderid() + "\t"
					+ folder.getName() + "\t" + folder.getUserid());

			FolderDataController.getInstance().insert(folder);
			folder = FolderDataController.getInstance().findByName(
					folder.getName());

		} else if (fdw.getQueryHeader().equals(
				Constants.QueryHeaderConstantFrame.DELETE)) {
			LogUtil.d("QueryHeader is "
					+ Constants.QueryHeaderConstantFrame.DELETE);
			folder = WrappedClassOpener.getInstance()
					.OpenFolderDataWrapper(fdw);

			LogUtil.d("openedclass: " + folder.getFolderid() + "\t"
					+ folder.getName() + "\t" + folder.getUserid());

			LogUtil.d("delete: " + folder.getFolderid() + "\t"
					+ folder.getName() + "\t" + folder.getUserid());
			FolderDataController.getInstance().deleteByID(folder.getFolderid());
			LogUtil.d("deleted");
			folder.setName("NULL");
		} else if (fdw.getQueryHeader().equals(
				Constants.QueryHeaderConstantFrame.SELECT)) {
			if (fdw.getExpression().equals(
					Constants.ExpressionConstantFrame.FID)) {
				ArrayList<NoteData> resultnotes = new ArrayList<NoteData>();
				LogUtil.d("QueryHeader is "
						+ Constants.QueryHeaderConstantFrame.SELECT);
				LogUtil.d("Expression is "
						+ Constants.ExpressionConstantFrame.FID);
				folder = WrappedClassOpener.getInstance()
						.OpenFolderDataWrapper(fdw);

				LogUtil.d("openedclass: " + folder.getFolderid() + "\t"
						+ folder.getName() + "\t" + folder.getUserid());

				resultnotes = NoteDataController.getInstance().findAllByFid(
						folder.getFolderid());

				ObjectOutputStream oos = new ObjectOutputStream(
						socket.getOutputStream());
				oos.writeObject(resultnotes);
				LogUtil.d("Message Sent: "
						+ "this is Selecting Notes from Folder");
				isreturnfolder = false;
			}
		}

		if (isreturnfolder == true) {

			// create ObjectOutputStream object
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			// write object to Socket
			oos.writeObject(folder);
			LogUtil.d("Message Sent: " + folder.getFolderid());
			// close resources

			oos.close();

		} else {

		}

		ois.close();
		socket.close();
		// terminate the server if client sends exit request
		// close the ServerSocket object
		subserver.close();

	}

}
