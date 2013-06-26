package com.shahadazub.hat_the_game;

import java.util.Random;

import android.R;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class ActivityPlay extends Activity implements OnClickListener {
	
	Button ButClue;
	Button ButTimer;
	CheckBox[] ChBoxes = new CheckBox[20];
	String PlayerAndTeam;
	String Word;
	int WordsGessed = 0;
	SharedPreferences PrefPlayers;
	SharedPreferences PrefWords;
	SharedPreferences WordsToGuess;
	SharedPreferences WordsGuessed;
	SharedPreferences Points;
	SharedPreferences Settings;
	int NumberOfPlayers;
	int NumberOfTeams;
	int NumberOfWordsToGuess;
	int NumberOfWordsToGuessPerPlayer;
	int Turn;
	int Round;
	String[] Teams;
	String[] Players;
	String[] Words;
 	String PlayerString = "Player_";
	String IdForCheckBox;
	int WordCounter = 0;
	int GuessedWordCounter;
	int Player;
	int Team;
	TextView ViewWord;
	int Time = 2; 
	Boolean GuessWords;
	
	protected int brewTime;
	protected CountDownTimer brewCountDownTimer, waitTimer;
	static boolean green;
	public MediaPlayer mp;
	protected boolean isBrewing = false;
	public Intent intent = new Intent();
	public long q;
	protected Button startBrew;
	
	String LOG_TAG = "PlayLog";
 	
	int[] CreatedRnd;
	Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	Log.d(LOG_TAG, "Activity Play Started");
    	
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        super.onCreate(savedInstanceState);
        setContentView(com.shahadazub.hat_the_game.R.layout.play);
        
        context = ActivityPlay.this;
        
        mp = MediaPlayer.create(this, com.shahadazub.hat_the_game.R.raw.siren);
        
        GetButtonsAndCheckboxes();
        Log.d(LOG_TAG, "We got some views and thinking about Prefs");
    	
        GetPrefs();
        Log.d(LOG_TAG, "We got Prefs");
        
        setBrewTime(Time);
        Log.d(LOG_TAG, "Brew time is set");
        
        Team = Turn % NumberOfTeams;
        Player = ( Team * 2 ) + ( ( Turn / NumberOfTeams ) % 2 );        
        Log.d(LOG_TAG, "Player is number " + Player + " and Team is number " + Team);
        
        
        TextView ViewPlayerAndTeam = (TextView) findViewById(com.shahadazub.hat_the_game.R.id.TextPlayerTeam);
        PlayerAndTeam = "загадывает игрок: " + Players[Player] + "    команда: " + Teams[Team];
        ViewPlayerAndTeam.setText(PlayerAndTeam);
        Log.d(LOG_TAG, PlayerAndTeam);

        
        ViewWord = (TextView)findViewById(com.shahadazub.hat_the_game.R.id.TextWord);
        ViewWord.setText("********");
        
        if(Turn == 0){
        	Alert("Передайте телефон игроку " + Players[Player] + " для начала хода");
        } else {
        	Alert("Теперь вы, " + Players[Player] + ", загадываете");
        }
        
        CheckIfThereIsEnoughfWords();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.shahadazub.hat_the_game.R.menu.play, menu);
        return true;
    }

    
    
    
    
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == com.shahadazub.hat_the_game.R.id.ButTimer){
			
			ButClue.setText("Отгадано");
			Log.d(LOG_TAG, Words[WordCounter]);
			ViewWord.setText(Words[WordCounter]);
			startBrew.setEnabled(false);
			ButClue.setEnabled(true);
			Log.d(LOG_TAG, "Timer Starting");
			startBrew();
			
			
			
			
		}else if (v.getId() == com.shahadazub.hat_the_game.R.id.ButClueded){
			
			ChBoxes[WordCounter].setChecked(true);
			
			Editor ed = WordsGuessed.edit();
			
			ed.putString("Word_" + GuessedWordCounter, Words[WordCounter]);
			ed.commit();
			
			GuessedWordCounter++;			
			WordCounter++;
			
			if(WordCounter == NumberOfWordsToGuess){
				brewCountDownTimer.cancel();
				
				
				
				
				AfterTurnTramites();
				
				Intent intent = new Intent();
				intent.setClass(context, ActivityControl.class);
				startActivity(intent);
			}else{
				ViewWord.setText(Words[WordCounter]);
			}
			
			
			
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	public void GetPrefs(){
		
		PrefPlayers = getSharedPreferences("Players", MODE_PRIVATE);
        WordsToGuess = getSharedPreferences("WordsToGuess", MODE_PRIVATE);
        WordsGuessed = getSharedPreferences("WordsGuessed", MODE_PRIVATE);
        Points = getSharedPreferences("Points", MODE_PRIVATE);
        Settings = getSharedPreferences("Settings", MODE_PRIVATE);
        
        Editor ed = WordsGuessed.edit();
        ed.clear();
        
        NumberOfPlayers = PrefPlayers.getInt("NumberOfPlayers", 0);
        NumberOfTeams = PrefPlayers.getInt("NumberOfTeams", 0);
        NumberOfWordsToGuess = PrefPlayers.getInt("NumberOfWordsToGuess", 0);
        Log.d(LOG_TAG, "NumberofWordtoguess = " + NumberOfWordsToGuess);
        
        Turn = PrefPlayers.getInt("NumberOfTurn", 0);
        Round = PrefPlayers.getInt("Round", 0);
        
        Teams = new String[NumberOfTeams];
        Players = new String[NumberOfPlayers];
        
        for (int i = 0; i < NumberOfTeams; i++) {
			
        	Teams[i] = PrefPlayers.getString("Team_" + String.valueOf(i), "Que conyo que no existe");
        	Log.d(LOG_TAG, "Team_" + String.valueOf(i) + " is " + Teams[i]);
        	Players[((i+1)*2)-2] = PrefPlayers.getString(PlayerString + String.valueOf(0) +"_Team_" + String.valueOf(i), "Que conyo que no existe");
        	Players[((i+1)*2)-1] = PrefPlayers.getString(PlayerString + String.valueOf(1) +"_Team_" + String.valueOf(i), "Que conyo que no existe");
        	
        	
		}
        
        Words = new String[NumberOfWordsToGuess];
        PrefWords = getSharedPreferences("WordsForGame", MODE_PRIVATE);
        Log.d(LOG_TAG, "Number of words to guess is " + NumberOfWordsToGuess);
        for (int i = 0; i < NumberOfWordsToGuess; i++) {
        	
        	Words[i] = WordsToGuess.getString("Word_" + String.valueOf(i), "Que conyo que no existe");
        	Log.d(LOG_TAG, Words[i] + " is the word number " + "Word_" + String.valueOf(i));
			
		}
        
        GuessWords = Settings.getBoolean("GuessWords", true);
       
        
        if (GuessWords == true) {
        	Time = Settings.getInt("TimeToGuess", 30);
        	NumberOfWordsToGuessPerPlayer = Settings.getInt("NumberOfWordsToGuessPerPlayer", 5);
        } else{
        	NumberOfWordsToGuessPerPlayer = 5;
        	switch (Round){
        	case 1:
        		Time = 30;
        		break;
        	case 2:
        		Time = 30;
        		break;
        	case 3:
        		Time = 15;
        		break;
        	}
        	
        }
        
	}
	
	
	public void startBrew() {
		  
	    // Create a new CountDownTimer to track the brew time
		  
		  
	    brewCountDownTimer = new CountDownTimer((brewTime + 3) * 1000, 1000) {
	    	
	      @Override
	      public void onTick(long millisUntilFinished) {
	    	q = millisUntilFinished;
	    	if ((((millisUntilFinished - 100) / 1000) - 3) >= 0) {
	       startBrew.setText(String.valueOf(((millisUntilFinished - 100) / 1000) - 3) + " сек");
	       Log.d(LOG_TAG, "Text on the timer button is set");
	    	}
	    	
	        if ((millisUntilFinished/1000) > 9){
	        	
	        	Log.d(LOG_TAG, "The time is enough");
	        	startBrew.setBackgroundColor(getResources().getColor(com.shahadazub.hat_the_game.R.color.GreenColor));
	        	
	        	green = true;  
	        } else if ((millisUntilFinished/1000) < 3) {
	        	
	        	startBrew.setText("Время!");
	        	startBrew.setBackgroundColor(getResources().getColor(com.shahadazub.hat_the_game.R.color.DarkRedColor));
	        	startBrew.setTextColor(getResources().getColor(R.color.black));
	        	
	        	ButClue.setEnabled(false);
	        	ViewWord.setText("********");
	        	
	        	
				if (mp.isPlaying()){			
				}else {
					mp.start();	
					
				}
	        	
	      	} else if (green == true){
	      		
	        	
	        	startBrew.setBackgroundColor(getResources().getColor(com.shahadazub.hat_the_game.R.color.redColor));
	        	startBrew.setTextColor(getResources().getColor(com.shahadazub.hat_the_game.R.color.GreenColor));
	        	green = false;
	        	
	        }else if (green == false) {
	        	
	        	
	        	startBrew.setBackgroundColor(getResources().getColor(com.shahadazub.hat_the_game.R.color.GreenColor));
	        	startBrew.setTextColor(getResources().getColor(com.shahadazub.hat_the_game.R.color.redColor));
	        	green = true;
	        	
	        }
	        
	      }
	      
	      @Override
	      public void onFinish() {
	    	 
	        isBrewing = false;
	        setBrewTime(brewTime);
	        
	        AfterTurnTramites();       
	        
	        
	        Intent intent = new Intent();
	        intent.setClass(context, ActivityControl.class);
	        startActivity(intent);

	      }
	    };
	    
	    
	    brewCountDownTimer.start();
	    isBrewing = true;
	    
	    
	  }
	
	public void setBrewTime(int minutes) {
		   
	    brewTime = minutes;
	    
	    startBrew.setText("Нажмите сюда когда будете готовы загадывать и начнется отсчет времени");
	    startBrew.setBackgroundColor(getResources().getColor(com.shahadazub.hat_the_game.R.color.HatAppBackColor));
	    startBrew.setTextColor(getResources().getColor(R.color.black));
	    if (mp.isPlaying()){
	    	mp.pause();
	    }      

	}
	
	
	
	
	
	
	public void AfterTurnTramites(){
		
		Editor EdPlayers = PrefPlayers.edit();
		Editor EdPoints = Points.edit();
		Editor edWordsGuessed = WordsGuessed.edit();
		
		
		if (NumberOfWordsToGuess == GuessedWordCounter) {
			EdPoints.putBoolean("Final", true);
			EdPoints.commit();
			EdPlayers.putInt("NumberOfWordsToGuess", NumberOfWordsToGuessPerPlayer * NumberOfPlayers);
			EdPlayers.commit();
		}else {
			RandomizeWords();
			EdPoints.putBoolean("Final", false);
			EdPoints.commit();
			NumberOfWordsToGuess = NumberOfWordsToGuess - GuessedWordCounter;
			Log.d(LOG_TAG, "Number of Words to guess = " + NumberOfWordsToGuess + " and guessed words are " + GuessedWordCounter);
			EdPlayers.putInt("NumberOfWordsToGuess", NumberOfWordsToGuess);
			EdPlayers.commit();
			
		}
		
		
		
		
		int BeforePoints = Points.getInt("Team_" + Team, 998);
		EdPoints.putInt("Team_" + Team, (GuessedWordCounter + BeforePoints));
		EdPoints.commit();
		Log.d(LOG_TAG, "Points: " + "Team_" + Team + " " + GuessedWordCounter + " " + BeforePoints);
		
		edWordsGuessed.putInt("NumberOfWordsGuessed", GuessedWordCounter);
		edWordsGuessed.commit();
		
			
	}
	
	
	
	public void Alert(String s){
		
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle("");
		ad.setMessage(s);
		ad.setPositiveButton("Далее", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		}); 
		ad.show();
	}
	
	
	public void RandomizeWords(){
    	
    	int rnd;
    	CreatedRnd = new int[NumberOfWordsToGuess - GuessedWordCounter];
    	Editor ed = WordsToGuess.edit();
    	ed.clear();
    	
    	for (int i = 0; i < (NumberOfWordsToGuess - GuessedWordCounter); i++) {
    		
    		boolean Exist = false;
			
    		do {
				rnd = new Random().nextInt(NumberOfWordsToGuess - GuessedWordCounter);
				Log.d(LOG_TAG, "Random int is = " + rnd);
				Exist = false;
				for (int j = 0; j < i; j++) {
					
					if (rnd == CreatedRnd[j]){
						
						Exist = true;
						Log.d(LOG_TAG, rnd + " is  exist = "+ Exist + " and its number is " + j);
					}
						
					
				}
    		
			} while (Exist == true);
    		
    		CreatedRnd[i] = rnd;
    		ed.putString("Word_" + i, Words[rnd]);
    		ed.commit();
    		Log.d(LOG_TAG, Words[rnd] + " putted as a " + "Word_" + i);
    		
		}
    	
    	
    	
    	
    }
	
	
	public void GetButtonsAndCheckboxes(){
		
		ButClue = (Button)findViewById(com.shahadazub.hat_the_game.R.id.ButClueded);
        ButClue.setOnClickListener(this);
        ButClue.setEnabled(false);
        ButClue.setText("Сюда нажимать когда слово отгадано");
        Log.d(LOG_TAG, "ButClue added");
        
        startBrew = (Button)findViewById(com.shahadazub.hat_the_game.R.id.ButTimer);
        startBrew.setOnClickListener(this);
        startBrew.setEnabled(true);
        Log.d(LOG_TAG, "startBrew added");
        	
        	
        	ChBoxes[0] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox1);
        	ChBoxes[1] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox2);
        	ChBoxes[2] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox3);
        	ChBoxes[3] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox4);
        	ChBoxes[4] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox5);
        	ChBoxes[5] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox6);
        	ChBoxes[6] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox7);
        	ChBoxes[7] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox8);
        	ChBoxes[8] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox9);
        	ChBoxes[9] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox10);
        	ChBoxes[10] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox11);
        	ChBoxes[11] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox12);
        	ChBoxes[12] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox13);
        	ChBoxes[13] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox14);
        	ChBoxes[14] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox15);
        	ChBoxes[15] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox16);
        	ChBoxes[16] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox17);
        	ChBoxes[17] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox18);
        	ChBoxes[18] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox19);
        	ChBoxes[19] = (CheckBox)findViewById(com.shahadazub.hat_the_game.R.id.ChBox20);
        	
        	for (int i = 0; i < 20; i++) {
				
			ChBoxes[i].setChecked(false);
        	ChBoxes[i].setClickable(false);
        	ChBoxes[i].setFocusable(false);
        	
        	}
	}
	
	
	
	public void CheckIfThereIsEnoughfWords(){
		
		if (NumberOfWordsToGuess < 20){
			for (int i = 0; i < (20 - NumberOfWordsToGuess); i++) {
				ChBoxes[i+NumberOfWordsToGuess].setEnabled(false);
			}
		}
		
	}
	
	public void onBackPressed(){
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		Intent intent = new Intent();
		switch (item.getItemId()){
		case com.shahadazub.hat_the_game.R.id.NewGame:
			
	        intent.setClass(this, ActivityCountPlayers.class);
	        startActivity(intent);
	        break;
		case com.shahadazub.hat_the_game.R.id.ToMainMenu:
			
	        intent.setClass(this, ActivityMainMenu.class);
	        startActivity(intent);
	        break;
		
		}
		
		return super.onOptionsItemSelected(item);
		
	}
	
}