package com.example.kwowdy3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.agecheq.kwowdy3.R;
import com.agecheq.lib.AgeCheckServerInterface;
import com.agecheq.lib.AgeCheqApi;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;

public class AgeCheqActivity  extends Activity implements AgeCheckServerInterface {
	
	private String DevKey   = "06c3a8ba-8d2e-429c-9ce6-4f86a70815d6";
	private String AppId    = "21cdc227-48ad-4cf5-a67c-92f00a3dbef7";
	private String DeviceId = "";
	public  String ZipCode  = "";
	
	private boolean boolDebug = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

		//test the zip code and ParentID
	    if (!HomeActivity.globalZipcode.equals("")) {
	    	ZipCode = HomeActivity.globalZipcode;
	    }
	    if (!HomeActivity.globalDeviceID.equals("")) {
	    	DeviceId = HomeActivity.globalDeviceID;
	    }
	    
	    //get the device id even if it is already stored- just in case it has changed
	    new GetDeviceId().execute("");
	    
		//lock in portrait
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
						
		//set the layout
		setContentView(R.layout.activity_agecheq);
		
 	    //get the controls
	    TextView textHeading = (TextView) findViewById(R.id.textHeading);
	    TextView textParagraph = (TextView) findViewById(R.id.textParagraph);
		
