package kr.re.ec.bibim.server;

import java.util.ArrayList;

import kr.re.ec.bibim.server.da.UserDataController;
import kr.re.ec.bibim.util.LogUtil;
import kr.re.ec.bibim.vo.UserData;

public class ServerUserAuthentication {

	public static UserData checkLogin(String username, String userpwd)
			throws Exception {
		ArrayList<UserData> userlist = new ArrayList<UserData>();
		userlist = UserDataController.getInstance().findAll();
		boolean isRegistered = false;
		UserData user = new UserData();
		boolean isAuthenticated = false;
		LogUtil.v("" + userlist.size());
		
		for (int i = 0; i < userlist.size() ; i++) {
			LogUtil.v("forstart");

			user = userlist.get(i);

			
				isRegistered = username.matches(user.getName());
				LogUtil.v(""+username + "+and this+" + user.getName());	
			

		}

		if (isRegistered == true) {

			LogUtil.d("username is : " + username + " checking password...");
			isAuthenticated = userpwd.matches(user.getPassword());

		}

		if (isAuthenticated == true) {
			LogUtil.d("login success");
			return user;
		} else {
			throw new Exception("can't login");
		}

	}

}
