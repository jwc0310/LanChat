package Tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Parse {
	
	/**
	 * obj -> byte[]
	 * @param obj
	 * @return
	 */
	public static byte[] toByteArray(Object obj){
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try{
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
			oos.close();
			bos.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return bytes;
	}
	
	public static Object toObject(byte[] bytes){
		Object obj = null;
		try{
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();
			ois.close();
			bis.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		return obj;
		
	}
	
}
