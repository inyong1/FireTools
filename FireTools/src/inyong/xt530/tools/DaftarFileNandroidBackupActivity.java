package inyong.xt530.tools;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.stericson.RootTools.*;
import com.stericson.RootTools.execution.*;
import inyong.xt530.tools.adapter.*;
import java.io.*;
import java.util.*;


public class DaftarFileNandroidBackupActivity extends Activity
{
	Button tombolBackup;
	public static String TASK_CODE;
	TextView tv;
	ListView lv;
	ArrayList<ItemDetils> fileBackupObjectList=new ArrayList<ItemDetils>();
	BaseAdapterku adapterKu;
	String path=Environment.getExternalStorageDirectory().toString() + "/clockworkmod/backup";
	FungsiLinkData fungsiLinkData = new FungsiLinkData();

	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.daftar_file_nandroid_backup);
		tv = (TextView) findViewById(R.id.id_textview_daftar_file_nandroid_backup);
		tombolBackup = (Button) findViewById(R.id.id_tombol_create_nandroid_backup); tombolBackup.setOnClickListener(detekKlik());
		lv = (ListView) findViewById(R.id.id_listview_daftar_file_nandroid_backup);
		//	tampilkanListFileBackup();
		lv.setOnItemClickListener(lvClickListener());
		periksaBinaryMkyaffs2image();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		new Handler().postDelayed(new Runnable(){@Override
				public void run()
				{
					tampilkanListFileBackup();
				}}, 200);
	}

	private View.OnClickListener detekKlik()
	{
		return new View.OnClickListener(){

			public void onClick(View p1)
			{
				//	gaweToast("tes");
				fire();
			}
		};
	}

	private void gaweToast(String s)
	{
		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}

	private List<String>  folderBackup()
	{ 
		List<String> hasil = new ArrayList<String>();

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
						hasil.add(i);
					}
				}
			}
			catch (Exception e)
			{}
		}
		else
		{
			tv.setText(getResources().getString(R.string.fileBackupTidakDitemukan));
		}

		return hasil;
	}

	private ArrayList<ItemDetils> dapatkanListFileBackup()
	{
		ArrayList<ItemDetils> hasil =new ArrayList<ItemDetils>();
		ItemDetils fileBackup;
		File folder;
		long totalUkuran=0 ,temp;

		List<String> listfolder = folderBackup();
		for (String s:listfolder)
		{

			fileBackup = new ItemDetils();

			folder = new File(path + "/" + s);
			if (folder.exists())
			{
				fileBackup.setJudul(s);
			}

			totalUkuran = 0;
			for (String str : folder.list())
			{
				if (str.equals("system.yaffs2.img") || str.equals("data.yaffs2.img") || str.contains("sd-ext"))
				{
					temp = new File(path + "/" + s + "/" + str).length();
					totalUkuran += temp;
					fileBackup.appendRincian(str + " : " + fungsiLinkData.longToStringSize(temp) + " (" + temp + " bytes)");
				}
			}

			fileBackup.setTotal("Total : " + fungsiLinkData.longToStringSize(totalUkuran) + " (" + totalUkuran + " bytes)");
			hasil.add(fileBackup);
		}

		return hasil;
	}

	private void tampilkanListFileBackup()
	{	
		fileBackupObjectList.clear();
		fileBackupObjectList.addAll(dapatkanListFileBackup());
		if (fileBackupObjectList.size() > 0)
		{
			adapterKu = new BaseAdapterku(this, fileBackupObjectList);
			lv.setAdapter(adapterKu);
			adapterKu.notifyDataSetChanged();
			tv.setText("");
		}
		else
		{
			tv.setText(getResources().getString(R.string.fileBackupTidakDitemukan));
		}
	}

	private AdapterView.OnItemClickListener lvClickListener()
	{
		return new AdapterView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
			{
				tampilkanPilihan(p3);
			}
		};
	}

	private void tampilkanPilihan(final int posisi)
	{
		AlertDialog.Builder d = new AlertDialog.Builder(this);
		Object o=lv.getItemAtPosition(posisi);
		ItemDetils i=(ItemDetils)o;
		d.setTitle(path + "/" + i.getJudul());
		d.setItems(getResources().getStringArray(R.array.pilihanHapusFolderNandroidBackup),
			new DialogInterface.OnClickListener(){

				public void onClick(DialogInterface p1, int p2)
				{
					switch (p2)
					{
						case 0:
							hapusFolderTerpilih(posisi);
							break;
						case 1:
						bukaLogFile(posisi);
							break;

						default : break;
					}
				}
			});
		d.show();
	}

	public void hapusFolderTerpilih(final int posisi)
	{
		new Thread(new Runnable(){@Override
				public void run()
				{
					Object o =lv.getItemAtPosition(posisi);
					ItemDetils item = (ItemDetils) o;
					String namafolder = item.getJudul();
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
									gaweToast("Folder sudah dihapus");
									fileBackupObjectList.remove(posisi);
									new Handler().postDelayed(new Runnable(){@Override
											public void run()
											{
												adapterKu.notifyDataSetChanged();
												if (fileBackupObjectList.size() == 0)
												{
													tv.setText(getResources().getString(R.string.fileBackupTidakDitemukan));
												}
											}}, 300);
								}});
					}
					else
					{
						tv.post(new Runnable(){@Override
								public void run()
								{
									gaweToast("Gagal menghapus");
								}});
					}
				}}).start();
	}

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
											gaweToast("script installed");
										}});
							}
							try
							{
								RootTools.closeShell(true);
							}
							catch (IOException e)
							{}
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
	private void periksaBinaryMkyaffs2image()
	{
		new Thread(new Runnable(){@Override public void run()
				{
					if (!RootTools.exists("/system/bin/mkyaffs2image"))
					{
						try
						{
							Thread.sleep(400);
						}
						catch (InterruptedException e)
						{}
						lv.post(new Runnable(){@Override 
								public void run()
								{
									gaweToast("mkyaffs2image binary\nnot found in path");
								}});
						try
						{
							Thread.sleep(100);
						}
						catch (InterruptedException e)
						{}
						lv.post(new Runnable(){@Override 
								public void run()
								{
									gaweToast("Installing...\nmkyaffs2image binary to\n/system/bin/");
								}});
						RootTools.installBinary(getApplicationContext(), R.raw.mkyaffs2image, "mkyaffs2image");
						CommandCapture cc = new CommandCapture(0, "busybox cp /data/data/inyong.xt530.tools/files/mkyaffs2image /system/bin/");
						try
						{
							RootTools.getShell(true).add(cc).waitForFinish();
							RootTools.closeShell(true);

						}
						catch (Exception e)
						{}

						{}lv.post(new Runnable(){@Override 
								public void run()
								{
									if (RootTools.exists("/system/bin/mkyaffs2image"))
									{
										gaweToast("mkyaffs2image\nInstalled successfully");
									}
									else
									{
										gaweToast("mkyaffs2image\ninstallation failed\n(not installed)");
									}
								}});
					}
				}}).start();
	}
	
	public static String FOLDER_TERPILIH;
	private void bukaLogFile(int posisiLvItem){
		Object o=lv.getItemAtPosition( posisiLvItem);
		ItemDetils item= (ItemDetils) o;
		Intent i = new Intent(getApplicationContext(), PembukaFileLogBackup.class);
		i.putExtra(FOLDER_TERPILIH, item.getJudul());
		startActivity(i);
	}
}
