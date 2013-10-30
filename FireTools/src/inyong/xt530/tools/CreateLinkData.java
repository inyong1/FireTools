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

public class CreateLinkData extends Activity
{
	FungsiLinkData fld = new FungsiLinkData();
	ListView listviewLinked;//, listviewUnlinked;
	ArrayAdapter<String> listadapterlinked;//, listadapterunlinked;
	List<String> listdatalinked, listdataunlinked, listfinal ;
	TextView tv, tv2;
	boolean longClick = false;
	private String longTap ="Long tap an item for options";


	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.link_folder_data);
		listviewLinked = (ListView) findViewById(R.id.id_list_view_link_data_linked);
		setListViewListenner();
		tampilkanListFolderDataLinked();
		tampilkanListFolderDataUnLinked();
		tv2 = (TextView) findViewById(R.id.id_linkdata_tv_hitung);
		tampilkanPerhitungan();
	}

	private void setListViewListenner()
	{
		listviewLinked.setOnItemClickListener(new AdapterView.OnItemClickListener(){
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{ 
					Toast.makeText(getApplicationContext(), "Tap dan tahan (long press)\n pada nama folder", Toast.LENGTH_LONG).show();
				}	
			});

		listviewLinked.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					if (longClick == false)
					{
						Toast.makeText(getApplicationContext(), "Harap tunggu,\nSampai perhitungan selesai", Toast.LENGTH_LONG).show();
					}
					else
					{
						tv = (TextView) p2;
						eksekusiFolder(tv);
					}
					return true;
				}
			});
	}

	private void tampilkanPerhitungan()
	{
		new Thread(new Runnable(){@Override
				public void run()
				{
					while (fld.isHitung() == true)
					{
						tv2.post(new Runnable(){@Override
								public void run()
								{
									tv2.setText("Menghitung " + fld.getFolderTerhitung() + "/" + fld.getUnlinkedFolder() + " Folder\n"+fld.getFolderDihitung());
								}});
						try
						{
							Thread.sleep(30);
						}
						catch (InterruptedException e)
						{}
					}
					tv2.post(new Runnable(){@Override
							public void run()
							{
								tv2.setText(longTap);
								longClick = true;
							}});

				}}).start();
	}

	private void tampilkanListFolderDataLinked()
	{
		new Thread(new Runnable(){@Override
				public void run()
				{
					try
					{
						Thread.sleep(5);
					}
					catch (InterruptedException e)
					{}
					listdatalinked = fld.getListFolderData();
					listfinal = new ArrayList<String>();
					listfinal.addAll(listdatalinked);
					listadapterlinked = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_listview, listfinal);
					listviewLinked.post(new Runnable(){@Override
							public void run()
							{
								listviewLinked.setAdapter(listadapterlinked);
							}});

				}}).start();
	} 

	private void tampilkanListFolderDataUnLinked()
	{
		new Thread(new Runnable(){@Override
				public void run()
				{
					try
					{
						Thread.sleep(10);
					}
					catch (InterruptedException e)
					{}
					listdataunlinked = fld.getListFolderDataUnlinked();
					//		listadapterunlinked = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_listview, listdataunlinked);
					listfinal.addAll(listdataunlinked);
					listviewLinked.post(new Runnable(){@Override
							public void run()
							{
								//			listviewUnlinked.setAdapter(listadapterunlinked);
								listadapterlinked.notifyDataSetChanged();
							}});
				}}).start();
	} 


	private void eksekusiFolder(TextView tview)
	{
		String s = (String) tview.getText();
		if (s.contains("Linked"))
		{
			tampilkanDialog("Hapus Link (Restore)", tview);
		}
		else
		{
			tampilkanDialog("Buat Link (Create Link)", tview);
		}
	}

	private void tampilkanDialog(final String sOk,  final TextView tvFolder)
	{
		AlertDialog.Builder ad =new AlertDialog.Builder(this);
		ad.setCancelable(true);
		ad.setItems(new String[]{sOk,"Batal  (Cancel)"}, new DialogInterface.OnClickListener(){

				public void onClick(DialogInterface p1, int p2)
				{
					switch (p2)
					{
						case 0: linkOrUnlink(tvFolder);//Toast.makeText(getApplicationContext(), sFolder+"\nMaaf bro fungsi "+sOk+" belum dibikin", Toast.LENGTH_LONG).show();
							break;
						default : break;
					}
				}
			}).show();
	}

	private void linkOrUnlink(TextView textV)
	{
		final String stv= textV.getText().toString();
		final int index =listfinal.indexOf(stv);
		final String[] splited =stv.split(" ");
		if (stv.contains("Linked"))
		{
			tv2.setText("Working...");
			new Thread(new Runnable(){@Override
					public void run()
					{
						//to do for unlingking
						fld.unlink(splited[splited.length - 1], splited[0]);

						if (fld.isUnlingked() == true)
						{
							String terhitung = fld.hitungSizeFolderData(splited[0]);
							listfinal.set(index, terhitung);
							tv2.post(new Runnable(){@Override
									public void run()
									{
										listadapterlinked.notifyDataSetChanged();
										tv2.setText(longTap);
									}});
						}
						else
						{
							tv2.post(new Runnable(){@Override
									public void run()
									{
										tv2.setText("Proses gagal");
									}});
						}
					}}).start();
		}
		else
		{
			tv2.setText("Working...");
			new Thread(new Runnable(){@Override
					public void run()
					{
						//to do for linking
						fld.link(splited[0]);
						if (fld.isLinked() == true)
						{
							String linkedTo = fld.isLinkedTo(splited[0]);
							listfinal.set(index, splited[0] + " \n(Linked) -> "+linkedTo);
							tv2.post(new Runnable(){@Override
									public void run()
									{
										listadapterlinked.notifyDataSetChanged();
										tv2.setText(longTap);
									}});
						}
						else
						{
							tv2.post(new Runnable(){@Override
									public void run()
									{
										tv2.setText("Proses gagal");
									}});
						}
					}}).start();
		}
	}
}
