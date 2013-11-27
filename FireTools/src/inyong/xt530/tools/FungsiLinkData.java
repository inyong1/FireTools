package inyong.xt530.tools;

import com.stericson.RootTools.*;
import com.stericson.RootTools.exceptions.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import android.os.*;
import com.stericson.RootTools.execution.*;

public class FungsiLinkData
{
/////baru
CommandCapture cmd;
private List<String> cek;
String stringHasil ="<unknown>";
String[] arrayHasil =null;
	
	//fungsi mendapatkan rincian folderdata yg terlink
	public String[] getRincianFolderData(String pathFolderData){
		try
		{
			cek = RootTools.sendShell("busybox ls -ld "+pathFolderData, 0);
			arrayHasil=cek.get(0).split(" ");
			
		}
		catch (Exception e)
		{}
		return arrayHasil;
	}
	
	//fungsi ukur size folder data
	public String getSizeFolder(String pathFolder){
		try
		{
			cek = RootTools.sendShell("busybox du -hs "+pathFolder, 0);
			arrayHasil=cek.get(0).split("/");
			stringHasil = arrayHasil[0];
		}
		catch (Exception e)
		{}
		return stringHasil;
	}
/////// akhir baru


	List<String> listLs= new ArrayList<String>();

	public boolean hitung =true, unlingked = false, linked = false;
	public int unlinkedFolder=0;
	public int folderTerhitung =0;
	public String folderDihitung="---";

	public void setFolderDihitung(String folderDihitung)
	{
		this.folderDihitung = folderDihitung;
	}

	public String getFolderDihitung()
	{
		return folderDihitung;
	}

	public boolean isHitung()
	{
		return hitung;
	}

	public boolean isLinked()
	{
		return linked;
	}

	public boolean isUnlingked()
	{
		return unlingked;
	}

	public int getUnlinkedFolder()
	{
		return unlinkedFolder;
	}

	public int getFolderTerhitung()
	{
		return folderTerhitung;
	}



	public List<String> getListFolderData()
	{
		List<String> hasil = new ArrayList<String>();
		if (RootTools.isAccessGiven())
		{
			try
			{
				listLs = RootTools.sendShell("cd /data/data; ls -l", 0);
				for (String s:listLs)
				{		
					if (s.contains("->"))
					{
						String[] namafolder=s.split(" ");
						hasil.add(namafolder[namafolder.length - 3] + " \n(Linked)" + " " + namafolder[namafolder.length - 2] + " " + namafolder[namafolder.length - 1]);
					}
					else if (s.length() > 5)
					{
						unlinkedFolder += 1;
					}	
				}
			}
			catch (Exception e)
			{}
		}
		return hasil;
	}

	public List<String> getListFolderDataUnlinked()
	{
		List<String> hasil = new ArrayList<String>();
		TreeMap<Long,String> tm= new TreeMap<Long,String>();
		long lg =0;
		try
		{
			listLs = RootTools.sendShell("cd /data/data; ls -l", 0);
			for (String s:listLs)
			{
				if (!s.contains("->"))
				{
					String[] arrayUnlinked =s.split(" ");
					String namaFolder =arrayUnlinked[arrayUnlinked.length - 1];
					if (namaFolder.length() > 3)
					{
						//	String sizeData =hitungSizeData(namaFolder);
						//	hasil.add(namaFolder + "\n" + sizeData);
						lg = hitungSize(namaFolder);
						if (lg > 0)
						{
							tm.put(lg, namaFolder);
						}
					}
					folderTerhitung += 1;
					folderDihitung = namaFolder;
				}
			}
		}
		catch (Exception e)
		{}
		hitung = false;

		String fol;
		String siz;
		List<String> listAsc = new ArrayList<String>();
		for (long lng : tm.keySet())
		{
			fol = tm.get(lng);
			siz = longToStringSize(lng);
			listAsc.add(fol + " \n" + siz);
		}

		int i = listAsc.size();
		while (i > 0)
		{
			i -= 1;
			hasil.add(listAsc.get(i));
		}
		return hasil;
	}

