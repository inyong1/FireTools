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
import android.graphics.*;

public class BackupRun extends Activity implements OnClickListener
{
Typeface tf;
	Button skip, cancel;
	String firetask;
	TextView tv;
	ScrollView sv;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.jalankan_backup);

		tv = (TextView) findViewById(R.id.text_view_stdo_backup);
		sv = (ScrollView) findViewById(R.id.scroll_view_backup);

		skip = (Button) findViewById(R.id.id_tombol_skip_partisi); skip.setOnClickListener(this);
		cancel = (Button) findViewById(R.id.id_tombol_cancel_backup); cancel.setOnClickListener(this);
		startBackup();
		skip.setEnabled(false);
		tf = Typeface.MONOSPACE;
		tv.setTypeface(tf);
    }

	private void startBackup()
	{
		Intent intent =getIntent();
		firetask = intent.getStringExtra(backupMain.TASK_CODE);
		if (RootTools.isAccessGiven())
		{
			new Thread(new Runnable(){@Override
					public void run()
					{
						tv.post(new Runnable(){
								@Override
								public void run()
								{
									tv.append("\n");
									tv.append("\n");
									tv.append("executing... " + firetask + "\n");
									//		Toast.makeText(getApplicationContext(), firetask , Toast.LENGTH_LONG).show();
								}
							});
						RootTools.remount("/", "rw");
						try
						{
							RootTools.getShell(true).add(new CommandCapture(0, "echo \"\" > /fireout.txt", "rm /fire_tool_backup.log"));
							startReader();
							Thread.sleep(1000);
						}
						catch (Exception e)
						{}
						tv.post(new Runnable(){
								@Override
								public void run()
								{
									tv.setText("starting...\n");
								}
							});
						startFireScript();
					}

				}).start();
		}
		else
		{
			tv.setText("\n\n\n AGAN TIDAK MENDAPAT HAK AKSES SU");
		}
	}


	public void startFireScript()
	{
		CommandCapture cc = new CommandCapture(0, firetask + ">>/fireout.txt", "echo \"\" >> /fireout.txt", "echo stuvwxyz>>/fireout.txt");
		try
		{
			RootTools.getShell(true).add(cc).waitForFinish();
		}
		catch (Exception e)
		{}
	}



	LineNumberReader lnr;
	String baris = null;
	boolean ok=true;
	private void startReader()
	{
		deteckPid();
		new Thread(new Runnable(){@Override
				public void run()
				{
					if (RootTools.exists("/fireout.txt"))
					{
						try
						{
							lnr = new LineNumberReader(new FileReader("/fireout.txt"));
						}
						catch (FileNotFoundException e)
						{}
						while (ok)
						{
							try
							{
								tv.post(new Runnable(){@Override
										public void run()
										{
											try
											{
												if ((baris = lnr.readLine()) != null)
												{
													if (baris.contains("stuvwxyz"))
													{
														ok = false;
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
								Thread.sleep(10);
							}
							catch (InterruptedException e)
							{}
						}
						tv.post(new Runnable(){@Override
								public void run()
								{
									tv.append(" ----<DONE>----");
									tv.append("\n");
									tv.append("\n");
									sv.fullScroll(View.FOCUS_DOWN);
									skip.setEnabled(false);
									cancel.setEnabled(false);
								}});
						cancelBackup();
						openLogFile();
					}
					else
					{
						tv.post(new Runnable(){@Override
								public void run()
								{
									tv.append("Reader tidak menemukan file target");
								}});
					}
				}}).start();
	}

	//detect ID proses backup
	boolean ID = false;
	private void deteckPid()
	{
		new Thread(new Runnable(){@Override
				public void run()
				{
					while (ok)
					{
						ID = false;
						try
						{
							List<String> ls = RootTools.sendShell("pidof mk; pidof tar; pidof md5sum", 0);
							for(String i : ls){
								if(i.length() > 3){
									ID = true;
								}
							}
						}
						catch (Exception e)
						{}
		
							tv.post(new Runnable(){@Override
									public void run()
									{
										if(ID){
											skip.setEnabled(true);
										}else{
											skip.setEnabled(false);
										}

									}});
			
						try
						{
							Thread.sleep(500);
						}
						catch (InterruptedException e)
						{}
					}
				}}).start();
	}

	public void onClick(View tombol)
	{
		switch (tombol.getId())
		{
			case R.id.id_tombol_skip_partisi: skipPartisi(); break;
			case R.id.id_tombol_cancel_backup: skipPartisi();ok = false; break;
		}
	}

	public void skipPartisi()
	{
		try
		{
			RootTools.getShell(true).add(new CommandCapture(0, "killall mk", "killall tar", "killall md5sum")).waitForFinish();
		}
		catch (Exception e)
		{}
	}

	public void cancelBackup()
	{
		try
		{
			RootTools.getShell(true).add(new CommandCapture(0, "killall sh")).waitForFinish();
			RootTools.closeShell(true);
			lnr.close();

		}
		catch (Exception e)
		{}
	}

	String LOG;
	public void openLogFile()
	{
		if (RootTools.exists("/fire_tool_backup.log"))
		{

			tv.post(new Runnable(){@Override
					public void run()
					{
						tv.setText("");
					}});

			tv.post(new Runnable(){@Override
					public void run()
					{
						try
						{
							lnr = new LineNumberReader(new FileReader("/fire_tool_backup.log"));

							while ((LOG = lnr.readLine()) != null)
							{
								tv.append(" " + LOG + "\n");
							}
						}
						catch (Exception e)
						{}
					}});


			tv.post(new Runnable(){@Override
					public void run()
					{
						sv.fullScroll(View.FOCUS_DOWN);
					}});

		}
	}
}

