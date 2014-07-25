package com.example.kwowdy3;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.agecheq.kwowdy3.R;

public class ForecastActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		
		//lock in portrait
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		
		//set the layout
		setContentView(R.layout.activity_forecast);

		//get controls
		TextView txtWeatherTitle = (TextView) findViewById(R.id.textView1);
		TextView txtWeatherDescription = (TextView) findViewById(R.id.textView2);
		
		//set the font on the question text views
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"playtime.ttf");
		txtWeatherTitle.setTypeface(typeFace);
		txtWeatherDescription.setTypeface(typeFace);
				
		//get the weather feed
		LongRunningGetIO getWeather = new LongRunningGetIO();
		
		//execute weather feed based on the global zip code
		getWeather.execute(HomeActivity.globalZipcode);
		
	}

	public interface AsyncResponse {
		void processFinish(String output);
	}
	
	private class LongRunningGetIO extends AsyncTask <String, Void, String> {

		
		protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
	       InputStream in = entity.getContent();
	         StringBuffer out = new StringBuffer();
	         int n = 1;
	         while (n>0) {
	             byte[] b = new byte[4096];
	             n =  in.read(b);
	             if (n>0) out.append(new String(b, 0, n));
	         }
	         return out.toString();
	    }
		
		
		protected String doGetWeather(String zipCode) {
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			String text = null;
			
			//Generate the appropriate dates
			Calendar c = Calendar.getInstance(); 
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH)+1;
			int date = c.get(Calendar.DAY_OF_MONTH);
			
			String strYear = String.valueOf(year);
			String strMonth = String.valueOf(month);
			String strDate = String.valueOf(date);
			
			if (strMonth.length() < 2) {
				strMonth = "0" + strMonth;
			}
			

			if (strDate.length() < 2) {
				strDate = "0" + strDate;
			}
			
			String today = strYear + "-" + strMonth + "-" + strDate;
			
			GregorianCalendar gc = new GregorianCalendar();
			gc.add(Calendar.DATE,1);
			
			year = gc.get(Calendar.YEAR);
			month = gc.get(Calendar.MONTH)+1;
			date = gc.get(Calendar.DAY_OF_MONTH);
			
			strYear = String.valueOf(year);
			strMonth = String.valueOf(month);
			strDate = String.valueOf(date);
			
			if (strMonth.length() < 2) {
				strMonth = "0" + strMonth;
			}

			if (strDate.length() < 2) {
				strDate = "0" + strDate;
			}
			String tomorrow = strYear + "-" + strMonth + "-" + strDate;
			 
			//NOAA URL
			String NOAA_URL = "http://graphical.weather.gov/xml/SOAP_server/ndfdXMLclient.php?whichClient=NDFDgenMultiZipCode&lat=&lon=&listLatLon=&lat1=&lon1=&lat2=&lon2=&resolutionSub=&listLat1=&listLon1=&listLat2=&listLon2=&resolutionList=&endPoint1Lat=&endPoint1Lon=&endPoint2Lat=&endPoint2Lon=&listEndPoint1Lat=&listEndPoint1Lon=&listEndPoint2Lat=&listEndPoint2Lon=" 
			+ "&zipCodeList=" + zipCode + "&listZipCodeList=" 
			+ "&centerPointLat=&centerPointLon=&distanceLat=&distanceLon=&resolutionSquare=&listCenterPointLat=&listCenterPointLon=&listDistanceLat=&listDistanceLon=&listResolutionSquare=&citiesLevel=&listCitiesLevel=&sector=&gmlListLatLon=&featureType=&requestedTime=&startTime=&endTime=&compType=&propertyName=&product=glance" 
			+ "&begin=" + today + "&end=" + tomorrow 
			+ "&Unit=e&maxt=maxt&wx=wx&icons=icons&Submit=Submit";
			
			//execute the request
			HttpGet httpGet = new HttpGet(NOAA_URL);
			
			try {
				HttpResponse response = httpClient.execute(httpGet, localContext);
			    HttpEntity entity = response.getEntity();
			    text = getASCIIContentFromEntity(entity);
			   } catch (Exception e) {
			  	 return e.getLocalizedMessage();
			   }
			   return text;
		}
		
		protected void onPreExecute() {
			ProgressBar spinner =  (ProgressBar) findViewById(R.id.progressBar1);
			spinner.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected String doInBackground(String... params) {

			//do this in the background
			String xmlString = doGetWeather(params[0]);
			
			return xmlString;
			
		}
		

	    @Override
	    protected void onPostExecute(String result) {
	       updateWeatherScreen(result);
	    }
		
		

	}
	
	
	//once we have the weather, show the update
	@SuppressLint("NewApi")
	private void updateWeatherScreen(String weatherXML) {
		
		//if there is no SOAP error, continue
		if (weatherXML.indexOf("SOAP ERROR") == -1) {

			//identify all the views
			TextView txtWeatherTitle = (TextView) findViewById(R.id.textView1);
			TextView txtWeatherDescription = (TextView) findViewById(R.id.textView2);
			ImageView imgWeatherIcon = (ImageView)findViewById(R.id.imageView1);
			ProgressBar spinner =  (ProgressBar) findViewById(R.id.progressBar1);
			
			//Parse out the forecast data
			StringReader sr = new StringReader(weatherXML);
			org.w3c.dom.Document doc = null;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = null;
			try {
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(weatherXML));
			try {
				doc=builder.parse(is);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//get the current weather icon
			NodeList images = doc.getElementsByTagName("icon-link");
			String currentWeatherImage = images.item(0).getChildNodes().item(0).getNodeValue();
			
			//get the current weather conditions
			String currentWeatherText = "";
			NodeList allWeatherConditions = doc.getElementsByTagName("weather-conditions");
			NodeList weatherConditions = allWeatherConditions.item(0).getChildNodes();
			for (int wc=0; wc<allWeatherConditions.getLength();wc++){
				weatherConditions = allWeatherConditions.item(wc).getChildNodes();
				if (weatherConditions.getLength() > 0) {
					//find a node that ACTUALLY HAS WEATHER
					wc = allWeatherConditions.getLength()+1;
				}
			}
			
			for (int c=0;c<weatherConditions.getLength();c++) {
				
				if (weatherConditions.item(c).hasAttributes()) {
				
					NamedNodeMap weatherValues = weatherConditions.item(c).getAttributes();
					
					/*
					String strCoverage = "";
					String strIntensity = "";
					String strWeather = "";
					String strQualifier = "";
					String stradditive = "";
					*/
					if (currentWeatherText != "") { currentWeatherText = currentWeatherText + " and "; }
					for (int w=0;w<weatherValues.getLength();w++) {
						
						/*
						if (weatherValues.item(w).getNodeName().equals("additive")) {
							//additive
							if (!weatherValues.item(w).getNodeValue().equals("none")) {
								currentWeatherText = weatherValues.item(w).getNodeValue() + " ";
							}
						}
						*/
						
						if (weatherValues.item(w).getNodeName().equals("coverage")) {
							//coverage
							if (weatherValues.item(w).getNodeValue().indexOf("chance") != -1) {
								currentWeatherText = currentWeatherText +weatherValues.item(w).getNodeValue() + " of ";
							}
						}
						
						if (weatherValues.item(w).getNodeName().equals("intensity")) {
							//intensity
							if (!weatherValues.item(w).getNodeValue().equals("none")) {
								currentWeatherText = currentWeatherText +weatherValues.item(w).getNodeValue() + " ";
							}
						}
	
						if (weatherValues.item(w).getNodeName().equals("weather-type")) {
							//weather-type
							if (!weatherValues.item(w).getNodeValue().equals("none")) {
								currentWeatherText = currentWeatherText +weatherValues.item(w).getNodeValue() + " ";
							}
						}
						
	
						if (weatherValues.item(w).getNodeName().equals("qualifier")) {
							//qualifier
							if (!weatherValues.item(w).getNodeValue().equals("none")) {
								currentWeatherText = currentWeatherText +weatherValues.item(w).getNodeValue() + " ";
							}
						}
					}
				}
	
			}
			
			//get the current temperature high and low
			NodeList arrTemperatures = doc.getElementsByTagName("temperature");
			currentWeatherText = currentWeatherText + "\n";
			for (int i=0;i<arrTemperatures.getLength();i++){
				NodeList temperatureElements = arrTemperatures.item(i).getChildNodes();
				for (int t=0;t<temperatureElements.getLength();t++) {
					if (temperatureElements.item(t).getNodeName().equals("name")) {
						currentWeatherText = currentWeatherText + temperatureElements.item(t).getTextContent() + " ";
					}
					if (temperatureElements.item(t).getNodeName().equals("value")) {
						currentWeatherText = currentWeatherText + temperatureElements.item(t).getTextContent() +  (char) 0x00B0 + "\n";
						t=temperatureElements.getLength();  //break out of any temperature loop
					}
				}
			}
	
	
	
		    
	        //Display the weather forecast 
			spinner.setVisibility(View.INVISIBLE);
			txtWeatherTitle.setText("Your Weather Forecast");
			txtWeatherDescription.setText(currentWeatherText);
				
			//download the weather image
			new DownloadImageTask((ImageView) findViewById(R.id.imageView1)).execute(currentWeatherImage);
			
			//size the weather image coming from NOAA
			//imgWeatherIcon.setScaleX(3);
			//imgWeatherIcon.setScaleY(3);
			
			//make the weather icon visible
			imgWeatherIcon.setVisibility(View.VISIBLE);
		}
		else
		{
			//SOAP ERROR - probably an invalid zip code
			//invalid zip code
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);                      
		    dlgAlert.setMessage("I\'m afraid this is an invalid zip code.  Please try again.");
		    dlgAlert.setTitle("Invalid Zip Code");              
		    dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Log.d( "AlertDialog", "Positive" );
                    finish();
                }
            });
 		    dlgAlert.setCancelable(false);
		    dlgAlert.create().show();
		    
			
		}
	
	
	}
	
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}


}
