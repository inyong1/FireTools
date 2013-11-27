package inyong.xt530.tools;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.widget.*;
import inyong.xt530.tools.adapter.*;
import java.util.*; 


public class BuatLinkFolderData extends Activity
{
	Context context;
	TextView tv;
	ListView lv;
	ArrayList<ItemDetils> listDetilApp;
	ArrayList<ItemDetils> listDetilUserApp;
	ArrayList<ItemDetils> listDetilSystemApp;
	ListAppBaseAdapterKu adapterKu; 
	Thread threadPekerja;
	FungsiLinkData fungsi = new FungsiLinkData();

	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.create_link_folder_data);
		tv = (TextView) findViewById(R.id.id_tv_buatlink_folderata);
		lv = (ListView) findViewById(R.id.id_listview_buat_link_folderdata);
		context = getApplicationContext();
		listDetilApp = new ArrayList<ItemDetils>();
		new Handler().postDelayed(new Runnable(){@Override
				public void run()
				{ 
					adapterKu = new ListAppBaseAdapterKu(context, listDetilApp);
					lv.setAdapter(adapterKu);
					detekSemuaApp();
					tampilkanApp("user");
				}}, 100);
	}

////////////////////////////////
	// percobaan nampilin list app
	int berlangsung =0;
	int jumlahApp =0;
	boolean membuatList = true;
	String status="";
	ItemDetils itemApp;
	List<PackageInfo> listPackageUserApp;
	List<PackageInfo> listPackageSystemApp;
	PackageManager pm;
	List<PackageInfo> listPackage ;
	PackageInfo pInfo;
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
						Thread.sleep(100);
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
	private String  realPathFolderData;
	private String[] arrayRincianFolderData;
	private ApplicationInfo appInfo;
	
	private void tampilkanApp(final String ket)
	{
		//buat list hanya user app.........	
		threadPekerja = new Thread(new Runnable(){@Override 
				public void run()
				{
					listDetilApp.clear();
					listDetilApp.addAll(listDetilUserApp);
					status = "Membuat list " + ket + " app...";
				//	jumlahApp = listDetilUserApp.size();
				jumlahApp=0; //tes
					berlangsung = 0;
					membuatList = true;
					threadStatus.start();
					int totalApp=listDetilUserApp.size();
					for (int i=0;i < totalApp;i++)
					{
						appInfo  =listPackageUserApp.get(i).applicationInfo;
						
						itemApp = listDetilApp.get(i);
						itemApp.setNamaAplikasi(appInfo.loadLabel(pm).toString());
						itemApp.setIcon(appInfo.loadIcon(pm));
						
						listDetilApp.set(i, itemApp);
						berlangsung++;
						jumlahApp++;
					}
					
					//menambahkan rincian
					status = "Membuat rincian "+ket+" app... ";
					berlangsung = 0;
					for (int i=0;i < jumlahApp;i++)
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
						itemApp.setNamaFolderData(listPackageUserApp.get(i).packageName);
						realPathFolderData = arrayRincianFolderData[arrayRincianFolderData.length -1];
						itemApp.setFolderData(realPathFolderData);
						itemApp.setSizeFolderData("Data : "+fungsi.getSizeFolder(realPathFolderData));
						listDetilApp.set(i, itemApp);
						berlangsung++;
					}
					tv.post(new Runnable(){@Override public void run()
							{
								tv.setText("Total "+ket + " app : " + jumlahApp);
								adapterKu.notifyDataSetChanged();
							}});
					membuatList = false;
					listDetilUserApp.clear();
					listDetilUserApp.addAll(listDetilApp);
				fungsi.tutupShell();
				}});
		threadPekerja.start();

	}

}
