package inyong.xt530.tools;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import inyong.xt530.tools.fungsiFungsi.*;

public class SmsContactTools extends Activity implements OnClickListener
{
	LinearLayout tabSmsTool, tabContactTool;
	FungsiBackupSmsContact fungsi;
	TextView tvSms, tvContact;
	String tabAktif = "SMS";
	Button tombolBackupSms, tombolBackupContact;

	@Override
	public void onCreate(Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.tab_sms_tool);
		deteksiTab();
		fungsi = new FungsiBackupSmsContact();
		detekTombolDanTvTabSms();
		tampilkanListFileBackupSms();
		tampilkanTextStatus();
	}

	private void tampilkanTextStatus()
	{
		if (listFileBackup.size() > 0)
		{
			if (tabAktif == "SMS")
			{
				tvSms.setText(R.string.tap_file_backup);
			}
			else
			{
				tvContact.setText(R.string.tap_file_backup);
			}
		}
		else
		{
			if (tabAktif == "SMS")
			{
				tvSms.setText(R.string.tidak_ada_file_backup);
			}
			else
			{
				tvContact.setText(R.string.tidak_ada_file_backup);
			}
		}
	}

	private void detekTombolDanTvTabSms()
	{
		tvSms = (TextView) findViewById(R.id.id_tv_tab_sms_status);
		tombolBackupSms = (Button) findViewById(R.id.id_tombol_backup_sms);
		tombolBackupSms.setOnClickListener(this);
	}

	private void detekTombolDanTvTabContact()
	{
		tvContact = (TextView) findViewById(R.id.id_tv_tab_contact_status);
		tombolBackupContact = (Button) findViewById(R.id.id_tombol_backup_contact);
		tombolBackupContact.setOnClickListener(this);
	}

	private void deteksiTab()
	{
		tabSmsTool =     (LinearLayout) findViewById(R.id.id_tab_sms_tool);     tabSmsTool.setOnClickListener(this);
		tabContactTool = (LinearLayout) findViewById(R.id.id_tab_contact_tool); tabContactTool.setOnClickListener(this);
	}

	///////////////////////////////////
	List<String> listFileBackup;
	ArrayAdapter adapter;
	ListView lv;
	private void tampilkanListFileBackupSms()
	{
		lv = (ListView) findViewById(R.id.id_lv_backup_sms);
		listFileBackup = new ArrayList<String>();
		listFileBackup = fungsi.getLisfFiles(fungsi.folderBackupSms);
		adapter = new ArrayAdapter(this, R.layout.row_listview_sms_contact_tool, listFileBackup);
		lv.setAdapter(adapter);
		tampilkanTextStatus();
		detekLongClickPadaListView();
	}

	private void tampilkanListFileBackupContact()
	{
		lv = (ListView) findViewById(R.id.id_lv_backup_contact);
		listFileBackup = new ArrayList<String>();
		listFileBackup = fungsi.getLisfFiles(fungsi.folderBackupContact);
		adapter = new ArrayAdapter(this, R.layout.row_listview_sms_contact_tool, listFileBackup);
		lv.setAdapter(adapter);
		tampilkanTextStatus();
		detekLongClickPadaListView();
	}

	public void updateListView(List<String> listString, String pathSumber)
	{
		if (listString.size() > 0)
		{
			listString.clear();
		}
		listString.addAll(fungsi.getLisfFiles(pathSumber));
		adapter.notifyDataSetChanged();
	}
	/////////////////////////////////////

	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.id_tab_sms_tool:
				setContentView(R.layout.tab_sms_tool);
				tabAktif = "SMS";
				deteksiTab(); 
				detekTombolDanTvTabSms();
				tampilkanListFileBackupSms();
				detekLongClickPadaListView();
				break;
			case R.id.id_tab_contact_tool: 
				setContentView(R.layout.tab_contact_tool); 
				tabAktif = "CONTACT";
				deteksiTab();
				detekTombolDanTvTabContact();
				tampilkanListFileBackupContact();
				break;
			case  R.id.id_tombol_backup_sms:
				backupSms(); break;
			case R.id.id_tombol_backup_contact:
				backupContact();
				break;
			default: break;
		}
	}

	public void backupContact()
	{
		tvContact.setText("Working...");
		tombolBackupContact.setEnabled(false);
		new Thread(new Runnable(){@Override 
				public void run()
				{
					fungsi.setTanggalDanJam();
					final File folderBackupContact =	new File(fungsi.folderBackupContact + "/" + fungsi.getTanggalDanJam());
					if (!folderBackupContact.exists())
					{
						folderBackupContact.mkdirs();
					}

					if (fungsi.backupContact() == true)
					{
						tabContactTool.post(new Runnable(){@Override public void run()
								{
									buatToast(getApplicationContext(), "Backup Sukses");
									updateListView(listFileBackup, fungsi.folderBackupContact);
								}});
					}
					else
					{
						tabContactTool.post(new Runnable(){@Override public void run()
								{
									buatToast(getApplicationContext(), "Backup Gagal");
								}});
					}
					tvContact.post(new Runnable(){@Override public void run()
							{
								tampilkanTextStatus();
								tombolBackupContact.setEnabled(true);
							}});
				}}).start();

	}

	private void backupSms()
	{
		tvSms.setText("Working...");
		tombolBackupSms.setEnabled(false);
		new Thread(new Runnable(){@Override 
				public void run()
				{
					fungsi.setTanggalDanJam();
					final File folderBackupSms =	new File(fungsi.folderBackupSms + "/" + fungsi.getTanggalDanJam());
					if (!folderBackupSms.exists())
					{
						folderBackupSms.mkdirs();
					}

					if (fungsi.backupSms() == true)
					{
						tabSmsTool.post(new Runnable(){@Override public void run()
								{
									buatToast(getApplicationContext(), "Backup Sukses");
									updateListView(listFileBackup, fungsi.folderBackupSms);
								}});
					}
					else
					{
						tabSmsTool.post(new Runnable(){@Override public void run()
								{
									buatToast(getApplicationContext(), "Backup Gagal");
								}});
					}
					tvSms.post(new Runnable(){@Override public void run()
							{
								tvSms.setText(R.string.tap_file_backup);
								tombolBackupSms.setEnabled(true);
							}});
				}}).start();

	}



	public void buatToast(Context c, String s)
	{
		Toast.makeText(c, s, Toast.LENGTH_SHORT).show();
	}

	private void detekLongClickPadaListView()
	{
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
				public boolean onItemLongClick(AdapterView<?> p1, View view, int p3, long p4)
				{
					tampilkanPilihanRestoreDanHapus(view);
					return true;
				}
			});
	}

	private void tampilkanPilihanRestoreDanHapus(final View v)
	{
		String[] pilihan = new String[]{"Kembalikan (*Beta version)\n(Restore)","Hapus file backup\n(delete)","Hapus Semua file backup\n(delete all backup)"};
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setCancelable(true);
		dialog.setItems(pilihan, new DialogInterface.OnClickListener(){@Override
				public void onClick(DialogInterface p1, int p2)
				{
					TextView tvList = (TextView) v;
					String namaFolderBackup = tvList.getText().toString();
					String fullPathfolderBackup = fungsi.folderBackupSms + "/" + namaFolderBackup;
					String parentFolder = fungsi.folderBackupSms;
					if (!new File(fullPathfolderBackup).exists())
					{
						fullPathfolderBackup = fungsi.folderBackupContact + "/" + namaFolderBackup;
						parentFolder = fungsi.folderBackupContact;
					}
					//	buatToast(getApplicationContext() , fullPathfolderBackup);
					switch (p2)
					{
						case 0: // restore
					//		restore(fullPathfolderBackup);
					konfirmasiRestore(fullPathfolderBackup);
							break;
						case 1:  // hapus
							hapus(fullPathfolderBackup);
							break;
						case 2:  // hapus smua
							hapusSmua(parentFolder);
							break;
						default: break;
					}
				}

			});
		dialog.show();
	}

	// fungsi hapus smua
	private void hapusSmua(final String parentFolder)
	{
		hapus(parentFolder);
	}

	// fungsi hapus
	private void hapus(final String fullPathfolderBackup)
	{
		new Handler().postDelayed(new Runnable(){
				@Override public void run()
				{
					if (fungsi.hapusFileAtauFolder(fullPathfolderBackup))
					{
						if (tabAktif.equals("SMS"))
						{
							updateListView(listFileBackup, fungsi.folderBackupSms);
						}
						else
						{
							updateListView(listFileBackup, fungsi.folderBackupContact);
						}
						tampilkanTextStatus();
					}
					else
					{
						buatToast(getApplicationContext(), "Gagal menghapus");
					}
				}}, 500);
	}

	// fungsi restore
