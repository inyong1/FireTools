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
import inyong.xt530.tools.fungsiFungsi.*;
import java.io.*;
import java.util.*;

public class ClearSystemLog extends Activity implements OnClickListener
{ 
	FungsiClearLog fcl = new FungsiClearLog();
	Button clearButton;
	TextView jumFile, jumFileSize,
	jumFileTomb, jumFileSizeTomb,
	jumFileDrop, jumFileSizeDrop,
	jumFileLog, jumFileSizeLog;
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.clear_system_log);
		clearButton = (Button)findViewById(R.id.id_tombol_clear_log);
		clearButton.setOnClickListener(this);
     	clearButton.setEnabled(false);
		detekTextView();
		hitungData();
	}

	//detect textView
	private void detekTextView()
	{
		jumFile = (TextView) findViewById(R.id.id_tabel_row_total_file);
		jumFileSize = (TextView) findViewById(R.id.id_tabel_row_total_size);
		jumFileTomb = (TextView) findViewById(R.id.id_clear_log_tv_tomb_total_file);
		jumFileSizeTomb = (TextView) findViewById(R.id.id_clear_log_tv_tomb_total_size);
		jumFileDrop = (TextView) findViewById(R.id.id_clear_log_tv_drop_total_file);
		jumFileSizeDrop = (TextView) findViewById(R.id.id_clear_log_tv_drop_total_size);
		jumFileLog = (TextView) findViewById(R.id.id_clear_log_tv_total_log_file);
		jumFileSizeLog = (TextView) findViewById(R.id.id_clear_log_tv_total_log_size);
	}

	//saat tombol ditap
	public void onClick(View p1)
	{
		bersihkan();
	}

	private void bersihkan()
	{
		
		clearButton.setEnabled(false);
		fcl.clearResidualFiles();
		hitungData();
	}

	int file;
	long size;
	private void hitungData()
	{
		setTvComputing();
		new Handler().postDelayed(new Runnable(){@Override
				public void run()
				{
					file = 0;size = 0;
					String data= Environment.getDataDirectory().toString();
					hitungFileDanSize(data + "/tombstones", jumFileTomb, jumFileSizeTomb);
					hitungFileDanSize(data + "/system/dropbox", jumFileDrop, jumFileSizeDrop);
					
					HashMap<String,String> hmlog = fcl.hitungTotalSizeLog();
					Iterator i = hmlog.keySet().iterator();
					while (i.hasNext())
					{
						String s = i.next().toString();
							jumFileLog.setText(s);
							jumFileSizeLog.setText(hmlog.get(s));
						break;
					}
					file +=fcl.getJumFileLog();
					size +=fcl.getJumFileSizeLog();
					
					String FILE = "000";
					String SIZE = "000";
					
					if(file>0){
						clearButton.setEnabled(true);
					}
					
					if (file > 1)
					{
						FILE = file + " Files";
					}
					else
					{
						FILE = file + " File";
					}

					if (size > 1048576)
					{
						SIZE = size / 1048575 + " MB";	
					}
					else if ((size > 1023) && (size < 1048576))
					{
						SIZE = size / 1024 + " KB";
					}
					else if((size > 0)&&(size < 1024))
					{
						SIZE = size + " B";
					}else{
						SIZE="0 B";
					}
//Toast.makeText(getApplicationContext(), size+"", Toast.LENGTH_LONG).show();
					jumFile.setText(FILE);
					jumFileSize.setText(SIZE);
				}}, 500);



	}

	private void hitungFileDanSize(String path, TextView jFile, TextView jSize)
	{
		HashMap<String,String> hm =fcl.hitungTotalSize(path);
		Iterator itr =hm.keySet().iterator();
		while (itr.hasNext())
		{
			String s = itr.next().toString();
			if (s != null)
			{
				jFile.setText(s);
				jSize.setText(hm.get(s));
				break;
			}
		}
		file += fcl.getJumFile();
		size += fcl.getJumFileSize();

	}

	private void setTvComputing(){
		TextView[] tvs= new TextView[]{
		jumFile,
		jumFileSize,
		jumFileTomb,
		jumFileSizeTomb,
		jumFileDrop,
		jumFileSizeDrop,
		jumFileLog,
		jumFileSizeLog
		};
		for(TextView t :tvs){
			t.setText("Computing...");
		}
	}
}
