package Tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AndyDate {
	public AndyDate(){
		
	}
	public static final String getCurrentTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
		return sdf.format(new Date()).toString();
	}
}