//	boolean restore;
	private void konfirmasiRestore(final String path){
		AlertDialog.Builder konfirmasi = new AlertDialog.Builder(this);
		konfirmasi.setCancelable(true);
		konfirmasi.setTitle(getResources().getString(R.string.titel_konfirmasi_restore_backup));
		konfirmasi.setMessage(getResources().getString(R.string.pesan_konfirmasi_restore_backup));
		konfirmasi.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){@Override
		public void onClick(DialogInterface d,int i){
			
		}});
		konfirmasi.setPositiveButton("OK",new DialogInterface.OnClickListener(){@Override
		public void onClick(DialogInterface d,int i){
			restore(path);
		}});
		konfirmasi.show();
	}
	private void restore(final String fullPathfolderBackup)
	{
		new Thread(new Runnable(){@Override
				public void run()
				{
			//		restore = false;
					if (fullPathfolderBackup.contains("Sms"))
					{
						tvSms.post(new Runnable(){@Override 
								public void run()
								{
									tvSms.setText("Working....");
								}});
						if (fungsi.restoreSms(fullPathfolderBackup) == true)
						{

							tvSms.post(new Runnable(){@Override 
									public void run()
									{
										buatToast(getApplicationContext(), "Restore sukses");
										tampilkanTextStatus();
								//		restore = true;
									}

								});
						}
						else
						{
							tvSms.post(new Runnable(){@Override 
									public void run()
									{
										buatToast(getApplicationContext(), "Restore gagal");
										tampilkanTextStatus();
									}});
						}
					}
					else
					{
						tvContact.post(new Runnable(){@Override 
								public void run()
								{
									tvContact.setText("Working....");
								}});
						if (fungsi.restoreContact(fullPathfolderBackup) == true)
						{

							tvContact.post(new Runnable(){@Override 
									public void run()
									{
										buatToast(getApplicationContext(), "Restore sukses");
										tampilkanTextStatus();
								//		restore = true;
									}});
						}
						else
						{
							tvContact.post(new Runnable(){@Override 
									public void run()
									{
										buatToast(getApplicationContext(), "Restore gagal");
										tampilkanTextStatus();
									}});
						}

					}
			//		lv.post(new Runnable(){@Override
			//		public void run(){
			//			dialogRestart();
			//		}});
				}}).start();
	}

//	public void dialogRestart()
//	{
//		AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
//;		dialog.setTitle("Restart Dibutuhkan");
//		dialog.setMessage("Pada versi beta ini, setelah melakukan restore HH harus direstart agar hasil restore terbaca oleh system").show();
//		dialog.setPositiveButton("Quick Restart", new DialogInterface.OnClickListener(){@Override
//				public void onClick(DialogInterface d, int id)
//				{
					//			restartCepat();
//				}});
//		dialog.setNegativeButton("Restart Nanti", new DialogInterface.OnClickListener(){@Override
//				public void onClick(DialogInterface d, int id)
//				{
//
//				}});

//	}

}
