package com.example.kwowdy3;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.agecheq.kwowdy3.R;

public class LearnActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//lock in portrait
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		
		//set the layout
		setContentView(R.layout.activity_learn);
		
		//set the font on the question text views
		TextView txtQuestion0 =(TextView)findViewById(R.id.txtQuestion0);
		TextView txtQuestion1 =(TextView)findViewById(R.id.txtQuestion1);
		TextView txtQuestion2 =(TextView)findViewById(R.id.txtQuestion2);
		TextView txtQuestion3 =(TextView)findViewById(R.id.txtQuestion3);
		TextView txtQuestion4 =(TextView)findViewById(R.id.txtQuestion4);
		TextView txtQuestion5 =(TextView)findViewById(R.id.txtQuestion5);
		TextView txtQuestion6 =(TextView)findViewById(R.id.txtQuestion6);
		TextView txtQuestion7 =(TextView)findViewById(R.id.txtQuestion7);
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"playtime.ttf");
		txtQuestion0.setTypeface(typeFace);
		txtQuestion1.setTypeface(typeFace);
		txtQuestion2.setTypeface(typeFace);
		txtQuestion3.setTypeface(typeFace);
		txtQuestion4.setTypeface(typeFace);
		txtQuestion5.setTypeface(typeFace);
		txtQuestion6.setTypeface(typeFace);
		txtQuestion7.setTypeface(typeFace);
		
	}
	
	public void doAnswers(View view) {
		
		//pass answers to the answers activity
		int intAnswer = 0;
		if (view.getId() == R.id.txtQuestion0) { intAnswer = 0; }
		if (view.getId() == R.id.txtQuestion1) { intAnswer = 1; }
		if (view.getId() == R.id.txtQuestion2) { intAnswer = 2; }
		if (view.getId() == R.id.txtQuestion3) { intAnswer = 3; }
		if (view.getId() == R.id.txtQuestion4) { intAnswer = 4; }
		if (view.getId() == R.id.txtQuestion5) { intAnswer = 5; }
		if (view.getId() == R.id.txtQuestion6) { intAnswer = 6; }
		if (view.getId() == R.id.txtQuestion7) { intAnswer = 7; }
		
		Intent answerIntent = new Intent(LearnActivity.this, AnswersActivity.class);
		answerIntent.putExtra("Answer",intAnswer);
		startActivity(answerIntent);
		
	}

}
