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
import kr.re.ec.bibim.server.da.UserDataController;
import kr.re.ec.bibim.util.LogUtil;
import kr.re.ec.bibim.vo.FolderData;
import kr.re.ec.bibim.vo.UserData;
import kr.re.ec.bibim.vowrapper.UserDataWrapper;
import kr.re.ec.bibim.vowrapper.WrappedClassOpener;

public class UserStream implements StreamLike {

	private static ServerSocket subserver;

	@Override
	public void Activate(StreamContext context) throws IOException,
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
