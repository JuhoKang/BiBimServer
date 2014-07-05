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
}
