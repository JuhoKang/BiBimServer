package kr.re.ec.bibim.server;

import java.io.IOException;

import kr.re.ec.bibim.server.network.MainController;

/**
 * Thread to control network MainController
 * 
 * 
 */

public class ServerNetworkController extends Thread {

	public void run() {
		
		try {
			new MainController().Start();
		} catch (ClassNotFoundException e5) {
			// TODO Auto-generated catch block
			e5.printStackTrace();
		} catch (IOException e5) {
			// TODO Auto-generated catch block
			e5.printStackTrace();
		}
	}
}
