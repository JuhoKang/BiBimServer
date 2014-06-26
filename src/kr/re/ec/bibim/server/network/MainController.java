package kr.re.ec.bibim.server.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import kr.re.ec.bibim.constants.Constants;
import kr.re.ec.bibim.server.ServerUserAuthentication;
import kr.re.ec.bibim.server.da.FolderDataController;
import kr.re.ec.bibim.server.da.NoteDataController;
import kr.re.ec.bibim.server.da.UserDataController;
import kr.re.ec.bibim.util.LogUtil;
import kr.re.ec.bibim.vo.FolderData;
import kr.re.ec.bibim.vo.NoteData;
import kr.re.ec.bibim.vo.UserData;
import kr.re.ec.bibim.vowrapper.FolderDataWrapper;
import kr.re.ec.bibim.vowrapper.NoteDataWrapper;
import kr.re.ec.bibim.vowrapper.UserDataWrapper;
import kr.re.ec.bibim.vowrapper.WrappedClassOpener;

public class MainController {

	// static ServerSocket variable
	private static ServerSocket subserver;
	private static ServerSocket server;

	// socket server port on which it will listen
	// private static int port = 9876;

	public void Start() throws IOException, ClassNotFoundException {

		String type = null;
		server = new ServerSocket(Constants.NetworkConstantFrame.PORT);
		StreamContext scontext = new StreamContext();

		while (true) {

			LogUtil.d("Waiting for Client");
			Socket socket = null;

			socket = server.accept();

			ObjectInputStream ois = null;

			ois = new ObjectInputStream(socket.getInputStream());

			// convert ObjectInputStream object to String

			type = (String) ois.readObject();

			LogUtil.d("type is: " + type);

			if (type.equals(Constants.NotificationConstantFrame.NOTE)) {

				ObjectOutputStream oos = null;

				oos = new ObjectOutputStream(socket.getOutputStream());

				// write object to Socket

				oos.writeObject(type);

				LogUtil.d("Message Sent: " + type);

				oos.flush();

				oos.close();

				scontext.setStream(new NoteStream());
				scontext.Activate();

			} else if (type.equals(Constants.NotificationConstantFrame.FOLDER)) {

				ObjectOutputStream oos = null;

				oos = new ObjectOutputStream(socket.getOutputStream());

				// write object to Socket

				oos.writeObject(type);

				LogUtil.d("Message Sent: " + type);

				oos.flush();

				oos.close();

				scontext.setStream(new FolderStream());
				scontext.Activate();

			} else if (type.equals(Constants.NotificationConstantFrame.USER)) {

				ObjectOutputStream oos = null;
				
					oos = new ObjectOutputStream(socket.getOutputStream());
				
				// write object to Socket
				
					oos.writeObject(type);
				
				LogUtil.d("Message Sent: " + type);
				
					oos.flush();
				
				
					oos.close();
				
				
					scontext.setStream(new UserStream());
					scontext.Activate();

			}
			// close resources
			
				ois.close();		
				socket.close();
			
			// terminate the server if client sends exit request
			// close the ServerSocket object

		}
	}

