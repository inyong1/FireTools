package inyong.xt530.tools;

import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import inyong.xt530.tools.fungsiFungsi.*;

public class FireToolsService extends Service
{
//------------------
	private Context appContext;
	//private BroadcastReceiver batteryInfoReceiver;
	private final IntentFilter intentFilterBatChanged = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	private boolean receiverSudahTerdaftar=false;
//-------------------
	private NotificationManager notifikasiManajer;
	private Notification notifikasi;
	private String judulNotifikasi=null;
	private String isiNotifikasi=null;
	private Intent intent;
	private PendingIntent pendingIntent;
	private int batteryVoltage;
	private static int persen=0;
	private static int stockLevel=0;
	private int sehat;
	private int jenisCharger;
	private int suhu;
	private int status;
	FungsiService fService;
	private static final int notifikasiId=12345;
	private static int persenSebelumnya=0;
	private static int voltaseSebelumnya=0;
	private static String logAktif="";

	//-------
	private	BroadcastReceiver	batteryInfoReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context p1, Intent p2)
		{
			stockLevel     = p2.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			batteryVoltage = p2.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
			status         = p2.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
			suhu           = p2.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
			sehat          = p2.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
			jenisCharger   = p2.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
			persen = fService.getPercent(batteryVoltage, stockLevel);
			judulNotifikasi = intVoltToString(batteryVoltage) + " / " + intStatusToString(status, jenisCharger, batteryVoltage);
			isiNotifikasi = intSuhuToString(suhu) + " / " + intSehatToString(sehat) + logAktif;
		
			if (persen != persenSebelumnya && FungsiSettings.toastMessage)
			{
				Toast.makeText(appContext, persen + "%", 300).show();
				persenSebelumnya = persen;
			}
			
			// penelitian logging
			if (FungsiSettings.loggingAktif)
			{
				logAktif = "   /  (!) Log enabled";
				if (batteryVoltage != voltaseSebelumnya)
				{
					fService.buatLog(batteryVoltage, stockLevel, persen);
				}

				voltaseSebelumnya = batteryVoltage;
			}
			else
			{
				logAktif = "";
			} 
			buatNotifikasi();
		}
	};
	//-------------
	public class LocalBinder extends Binder
	{
		FireToolsService getService()
		{
			return FireToolsService.this;
		}
	}

	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent p1)
	{
		return mBinder;
	}

	@Override
	public int onStartCommand(Intent i, int f, int id)
	{
		return START_STICKY;
	}

//--------
	@Override
	public void onCreate()
	{
		super.onCreate();
		appContext = getApplicationContext();
		SharedPreferences setingan =getSharedPreferences(FungsiSettings.NAMA_SETINGAN, 0);
		FungsiSettings.loggingAktif = setingan.getBoolean(FungsiSettings.NAMA_LOG_SETTING, false);
		FungsiSettings.extraLevel = setingan.getBoolean(FungsiSettings.NAMA_EXTRA_LEVEL_SETTING, false);
		FungsiSettings.toastMessage =setingan.getBoolean(FungsiSettings.NAMA_TOAST_SETTING, true);
		notifikasiManajer = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		intent = new Intent(this, MainActivity.class);
		pendingIntent = PendingIntent.getActivity(appContext, 0, intent, 0);
		fService = new FungsiService();
		//percobaan register receifer info battery di sini aja
		registerReceiver(batteryInfoReceiver, intentFilterBatChanged);
		//register pendeteksi screen On
		//	registerReceiver(screenOnReceiver, intentFilterScreenOn);
	}

	private void buatNotifikasi()
	{
		notifikasi = new Notification(fService.batteryIcon[persen], null, System.currentTimeMillis());
		//	notifikasi = new Notification(R.drawable.b110, null, System.currentTimeMillis()); //percobaan
		notifikasi.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
		notifikasi.setLatestEventInfo(appContext, judulNotifikasi, isiNotifikasi, pendingIntent);
		notifikasiManajer.notify(notifikasiId, notifikasi);
	}


	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if (receiverSudahTerdaftar)
		{
			unregisterReceiver(batteryInfoReceiver);
			receiverSudahTerdaftar = false;
		}
		notifikasiManajer.cancel(notifikasiId);
	}

	private String intJenisChargeToString(int code)
	{
		String hasil= "<unknown>";
		if (code == 1)
		{
			hasil = "AC";
		}
		else if (code == 2)
		{
			hasil = "USB"; 
		}
		else if (code == BatteryManager.BATTERY_PLUGGED_WIRELESS)
		{
			hasil = "Wireless";
		}
		return hasil;
	}

	private String intSuhuToString(int suhuInt)
	{
		String hasil="";
		String suhuString = Integer.toString(suhuInt);
		hasil += suhuString.charAt(0);
		hasil += suhuString.charAt(1);
		/*	if(suhuString.length() >= 3){
		 hasil +=".";
		 hasil +=suhuString.charAt(2);
		 }
		 */
		hasil += "°C";
		return hasil;
	}

	private String intVoltToString(int intV)
	{
		String hasil ="----V";
		String s=Integer.toString(intV);
		hasil = s.charAt(0) + ".";
		hasil += s.charAt(1);
		hasil += s.charAt(2);
		hasil += s.charAt(3) + "V";
		return hasil;
	}

	private String intSehatToString(int s)
	{
		String hasil ="Health: ";
		if (s == BatteryManager.BATTERY_HEALTH_COLD)
		{
			hasil += "cold";
		}
		else if (s == BatteryManager.BATTERY_HEALTH_DEAD)
		{
			hasil += "dead";
		}
		else if (s == BatteryManager.BATTERY_HEALTH_GOOD)
		{
			hasil += "good";
		}
		else if (s == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE)
		{
			hasil += "over voltage";
		}
		else if (s == BatteryManager.BATTERY_HEALTH_OVERHEAT)
		{
			hasil += "over heat";
		}
		else if (s == BatteryManager.BATTERY_HEALTH_UNKNOWN)
		{
			hasil += "unknown";
		}
		else if (s == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE)
		{
			hasil += "failure";
		}
		return hasil;
	}

	private String intStatusToString(int st, int jc, int volt)
	{
		String hasil="<unknown>";
		String charger =intJenisChargeToString(jc);
		if (st == BatteryManager.BATTERY_STATUS_CHARGING)
		{
			hasil = "Charging (" + charger + ")";
		}
		else if (st == BatteryManager.BATTERY_STATUS_DISCHARGING)
		{
			hasil = "Discharging";
		}
		else if (st == BatteryManager.BATTERY_STATUS_FULL)
		{
			hasil = "Full charged";
			if (batteryVoltage <= 4150)
			{
				hasil = "Extra charging (" + charger + ")";
			}
			if (volt < voltaseSebelumnya){
				hasil ="(!) Discharging ("+charger+")";
			}
		}
		else if (st == BatteryManager.BATTERY_STATUS_NOT_CHARGING)
		{
			hasil = "Not charging";
			if (jenisCharger == 0)
			{
				hasil = "Discharging";
			}
		}
		else if (st == BatteryManager.BATTERY_STATUS_UNKNOWN)
		{
			hasil = "Unknown state";
		}
		
		return hasil;
	}
}
