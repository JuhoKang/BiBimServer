package kr.re.ec.bibim.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import kr.re.ec.bibim.constants.Constants;
import kr.re.ec.bibim.server.da.UserDataController;
import kr.re.ec.bibim.util.LogUtil;
import kr.re.ec.bibim.vo.UserData;
import kr.re.ec.bibim.vowrapper.UserDataWrapper;
import kr.re.ec.bibim.vowrapper.WrappedClassOpener;

/**
 * This class implements java Socket server
 * 
 * @author pankaj
 * 
 */

public class ServerNetworkController {

	// static ServerSocket variable
	private static ServerSocket subserver;
	private static ServerSocket server;

	// socket server port on which it will listen
	// private static int port = 9876;

	public void Start() throws IOException, ClassNotFoundException {

		String type;
		server = new ServerSocket(Constants.NetworkConstantFrame.PORT);
		while (true) {
			

			LogUtil.d("Wating for Client");
			Socket socket = server.accept();
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());

			// convert ObjectInputStream object to String
			type = (String) ois.readObject();
			LogUtil.d("type is: " + type);

			if (type.equals(Constants.NotificationConstantFrame.NOTE)) {

				ObjectOutputStream oos = new ObjectOutputStream(
						socket.getOutputStream());
				// write object to Socket
				oos.writeObject(type);
				LogUtil.d("Message Sent: " + type);
				oos.flush();
				oos.close();

			} else if (type.equals(Constants.NotificationConstantFrame.FOLDER)) {

				ObjectOutputStream oos = new ObjectOutputStream(
						socket.getOutputStream());
				// write object to Socket
				oos.writeObject(type);
				
				LogUtil.d("Message Sent: " + type);
				oos.flush();
				oos.close();
				folderStreamActivation();

			} else if (type.equals(Constants.NotificationConstantFrame.USER)) {

				ObjectOutputStream oos = new ObjectOutputStream(
						socket.getOutputStream());
				// write object to Socket
				oos.writeObject(type);
				LogUtil.d("Message Sent: " + type);
				oos.flush();
				oos.close();
				userStreamActivation();
				

			}
			// close resources
			ois.close();
			socket.close();
			// terminate the server if client sends exit request
			// close the ServerSocket object

		}
	}

	private void folderStreamActivation() throws IOException,
			ClassNotFoundException{
		
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

		System.out.println("Waiting for client request");
		// creating socket and waiting for client connection
		Socket socket = subserver.accept();

		// read from socket to ObjectInputStream object
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

		// convert ObjectInputStream object to String
		udw = (UserDataWrapper) ois.readObject();
		LogUtil.d("udw is: " + udw.getUserid() + "\t" + udw.getName() + "\t"
				+ udw.getPassword());

		if (udw.getQueryHeader().matches(
				Constants.QueryHeaderConstantFrame.LOGIN)) {
			LogUtil.d("QueryHeader is "
					+ Constants.QueryHeaderConstantFrame.LOGIN);
			user = WrappedClassOpener.getInstance().OpenUserDataWrapper(udw);
			LogUtil.d("openedclass: " + user.getUserid() + "\t"
					+ user.getName() + "\t" + user.getPassword());

			user = ServerUserAuthentication.checkLogin(user.getName(),
					user.getPassword());

		} else if (udw.getQueryHeader().matches(
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
		}

		// create ObjectOutputStream object
		ObjectOutputStream oos = new ObjectOutputStream(
				socket.getOutputStream());
		// write object to Socket
		oos.writeObject(user);
		System.out.println("Message Sent: " + user.getUserid());
		// close resources
		ois.close();
		oos.close();
		socket.close();
		// terminate the server if client sends exit request
		// close the ServerSocket object
		subserver.close();
	
		
	}

	public void userStreamActivation() throws IOException,
			ClassNotFoundException {

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

		System.out.println("Waiting for client request");
		// creating socket and waiting for client connection
		Socket socket = subserver.accept();

		// read from socket to ObjectInputStream object
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

		// convert ObjectInputStream object to String
		udw = (UserDataWrapper) ois.readObject();
		LogUtil.d("udw is: " + udw.getUserid() + "\t" + udw.getName() + "\t"
				+ udw.getPassword());

		if (udw.getQueryHeader().matches(
				Constants.QueryHeaderConstantFrame.LOGIN)) {
			LogUtil.d("QueryHeader is "
					+ Constants.QueryHeaderConstantFrame.LOGIN);
			user = WrappedClassOpener.getInstance().OpenUserDataWrapper(udw);
			LogUtil.d("openedclass: " + user.getUserid() + "\t"
					+ user.getName() + "\t" + user.getPassword());

			user = ServerUserAuthentication.checkLogin(user.getName(),
					user.getPassword());

		} else if (udw.getQueryHeader().matches(
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
		}

		// create ObjectOutputStream object
		ObjectOutputStream oos = new ObjectOutputStream(
				socket.getOutputStream());
		// write object to Socket
		oos.writeObject(user);
		System.out.println("Message Sent: " + user.getUserid());
		// close resources
		ois.close();
		oos.close();
		socket.close();
		// terminate the server if client sends exit request
		// close the ServerSocket object
		subserver.close();
	}

}
