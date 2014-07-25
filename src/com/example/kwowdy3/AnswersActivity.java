package com.example.kwowdy3;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import com.agecheq.kwowdy3.R;

public class AnswersActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		
		//lock in portrait
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		
		//set the layout
		setContentView(R.layout.activity_answers);
		
		//lock in portrait
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		
		//set the layout
		setContentView(R.layout.activity_answers);
				
		//get questions and answers
		Resources res = getResources();
		
		//get text views
		TextView txtQuestion = (TextView) findViewById(R.id.txtQuestion);
		TextView txtAnswer = (TextView) findViewById(R.id.txtAnswer);
		
		//set the text view font
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"playtime.ttf");
		txtQuestion.setTypeface(typeFace);
		txtAnswer.setTypeface(typeFace);
		
		//get info from intent
		Intent passedIntent = getIntent();
		Bundle bundle = passedIntent.getExtras();
				
		if (bundle != null)
		{
			int intAnswer = bundle.getInt("Answer");
			
			//translate that into getting the right q and a
			if (intAnswer == 0)
			{
			txtQuestion.setText(res.getString(R.string.question0));
			txtAnswer.setText(res.getString(R.string.answer0));	
			}
			
			if (intAnswer == 1)
			{
			txtQuestion.setText(res.getString(R.string.question1));
			txtAnswer.setText(res.getString(R.string.answer1));	
			}
			
			if (intAnswer == 2)
			{
			txtQuestion.setText(res.getString(R.string.question2));
			txtAnswer.setText(res.getString(R.string.answer2));	
			}
			
			if (intAnswer == 3)
			{
			txtQuestion.setText(res.getString(R.string.question3));
			txtAnswer.setText(res.getString(R.string.answer3));	
			}
			
			if (intAnswer == 4)
			{
			txtQuestion.setText(res.getString(R.string.question4));
			txtAnswer.setText(res.getString(R.string.answer4));	
			}
			
			if (intAnswer == 5)
			{
			txtQuestion.setText(res.getString(R.string.question5));
			txtAnswer.setText(res.getString(R.string.answer5));	
			}
			
			if (intAnswer == 6)
			{
			txtQuestion.setText(res.getString(R.string.question6));
			txtAnswer.setText(res.getString(R.string.answer6));	
			}
			
			if (intAnswer == 7)
			{
			txtQuestion.setText(res.getString(R.string.question7));
			txtAnswer.setText(res.getString(R.string.answer7));	
			}
			
		}
		
	}

}
