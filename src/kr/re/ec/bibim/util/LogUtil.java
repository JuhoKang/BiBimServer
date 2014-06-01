package kr.re.ec.bibim.util;
/**
 * Class LogUtil.
 * print to log using Class and Method name.
 * TODO: DO NOT USE SYSOUT FOR LOGGING!!
 * @author Kim Taehee (slhyvaa@nate.com)
 * @since 130812
 * @modified 140514 (for java)
 */

public final class LogUtil {
	private static final boolean debug = true;// BuildConfig.DEBUG;
	private static final String TAG = "User Logged";

	private LogUtil() {} //cannot create instance
	
	/* vervose */
	public static void v(String log)
	{
		if(debug) {
			System.out.println(TAG + "(V):" + new Exception().getStackTrace()[1].getClassName() + "::" 
					+ new Exception().getStackTrace()[1].getMethodName() + "():" + log); //get class and method name
		}
	}

	/* debug */
	public static void d(String log)
	{
		if(debug) {
			System.out.println(TAG + "(D):" + new Exception().getStackTrace()[1].getClassName() + "::" 
					+ new Exception().getStackTrace()[1].getMethodName() + "():" + log); //get class and method name
		}
	}

	/* info */
	public static void i(String log)
	{
		if(debug) {
			System.out.println(TAG + "(I):" + new Exception().getStackTrace()[1].getClassName() + "::" 
					+ new Exception().getStackTrace()[1].getMethodName() + "():" + log); //get class and method name
		}
	}

	/* warn */
	public static void w(String log)
	{
		System.out.println(TAG + "(W):" + new Exception().getStackTrace()[1].getClassName() + "::" 
				+ new Exception().getStackTrace()[1].getMethodName() + "():" + log); //get class and method name
	}

	/* error */
	public static void e(String log)
	{
		System.out.println(TAG + "(E):" + new Exception().getStackTrace()[1].getClassName() + "::" 
				+ new Exception().getStackTrace()[1].getMethodName() + "():" + log); //get class and method name
	}
}