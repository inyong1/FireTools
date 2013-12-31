package inyong.xt530.tools;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.stericson.RootTools.*;
import com.stericson.RootTools.execution.*;
import inyong.xt530.tools.adapter.*;
import inyong.xt530.tools.fungsiFungsi.*;
import java.io.*;
import java.util.*;


public class BatteryTool extends Activity
{
	CheckBox checkBoxService, checkBoxLogging , checkBoxExtraLevel, checkBoxToast;
//ToggleButton toggleButtonService;
//	View batteryFill;
//	TextView tvBatteryProsentase;
	SharedPreferences setinganKu;

	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_tool);
//		tvBatteryProsentase  = (TextView) findViewById(R.id.tv_battery_tool_prosentase);
//		batteryFill = findViewById(R.id.view_battery_fill);
//		batteryFill.setOnClickListener(clickListener());
		checkBoxService = (CheckBox) findViewById(R.id.checkbox_on_of_service);
		checkBoxLogging =(CheckBox) findViewById(R.id.checkbox_on_off_batterytool_log);
		checkBoxExtraLevel =(CheckBox) findViewById(R.id.checkbox_on_off_batterytool_extra_level);
		checkBoxToast =(CheckBox) findViewById(R.id.checkbox_on_off_batterytool_toast);
		setinganKu = getSharedPreferences(FungsiSettings.NAMA_SETINGAN, 0);
		setCheckBoxService();
		setCheckBoxLogging();
		setCheckBoxExtraLevel();
		setCheckBoxToast();
		checkBoxService.setOnCheckedChangeListener(checkedListener());
		checkBoxLogging.setOnCheckedChangeListener(checkBoxLoggingListener());
		checkBoxExtraLevel.setOnCheckedChangeListener(checkBoxExtraLevelListener());
		checkBoxToast.setOnCheckedChangeListener(checkBoxToastListener());
	}

	private void setCheckBoxService()
	{
		if (setinganKu.getBoolean(FungsiSettings.NAMA_SERVICE_DIMATIKAN, false))
		{
			checkBoxService.setChecked(false);
			checkBoxLogging.setEnabled(false);
			checkBoxExtraLevel.setEnabled(false);
			checkBoxToast.setEnabled(false);
		}
		else
		{
			checkBoxService.setChecked(true);
			checkBoxLogging.setEnabled(true);
			checkBoxExtraLevel.setEnabled(true);
			checkBoxToast.setEnabled(true);
		}
	}
	
	private void setCheckBoxLogging()
	{
		if (setinganKu.getBoolean(FungsiSettings.NAMA_LOG_SETTING, false))
		{
			checkBoxLogging.setChecked(true);
		}
		else
		{
			checkBoxLogging.setChecked(false);
		}
	}
	
	private void setCheckBoxExtraLevel()
	{
		if (setinganKu.getBoolean(FungsiSettings.NAMA_EXTRA_LEVEL_SETTING, false))
		{
			checkBoxExtraLevel.setChecked(true);
		}
		else
		{
			checkBoxExtraLevel.setChecked(false);
		}
	}
	
	private void setCheckBoxToast()
	{
		if (setinganKu.getBoolean(FungsiSettings.NAMA_TOAST_SETTING, true))
		{
			checkBoxToast.setChecked(true);
		}
		else
		{
			checkBoxToast.setChecked(false);
		}
	}
	
	private CompoundButton.OnCheckedChangeListener checkedListener()
	{

		return new CompoundButton.OnCheckedChangeListener(){

			public void onCheckedChanged(CompoundButton p1, boolean p2)
			{
				SharedPreferences.Editor editor =setinganKu.edit();
				Intent i=new Intent(getApplicationContext(), FireToolsService.class);
				if (p2)
				{
					gaweToast("Enabling service...");
					startService(i);
					editor.putBoolean(FungsiSettings.NAMA_SERVICE_DIMATIKAN, false);
					checkBoxLogging.setEnabled(true);
					checkBoxExtraLevel.setEnabled(true);
					checkBoxToast.setEnabled(true);
					
				}
				else
				{
					gaweToast("Disabling service...");
					stopService(i);
					editor.putBoolean(FungsiSettings.NAMA_SERVICE_DIMATIKAN, true);
					checkBoxLogging.setEnabled(false);
					checkBoxExtraLevel.setEnabled(false);
					checkBoxToast.setEnabled(false);
					
				}
				editor.commit();
			}

		};

	}
	
	private CompoundButton.OnCheckedChangeListener checkBoxLoggingListener()
	{

		return new CompoundButton.OnCheckedChangeListener(){

			public void onCheckedChanged(CompoundButton p1, boolean p2)
			{
				SharedPreferences.Editor editor =setinganKu.edit();
				Intent i=new Intent(getApplicationContext(), FireToolsService.class);
				if (p2)
				{
					editor.putBoolean(FungsiSettings.NAMA_LOG_SETTING, true);
					FungsiSettings.loggingAktif=true;
					stopService(i);
					startService(i);
				}
				else
				{
					editor.putBoolean(FungsiSettings.NAMA_LOG_SETTING, false);
					FungsiSettings.loggingAktif=false;
					stopService(i);
					startService(i);
				}
				editor.commit();
			}

		};

	}
	
	private CompoundButton.OnCheckedChangeListener checkBoxExtraLevelListener()
	{

		return new CompoundButton.OnCheckedChangeListener(){

			public void onCheckedChanged(CompoundButton p1, boolean p2)
			{
				SharedPreferences.Editor editor =setinganKu.edit();
				Intent i=new Intent(getApplicationContext(), FireToolsService.class);
				if (p2)
				{
					editor.putBoolean(FungsiSettings.NAMA_EXTRA_LEVEL_SETTING, true);
					FungsiSettings.extraLevel=true;
					stopService(i);
					startService(i);
				}
				else
				{
					editor.putBoolean(FungsiSettings.NAMA_EXTRA_LEVEL_SETTING, false);
					FungsiSettings.extraLevel=false;
					stopService(i);
					startService(i);
				}
				editor.commit();
			}

		};

	}

	private CompoundButton.OnCheckedChangeListener checkBoxToastListener()
	{

		return new CompoundButton.OnCheckedChangeListener(){

			public void onCheckedChanged(CompoundButton p1, boolean p2)
			{
				SharedPreferences.Editor editor =setinganKu.edit();
				Intent i=new Intent(getApplicationContext(), FireToolsService.class);
				if (p2)
				{
					editor.putBoolean(FungsiSettings.NAMA_TOAST_SETTING, true);
					FungsiSettings.toastMessage=true;
					stopService(i);
					startService(i);
				}
				else
				{
					editor.putBoolean(FungsiSettings.NAMA_TOAST_SETTING, false);
					FungsiSettings.toastMessage=false;
					stopService(i);
					startService(i);
				}
				editor.commit();
			}

		};

	}
	
	private void gaweToast(String s)
	{
		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}
}
