package inyong.xt530.tools;

import android.app.*;
import android.os.*;
import android.widget.*;

public class About extends Activity
{ 
String versi;
	TextView about, logUpdate;
	@Override
	public void onCreate(Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.about);
			about =(TextView) findViewById(R.id.id_tv_about);
		//	logUpdate=(TextView) findViewById(R.id.id_tv_about_logupdate );
		versi = getResources().getString(R.string.versi);
		about.setText("FireTools   "+versi+" \nUseful tools for\nMotorola FIRE XT530/531");
	}

}
