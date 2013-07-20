package com.example.smstester;

import android.media.AudioManager;

public class AudioStatus {
	private int ringMode = AudioManager.RINGER_MODE_NORMAL;
	private int ringVol = 0;
	
	public AudioStatus(int rm, int ringVol){
		this.ringMode = rm;
		this.ringVol = ringVol;
	}
	
	public int getRingMode(){
		return ringMode;
	}
	
	public int getRingVol(){
		return ringVol;
		
	}
	
	public void setRingMode(int rm){
		ringMode = rm;
	}
	
	public void setRingVol(int rv){
		ringVol = rv;
	}
	
	public String getString(){
		return "Level: " + ringVol + ", RingMode: " + getStringRM(ringMode);
	}
	
	public static String getStringRM(int rm){
		String str;
		switch(rm){
		case AudioManager.RINGER_MODE_NORMAL:
			str = "Normal";
			break;
		case AudioManager.RINGER_MODE_SILENT:
			str = "Silent";
			break;
		case AudioManager.RINGER_MODE_VIBRATE:
			str = "Vibrate";
			break;
		default:
			str = "whoops";
		}
		
		return str;
	}

}
