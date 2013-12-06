package inyong.xt530.tools;

import android.app.*;
import android.view.*;
import android.view.View.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.widget.*;
import inyong.xt530.tools.adapter.*;
import inyong.xt530.tools.fungsiFungsi.*;
import java.util.*; 


public class BuatLinkFolderData extends Activity implements OnClickListener
{
	Button tombolUserApp, tombolSystemApp;
	Context context;
	TextView tv;
	ListView lv;
	ArrayList<ItemDetils> listDetilApp;
	ArrayList<ItemDetils> listDetilUserApp;
	ArrayList<ItemDetils> listDetilSystemApp;
	ListAppBaseAdapterKu adapterKu; 
	Thread threadPekerja;
	FungsiLinkData fungsi = new FungsiLinkData();
	boolean userApp=true;

	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.create_link_folder_data);
		tombolUserApp = (Button) findViewById(R.id.id_create_link_data_tombol_userapp); tombolUserApp.setOnClickListener(this);
		tombolSystemApp = (Button) findViewById(R.id.id_create_link_data_tombol_systemapp); tombolSystemApp.setOnClickListener(this);
		new Handler().postDelayed(new Runnable(){@Override
				public void run()
				{ 
					tv = (TextView) findViewById(R.id.id_tv_buatlink_folderata);
					lv = (ListView) findViewById(R.id.id_listview_buat_link_folderdata);
					context = getApplicationContext();
					listDetilApp = new ArrayList<ItemDetils>();
					adapterKu = new ListAppBaseAdapterKu(context, listDetilApp);
					lv.setAdapter(adapterKu);
					detekSemuaApp();
					tampilkanApp("user");
					//buat listDetil system app di background
					buatListSystemApp();
					tombolSystemApp.setEnabled(false);
					tombolUserApp.setEnabled(false);
					lv.setOnItemClickListener(lvClickListener());
				}}, 100);
	}

