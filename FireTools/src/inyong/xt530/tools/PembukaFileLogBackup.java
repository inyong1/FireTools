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
import java.util.concurrent.*;
import com.stericson.RootTools.exceptions.*;

public class PembukaFileLogBackup extends Activity
{


	TextView tv, tvJudul;
	ScrollView sv;
	String namaFolder, path;
	Intent i;



    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.pembuka_file_log_nandroid_backup);
		tv = (TextView) findViewById(R.id.id_tv_pembuka_file_log);
		tvJudul = (TextView) findViewById(R.id.id_tv_judul_pembuka_file_log);
		sv = (ScrollView) findViewById(R.id.scroll_view_pembuka_file_log);
		i = getIntent();
		namaFolder = i.getStringExtra(DaftarFileNandroidBackupActivity.FOLDER_TERPILIH);
		path = new DaftarFileNandroidBackupActivity().path;
		tvJudul.setText(path + "/" + namaFolder);
		tampilkanLog();
	}


	private void tampilkanLog()
	{
		final String stringFileLogTarget =path + "/" + namaFolder + "/manual.and.log";
		File fileLogTarget =new File(stringFileLogTarget);
		if (!fileLogTarget.exists())
		{
			tv.setText("\nFile manual.and.log tidak ditemukan\n(Log file not found)");
		}
		else
		{
			tv.setText("");
			new Thread(new Runnable(){@Override
					public void run()
					{
						try
						{
							List<String> semuaBarisText= RootTools.sendShell("cat " + stringFileLogTarget, 0);
							for (final String baris:semuaBarisText)
							{
								if (!baris.equals("0"))
								{
									tv.post(new Runnable(){@Override
											public void run()
											{
												tv.append("\n" + baris);
												sv.fullScroll(View.FOCUS_DOWN);
											}});
									Thread.sleep(100);
								}
							}
						}
						catch (Exception e)
						{}
					}}).start();
		}
	}
}
