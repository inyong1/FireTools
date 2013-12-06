package inyong.xt530.tools.fungsiFungsi;

import android.os.*;
import com.stericson.RootTools.*;
import com.stericson.RootTools.exceptions.*;
import com.stericson.RootTools.execution.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class FungsiClearLog
{int jumFile, jumFileLog;
	long jumFileSize, jumFileSizeLog;

	private HashMap<String,String>	hashmap = new HashMap<String, String>();
	private HashMap<String,String>	hashmaplog = new HashMap<String, String>();

	public HashMap<String, String> hitungTotalSize(String path)
	{

		hashmap.clear();
		jumFile = 0;
		jumFileSize = 0;
		File folder = new File(path);
		if (folder.exists())
		{
			File[] files =folder.listFiles();
			if (files.length > 0)
			{
				for (File f:files)
				{
					jumFile += 1;
					jumFileSize += f.length();
				}

				String k = "";
				String v = "";

				if (jumFile > 1)
				{
					k = jumFile + " Files";
				}
				else
				{
					k = jumFile + " File";
				}

				if (jumFileSize > 1048575)
				{
					v = jumFileSize / 1048576 + " MB";	
				}
				else if (jumFileSize > 1023)
				{
					v = jumFileSize / 1024 + " KB";
				}
				else
				{
					v = jumFileSize + " B";
				}

				hashmap.put(k, v);

			}
			else
			{
				hashmap.put("0 File", "0 B");
			}
		}
		return hashmap;
	}

	public int getJumFile()
	{
		return jumFile;
	}

	public long getJumFileSize()
	{
		return jumFileSize;
	}

	public HashMap<String, String> hitungTotalSizeLog()
	{

		hashmaplog.clear();
		jumFileLog = 0;
		jumFileSizeLog = 0;
		if (RootTools.isAccessGiven())
		{
			File folder = new File(Environment.getDataDirectory().toString());
			try
			{
				List<String> logfiles = RootTools.sendShell("ls " + folder + "/*.log", 0);
				for (String s:logfiles)
				{
					if (s.endsWith(".log"))
					{
						jumFileLog += 1;
						jumFileSizeLog += new File(s).length();
					}
				}
			}
			catch (Exception e)
			{}

			String k ="";
			String v ="";

			if (jumFileLog > 1)
			{
				k = jumFileLog + " Files";
			}
			else
			{
				k = jumFileLog + " File";
			}

			if (jumFileSizeLog > 1048576)
			{
				v = jumFileSizeLog / 1048576 + " MB";	
			}
			else if (jumFileSizeLog > 1024)
			{
				v = jumFileSizeLog / 1024 + " KB";
			}
			else
			{
				v = jumFileSizeLog + " B";
			}

			hashmaplog.put(k, v);
		}
		else
		{
			hashmaplog.put("null", "null");
		}
		return hashmaplog;
	}
	public int getJumFileLog()
	{
		return jumFileLog;
	}

	public long getJumFileSizeLog()
	{
		return jumFileSizeLog;
	}
	
	public void clearResidualFiles(){
		if(RootTools.isAccessGiven()){
			CommandCapture cm;
			cm = new CommandCapture(0,"rm /data/tombstones/*", "rm /data/system/dropbox/*", "rm /data/*.log");
			try
			{
				RootTools.getShell(true).add(cm).waitForFinish();
			}
			catch (TimeoutException e)
			{}
			catch (IOException e)
			{}
			catch (RootDeniedException e)
			{}
			catch (InterruptedException e)
			{}

			}
	}
}