////////////////////////////////
	// percobaan nampilin list app
	int berlangsung =0;
	int jumlahApp =0;
	String status="";

	Thread	threadStatus =	new Thread(new Runnable(){@Override
			public void run()
			{
				while (membuatList)
				{
					tv.post(new Runnable(){@Override 
							public void run()
							{						
								tv.setText(status + " (" + berlangsung + "/" + jumlahApp + ")");
								adapterKu.notifyDataSetChanged();
							}});
					try
					{
						Thread.sleep(400);
					}
					catch (InterruptedException e)
					{}
				}
			}});

	private void detekSemuaApp()
	{
		tv.setText("Detek semua app...");
		pm = getApplicationContext().getPackageManager(); 
		listPackage = pm.getInstalledPackages(0);
		PackageInfo pInfo;

		listPackageUserApp = new ArrayList<PackageInfo>();
		listPackageSystemApp = new ArrayList<PackageInfo>();
		listDetilUserApp = new ArrayList<ItemDetils>();
		listDetilSystemApp = new ArrayList<ItemDetils>();

		jumlahApp = listPackage.size();
		for (int i=0;i < jumlahApp;i++)
		{
			pInfo = listPackage.get(i);
			if (pInfo.applicationInfo.sourceDir.contains("system"))
			{
				listPackageSystemApp.add(pInfo);
				listDetilSystemApp.add(new ItemDetils());
			}
			else
			{
				listPackageUserApp.add(pInfo);
				listDetilUserApp.add(new ItemDetils());	
			}
		}
	}

	////////////
	List<PackageInfo> listPackageUserApp;
	List<PackageInfo> listPackageSystemApp;
	PackageManager pm;
	List<PackageInfo> listPackage ;
	boolean membuatList = true;

	private void tampilkanApp(final String ket)
	{
		//buat list hanya user app.........	
		threadPekerja = new Thread(new Runnable(){@Override 
				public void run()
				{
					ItemDetils itemApp;
					listDetilApp.clear();
					listDetilApp.addAll(listDetilUserApp);
					ApplicationInfo appInfo;
					status = "Membuat list " + ket + " app...";
					jumlahApp = 0; //tes
					berlangsung = 0;
					membuatList = true;
					threadStatus.start();
					int totalApp=listDetilUserApp.size();
					for (int i=0;i < totalApp;i++)
					{
						appInfo  = listPackageUserApp.get(i).applicationInfo;

						itemApp = listDetilApp.get(i);
						itemApp.setNamaAplikasi(appInfo.loadLabel(pm).toString());
						//	itemApp.setIcon(appInfo.loadIcon(pm));
						itemApp.setNamaFolderData(listPackageUserApp.get(i).packageName);

						listDetilApp.set(i, itemApp);
						//	berlangsung++;
						jumlahApp++;
					}

					//menambahkan rincian
					String[] arrayRincianFolderData;
					String realPathFolderData;
					status = "Membuat rincian " + ket + " app... ";
					berlangsung = 0;
					for (int i=0;i < jumlahApp;i++) //jumlahApp
					{
						arrayRincianFolderData = fungsi.getRincianFolderData(listPackageUserApp.get(i).applicationInfo.dataDir);
						itemApp = listDetilApp.get(i);
						if (arrayRincianFolderData[0].startsWith("l"))
						{
							itemApp.setStatusTerlink("Ter-link");
							itemApp.setStatusOnInternal("");
						}
						else
						{
							itemApp.setStatusTerlink("");
							itemApp.setStatusOnInternal("On Internal");
						}
						//	itemApp.setNamaFolderData(listPackageUserApp.get(i).packageName);
						realPathFolderData = arrayRincianFolderData[arrayRincianFolderData.length - 1];
						itemApp.setFolderData(realPathFolderData);
						itemApp.setSizeFolderData("Data : " + fungsi.getSizeFolder(realPathFolderData));
						itemApp.setIcon(listPackageUserApp.get(i).applicationInfo.loadIcon(pm));
						listDetilApp.set(i, itemApp);
						berlangsung++;
					}
					tv.post(new Runnable(){@Override public void run()
							{
								tv.setText("Total " + ket + " app : " + jumlahApp);
								adapterKu.notifyDataSetChanged();
								//aktifkan tombol
								tombolSystemApp.setEnabled(true);
							}});
					membuatList = false;
					listDetilUserApp.clear();
					listDetilUserApp.addAll(listDetilApp);
					fungsi.tutupShell();
				}});
		threadPekerja.start();

	}

	// buat list system app secara diam2
	// biar saat diperlukan tinggal pake
	boolean buatListDetilAppSystem = true;
	int totalAppSystem=0, jumlahAppSystemTerhitung=0;

	private void buatListSystemApp()
	{
		new Thread(new Runnable(){@Override
				public void run()
				{
					ItemDetils item;
					PackageInfo packInfoSystem;
					ApplicationInfo appInfoSystem;
					String[] rincianFolder;
					totalAppSystem = listDetilSystemApp.size();
					FungsiLinkData fungsi2 = new FungsiLinkData();

					for (int i = 0; i < totalAppSystem;i++)
					{
						item = listDetilSystemApp.get(i);
						packInfoSystem = listPackageSystemApp.get(i);
						appInfoSystem = packInfoSystem.applicationInfo;
						rincianFolder = fungsi2.getRincianFolderData(appInfoSystem.dataDir);

						item.setNamaFolderData(packInfoSystem.packageName);
						item.setIcon(appInfoSystem.loadIcon(pm));
						item.setNamaAplikasi(appInfoSystem.loadLabel(pm).toString());
						item.setFolderData(rincianFolder[rincianFolder.length - 1]);
						item.setSizeFolderData("Data : " + fungsi2.getSizeFolder(rincianFolder[rincianFolder.length - 1]));

						if (rincianFolder[0].startsWith("l"))
						{
							item.setStatusTerlink("Ter-link");
							item.setStatusOnInternal("");
						}
						else
						{
							item.setStatusTerlink("");
							item.setStatusOnInternal("On Internal");
						}
						listDetilSystemApp.set(i, item);
						jumlahAppSystemTerhitung++;
					}
					buatListDetilAppSystem = false;
				}}).start();
	}

	// detek saat tombol diklik
	public void onClick(View tombolTerKlik)
	{
		switch (tombolTerKlik.getId())
		{
			case R.id.id_create_link_data_tombol_systemapp:
				tombolSystemApp.setEnabled(false);
				tombolUserApp.setEnabled(true);
				tampilkanListSystemApp();
				userApp = false;
				break;
			case R.id.id_create_link_data_tombol_userapp:
				tampilkanListUserApp();
				tombolSystemApp.setEnabled(true);
				tombolUserApp.setEnabled(false);
				userApp = true;
				break;
			default: break;
		}
	}

	private void tampilkanListSystemApp()
	{
		tv.setText("System app : " + totalAppSystem);
		listDetilApp.clear();
		listDetilApp.addAll(listDetilSystemApp);
		adapterKu.notifyDataSetChanged();

		if (buatListDetilAppSystem == true)
		{
			tombolUserApp.setEnabled(false);
			new Thread(new Runnable(){@Override
					public void run()
					{
						while (buatListDetilAppSystem == true)
						{
							tv.post(new Runnable(){@Override
									public void run()
									{
										tv.setText("Membuat list system app... (" + jumlahAppSystemTerhitung + "/" + totalAppSystem + ")");
										adapterKu.notifyDataSetChanged();
									}});
							try
							{
								Thread.sleep(500);
							}
							catch (InterruptedException e)
							{}
						}
						membuatList = false;
						tv.post(new Runnable(){@Override
								public void run()
								{
									tv.setText("System app : " + totalAppSystem);
									tombolUserApp.setEnabled(true);
								}});
					}}).start();
		}


	}

	private void tampilkanListUserApp()
	{

		listDetilApp.clear();
		listDetilApp.addAll(listDetilUserApp);
		tv.setText("User app : " + jumlahApp);
		adapterKu.notifyDataSetChanged();
	}
	private void gaweToast(String str)
	{
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	//clickListener
	String strListDetilsTarget;
	private AdapterView.OnItemClickListener lvClickListener()
	{
		return new AdapterView.OnItemClickListener(){

			public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
			{
				if (userApp)
				{
					tampilkanPilihan(listDetilUserApp, p3);
					strListDetilsTarget = listDetilUserApp.toString();
				}
				else
				{
					tampilkanPilihan(listDetilSystemApp, p3);
					strListDetilsTarget = listDetilSystemApp.toString();
				}
			}
		};
	}

	private void tampilkanPilihan(final List<ItemDetils> list, final int posisi)
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		final ItemDetils item = list.get(posisi);
		dialog.setTitle(item.getNamaAplikasi() + "\n(" + item.getNamaFolderData() + ")");
		if (item.getStatusOnInternal().equals("On Internal"))
		{
			dialog.setItems(getResources().getStringArray(R.array.pilihanBuatLinkPadaListCreateLinkData), new DialogInterface.OnClickListener(){@Override
					public void onClick(DialogInterface d, int p)
					{
						switch (p)
						{
							case 0: buatLinkData(posisi, item);
								break;
							case 1: jalankanAplikasi(item.getNamaFolderData());
								break;
							default: break;
						}
					}});
		}
		else if (item.getStatusTerlink().equals("Ter-link"))
		{
			dialog.setItems(getResources().getStringArray(R.array.pilihanHapusLinkPadaListCreateLinkData), new DialogInterface.OnClickListener(){@Override
					public void onClick(DialogInterface d, int p)
					{
						switch (p)
						{
							case 0: hapusLink(posisi, item);
								break;
							case 1: jalankanAplikasi(item.getNamaFolderData());
								break;
							default: break;
						}
					}});
		}
		else
		{
			dialog.setMessage("Harap tunggu\n(Please wait)");
			dialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){@Override
					public void onClick(DialogInterface d, int p)
					{}});
		}
		dialog.show();
	}

	private void jalankanAplikasi(String packageName)
	{
		Intent i = pm.getLaunchIntentForPackage(packageName);
		if (i != null)
		{
			//	i.addCategory(Intent.CATEGORY_LAUNCHER);
//	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		}
		else
		{
			gaweToast("(!) Tidak dapat dijalankan");
		}
	}

	//fungsi update ui pada create link
	private void buatLinkData(final int posisiList, final ItemDetils iDetil)
	{
		tv.setText("Working...");
		new Thread(new Runnable(){@Override
				public void run()
				{
					FungsiLinkData fld = new FungsiLinkData();
					if (fld.linkanFolder(iDetil.getNamaFolderData()) == true)
					{
						final String[]	 rincianFolder = fld.getRincianFolderData("/data/data/" + iDetil.getNamaFolderData());
						tv.postDelayed(new Runnable(){@Override
								public void run()
								{
									ItemDetils i = iDetil;
									i.setFolderData(rincianFolder[rincianFolder.length - 1]);
									i.setStatusOnInternal("");
									i.setStatusTerlink("Ter-link");
									listDetilApp.set(posisiList, i);
									if (userApp)
									{
										listDetilUserApp.set(posisiList, i);
										tv.setText("User app : " + listDetilUserApp.size());
									}
									else
									{
										listDetilSystemApp.set(posisiList, i);
										tv.setText("System app : " + listDetilSystemApp.size());
									}
									adapterKu.notifyDataSetChanged();
									gaweToast("Sukses\n(Success)");
								}}, 200);
					}
					else
					{
						tv.post(new Runnable(){
								@Override public void run()
								{
									gaweToast("Gagal\n(Failed)");
								}
							});
					}
				}}).start();
	}

	//fubgsi hapus link/ restore folder data ke internal
	private void hapusLink(final int posisi, final ItemDetils iDetil)
	{
		tv.setText("Working...");
		new Thread(new Runnable(){@Override
				public void run()
				{
					FungsiLinkData fld = new FungsiLinkData();
					if (fld.unlinkFolder(iDetil.getFolderData(),iDetil.getNamaFolderData()) == true)
					{
						final String[]	 rincianFolder = fld.getRincianFolderData("/data/data/" + iDetil.getNamaFolderData());
						tv.postDelayed(new Runnable(){@Override
								public void run()
								{
									ItemDetils i = iDetil;
									i.setFolderData(rincianFolder[rincianFolder.length - 1]);
									i.setStatusOnInternal("On Internal");
									i.setStatusTerlink("");
									listDetilApp.set(posisi, i);
									if (userApp)
									{
										listDetilUserApp.set(posisi, i);
										tv.setText("User app : " + listDetilUserApp.size());
									}
									else
									{
										listDetilSystemApp.set(posisi, i);
										tv.setText("System app : " + listDetilSystemApp.size());
									}
									adapterKu.notifyDataSetChanged();
									gaweToast("Sukses\n(Success)");
								}}, 200);
					}
					else
					{
						tv.post(new Runnable(){
								@Override public void run()
								{
									gaweToast("Gagal\n(Failed)");
								}
							});
					}
				}}).start();
	}
}
