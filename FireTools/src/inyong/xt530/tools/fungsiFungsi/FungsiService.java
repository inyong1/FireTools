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

	public static final int[] batteryIcon=new int[117];
	public FungsiService()
	{
		batteryIcon[0] = R.drawable.b000;
		batteryIcon[1] = R.drawable.b001;
		batteryIcon[2] = R.drawable.b002;
		batteryIcon[3] = R.drawable.b003;
		batteryIcon[4] = R.drawable.b004;
		batteryIcon[5] = R.drawable.b005;
		batteryIcon[6] = R.drawable.b006;
		batteryIcon[7] = R.drawable.b007;
		batteryIcon[8] = R.drawable.b008;
		batteryIcon[9] = R.drawable.b009;
		batteryIcon[10] = R.drawable.b010;
		batteryIcon[11] = R.drawable.b011;
		batteryIcon[12] = R.drawable.b012;
		batteryIcon[13] = R.drawable.b013;
		batteryIcon[14] = R.drawable.b014;
		batteryIcon[15] = R.drawable.b015;
		batteryIcon[16] = R.drawable.b016;
		batteryIcon[17] = R.drawable.b017;
		batteryIcon[18] = R.drawable.b018;
		batteryIcon[19] = R.drawable.b019;
		batteryIcon[20] = R.drawable.b020;
		batteryIcon[21] = R.drawable.b021;
		batteryIcon[22] = R.drawable.b022;
		batteryIcon[23] = R.drawable.b023;
		batteryIcon[24] = R.drawable.b024;
		batteryIcon[25] = R.drawable.b025;
		batteryIcon[26] = R.drawable.b026;
		batteryIcon[27] = R.drawable.b027;
		batteryIcon[28] = R.drawable.b028;
		batteryIcon[29] = R.drawable.b029;
		batteryIcon[30] = R.drawable.b030;
		batteryIcon[31] = R.drawable.b031;
		batteryIcon[32] = R.drawable.b032;
		batteryIcon[33] = R.drawable.b033;
		batteryIcon[34] = R.drawable.b034;
		batteryIcon[35] = R.drawable.b035;
		batteryIcon[36] = R.drawable.b036;
		batteryIcon[37] = R.drawable.b037;
		batteryIcon[38] = R.drawable.b038;
		batteryIcon[39] = R.drawable.b039;
		batteryIcon[40] = R.drawable.b040;
		batteryIcon[41] = R.drawable.b041;
		batteryIcon[42] = R.drawable.b042;
		batteryIcon[43] = R.drawable.b043;
		batteryIcon[44] = R.drawable.b044;
		batteryIcon[45] = R.drawable.b045;
		batteryIcon[46] = R.drawable.b046;
		batteryIcon[47] = R.drawable.b047;
		batteryIcon[48] = R.drawable.b048;
		batteryIcon[49] = R.drawable.b049;
		batteryIcon[50] = R.drawable.b050;
		batteryIcon[51] = R.drawable.b051;
		batteryIcon[52] = R.drawable.b052;
		batteryIcon[53] = R.drawable.b053;
		batteryIcon[54] = R.drawable.b054;
		batteryIcon[55] = R.drawable.b055;
		batteryIcon[56] = R.drawable.b056;
		batteryIcon[57] = R.drawable.b057;
		batteryIcon[58] = R.drawable.b058;
		batteryIcon[59] = R.drawable.b059;
		batteryIcon[60] = R.drawable.b060;
		batteryIcon[61] = R.drawable.b061;
		batteryIcon[62] = R.drawable.b062;
		batteryIcon[63] = R.drawable.b063;
		batteryIcon[64] = R.drawable.b064;
		batteryIcon[65] = R.drawable.b065;
		batteryIcon[66] = R.drawable.b066;
		batteryIcon[67] = R.drawable.b067;
		batteryIcon[68] = R.drawable.b068;
		batteryIcon[69] = R.drawable.b069;
		batteryIcon[70] = R.drawable.b070;
		batteryIcon[71] = R.drawable.b071;
		batteryIcon[72] = R.drawable.b072;
		batteryIcon[73] = R.drawable.b073;
		batteryIcon[74] = R.drawable.b074;
		batteryIcon[75] = R.drawable.b075;
		batteryIcon[76] = R.drawable.b076;
		batteryIcon[77] = R.drawable.b077;
		batteryIcon[78] = R.drawable.b078;
		batteryIcon[79] = R.drawable.b079;
		batteryIcon[80] = R.drawable.b080;
		batteryIcon[81] = R.drawable.b081;
		batteryIcon[82] = R.drawable.b082;
		batteryIcon[83] = R.drawable.b083;
		batteryIcon[84] = R.drawable.b084;
		batteryIcon[85] = R.drawable.b085;
		batteryIcon[86] = R.drawable.b086;
		batteryIcon[87] = R.drawable.b087;
		batteryIcon[88] = R.drawable.b088;
		batteryIcon[89] = R.drawable.b089;
		batteryIcon[90] = R.drawable.b090;
		batteryIcon[91] = R.drawable.b091;
		batteryIcon[92] = R.drawable.b092;
		batteryIcon[93] = R.drawable.b093;
		batteryIcon[94] = R.drawable.b094;
		batteryIcon[95] = R.drawable.b095;
		batteryIcon[96] = R.drawable.b096;
		batteryIcon[97] = R.drawable.b097;
		batteryIcon[98] = R.drawable.b098;
		batteryIcon[99] = R.drawable.b099;
		batteryIcon[100] = R.drawable.b100;
		batteryIcon[101] = R.drawable.b101;
		batteryIcon[102] = R.drawable.b102;
		batteryIcon[103] = R.drawable.b103;
		batteryIcon[104] = R.drawable.b104;
		batteryIcon[105] = R.drawable.b105;
		batteryIcon[106] = R.drawable.b106;
		batteryIcon[107] = R.drawable.b107;
		batteryIcon[108] = R.drawable.b108;
		batteryIcon[109] = R.drawable.b109;
		batteryIcon[110] = R.drawable.b110;
		batteryIcon[111] = R.drawable.b111;
		batteryIcon[112] = R.drawable.b112;
		batteryIcon[113] = R.drawable.b113;
		batteryIcon[114] = R.drawable.b114;
		batteryIcon[115] = R.drawable.b115;
		batteryIcon[116] = R.drawable.b116;


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
		int rentangVoltage=volt100persen - volt0persen;
		hasil = ((voltage - volt0persen) * 100) / rentangVoltage; 

		//cara baru
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
