package inyong.xt530.tools;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import com.stericson.RootTools.*;
import com.stericson.RootTools.execution.*;
import java.util.concurrent.*;
import com.stericson.RootTools.exceptions.*;
import java.io.*;
import java.util.*;

public class CreateLinkDalvik extends Activity implements OnClickListener
{
		Handler handler;
		Button cl, ul;
		TextView tv, guide, status;
		ScrollView sv;
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
				super.onCreate(savedInstanceState);
				setContentView(R.layout.create_link_dalvik_system);

				status    = (TextView) findViewById(R.id.id_text_view_status_link_dalvik);
				tv    = (TextView) findViewById(R.id.id_text_view_stdo_link_dalvik);
				guide = (TextView) findViewById(R.id.id_text_view_guide_link_dalvik);
				sv    = (ScrollView) findViewById(R.id.id_scroll_view_link_dalvik);

				cl = (Button) findViewById(R.id.id_tombol_create_link_dalvik); cl.setOnClickListener(this);
				ul = (Button) findViewById(R.id.id_tombol_unlink_dalvik); ul.setOnClickListener(this);
				cl.setEnabled(false);
				ul.setEnabled(false);
				handler = new Handler();
				installScript();
				cekStatus();
				
				new Handler().postDelayed(new Runnable(){@Override
								public void run()
								{getDalvikSize();}}, 1000);
		}

		String pathFolderFiles;
		boolean moveScript, restoreScript;
		public void installScript()
		{
				new Thread(new Runnable(){@Override
								public void run()
								{
										moveScript = false;
										restoreScript = false;
										pathFolderFiles = Environment.getDataDirectory().toString() + "/data/inyong.xt530.tools/files";
										if (!RootTools.exists(pathFolderFiles + "/move_cache.sh"))
										{
												RootTools.installBinary(getApplicationContext(), R.raw.move_cache, "move_cache.sh");
												if (RootTools.exists(pathFolderFiles + "/move_cache.sh"))
												{
														moveScript = true;
												}
										}
										if (!RootTools.exists(pathFolderFiles + "/restore_cache.sh"))
										{
												RootTools.installBinary(getApplicationContext(), R.raw.restore_cache, "restore_cache.sh");
												if (RootTools.exists(pathFolderFiles + "/restore_cache.sh"))
												{
														restoreScript = true;
												}
										}
										if (moveScript && restoreScript)
										{
												handler.post(new Runnable(){@Override
																public void run()
																{
																		Toast.makeText(getApplicationContext(), "Script installed", Toast.LENGTH_SHORT).show();
																}});
										}
								}}).start();
		}

		public void onClick(View v)
		{
				switch (v.getId())
				{
						case R.id.id_tombol_create_link_dalvik: konfirmasiLink(); break;
						case R.id.id_tombol_unlink_dalvik :     konfirmasiUnLink(); break;
				}
		}

		private void konfirmasiLink()
		{
				AlertDialog.Builder kon_link = new AlertDialog.Builder(this);
				kon_link.setCancelable(true);
				kon_link.setTitle("KONFIRMASI...!");
				kon_link.setMessage("Pindahkan file-fie dalvik-cache ke sd-ext (buat link)?\n\n(Do with your own risk!)");
				kon_link.setPositiveButton("Pindahkan", new DialogInterface.OnClickListener(){@Override
								public void onClick(DialogInterface d, int id)
								{
										moveCache();
								}
						});
				kon_link.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){@Override
								public void onClick(DialogInterface d, int id)
								{
										d.cancel();
								}}).show();
		}

		private void konfirmasiUnLink()
		{
				AlertDialog.Builder kon_link = new AlertDialog.Builder(this);
				kon_link.setCancelable(true);
				kon_link.setTitle("KONFIRMASI...!");
				kon_link.setMessage("Kembalikan file-fie dalvik-cache ke memori internal (hapus link)?");
				kon_link.setPositiveButton("Kembalikan", new DialogInterface.OnClickListener(){@Override
								public void onClick(DialogInterface d, int id)
								{
										restoreCache();
								}});
				kon_link.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){@Override
								public void onClick(DialogInterface d, int id)
								{
										d.cancel();
								}}).show();
		}


		public void moveCache()
		{

				String moveScriptFile = Environment.getDataDirectory().toString() + "/data/inyong.xt530.tools/files/move_cache.sh";
				if (new File(moveScriptFile).exists())
				{
						ekskusi(moveScriptFile);
				}
				else
				{
						tv.setText("Error. Move_cache.sh script not found in path");
				}
		}

		public void restoreCache()
		{
				String restoreScriptFile = Environment.getDataDirectory().toString() + "/data/inyong.xt530.tools/files/restore_cache.sh";
				if (new File(restoreScriptFile).exists())
				{
						ekskusi(restoreScriptFile);
				}
				else
				{
						tv.setText("Error. Restore_cache.sh script not found in path");
				}
		}

		boolean OK=true;
		LineNumberReader lnr;
		String baris;
		public void ekskusi(final String script)
		{
		
				if (RootTools.isAccessGiven())
				{
					status.setText("Please wait...");
					cl.setEnabled(false);
					ul.setEnabled(false);
						Thread reader = new Thread(new Runnable(){@Override
										public void run()
										{ 
												RootTools.remount("/", "rw");
												CommandCapture c = new CommandCapture(0, "echo \"\" > /std_o_link.txt");
												try
												{
														RootTools.getShell(true).add(c).waitForFinish();
												}
												catch (Exception e)
												{}

												try
												{
														lnr = new LineNumberReader(new FileReader("/std_o_link.txt"));
												}
												catch (FileNotFoundException e)
												{}
												tv.post(new Runnable(){@Override
																public void run()
																{
																		guide.setText("");
																}});

												Thread executor = new Thread(new Runnable(){@Override
																public void run()
																{
																	Command cm= new Command(0, "sh "+script+" >> /std_o_link.txt", "echo wisrampung >> /std_o_link.txt" ){

																		public void output(int p1, String p2)
																		{
																			// TODO: Implement this method
																		}
																	};
																	//	CommandCapture c =new CommandCapture(0, "sh "+script+" >> /std_o_link.txt", "echo wisrampung >> /std_o_link.txt");
																		try
																		{
																				RootTools.getShell(true).add(cm).waitForFinish();

																		}
																		catch (Exception e)
																		{}
																		{}}});
												executor.start();

												while (OK)
												{
														tv.post(new Runnable(){@Override
																		public void run()
																		{
																				try
																				{
																						if ((baris = lnr.readLine()) != null)
																						{
																								if (baris.contains("wisrampung"))
																								{
																										OK = false;
																								}
																								else
																								{
																										tv.append(baris + "\n");
																										sv.fullScroll(View.FOCUS_DOWN);
																								}
																						}
																				}
																				catch (IOException e)
																				{}
																		}});
														try
														{
																Thread.sleep(20);
														}
														catch (InterruptedException e)
														{}
												}
												try
												{
														lnr.close();
												}
												catch (IOException e)
												{}
												tv.post(new Runnable(){@Override
																public void run()
																{
																		tv.append("    ----<DONE>---");
																		sv.fullScroll(View.FOCUS_DOWN);
																		cekStatus();
																}});
																

										}});
						reader.start();

				}
		}

		long freeSpace, totalSpace, totalDalvik, freeSpaceExt;
		String freeinternal, freeext, dalvikFinal, jumfiledalvik, dalvikterlink, dalviktakterlink;
		int jumlahFileDalvik, dalvikTerlink, dalvikTakTerlink;
		public void cekStatus()
		{
				new Thread(new Runnable(){@Override
								public void run()
								{	
										freeinternal = getFreeSpaceData();
										freeext = getFreeSpaceExt();
										totalDalvik  = getDalvikSize();
										dalvikFinal = totalDalvik + "kb";
										if (totalDalvik > 1024)
										{
												dalvikFinal = (totalDalvik / 1024) + "MB";
										}
										jumfiledalvik = jumlahFileDalvik + " file(s)";
										dalvikterlink = jumlahFileDalvik - dalvikTakTerlink + " file(s)";
										dalviktakterlink = dalvikTakTerlink + " file(s)";
										handler.post(new Runnable(){@Override
														public void run()
														{
																status.setText(freeinternal	+" -- Free internal memory\n"+ 
																							 freeext +     " -- free sd-ext memory\n"+
																							 dalvikFinal + " -- Total(size) dalvik-cache in internal\n"+
																							 jumfiledalvik+" -- Dalvik-cache system\n"+
																							 dalvikterlink+" -- Linked (ter-link) to sd-ext\n"+
																							 dalviktakterlink+" -- Unlinked (tidak ter-link)");
														if(dalvikTakTerlink > 0){
															cl.setEnabled(true);
														}else{
															cl.setEnabled(false);
														}
														if((jumlahFileDalvik - dalvikTakTerlink)>0){
															ul.setEnabled(true);
														}else{
															ul.setEnabled(false);
														}
														}});

								}}).start();
		}

		public String getFreeSpaceData()
		{
				String hasil=null;
				try
				{
						List<String> l = RootTools.sendShell("df /data | tail -1 | awk '{print $4}'", 0);
						hasil = l.get(0);
						return hasil;
				}
				catch (Exception e)
				{}
				return hasil;
		}

		public String getFreeSpaceExt()
		{
				String hasil=null;
				try
				{
						List<String> l = RootTools.sendShell("df /data/sdext2 | tail -1 | awk '{print $4}'", 0);
						hasil = l.get(0);
						return hasil;
				}
				catch (Exception e)
				{}
				return hasil;
		}

		// menghitung size seluruh file dalvik
		public long getDalvikSize()
		{			dalvikTerlink = 0;
				dalvikTakTerlink = 0;
				long total =0;
				try
				{
						List<String> files = RootTools.sendShell("ls -l /data/dalvik-cache/system@app*", 0);
						jumlahFileDalvik = files.size() - 3;
						String[] sa;
						for (String i:files)
						{
								if (!i.contains("->"))
								{
										dalvikTakTerlink += 1;
										sa = i.split(" ");
										total += new File("/data/dalvik-cache/" + sa[sa.length - 1]).length();
								}
						}
						total /= 1024;
						dalvikTakTerlink -=3;
				}
				catch (Exception e)
				{}
				return total;
		}


}
