package inyong.xt530.tools.fungsiFungsi;

import android.os.*;
import com.stericson.RootTools.*;
import com.stericson.RootTools.exceptions.*;
import com.stericson.RootTools.execution.*;
import inyong.xt530.tools.*;
import java.io.*;
import java.sql.*;
import java.util.concurrent.*;

public class FungsiService
{
	private final int volt0persen= 3410;
	private final int volt5persen= 3506;
	private final int volt10persen=3626;
	private final int volt15persen=3653;
	private final int volt20persen=3678;
	private final int volt30persen=3701;
	private final int volt40persen=3722;
	private final int volt50persen=3753;
	private final int volt60persen=3796;
	private final int volt70persen=3852;
	private final int volt80persen=3914;
	private final int volt90persen=3992;
	private final int volt100persen=4100; // udah fix

	public FungsiService()
	{

		fString = Environment.getExternalStorageDirectory().toString() + "/FireTools/Log";
		f = new File(fString);

		if (f.exists())
		{}
		else
		{f.mkdirs();}

		batteryLog = new File(fString + "/BatteryTool.log");

		if (batteryLog.exists())
		{}
		else
		{try
			{
				batteryLog.createNewFile();
			}
			catch (IOException e)
			{}}

		/*		try
		 {
		 fos = new FileOutputStream(batteryLog);
		 }
		 catch (FileNotFoundException e)
		 {}
		 */
	}

	//-------//hitung prosentase (menginduk ke stock prosentase
	public int getPercent(int voltage, int stockLevel)
	{
		int hasil=0;
	
		if (stockLevel == 0)
		{
			hasil =	Persen(stockLevel, voltage, volt0persen, volt5persen, 5);
		}
		else if (stockLevel == 5)
		{
			hasil =	Persen(stockLevel, voltage, volt5persen, volt10persen, 5);
		}
		else if (stockLevel == 10)
		{
			hasil = Persen(stockLevel, voltage, volt10persen, volt15persen, 5);
		}
		else if (stockLevel == 15)
		{
			hasil =	Persen(stockLevel, voltage, volt15persen, volt20persen, 5);
		}
		else if (stockLevel == 20)
		{
			hasil =	Persen(stockLevel, voltage, volt20persen, volt30persen, 10);
		}
		else if (stockLevel == 30)
		{
			hasil =	Persen(stockLevel, voltage, volt30persen, volt40persen, 10);
		}
		else if (stockLevel == 40)
		{
			hasil =	Persen(stockLevel, voltage, volt40persen, volt50persen, 10);
		}
		else if (stockLevel == 50)
		{
			hasil =	Persen(stockLevel, voltage, volt50persen, volt60persen, 10);
		}
		else if (stockLevel == 60)
		{
			hasil =	Persen(stockLevel, voltage, volt60persen, volt70persen, 10);
		}
		else if (stockLevel == 70)
		{
			hasil =	Persen(stockLevel, voltage, volt70persen, volt80persen, 10);
		}
		else if (stockLevel == 80)
		{
			hasil =	Persen(stockLevel, voltage, volt80persen, volt90persen, 10);
		}
		else if (stockLevel == 90)
		{
			hasil =	Persen(stockLevel, voltage, volt90persen, volt100persen, 10);
		}
		else if (stockLevel == 100)
		{
			hasil =	Persen(stockLevel, voltage, volt100persen, 4200, 14);
		}

		if (FungsiSettings.extraLevel)
		{
			if (hasil > 116)	hasil = 116;
		}
		else
		{
			if (hasil > 100) hasil = 100;
		}

		if (hasil < 0) hasil = 0;

		return hasil;
	}

	private int Persen(int stockLevel, int volt, int threshold, int thresholdAtasnya, int faktorBagi)
	{
		int hasil =0;
		int voltSelisih = volt - threshold;
		int tambahan = voltSelisih / ((thresholdAtasnya - threshold) / faktorBagi);
		if (tambahan >= faktorBagi && stockLevel != 100)
		{
			tambahan = faktorBagi - 1;
		}
		if (tambahan < 0)
		{
			tambahan = 0;
		}
		hasil = stockLevel + tambahan;
		return hasil;
	}

	//keperluan logging
	String fString;
	File f;
	File batteryLog;
	FileOutputStream fos;

	public void buatLog(int v, int p, int pr)
	{
		String s=new Time(System.currentTimeMillis()) + " " + v + "mV Stock:" + p + "% fTool:" + pr + "%";
		/*	try
		 {
		 fos.write(s.getBytes());
		 }
		 catch (IOException e)
		 {}

		 */
		CommandCapture cmd=new CommandCapture(0, "echo " + s + ">>/sdcard/FireTools/Log/BatteryTool.log");
		try
		{
			RootTools.getShell(false).add(cmd).waitForFinish();
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
