package com.example.kwowdy3;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import com.agecheq.kwowdy3.R;

public class AboutActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		
		//lock in portrait
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		
		//set the layout
		setContentView(R.layout.activity_about);
		
		//set the font on the question text views
		TextView textView1 =(TextView)findViewById(R.id.textView1);
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"playtime.ttf");
		textView1.setTypeface(typeFace);

		
	}

}
