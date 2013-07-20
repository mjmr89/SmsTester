package com.example.smstester;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	final IntentFilter smsFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
	public static TextView comtv, timtv,conftv;
	public static EditText comet, timet;
	public static CheckBox confcb;
	public static Button savebtn;
	
	public static String command;
	public static int timersec = 60;
	public static boolean conf = false;
	
	public static AudioStatus audstat;
	
	public static MainActivity context;
	Handler handler;
	static NotificationManager notman;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		context = this;
		notman = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		comtv = (TextView)findViewById(R.id.comtv);
		timtv = (TextView)findViewById(R.id.timtv);
		conftv = (TextView)findViewById(R.id.conftv);
		
		comet = (EditText)findViewById(R.id.comet);
		timet = (EditText)findViewById(R.id.timet);
		
		confcb = (CheckBox)findViewById(R.id.confcb);
		
		
		savebtn = (Button)findViewById(R.id.savebtn);
		
		//load prefs file, set tvs to those values to start
		loadPrefs();
		
		comet.setText(command);
		timet.setText("" + timersec);
		confcb.setChecked(conf);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void createNotification(String title, String text){
		
		Intent intent = new Intent(this,NotificationReceiver.class);
		PendingIntent pIntent = PendingIntent.getActivity(this,0,intent,0);
		
		Notification noti = new Notification.Builder(this)
	        .setContentTitle(title)
	        .setContentText(text)
	        .setTicker("demute!")
	        .setSmallIcon(R.drawable.ic_launcher)
	        .setContentIntent(pIntent).build();
		
		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		
		notman.notify(0,noti);
	}
	
	
	
	public void savePrefsBtn(View view){
		command = procStr((String)comet.getText().toString());
		timersec = procInt((String)timet.getText().toString());
		conf = confcb.isChecked();
		savePrefs();
	}
	
	public void savePrefs(){
		SharedPreferences.Editor pref = this.getSharedPreferences("com.example.smstester", Context.MODE_PRIVATE).edit();

		pref.putString("command", command).commit();
		pref.putInt("timersec", timersec).commit();	
		pref.putBoolean("conf", conf).commit();
		
		Toast.makeText(this, "--Saved Prefs--\nCommand: " + command 
				+ "\nLength: " + timersec
				+ "\nConfirm: " + conf
				, Toast.LENGTH_SHORT).show();
		
	}
	
	public String procStr(String str){
		str = str.trim().replace(" ", "_");
		comet.setText(str);
		return str;
	}
	
	public int procInt(String str){
		int nint;
		
		try{
			nint = Integer.parseInt(str);
		}catch(Exception e){
			Toast.makeText(this,"Exception in procint: " + e.getMessage(),Toast.LENGTH_SHORT).show();
			nint = -1;
		}
		
		return nint;
	}
	
	public void loadPrefs(){
		SharedPreferences pref = this.getSharedPreferences("com.example.smstester", Context.MODE_PRIVATE);
		command = pref.getString("command", "demute");
		timersec = pref.getInt("timersec", timersec);
		conf = pref.getBoolean("conf", false);
		Toast.makeText(this, "--Loaded Prefs--\nCommand: " + command 
				+ "\nLength: " + timersec
				+ "\nConfirm: " + conf
				, Toast.LENGTH_SHORT).show();

		
	}

}