	private long hitungSize(String folder)
	{
		long hasil=0;
		try
		{
			List<String> fileList = RootTools.sendShell("ls -l $(find /data/data/" + folder + ") | awk '{print $4}'", 0);
			for (String i:fileList)
			{
				if (!i.contains("-"))
				{
					hasil += Integer.valueOf(i);
				}
			}
		}
		catch (Exception e)
		{}
		hasil /= 2;
		return hasil;
	}

	public String longToStringSize(long l)
	{
		String hasil="";
		if (l > 1048575)
		{
			hasil = l / 1048576 + " MB";	
		}
		else if (l > 1023)
		{
			hasil = l / 1024 + " KB";
		}
		else
		{
			hasil = l + " B";
		}
		return hasil;
	}

	public String hitungSizeFolderData(String namaFolderData)
	{
		String hasil ="";
		long sizeLong = hitungSize(namaFolderData);
		String sizeS = longToStringSize(sizeLong);
		hasil = namaFolderData + " \n" + sizeS;
		return hasil;
	}

	//fungsi link folder data 
	public void link(String folderName)
	{
		CommandCapture cmd;
		if (RootTools.isBusyboxAvailable() && RootTools.isAccessGiven())
		{
			cmd = new CommandCapture(0, "busybox mkdir -p /data/sdext2/data.data", "busybox cp -Lpr /data/data/" + folderName + " /data/sdext2/data.data/");
			try
			{
				RootTools.getShell(true).add(cmd).waitForFinish();
			}
			catch (Exception e)
			{}
			if (new File("/data/sdext2/data.data/" + folderName).exists())
			{
				cmd = new CommandCapture(0, "busybox rm -r /data/data/" + folderName, "busybox ln -s /data/sdext2/data.data/" + folderName + " /data/data/" + folderName);
				try
				{
					RootTools.getShell(true).add(cmd).waitForFinish();
				}
				catch (Exception e)
				{}
			}
			try
			{
				List<String> konfirmasi = RootTools.sendShell("cd /data/data; ls -l " + folderName, 0);
				for (String i : konfirmasi)
				{
					if (i.contains("->"))
					{
						linked = true;
						tutupShell();
						break;
					}
				}
			}
			catch (Exception e)
			{}
		}
	}

	public String isLinkedTo(String folderData)
	{
		String hasil="some where";
		if (RootTools.isAccessGiven())
		{
			try
			{
				List<String> h = RootTools.sendShell("ls -l /data/data/"+folderData, 0);
				String[] outLs =null;
				for (String s :h)
				{
					if (s.contains("->"))
					{
						outLs = s.split(" ");
						hasil = outLs[outLs.length - 1];
						break;
					}
				}
			}
			catch (Exception e)
			{}
		}
		return hasil;
	}

	public void unlink(String fullPath, String folderName)
	{
		if (RootTools.isAccessGiven() && RootTools.isBusyboxAvailable())
		{
			try
			{
				RootTools.getShell(true).add(new CommandCapture(0, "busybox rm -r /data/data/" + folderName, "busybox cp -pr " + fullPath + " /data/data/")).waitForFinish();
			}
			catch (Exception e)
			{}
			try
			{
				List<String> output = RootTools.sendShell("ls -l /data/data/" + folderName, 0);
				for (String s:output)
				{
					if (s.startsWith("d"))
					{
						unlingked = true;
						break;
					}
				}
				if (unlingked == true)
				{
					try
					{
						RootTools.getShell(true).add(new CommandCapture(0, "busybox rm -r " + fullPath)).waitForFinish();
						tutupShell();
					}
					catch (Exception e)
					{}
				}
			}
			catch (Exception e)
			{}		
		}
	}

	public void tutupShell()
	{
		try
		{
			RootTools.closeShell(true);
			RootTools.closeAllShells();
		}
		catch (IOException e)
		{}
	}
}


