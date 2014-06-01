package kr.re.ec.bibim.server;

import java.io.IOException;

public class ServerActivity {
	
	public void Start(){
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