		//set the font on the question text views
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"playtime.ttf");
		textHeading.setTypeface(typeFace);
		textParagraph.setTypeface(typeFace);
	    	
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		
		//clear the edit field
		EditText editText = (EditText)findViewById(R.id.editTextField);
		editText.setText("");
		
		doPing();
	}
	
	//----------------------------------------
	// AgeCheq Commands
	//----------------------------------------
	private void doPing() {
		AgeCheqApi.ping(this);
	}
	
	private void doIsRegistered() {

		if (!DeviceId.equals("") )
		{
			AgeCheqApi.isRegistered(this, DevKey, DeviceId);
		}
		else
		{
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);                      
		    dlgAlert.setMessage("NO DEVICE ID");
		    dlgAlert.setTitle("ERROR");              
		    dlgAlert.setPositiveButton("OK", null);
		    dlgAlert.setCancelable(true);
		    dlgAlert.create().show();
		}
	}
	
	private void doCheck() {
		if (!DeviceId.equals("") )
		{
			AgeCheqApi.check(this, DevKey, DeviceId, AppId);
			
		}
		else
		{
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);                      
		    dlgAlert.setMessage("NO DEVICE ID");
		    dlgAlert.setTitle("ERROR");              
		    dlgAlert.setPositiveButton("OK", null);
		    dlgAlert.setCancelable(true);
		    dlgAlert.create().show();
		}
		
	}
	
	private void doRegister() {
	
		//get the edit field
		EditText editText = (EditText)findViewById(R.id.editTextField);
		
		if (!editText.getText().toString().equals("")) {
			
			String ParentID = editText.getText().toString();
	
			Log.d("zz", "doRegister");
			Log.d("zz", "DevKey= " + DevKey);
			Log.d("zz", "DeviceId= " + DeviceId);	
			Log.d("zz", "ParentID= *" + ParentID + "*");			
			
			//register the device passing the AgeCheq Parent Dashboard username
			AgeCheqApi.register(this, DevKey, DeviceId, "New Android Device", ParentID);
		}
		
	}
	
	//----------------------------------------
	// Multi-Button Clicks
	//----------------------------------------
	public void doLeft(View view) {
		
		if (boolDebug == true) {
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);                      
		    dlgAlert.setMessage("IN DO LEFT");
		    dlgAlert.setTitle("DEBUG");              
		    dlgAlert.setPositiveButton("OK", null);
		    dlgAlert.setCancelable(true);
		    dlgAlert.create().show();
		}
		

		//get the text on the button on the click
		Button tempBtn = (Button)view;
		
		//register the parent dashboard account
		if (tempBtn.getText().toString().equals(getResources().getString(R.string.btnRegister))) {
			//try to register the parent dashboard account
			doRegister();
		}
		
		//check for parental approval again
		if (tempBtn.getText().toString().equals(getResources().getString(R.string.btnCheckForApproval))) {
			//check for parental approval again
			doCheck();
		} 
		
		//zip code submit
		if (tempBtn.getText().toString().equals(getResources().getString(R.string.enterZipCode))) {
			
			//collect the zip code
			EditText editText = (EditText)findViewById(R.id.editTextField);
			ZipCode = editText.getText().toString();
			HomeActivity.globalZipcode = ZipCode;
			
			//TODO: Make sure it is a valid zip code?
			if (ZipCode.length() == 5) {

				//show the weather at this ZIP code
				startActivity(new Intent(AgeCheqActivity.this, ForecastActivity.class));
				
				//clear the edit field
				editText.setText("");
			}
			else
			{
				//invalid zip code
				AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);                      
			    dlgAlert.setMessage("I\'m afraid this is an invalid zip code.  Please try again.");
			    dlgAlert.setTitle("Invalid Zip Code");              
			    dlgAlert.setPositiveButton("OK", null);
			    dlgAlert.setCancelable(true);
			    dlgAlert.create().show();
			    editText.setText("");
			}
			
			//TODO: Save the ZIP code if it is valid?
		} 
	}
	
	public void doRight(View view) {
		//open a browser window to the Parent Dashboard
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://parent.agecheq.com"));
		startActivity(browserIntent);
	}
	
	
	//----------------------------------------
	// Response Handlers
	//----------------------------------------
    @Override
    public void onPingResponse(String pingResponse) {
    	/*
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);                      
	    dlgAlert.setMessage("Ping Response = " + pingResponse);
	    dlgAlert.setTitle("PING SERVER");              
	    dlgAlert.setPositiveButton("OK", null);
	    dlgAlert.setCancelable(true);
	    dlgAlert.create().show();
    	*/
    	
    	if (pingResponse.equals("ok") )
    	{
    		doCheck();
    		
    	}
    	else
    	{
    		//TODO: turn away the user
    		
    	}
    }
    
    @Override
    public void onIsRegisteredResponse(String rtn, String rtnmsg, Boolean deviceregistered) {
		
    	//isRegistered is not used in favor of the Check API call
    	
    	/*
    	AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);                      
	    dlgAlert.setMessage("IsRegistered Response = " + rtn + "-" + rtnmsg + "-" + deviceregistered);
	    dlgAlert.setTitle("IS REGISTERED");              
	    dlgAlert.setPositiveButton("OK", null);
	    dlgAlert.setCancelable(true);
	    dlgAlert.create().show();
	    */
    }
    
    @Override
    public void onRegisterResponse(String rtn, String rtnmsg) {
    
    	
    	Log.d("zz", "onRegisterResponse");     	
    	Log.d("zz", "rtn=" + rtn);      	
    	Log.d("zz", "rtnmsg=" + rtnmsg);   
    	
	    if (rtn.equals("ok")) {
	    	
	    	//This device has *just* been successfully registered to a parent!  
	    	//showUnauthorizedDevice(true);
	    	
	    	doCheck();
	    	
	    } else {
			AlertDialog.Builder dlgAlert1  = new AlertDialog.Builder(this);                      
		    dlgAlert1.setMessage(rtnmsg);
		    dlgAlert1.setTitle("ERROR");              
		    dlgAlert1.setPositiveButton("OK", null);
		    dlgAlert1.setCancelable(true);
		    dlgAlert1.create().show();
	    }
	    
    }    
    
    @Override
    public void onCheckResponse(String rtn, String rtnmsg, Boolean deviceregistered, Boolean appauthorized, Boolean appblocked, Boolean knowndevice, Boolean under13, Boolean under18, int age) 
    {
    	if (boolDebug) {
	    	AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);                      
		    dlgAlert.setMessage("Check Response = " + rtn +"-DeviceRegistered=" + deviceregistered + ",AppAuthorized=" + appauthorized + ",AppBlocked=" + appblocked);
		    dlgAlert.setTitle("CHECK");              
		    dlgAlert.setPositiveButton("OK", null);
		    dlgAlert.setCancelable(true);
		    dlgAlert.create().show();
    	}
    	Log.d("zz", "onCheckResponse");     	
    	Log.d("zz", "rtn=" + rtn);      	
    	Log.d("zz", "rtnmsg=" + rtnmsg);   

	    
	    if (!deviceregistered){
	    	//This device isn't registered yet!
	    	showUnregisteredDevice();
	    }
	    else
	    {
	     	if (!appauthorized) {
	     		showUnauthorizedDevice(false);

	     	} else {
	     		if (appblocked) {
	     			showBlockedDevice();
	     		} else {
	     			showZipCodeCollection();
	     		}
	     	}
	    }
    	
    	/*
     	RelativeLayout ua = (RelativeLayout) findViewById(R.id.notauthorized);
     	TextView msg = (TextView) findViewById(R.id.uaMsg);
     	*/
    } 
    
    @Override
    public void onAssociateDataResponse(String rtn, String rtnmsg) {
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);                      
	    dlgAlert.setMessage("Register Response = " + rtn +"-" + rtnmsg);
	    dlgAlert.setTitle("REGISTER");              
	    dlgAlert.setPositiveButton("OK", null);
	    dlgAlert.setCancelable(true);
	    dlgAlert.create().show();
    }    
    
    @Override
    public void onAgeCheckServerError(String paramString) {
    	
    }
    @Override
    public void onAgegateResponse(String rtn, String rtnmsg) {
    	
    }

    //-----------------------------------------------
    // UI Setups
    //-----------------------------------------------    
    private void showUnregisteredDevice() {
    	
 	    //get the controls
	    TextView textHeading = (TextView) findViewById(R.id.textHeading);
	    TextView textParagraph = (TextView) findViewById(R.id.textParagraph);
	    ImageView imgCloud = (ImageView) findViewById(R.id.imageSadKwowdy);
	    Button btnLeft = (Button)findViewById(R.id.buttonLeft);
	    Button btnRight = (Button)findViewById(R.id.buttonRight);
	    EditText editText = (EditText)findViewById(R.id.editTextField);

	    //clear the edit field
	    editText.setText("");
	    
	    //set the text of the paragraph and buttons
	    textHeading.setText(R.string.giveParentalConsent);
	    textParagraph.setText(R.string.parentalVerification);
	    btnLeft.setText(R.string.btnRegister);
	    btnRight.setText(R.string.btnNewParent);
	    
	    //make the buttons visible
	    btnLeft.setVisibility(View.VISIBLE);
	    btnRight.setVisibility(View.VISIBLE);
	    
	    //TODO: Update the Kwowdy character
    	
    }
    
    private void showUnauthorizedDevice(boolean boolJustRegistered) {
	    //get the controls
	    TextView textHeading = (TextView) findViewById(R.id.textHeading);
	    TextView textParagraph = (TextView) findViewById(R.id.textParagraph);
	    ImageView imgCloud = (ImageView) findViewById(R.id.imageSadKwowdy);
	    Button btnLeft = (Button)findViewById(R.id.buttonLeft);
	    Button btnRight = (Button)findViewById(R.id.buttonRight);
	    EditText editText = (EditText)findViewById(R.id.editTextField);
	    
	    //clear the edit field
	    editText.setText("");

	    //set the text of the paragraph and buttons
	    textParagraph.setText(R.string.explainParentalConsent);
	    btnLeft.setText(R.string.btnCheckForApproval);
	    btnRight.setText(R.string.btnOpenParentDashboard);
	    	    
	    //hide the buttons and edit field
	    btnLeft.setVisibility(View.VISIBLE);
	    btnRight.setVisibility(View.VISIBLE);
	    editText.setVisibility(View.GONE);
	    
	   
	    if (boolJustRegistered) {
	    	//this device was *just* registered with an AgeCheq Parent Dashboard account
	    	 textHeading.setText(R.string.oneMoreStep);
	    } else {
	    	//device already registered, but we still aren't authorized 
	    	 textHeading.setText(R.string.giveParentalConsent);
	    }
	    
	    //TODO: Update the Kwowdy character
    	
    }
    
    private void showBlockedDevice() {
	    //get the controls
	    TextView textHeading = (TextView) findViewById(R.id.textHeading);
	    TextView textParagraph = (TextView) findViewById(R.id.textParagraph);
	    ImageView imgCloud = (ImageView) findViewById(R.id.imageSadKwowdy);
	    Button btnLeft = (Button)findViewById(R.id.buttonLeft);
	    Button btnRight = (Button)findViewById(R.id.buttonRight);
	    EditText editText = (EditText)findViewById(R.id.editTextField);

	    //clear the edit field
	    editText.setText("");
	    
	    //set the text of the paragraph and buttons
	    textHeading.setText(R.string.blocked);
	    textParagraph.setText(R.string.aboutBlocked);
	    	    
	    //hide the buttons and edit field
	    btnLeft.setVisibility(View.GONE);
	    btnRight.setVisibility(View.GONE);
	    editText.setVisibility(View.GONE);
	    
	    //TODO: Update the Kwowdy character
    	
    }
    
    private void showZipCodeCollection() {
	    //get the controls
	    TextView textHeading = (TextView) findViewById(R.id.textHeading);
	    TextView textParagraph = (TextView) findViewById(R.id.textParagraph);
	    ImageView imgCloud = (ImageView) findViewById(R.id.imageSadKwowdy);
	    Button btnLeft = (Button)findViewById(R.id.buttonLeft);
	    Button btnRight = (Button)findViewById(R.id.buttonRight);
	    EditText editText = (EditText)findViewById(R.id.editTextField);
	    
	    //clear the edit field
	    editText.setText("");
	    
		//Prime the zip code field and change the edit field to accept numbers only
	    if (!ZipCode.equals("")) {
	    	editText.setText(ZipCode);
	    }
		editText.setInputType(InputType.TYPE_CLASS_NUMBER);
	    
	    //set the text of the paragraph and buttons
	    textHeading.setText(R.string.getZipCode);
	    textParagraph.setText(R.string.explainZipCode);
	    btnLeft.setText(R.string.enterZipCode);
	    	    
	    //hide the buttons and edit field
	    btnLeft.setVisibility(View.VISIBLE);
	    btnRight.setVisibility(View.GONE);
	    editText.setVisibility(View.VISIBLE);
    	
	    //make Kwowdy happy!
	    imgCloud.setImageResource(R.drawable.kwowdyhappy);
     }
    
    //-----------------------------------------------
    // Get Device ID
    //-----------------------------------------------
    private class GetDeviceId extends AsyncTask<String, Void, String> {
    	@Override
    	protected String doInBackground(String... params) {
    		Context c = getApplicationContext();
    		
    		Info adInfo = null;
    		try {
    			adInfo = AdvertisingIdClient.getAdvertisingIdInfo(c);
    		}
    		catch (Exception ex) {
    			Log.d("zz", "Exception: " + ex.getMessage());
    		}
    		  
    		String id = adInfo.getId();
    		Log.d("zz", "Device Id=" + id);   
    		
    		//Save the device id in the preferences file
    		/*
			SharedPreferences settings = getSharedPreferences(HomeActivity.PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("deviceid", id);
		    editor.commit();
		    */
    		
    		return id;
    	}
    	
    	@Override
    	protected void onPostExecute(String result) {
    		DeviceId = result;
    	}
    }


}
