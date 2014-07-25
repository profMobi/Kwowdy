package com.example.kwowdy3;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.agecheq.kwowdy3.R;

public class InitialActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		//lock in portrait
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		
		//set the layout
		setContentView(R.layout.activity_initial);

		
	}
	
	
	public void onGetStarted(View view) {
		startActivity(new Intent(InitialActivity.this, HomeActivity.class));
		finish();
	}
	
	
	
}
