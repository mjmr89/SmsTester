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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private SmsListener Smsl;
	final IntentFilter smsFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
	public static TextView commtv, timtv;
	public static EditText commet, timet;
	public static Button savebtn;
	
	static String command;
	static int timersec = 60;
	
	public static AudioStatus audstat;
	
	public static MainActivity context;
	Handler handler;
	static NotificationManager notman;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		context = this;
		Handler handler = new Handler();
		notman = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		commtv = (TextView)findViewById(R.id.commtv);
		timtv = (TextView)findViewById(R.id.conftv);
		
		commet = (EditText)findViewById(R.id.commandField);
		timet = (EditText)findViewById(R.id.timField);
		
		savebtn = (Button)findViewById(R.id.savebtn);
		
		//load prefs file, set tvs to those values to start
		loadPrefs();
		
		commet.setText(command);
		timet.setText("" + timersec);
		
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
		command = procStr((String)commet.getText().toString());
		timersec = procInt((String)timet.getText().toString());
		savePrefs();
	}
	
	public void savePrefs(){
		SharedPreferences.Editor pref = this.getSharedPreferences("com.example.smstester", Context.MODE_PRIVATE).edit();

		pref.putString("command", command).commit();
		pref.putInt("timersec", timersec).commit();	
		
		Toast.makeText(this, "--Saved Prefs--\nCommand: " + command + "\nLength: " + timersec, Toast.LENGTH_SHORT).show();
	}
	
	public String procStr(String str){
		str = str.trim().replace(" ", "_");
		commet.setText(str);
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
		Toast.makeText(this, "--Loaded Prefs--\nCommand: " + command + "\nLength: " + timersec, Toast.LENGTH_SHORT).show();

		
	}

}
