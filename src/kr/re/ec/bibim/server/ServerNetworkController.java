package kr.re.ec.bibim.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;

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
	private static ServerSocket server;
	// socket server port on which it will listen
	private static int port = 9876;

	public void Start() throws IOException, ClassNotFoundException {

		UserDataWrapper udw = new UserDataWrapper();
		UserData loginuser = new UserData();
		// create the socket server object
		server = new ServerSocket(Constants.NetworkConstantFrame.PORT);
		// keep listens indefinitely until receives 'exit' call or program
		// terminates
		
	/*
		UserDataController.getInstance().createTable();
		 
		for(int i=4;i<10;i++){ loginuser.setName("juho"+i);
		loginuser.setPassword("aaa"+i+i);
		UserDataController.getInstance().insert(loginuser); }
		*/
		
		

		while (true) {
			System.out.println("Waiting for client request");
			// creating socket and waiting for client connection
			Socket socket = server.accept();

			// read from socket to ObjectInputStream object
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());

			// convert ObjectInputStream object to String
			udw = (UserDataWrapper) ois.readObject();
			LogUtil.d("udw is: " + udw.getUserid() + "\t" + udw.getName()
					+ "\t" + udw.getPassword());

			if (udw.getQueryHeader().matches("Login")) {
				LogUtil.d("QueryHeader is Login");
				loginuser = WrappedClassOpener.getInstance()
						.OpenUserDataWrapper(udw);
				LogUtil.d("openedclass: " + loginuser.getUserid() + "\t"
						+ loginuser.getName() + "\t" + loginuser.getPassword());

				loginuser = ServerUserAuthentication.checkLogin(
						loginuser.getName(), loginuser.getPassword());

			}

			// create ObjectOutputStream object
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			// write object to Socket
			oos.writeObject(loginuser);
			System.out.println("Message Sent: " + loginuser.getUserid());
			// close resources
			ois.close();
			oos.close();
			socket.close();
			// terminate the server if client sends exit request
			// close the ServerSocket object
		}
		
	}

}
