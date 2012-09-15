package core;

public class Logger {

	public static void logDebug(String message) {
		if(Main.DEBUG) {
//			System.out.println(message);
		}
	}
	
	public static void logInfo(String message) {		
		System.out.println(message);
	}

}
