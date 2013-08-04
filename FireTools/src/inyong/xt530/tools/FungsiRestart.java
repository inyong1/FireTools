package inyong.xt530.tools;


import android.os.*;
import java.io.*;
import com.stericson.RootTools.execution.*;
import com.stericson.RootTools.*;
import java.util.concurrent.*;
import com.stericson.RootTools.exceptions.*;

public class FungsiRestart
{
	public String system = Environment.getRootDirectory().toString();

	public void hapusScritLama()
	{
		RootTools.remount("/system","rw");
		CommandCapture c=new CommandCapture(0,"rm /system/bin/inyong","rm /data/data/inyong.xt530.tools/files/*");
		try
		{
			RootTools.getShell(true).add(c);
		}
		catch (TimeoutException e)
		{}
		catch (IOException e)
		{}
		catch (RootDeniedException e)
		{}
	}
	public boolean isCwmInstalled()
	{
		boolean hasil = false;
		if (isInitD() && isPerlengkapanCwm())
		{
			hasil = true;
		}
		return hasil;
	}

	private boolean isInitD()
	{
		boolean hasil = false;
		File initD=new File(system + "/etc/init.d");
		File sysinit = new File(system + "/bin/sysinit");
		if (initD.exists() && sysinit.exists())
		{
			hasil = true;
		}
		return hasil;
	}

	private boolean isPerlengkapanCwm()
	{
		boolean hasil = false;
		File bootmenu = new File(system + "/bootmenu/binary/bootmenu");
		if (bootmenu.exists())
		{
			hasil = true;
		}
		return hasil;
	}

	public void jalankanShellCmd(String c)
	{
		CommandCapture cmd=new CommandCapture(0,c);
		try
		{
			RootTools.getShell(true).add(cmd).waitForFinish();
		}
		catch (IOException e)
		{}
		catch (TimeoutException e)
		{}
		catch (RootDeniedException e)
		{}
		catch (InterruptedException e)
		{}
	}

}
