package com.example.kwowdy3;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import com.agecheq.kwowdy3.R;

public class HomeActivity extends Activity {

	public static final String PREFS_NAME = "PrefsFile";
	public static String globalZipcode;
	public static String globalDeviceID;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
		//lock in portrait
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		
		//set the layout
		setContentView(R.layout.activity_home);
		
	    //restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        globalZipcode = settings.getString("zipcode","");
        globalDeviceID = settings.getString("deviceid","");
	}
	
	
	public void doLearn(View view) {
		startActivity(new Intent(HomeActivity.this, LearnActivity.class));
	}
	
	public void doIdentify(View view) {
		startActivity(new Intent(HomeActivity.this, IdentifyActivity.class));
	}
	
	public void doAbout(View view) {
		startActivity(new Intent(HomeActivity.this, AboutActivity.class));
	}
	
	public void doForecast(View view) {
		startActivity(new Intent(HomeActivity.this, AgeCheqActivity.class));
	}

}
