package inyong.xt530.tools.fungsiFungsi;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import java.io.*;
import com.stericson.RootTools.*;
import com.stericson.RootTools.execution.*;
import java.util.concurrent.*;
import com.stericson.RootTools.exceptions.*;
import java.util.*;

public class FungsiBackupSmsContact
{
	String folderBackup;
 public	String folderBackupSms;
 public	String folderBackupContact;
	String fileSms, fileContact;
	String folderDataData;
	String TanggalDanJam;
	CommandCapture cmd;

	public FungsiBackupSmsContact()
	{
		folderBackup = Environment.getExternalStorageDirectory().toString() + "/FireTools/Backup";
		folderBackupSms = folderBackup + "/Sms";
		folderBackupContact = folderBackup + "/Contact";
		folderDataData = Environment.getDataDirectory() + "/data";
		fileSms = folderDataData + "/com.android.providers.telephony/databases/mmssms.db";
		fileContact = folderDataData + "/com.android.providers.contacts/databases/contacts2.db";
	}

	public void setTanggalDanJam()
	{
		TanggalDanJam = dateTime();
	}

	public String getTanggalDanJam()
	{
		return TanggalDanJam;
	}


	public String dateTime()
	{
		String hasil="";
		try
		{
			List<String> tanggal = RootTools.sendShell("date +%d-%m-%Y_jam-%H.%M.%S", -1);
			hasil = "Tgl-" + tanggal.get(0);
			RootTools.closeShell(false);
		}
		catch (RootToolsException e)
		{}
		catch (IOException e)
		{}
		catch (TimeoutException e)
		{}

		return hasil;
	}

	public List<String> getLisfFiles(String PathFile)
	{
		List<String> hasil = new ArrayList<String>();
		File path = new File(PathFile);
		if (path.exists())
		{
			String[] files = path.list();
			if (files.length > 0)
			{
				for (String s: files)
				{
					if (s.contains("_jam-"))
					{
						if (new File(PathFile + "/" + s + "/sms.db").exists() ||  new File(PathFile + "/" + s + "/contact.db").exists())
						{
							hasil.add(s);
						}
					}
				}
			}
		}
		return hasil;
	}

	///////////////////////////////
	public boolean backupSms()
	{
		boolean hasil = false;
		String targetFolderBackupSms = folderBackupSms + "/" + getTanggalDanJam();
		String fileBackupSms =targetFolderBackupSms + "/sms.db";
		if (RootTools.copyFile(fileSms, fileBackupSms, false, false) == true)
		{
			hasil = true;
		}
		else
		{
			if (new File(targetFolderBackupSms).exists())
			{
				hapusFileAtauFolder(targetFolderBackupSms);
			}
		}
		return hasil;
	}

	public boolean backupContact()
	{
		boolean hasil =false;
		String folderTargetBackupContact = folderBackupContact + "/" + getTanggalDanJam();
		String fileBackupContact = folderTargetBackupContact + "/contact.db";
		//	if (RootTools.isAccessGiven())
		//	{
		if (RootTools.copyFile(fileContact, fileBackupContact, false, false) == true)
		{
			hasil = true;
		}
		else
		{
			if (new File(folderTargetBackupContact).exists())
			{
				hapusFileAtauFolder(folderTargetBackupContact);
			}
		}
//		}
		return hasil;
	}
	/////////////////////////////////

	public boolean hapusFileAtauFolder(String path)
	{
		boolean hasil = false;
		File target = new File(path);
		if (target.exists())
		{
			cmd = new CommandCapture(0, "rm -r " + path);
			try
			{
				RootTools.getShell(false).add(cmd).waitForFinish();
			}
			catch (RootDeniedException e)
			{}
			catch (InterruptedException e)
			{}
			catch (IOException e)
			{}
			catch (TimeoutException e)
			{}
		}

		if (!target.exists())
		{
			try
			{
				RootTools.closeShell(false);
			}
			catch (IOException e)
			{}
			hasil = true;
		}
		return hasil;
	}

	public boolean restoreSms(String folderFileBackup)
	{
		boolean hasil = false;

		File fileSmsInternal = new File(fileSms);
		File file = new File(folderFileBackup + "/sms.db");
		if (RootTools.isAccessGiven())
		{
			cmd = new CommandCapture(0, "cp -r " + file.toString() + " " + fileSms + ";chmod 660 " + fileSms + ";chown 1001 " + fileSms + ";chgrp 1001 " + fileSms);
			try
			{
				RootTools.getShell(true).add(cmd).waitForFinish();
			}
			catch (Exception e)
			{}
			if (fileSmsInternal.exists())
			{
				if (fileSmsInternal.length() == file.length())
				{
					try
					{
						RootTools.closeShell(true);
					}
					catch (IOException e)
					{}

					hasil = true;
				}
			}
		}
		return hasil;
	}
	
	public boolean restoreContact(String folderFileBackup)
	{
		boolean hasil = false;

		File fileContactInternal = new File(fileContact);
		File file = new File(folderFileBackup + "/contact.db");
		if (RootTools.isAccessGiven())
		{
			cmd = new CommandCapture(0, "cp -r " + file.toString() + " " + fileContact + ";chmod 666 " + fileContact + ";chown 10007 " + fileContact + ";chgrp 10007 " + fileContact);
			try
			{
				RootTools.getShell(true).add(cmd).waitForFinish();
			}
			catch (Exception e)
			{}
			if (fileContactInternal.exists())
			{
				if (fileContactInternal.length() == file.length())
				{
					try
					{
						RootTools.closeShell(true);
					}
					catch (IOException e)
					{}

					hasil = true;
				}
			}
		}
		return hasil;
	}
}
