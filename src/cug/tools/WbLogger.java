package cug.tools;



import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class WbLogger {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	static Logger logger;
	public static void println(String msg)
	{
		if(logger==null)
		{
			logger=Logger.getLogger("WbLogger");			
		}
		;
		logger.log(Level.DEBUG, msg);
	
	}
	public static void print(String msg)
	{
		println(msg);
	}

}
