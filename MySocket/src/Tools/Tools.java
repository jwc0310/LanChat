package Tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools {
	public Tools(){
		
	}
	public static final String getCurrentTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
		return sdf.format(new Date()).toString();
	}
}
