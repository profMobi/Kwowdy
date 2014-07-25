package com.example.kwowdy3;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Typeface;
import com.agecheq.kwowdy3.R;


public class IdentifyActivity extends Activity {
	
	//Global Variables
	int currentCloud = 0;
	
	//cloud images
	int []arrCloudImages=new int[]{
			            R.drawable.cirrus1,
			            R.drawable.cirrostratus1,
			            R.drawable.cirrocumulus1,
			            R.drawable.altostratus1,
			            R.drawable.altocumulus1,
			            R.drawable.stratus1,
 			            R.drawable.stratocumulus1,
			            R.drawable.nimbostratus1,
			            R.drawable.cumulus1,
			            R.drawable.cumulonimbus1,
			            R.drawable.mammatus3,
			            R.drawable.lenticular_clouds,
			            R.drawable.fog1,
			            R.drawable.contrails1,
			            R.drawable.cloud_green	};
	int intTotalClouds=arrCloudImages.length;	
	
	String[] arrCloudDescriptions;
	String[] arrCloudNames;
	
    //End Globals
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 
		//lock in portrait
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		
		//set the layout
		setContentView(R.layout.activity_identify);
		
		//get the arrays from strings.xml
		arrCloudDescriptions = getResources().getStringArray(R.array.arrclouddescriptions);
		arrCloudNames =  getResources().getStringArray(R.array.arrcloudnames);
	    			
	    //Get the controls
	    TextView textCloud = (TextView) findViewById(R.id.txtCloudName);
	    TextView textCloudInfo = (TextView) findViewById(R.id.txtCloudInfo);
	    TextView txtInstructions = (TextView) findViewById(R.id.txtInstructions);
	    ImageView imgCloud = (ImageView) findViewById(R.id.imgCloud);
	    
	    //set the font
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"playtime.ttf");
		textCloud.setTypeface(typeFace);
		textCloudInfo.setTypeface(typeFace);
		txtInstructions.setTypeface(typeFace);
				
		//Set the views to display the currently selected cloud
	    imgCloud.setImageResource(arrCloudImages[currentCloud]);
	    textCloud.setText(arrCloudNames[currentCloud]);
	    textCloudInfo.setText(arrCloudDescriptions[currentCloud]);
	}

	public void doNext(View view) {

		//show the next cloud
		currentCloud = currentCloud+1;
		if (currentCloud >= intTotalClouds) { currentCloud = 0; }
		
	    //Get the controls
	    TextView textCloud = (TextView) findViewById(R.id.txtCloudName);
	    TextView textCloudInfo = (TextView) findViewById(R.id.txtCloudInfo);
	    ImageView imgCloud = (ImageView) findViewById(R.id.imgCloud);
		
		//Set the views to display the currently selected cloud
	    imgCloud.setImageResource(arrCloudImages[currentCloud]);
	    textCloud.setText(arrCloudNames[currentCloud]);
	    textCloudInfo.setText(arrCloudDescriptions[currentCloud]);
	}
	
	public void doPrevious(View view) {

		//show the next cloud
		currentCloud = currentCloud-1;
		if (currentCloud <= 0 ) { currentCloud = intTotalClouds-1; }
		
	    //Get the controls
	    TextView textCloud = (TextView) findViewById(R.id.txtCloudName);
	    TextView textCloudInfo = (TextView) findViewById(R.id.txtCloudInfo);
	    ImageView imgCloud = (ImageView) findViewById(R.id.imgCloud);
		
		//Set the views to display the currently selected cloud
	    imgCloud.setImageResource(arrCloudImages[currentCloud]);
	    textCloud.setText(arrCloudNames[currentCloud]);
	    textCloudInfo.setText(arrCloudDescriptions[currentCloud]);
	}
	
}
