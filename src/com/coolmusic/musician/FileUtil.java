package com.coolmusic.musician;

import java.io.File; 
  
import android.os.Environment;

public class FileUtil {
	public static String getSDPath()
	{
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			return null;
		return Environment.getExternalStorageDirectory().toString();
	}
	
	public static String getMusicFloder(String folder) {
		String sd = getSDPath();
		if(null == sd)
			return null;
		String s = sd + "/"+ folder +"/";
		File path = new File(s);
		if(!path.exists()) {
			path.mkdirs();
		}
		path = null;
		return s;
	}
}
