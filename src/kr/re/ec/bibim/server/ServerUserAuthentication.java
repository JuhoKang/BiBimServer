package kr.re.ec.bibim.server;

import java.util.ArrayList;

import kr.re.ec.bibim.server.da.UserDataController;
import kr.re.ec.bibim.util.LogUtil;
import kr.re.ec.bibim.vo.UserData;

public class ServerUserAuthentication {

	public static UserData checkLogin(String username, String userpwd){
		ArrayList<UserData> userlist = new ArrayList<UserData>();
		userlist = UserDataController.getInstance().findAll();
		boolean isRegistered = false;
		UserData user = new UserData();
		boolean isAuthenticated = false;
		LogUtil.v("" + userlist.size());
		
		for (int i = 0; i < userlist.size() ; i++) {
			LogUtil.v("forstart");

			user = userlist.get(i);

			
				isRegistered = username.equals(user.getName());
				if (isRegistered == true){
					break;
				}
				LogUtil.v(""+user.getName() + "+and this+" + username);
				LogUtil.v("this is"+isRegistered);
			

		}

		if (isRegistered == true) {

			LogUtil.d("username is : " + username + " checking password...");
			isAuthenticated = userpwd.equals(user.getPassword());

		}
		else {
			user.setUserid(-1);
			return user;
		}

		if (isAuthenticated == true) {
			LogUtil.d("login success");
			return user;
		}
		else {
			user.setUserid(-1);
			return user;
		}
		

	}

}