	public void noteStreamActivation() throws IOException,
			ClassNotFoundException {

		NoteDataWrapper ndw = new NoteDataWrapper();
		NoteData note = new NoteData();
		// create the socket server object
		subserver = new ServerSocket(Constants.NetworkConstantFrame.SUBPORT);
		// keep listens indefinitely until receives 'exit' call or program
		// terminates

		LogUtil.d("Waiting for client note request");
		// creating socket and waiting for client connection
		Socket socket = subserver.accept();

		// read from socket to ObjectInputStream object
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

		// convert ObjectInputStream object to String
		ndw = (NoteDataWrapper) ois.readObject();
		LogUtil.d("ndw is: " + ndw.getUserid() + "\t" + ndw.getTitle() + "\t"
				+ ndw.getFolderid());

		if (ndw.getQueryHeader().equals(
				Constants.QueryHeaderConstantFrame.INSERT)) {

			LogUtil.d("QueryHeader is "
					+ Constants.QueryHeaderConstantFrame.INSERT);
			note = WrappedClassOpener.getInstance().OpenNoteDataWrapper(ndw);

			LogUtil.d("openedclass: " + note.getUserid() + "\t"
					+ note.getFolderid() + note.getTitle() + "\t"
					+ note.getContent() + "\t" + note.getDate());

			NoteDataController.getInstance().insert(note);

		} else if (ndw.getQueryHeader().equals(
				Constants.QueryHeaderConstantFrame.SELECT)) {

			LogUtil.d("QueryHeader is "
					+ Constants.QueryHeaderConstantFrame.SELECT);
			note = WrappedClassOpener.getInstance().OpenNoteDataWrapper(ndw);

			LogUtil.d("openedclass: " + note.getUserid() + "\t"
					+ note.getFolderid() + note.getTitle() + "\t"
					+ note.getContent() + "\t" + note.getDate());

			NoteDataController.getInstance().findById(note.getNoteid());

		} else if (ndw.getQueryHeader().equals(
				Constants.QueryHeaderConstantFrame.DELETE)) {

			LogUtil.d("QueryHeader is "
					+ Constants.QueryHeaderConstantFrame.DELETE);
			note = WrappedClassOpener.getInstance().OpenNoteDataWrapper(ndw);

			LogUtil.d("openedclass: " + note.getUserid() + "\t"
					+ note.getFolderid() + note.getTitle() + "\t"
					+ note.getContent() + "\t" + note.getDate());

			NoteDataController.getInstance().deleteByID(note.getNoteid());

		} else if (ndw.getQueryHeader().equals(
				Constants.QueryHeaderConstantFrame.UPDATE)) {
			LogUtil.d("QueryHeader is "
					+ Constants.QueryHeaderConstantFrame.UPDATE);
			note = WrappedClassOpener.getInstance().OpenNoteDataWrapper(ndw);

			LogUtil.d("openedclass: " + note.getUserid() + "\t"
					+ note.getFolderid() + note.getTitle() + "\t"
					+ note.getContent() + "\t" + note.getDate());

			NoteDataController.getInstance().updateNote(note);
		}
		// create ObjectOutputStream object
		ObjectOutputStream oos = new ObjectOutputStream(
				socket.getOutputStream());
		// write object to Socket
		oos.writeObject(note);
		LogUtil.d("Message Sent: " + note.getNoteid());
		// close resources

		oos.close();
		ois.close();
		socket.close();
		// terminate the server if client sends exit request
		// close the ServerSocket object
		subserver.close();
	}

	private void folderStreamActivation() throws IOException,
			ClassNotFoundException {
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

	public void userStreamActivation() throws IOException,
			ClassNotFoundException {

		boolean isreturnuser = true;

		UserDataWrapper udw = new UserDataWrapper();
		UserData user = new UserData();
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

		LogUtil.d("Waiting for client user request");
		// creating socket and waiting for client connection
		Socket socket = subserver.accept();

		// read from socket to ObjectInputStream object
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

		// convert ObjectInputStream object to String
		udw = (UserDataWrapper) ois.readObject();
		LogUtil.d("udw is: " + udw.getUserid() + "\t" + udw.getName() + "\t"
				+ udw.getPassword());

		if (udw.getQueryHeader().equals(
				Constants.QueryHeaderConstantFrame.LOGIN)) {
			LogUtil.d("QueryHeader is "
					+ Constants.QueryHeaderConstantFrame.LOGIN);
			user = WrappedClassOpener.getInstance().OpenUserDataWrapper(udw);
			LogUtil.d("openedclass: " + user.getUserid() + "\t"
					+ user.getName() + "\t" + user.getPassword());

			user = ServerUserAuthentication.checkLogin(user.getName(),
					user.getPassword());

		} else if (udw.getQueryHeader().equals(
				Constants.QueryHeaderConstantFrame.INSERT)) {
			int check = 0;
			LogUtil.d("QueryHeader is "
					+ Constants.QueryHeaderConstantFrame.INSERT);
			user = WrappedClassOpener.getInstance().OpenUserDataWrapper(udw);
			LogUtil.d("openedclass: " + user.getUserid() + "\t"
					+ user.getName() + "\t" + user.getPassword());

			check = UserDataController.getInstance().insert(user);

			user = UserDataController.getInstance().findByName(user.getName());
			LogUtil.d("User Added :" + user.getUserid() + "\t" + user.getName()
					+ "\t" + user.getPassword());
			if (check == 0) {
				LogUtil.d("Insert Error Telling Client");
				user.setUserid(-1);
			}
		} else if (udw.getQueryHeader().equals(
				Constants.QueryHeaderConstantFrame.SELECT)) {
			if (udw.getExpression().equals(
					Constants.ExpressionConstantFrame.ALL)) {
				ArrayList<FolderData> resultfolders = new ArrayList<FolderData>();

				resultfolders = FolderDataController.getInstance()
						.findFolderListById(udw.getUserid());

				ObjectOutputStream oos = new ObjectOutputStream(
						socket.getOutputStream());
				oos.writeObject(resultfolders);
				LogUtil.d("Message Sent: " + "this is Selecting Folders");
				isreturnuser = false;
			}
		}

		if (isreturnuser == true) {
			// create ObjectOutputStream object
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			// write object to Socket
			oos.writeObject(user);
			LogUtil.d("Message Sent: " + user.getUserid());
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
