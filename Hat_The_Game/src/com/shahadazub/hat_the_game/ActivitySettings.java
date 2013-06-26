package com.shahadazub.hat_the_game;

import android.R.color;
import android.os.Bundle;
import android.renderscript.Int2;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class ActivitySettings extends Activity  implements OnClickListener, OnCheckedChangeListener {
	
	RadioButton RadioNames, RadioWords;
	RadioGroup RadioGroupSettings;
	EditText EditNumberOfWordsToGuessPerPlayer, EditTimeToGuess;
	TextView TextTimeToGuess, TextNumberOfWordsToGuessPerPlayer;
	SharedPreferences Settings;
	Button Save;
	Boolean GuessWords;
	int TimeToGuess;
	int NumberOfWordsToGuessPerPlayer;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
            	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        RadioGroupSettings = (RadioGroup)findViewById(R.id.RadioGroupSettings);
        RadioWords = (RadioButton)findViewById(R.id.RadioWords);
        RadioNames = (RadioButton)findViewById(R.id.RadioNames);
        Save = (Button)findViewById(R.id.ButSaveSettings);
        Save.setOnClickListener(this);
        EditNumberOfWordsToGuessPerPlayer = (EditText)findViewById(R.id.EditNumbeerOfWords);
        EditTimeToGuess = (EditText)findViewById(R.id.EditTime);
        RadioGroupSettings.setOnCheckedChangeListener(this);
        TextNumberOfWordsToGuessPerPlayer = (TextView)findViewById(R.id.TextNumberOfWordsSettings);
        TextTimeToGuess = (TextView)findViewById(R.id.TextTimeSettings);
        
        
        GetPrefs();
        
        PutPrefsOnLayout();
        
                
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		TimeToGuess = Integer.parseInt(EditTimeToGuess.getText().toString());
		NumberOfWordsToGuessPerPlayer = Integer.parseInt(EditNumberOfWordsToGuessPerPlayer.getText().toString());
		
		Editor ed = Settings.edit();
		
		ed.putBoolean("GuessWords", GuessWords);
		ed.commit();
		ed.putInt("NumberOfWordsToGuessPerPlayer", NumberOfWordsToGuessPerPlayer);
		ed.commit();
		ed.putInt("TimeToGuess", TimeToGuess);
		ed.commit();
		
		
		Intent intent = new Intent();
		intent.setClass(this, ActivityMainMenu.class);
		startActivity(intent);
	}
	
	public void GetPrefs(){
		
		Settings = getSharedPreferences("Settings", MODE_PRIVATE);
		
		if (Settings.contains("GuessWords")){
			GuessWords = Settings.getBoolean("GuessWords", false);
			TimeToGuess = Settings.getInt("TimeToGuess", 60);
			NumberOfWordsToGuessPerPlayer = Settings.getInt("NumberOfWordsToGuessPerPlayer", 6);		
			
		} else {
			GuessWords = true;
			TimeToGuess = 30;
			NumberOfWordsToGuessPerPlayer = 5;
		}
	}
	
	
	public void PutPrefsOnLayout(){
		
		if (GuessWords){
			RadioWords.setChecked(true);
			RadioNames.setChecked(false);
			EditTimeToGuess.setEnabled(true);
			EditNumberOfWordsToGuessPerPlayer.setEnabled(true);
			TextNumberOfWordsToGuessPerPlayer.setTextColor(getResources().getColor(color.black));
			TextTimeToGuess.setTextColor(getResources().getColor(color.black));
			EditNumberOfWordsToGuessPerPlayer.setTextColor(getResources().getColor(color.black));
			EditTimeToGuess.setTextColor(getResources().getColor(color.black));
		} else {
			RadioWords.setChecked(false);
			RadioNames.setChecked(true);
			EditTimeToGuess.setEnabled(false);
			EditNumberOfWordsToGuessPerPlayer.setEnabled(false);
			TextNumberOfWordsToGuessPerPlayer.setTextColor(getResources().getColor(R.color.HatUnchoosedTextColor));
			TextTimeToGuess.setTextColor(getResources().getColor(R.color.HatUnchoosedTextColor));
			EditNumberOfWordsToGuessPerPlayer.setTextColor(getResources().getColor(R.color.HatUnchoosedTextColor));
			EditTimeToGuess.setTextColor(getResources().getColor(R.color.HatUnchoosedTextColor));
		}
		
		EditTimeToGuess.setText(String.valueOf(TimeToGuess));
		EditNumberOfWordsToGuessPerPlayer.setText(String.valueOf(NumberOfWordsToGuessPerPlayer));
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if (checkedId == R.id.RadioWords){
			
			EditTimeToGuess.setEnabled(true);
			EditNumberOfWordsToGuessPerPlayer.setEnabled(true);
			GuessWords = true;
			TextNumberOfWordsToGuessPerPlayer.setTextColor(getResources().getColor(color.black));
			TextTimeToGuess.setTextColor(getResources().getColor(color.black));
			EditNumberOfWordsToGuessPerPlayer.setTextColor(getResources().getColor(color.black));
			EditTimeToGuess.setTextColor(getResources().getColor(color.black));
		} else {
			
			EditTimeToGuess.setEnabled(false);
			EditNumberOfWordsToGuessPerPlayer.setEnabled(false);
			GuessWords= false;
			TextNumberOfWordsToGuessPerPlayer.setTextColor(getResources().getColor(R.color.HatUnchoosedTextColor));
			TextTimeToGuess.setTextColor(getResources().getColor(R.color.HatUnchoosedTextColor));
			EditNumberOfWordsToGuessPerPlayer.setTextColor(getResources().getColor(R.color.HatUnchoosedTextColor));
			EditTimeToGuess.setTextColor(getResources().getColor(R.color.HatUnchoosedTextColor));
		}
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		
		switch (item.getItemId()){
		case com.shahadazub.hat_the_game.R.id.MakeDefault:
			
			GuessWords = true;
			TimeToGuess = 30;
			NumberOfWordsToGuessPerPlayer = 5;
			PutPrefsOnLayout();
			
	        
	        break;
		
		}
		
		return super.onOptionsItemSelected(item);
		
	}
}
