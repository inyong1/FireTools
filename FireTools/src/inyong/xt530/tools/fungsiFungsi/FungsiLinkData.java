package inyong.xt530.tools.fungsiFungsi;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import com.stericson.RootTools.*;
import com.stericson.RootTools.execution.*;
import java.io.*;
import java.util.*;

public class FungsiLinkData
{
/////baru
	CommandCapture cmd;
	private List<String> cek;
	String stringHasil ="<unknown>";
	String[] arrayHasil =null;

	//fungsi mendapatkan rincian folderdata yg terlink
	public String[] getRincianFolderData(String pathFolderData)
	{
		try
		{
			cek = RootTools.sendShell("busybox ls -ld " + pathFolderData, 0);
			arrayHasil = cek.get(0).split(" ");

		}
		catch (Exception e)
		{}
		return arrayHasil;
	}

	//fungsi ukur size folder data
	public String getSizeFolder(String pathFolder)
	{
		try
		{
			cek = RootTools.sendShell("busybox du -hs " + pathFolder, 0);
			arrayHasil = cek.get(0).split("/");
			stringHasil = arrayHasil[0];
		}
		catch (Exception e)
		{}
		return stringHasil;
	}
// fungsi link data
	public boolean linkanFolder(String namaFolderPackage)
	{
		boolean hasil = false;
		cmd = new CommandCapture(0, "busybox mkdir -p /data/sdext2/data.data", "busybox cp -Lpr /data/data/" + namaFolderPackage + " /data/sdext2/data.data/");
		try
		{
			RootTools.getShell(true).add(cmd).waitForFinish();
		}
		catch (Exception e)
		{}
		if (new File("/data/sdext2/data.data/" + namaFolderPackage).exists())
		{
			cmd = new CommandCapture(0, "busybox rm -r /data/data/" + namaFolderPackage, "busybox ln -s /data/sdext2/data.data/" + namaFolderPackage + " /data/data/" + namaFolderPackage);
			try
			{
				RootTools.getShell(true).add(cmd).waitForFinish();
			}
			catch (Exception e)
			{}
		}
		try
		{
			List<String> konfirmasi = RootTools.sendShell("cd /data/data; ls -l " + namaFolderPackage, 0);
			for (String i : konfirmasi)
			{
				if (i.contains("->"))
				{
					hasil = true;
					tutupShell();
					break;
				}
			}
		}
		catch (Exception e)
		{}
		return hasil;
	}

	//fungsi link data baru
	public boolean unlinkFolder(String fullPath, String folderName)
	{
		boolean hasil=false;
		try
		{
			RootTools.getShell(true).add(new CommandCapture(0, "busybox rm -r /data/data/" + folderName, "busybox cp -Lpr " + fullPath + " /data/data/")).waitForFinish();
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
					hasil= true;
					try
					{
						RootTools.getShell(true).add(new CommandCapture(0, "busybox rm -r " + fullPath)).waitForFinish();
						tutupShell();
					}
					catch (Exception e)
					{}
					break;
				}
			}	
		}
		catch (Exception e)
		{}	
		return hasil;
	}
/////// akhir baru

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


