package player.strategy.opponentModel;

public class Logger {
	
	public static boolean DEBUG = true;

	public static void logDebug(String message) {
		if(DEBUG) {
			System.out.println(message);
		}
	}
	
	public static void logInfo(String message) {		
		System.out.println(message);
	}

}
