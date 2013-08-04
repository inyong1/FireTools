package inyong.xt530.tools;

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

public class MainActivity extends Activity implements OnClickListener
{
	public String  pathData = Environment.getDataDirectory().toString() + "/data/inyong.xt530.tools/data";
	LinearLayout ly_backup, ly_linkData, ly_linkDalvik, ly_clearLog, ly_restart;
	boolean cwmInstalled = false, inyongScript = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		ly_backup     = (LinearLayout) findViewById(R.id.layout_backup_tool);        ly_backup.setOnClickListener(this);
		ly_linkData   = (LinearLayout) findViewById(R.id.layout_link_folder_data);   ly_linkData.setOnClickListener(this);
		ly_linkDalvik = (LinearLayout) findViewById(R.id.layout_link_dalvik_system); ly_linkDalvik.setOnClickListener(this);
		ly_clearLog   = (LinearLayout) findViewById(R.id.layout_clear_system_log);   ly_clearLog.setOnClickListener(this);
	    ly_restart    = (LinearLayout) findViewById(R.id.layout_restart);            ly_restart.setOnClickListener(this);
		buatFolderData();
    }

	private void buatFolderData()
	{
		if (RootTools.isAccessGiven())
		{
			File folderData =new File(pathData);
			if (!folderData.exists())
			{
				folderData.mkdir();
			}
			new Handler().postDelayed(new Runnable(){@Override
					public void run()
					{
						final File notif = new File(pathData + "/notif_versi_1.2");
						if (!notif.exists())
						{
							//	Toast.makeText(MainActivity.this, notif.toString(), Toast.LENGTH_SHORT).show();
							AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
							a.setCancelable(false);
							a.setTitle("FIRE TOOLS V1.2");
							a.setMessage("- Alhamdulillah\n- v1.2 improvement in link & restore dalvik-cache script\n- V1.1 bug link folder data solved\n- Jika masih banyak kekurangan harap maklum\n\n- Salam oprekkers...!");
							a.setPositiveButton("OK", new DialogInterface.OnClickListener(){@Override
									public void onClick(DialogInterface d, int i)
									{
										new Thread(new Runnable(){@Override
												public void run()
												{
													fr.hapusScritLama();
													try
													{
														notif.createNewFile();
													}
													catch (IOException e)
													{}
												}}).start();
									}}).show();
						}
					}}, 500);
		}
		else
		{
			dialogError("Error...!", "Gagal mendapatkan Hak Akses SuperUser");
		}
	}

	public void onClick(View v)
	{
		if (RootTools.isAccessGiven())
		{
			if (RootTools.isBusyboxAvailable())
			{
				switch (v.getId())
				{
					case R.id.layout_backup_tool: goToBackup(); break;
					case R.id.layout_link_dalvik_system: goToCreateLinkDalvik(); break;
					case R.id.layout_restart: restart(); break;
					case R.id.layout_clear_system_log: goToClearLog(); break;
					case R.id.layout_link_folder_data: goToLinkData(); break;
				}
			}
			else
			{
				busyboxTidakDitemukan();
			}
		}
		else
		{
			dialogError("Error...!", "Anda tidak memilikih hak Super User");
		}
	}

	private void goToLinkData()
	{
		Intent cld = new Intent(this, CreateLinkData.class);
		startActivity(cld);
	}

	private void goToClearLog()
	{
		Intent cll = new Intent(this, ClearSystemLog.class);
		startActivity(cll);
	}

	private void goToCreateLinkDalvik()
	{
		Intent cld = new Intent(this, CreateLinkDalvik.class);
		startActivity(cld);
	}

	private void goToBackup()
	{
		Intent backup = new Intent(this, backupMain.class);
		startActivity(backup);
	};

	//fungsi restart
	FungsiRestart fr=new FungsiRestart();
	String[] cmdRestart = new String[]{"reboot",                   "reboot recovery",                     "inyong c n",                   "reboot bootloader"};
	String[] opsiRestart= new String[]{"Restart\n(normal restart)","Stock Recovery Mode\n(Recovery bawaan)", "Custom Recovery Mode\n(CWM)","Fastboot Mode" };
	public void restart()
	{

		new Thread(new Runnable(){@Override
				public void run()
				{
					if (!RootTools.exists("/system/bin/inyong"))
					{
						RootTools.installBinary(getApplicationContext(), R.raw.inyong, "inyong");
						RootTools.remount("/system", "rw");
						try
						{
							RootTools.getShell(true).add(new CommandCapture(0, "cp /data/data/inyong.xt530.tools/files/inyong /system/bin/", "chmod 755 /system/bin/inyong")).waitForFinish();
						}
						catch (Exception e)
						{}

					}
					if (fr.isCwmInstalled())
					{
						cwmInstalled = true;
					}
				}}).start();
		AlertDialog.Builder ab =new AlertDialog.Builder(this);
		ab.setCancelable(true);
		ab.setItems(opsiRestart, new DialogInterface.OnClickListener(){@Override
				public void onClick(DialogInterface d, int id)
				{
					Toast.makeText(getApplicationContext(), cmdRestart[id], Toast.LENGTH_LONG).show();
					jalankanPilihanRestart(cmdRestart[id]);
				}}).show();
	}

	public void jalankanPilihanRestart(String s)
	{
		if (s.equals("inyong c n"))
		{
			if (cwmInstalled == true)
			{
				tampilkanPetunjukBootmenu();
				//to do
			}
			else
			{
				dialogError("ERROR...!", "Handphone anda belum terinstall CWM");
			}
		}
		else
		{
			if (RootTools.isAccessGiven())
			{
				fr.jalankanShellCmd(s);
			}
			else
			{
				dialogError("ERROR...!", "Anda tidak mendapat hak Super User");
			}
		}
	}

	public void dialogError(String judul, String pesan)
	{
		AlertDialog.Builder ab =new AlertDialog.Builder(this);
		ab.setCancelable(true);
		ab.setTitle(judul);
		ab.setMessage(pesan).show();
	}

	private void tampilkanPetunjukBootmenu()
	{
		final File petunjuk_bootmenu = new File(pathData + "/petunjuk_bootmenu");
		if (!petunjuk_bootmenu.exists())
		{
			AlertDialog.Builder ab =new AlertDialog.Builder(this);
			ab.setCancelable(false);
			ab.setTitle("PESAN SEKALI TAMPIL");
			ab.setMessage("Untuk booting dari bootmenu,\nPilih menu Recovery -> Custom recovery -> Reboot system now\nAtau\nPilih menu Boot -> Stock");
			ab.setPositiveButton("OK", new DialogInterface.OnClickListener(){@Override
					public void onClick(DialogInterface d, int i)
					{
						try
						{
							petunjuk_bootmenu.createNewFile();
							rebootCwm();
						}
						catch (IOException e)
						{}
					}}).show();
		}
		else
		{
			rebootCwm();
		}
	}

	public void rebootCwm()
	{
		dialogError("Rebooting...", "Please wait...");
		new Thread(new Runnable(){@Override 
				public void run()
				{
					if (RootTools.isAccessGiven())
					{
						fr.jalankanShellCmd("inyong c n");
					}
				}}).start();
	}

	private void busyboxTidakDitemukan()
	{
		AlertDialog.Builder ab =new AlertDialog.Builder(this);
		ab.setCancelable(true);
		ab.setMessage("ERROR...!");
		ab.setMessage("HP anda belum terinstall busybox. \nAplikasi ini membutuhkan busybox").show();
	}

	public void onBackPressed()
	{
		finish();
	}
}
