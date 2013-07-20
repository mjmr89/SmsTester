package com.example.smstester;

import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsListener extends BroadcastReceiver {
	AudioManager audman;
	
	
	
	@Override
	 public void onReceive(Context context, Intent intent) {
	  Bundle bundle = intent.getExtras();
	         String msg,from;         
	         if (bundle != null){
	             //---retrieve the SMS message received---
	             Object[] pdus = (Object[]) bundle.get("pdus");
	             msg = SmsMessage.createFromPdu((byte[])pdus[0]).getMessageBody();
	             from = SmsMessage.createFromPdu((byte[])pdus[0]).getOriginatingAddress();
	             
	             if(msg.trim().equalsIgnoreCase(MainActivity.command)){
            	 	 audman = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	            	 MainActivity.audstat = new AudioStatus(audman.getRingerMode(),audman.getStreamVolume(AudioManager.STREAM_RING));
	            	 turnRingVibOn();
	            	 Timer timer = new Timer();
	            	 //testcomment
	            	 timer.schedule(new RingTimerTask(), MainActivity.timersec*1000);
	            	 
	            	 Toast.makeText(context, MainActivity.audstat.getString() + " restoring in " + MainActivity.timersec + " seconds.", Toast.LENGTH_SHORT).show();
	            	 
	            	 if(MainActivity.conf){
		            	 	sendSMS(from,"DeMute received!");
	            	 }
	            	 
	            	 abortBroadcast();
	            	 
	            	 MainActivity.context.createNotification("New Demute", SmsMessage.createFromPdu((byte[])pdus[0]).getOriginatingAddress());

	             }
	         }         
	 }
	
	public void sendSMS(String num, String msg){
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(num, null, msg, null, null);
		
	}
	
	
	
	public void turnRingVibOn(){
		audman.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		audman.setStreamVolume(AudioManager.STREAM_RING, audman.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
	}
	
	public void restoreRingVib(){
		audman.setRingerMode(MainActivity.audstat.getRingMode());
		audman.setStreamVolume(AudioManager.STREAM_RING, MainActivity.audstat.getRingVol(), 0);
	}
	
	class RingTimerTask extends TimerTask{
		Context cont;
		
		public void run(){
			MainActivity.context.runOnUiThread(new Runnable(){
				public void run(){
					restoreRingVib();
					Toast.makeText(MainActivity.context, MainActivity.timersec + " seconds, ding!", Toast.LENGTH_SHORT)
						.show();
				}
			});
		}
	}
}