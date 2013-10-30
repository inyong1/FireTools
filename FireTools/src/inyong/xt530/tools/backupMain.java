package inyong.xt530.tools;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.stericson.RootTools.*;
import com.stericson.RootTools.execution.*;
import java.io.*;
import java.util.*;

public class backupMain extends ListActivity implements OnClickListener
{

	Button tombolBackup;
	public static String TASK_CODE;
	TextView tv;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.backup_main);
		tv = (TextView) findViewById(R.id.id_text_view_backup_main);
		tombolBackup = (Button) findViewById(R.id.tombol_create_backup_id); tombolBackup.setOnClickListener(this);

		new Handler().postDelayed((new Runnable(){@Override public void run()
									  {buatList()	; }}), 20);
		new Handler().postDelayed(new Runnable(){@Override public void run()
				{	tampilkanList();	}}, 40);
    }

	//update v2.1
	public void onResume()
	{
		super.onResume();
		new Handler().postDelayed(new Runnable(){@Override public void run()
				{
					listFolder.clear();

					buatList();

					tampilkanList();

				}}, 500);

	}

	List<String> listFolder = new ArrayList<String>();
	ArrayAdapter la;
	String[] arrayFolder;

	public void buatList()
	{ 
		//listBackup = true;
		//	listFolder = new ArrayList<String>();
		String path=Environment.getExternalStorageDirectory().toString() + "/clockworkmod/backup";
		File pathfolder = new File(path);
		if (pathfolder.exists())
		{			
			try
			{
				List<String> output = RootTools.sendShell("cd sdcard/clockworkmod/backup; ls", -1);
				for (String i:output)
				{
					if (i.contains("-"))
					{
						long ukuran = cekUkuran(i);
						if (ukuran > 1024)
						{ 
							ukuran = ukuran / 1024;
							listFolder.add(i + " \n (" + ukuran + "MB)");
						}
						else
						{
							listFolder.add(i + " \n (" + ukuran + "KB)");
						}

					}
				}
			}
			catch (Exception e)
			{}
			String[]	arrayTemp = new String[listFolder.size()];
			arrayFolder = listFolder.toArray(arrayTemp);
		}
		else
		{

			tv.setText("\n\n\n\n File Backup Tidak Ditemukan");

		}
	}

	//upadet v1.3
	public void tampilkanList()
	{
		if (listFolder.size() > 0)
		{
			if (la == null)
			{
				la = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , listFolder);
			}
			setListAdapter(la);
			la.notifyDataSetChanged();
			tv.setText("");
		}
		else
		{
			tv.setText("\n\n\n\n File Backup Tidak Ditemukan");

		}
	}

	List<String> listFiles;
	public long cekUkuran(String dir)
	{

		long hasilKB = -1;
		String pathdir = "/sdcard/clockworkmod/backup/" + dir;
		try
		{
			listFiles = RootTools.sendShell("ls " + pathdir + "/*", 0);
		}
		catch (Exception e)
		{}
		long		 ukuranBytes = 0;
		for (String i : listFiles)
		{
			if (i.endsWith(".img") || i.endsWith(".tar") || i.endsWith("log"))
			{
				try
				{
					ukuranBytes += new File(i).length();
				}
				catch (Exception e)
				{}
			}
		}
		hasilKB = ukuranBytes / 1024;
		return hasilKB;

	}

	public void onListItemClick(ListView lv, View v, final int p, long id)
	{

		String[] folders = arrayFolder[p].split(" ");
		final String folder = folders[0];
		File path =new File(Environment.getExternalStorageDirectory().toString() + "/clockworkmod/backup/" + folder);
		//		Toast.makeText(this,path,Toast.LENGTH_LONG).show();
		if (path.exists())
		{
			AlertDialog.Builder hapus = new AlertDialog.Builder(this);
			hapus.setCancelable(true);
			//	hapus.setMessage("Setelah folder terhapus, kembali ke menu utama dan masuk lagi ke sini untuk me-refresh daftar folder");
			hapus.setTitle("Hapus Folder Backup?");
			hapus.setPositiveButton("HAPUS", new DialogInterface.OnClickListener(){@Override
					public void onClick(DialogInterface d, int i)
					{
						hapusFolderTerpilih(folder);
						// update otomatis perbarui list folder
						listFolder.remove(p);
						la.notifyDataSetChanged();
						if (listFolder.size() == 0)
						{
							tv.setText("\n\n\n\n File Backup Tidak Ditemukan");
						}
					}});
			hapus.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){@Override
					public void onClick(DialogInterface d, int i)
					{
						d.cancel();
					}}).show();
		}
		//	else
		//	{
		//			AlertDialog.Builder terhapus = new AlertDialog.Builder(this);
		//			terhapus.setCancelable(true);
		//			terhapus.setMessage("Folder ini sudah dihapus, kembali ke menu utama dan masuk lagi ke sini untuk me-refresh daftar folder");
		//			terhapus.setTitle("FOLDER SUDAH DIHAPUS...!").show();
		//			terhapus.setNegativeButton("OK", new DialogInterface.OnClickListener(){@Override
		//							public void onClick(DialogInterface d, int i)
		//							{
		//								d.cancel();
		//						}}).show();
		//	}

	}

	public void hapusFolderTerpilih(final String namafolder)
	{
		new Thread(new Runnable(){@Override
				public void run()
				{
					CommandCapture com = new CommandCapture(0, "rm -r /sdcard/clockworkmod/backup/" + namafolder);
					try
					{
						RootTools.getShell(false).add(com).waitForFinish();
					}
					catch (Exception e)
					{}
					if (!RootTools.exists("/sdcard/clockworkmod/backup" + namafolder))
					{
						tv.post(new Runnable(){@Override
								public void run()
								{
									Toast.makeText(getApplicationContext(), "Folder sudah dihapus", Toast.LENGTH_LONG).show();
								}});
					}
				}}).start();
	}

	public void onClick(View v)
	{
		fire();
	};

	String[] fireCode   = new String[]{"inyong f",                        "inyong sd","inyong s", "inyong d", "inyong e"};
	String[] dialogCode = new String[]{"Full\n(system, data, sd-ext)","System & Data", "System",  "Data",     "SD-ext"};
	private void fire()
	{
		if (!RootTools.exists("/system/bin/inyong"))
		{
			new Thread(new Runnable(){@Override
					public void run()
					{
						if (RootTools.isAccessGiven())
						{
							RootTools.installBinary(getApplicationContext(), R.raw.inyong, "inyong");
							try
							{
								RootTools.remount("/system", "rw");
								RootTools.getShell(true).add(new CommandCapture(0, "cp /data/data/inyong.xt530.tools/files/inyong /system/bin/", "chmod 755 /system/bin/inyong")).waitForFinish();
							}
							catch (Exception e)
							{}
							if (RootTools.exists("/system/bin/inyong"))
							{
								tombolBackup .post(new Runnable(){@Override
										public void run()
										{
											Toast.makeText(getApplicationContext(), "script installed", Toast.LENGTH_SHORT).show();
										}});
							}
						}
					}}).start();
		}

		AlertDialog.Builder abd = new AlertDialog.Builder(this);
		abd.setCancelable(true);
		abd.setTitle("PILIH PARTISI");
		abd.setItems(dialogCode, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					Intent i = new Intent(getApplicationContext(), BackupRun.class);
					i.putExtra(TASK_CODE, fireCode[p2]);
					startActivity(i);
				}
			}).show();
	}

}
