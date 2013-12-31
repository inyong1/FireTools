package inyong.xt530.tools;

import android.content.*;
import android.preference.*;
import inyong.xt530.tools.fungsiFungsi.*;

public class BootCompletedReceiver extends BroadcastReceiver
{
	public void onReceive(Context p1, Intent p2)
	{
		SharedPreferences	setinganKu = p1.getSharedPreferences(FungsiSettings.NAMA_SETINGAN, 0);
		if (setinganKu.getBoolean(FungsiSettings.NAMA_SERVICE_DIMATIKAN, false))
		{
		}
		else
		{
		Intent i= new Intent();
			i.setComponent( new ComponentName(p1.getPackageName(),FireToolsService.class.getName()));
			p1.startService(i);
		}
	}

}
