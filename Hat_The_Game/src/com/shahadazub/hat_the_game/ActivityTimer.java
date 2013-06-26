package com.shahadazub.hat_the_game;

import com.shahadazub.hat_the_game.R.layout;

import android.R;
import android.R.color;
import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Layout;

import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
 
public class ActivityTimer extends Activity implements OnClickListener, TabListener {
  /** Properties **/
	
  protected Button startBrew;
  protected ImageButton rebootBrew;
  
  
  protected int brewTime;
  protected CountDownTimer brewCountDownTimer, waitTimer;
  static boolean green;
  public MediaPlayer mp;
  protected boolean isBrewing = false;
  public Intent intent = new Intent();
  public long q;
  
 
  RelativeLayout TimerLay;
 
  public TextView text;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
   
    
    
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    
    setContentView(com.shahadazub.hat_the_game.R.layout.timer);
    TimerLay = (RelativeLayout)findViewById(com.shahadazub.hat_the_game.R.layout.timer);
    mp = MediaPlayer.create(this, com.shahadazub.hat_the_game.R.raw.siren);
 
    
    startBrew = (Button) findViewById(com.shahadazub.hat_the_game.R.id.brew_start_stop);
    rebootBrew = (ImageButton)findViewById(com.shahadazub.hat_the_game.R.id.imageButton1);
   
    
    
    
    
    // Setup ClickListeners
    rebootBrew.setOnClickListener(this);
    startBrew.setOnClickListener(this);
    
    final ActionBar BarWords = getActionBar();
    
    BarWords.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    
    BarWords.setDisplayShowHomeEnabled(false);
    
    BarWords.setDisplayShowTitleEnabled(false);
    
    Tab tabadder = BarWords.newTab();
    tabadder.setText("60 сек");
    tabadder.setTabListener(this);
    BarWords.addTab(tabadder);
    
    tabadder = BarWords.newTab();
    tabadder.setText("30 сек");
    tabadder.setTabListener(this);
    BarWords.addTab(tabadder);
    
    tabadder = BarWords.newTab();
    tabadder.setText("20 сек");
    tabadder.setTabListener(this);
    BarWords.addTab(tabadder);
   
    
    // Connect interface elements to properties
    
    
    setBrewTime(60);
    
    // Set the initial brew values
    super.onCreate(savedInstanceState);
  }
  
  
  
  
  public void setBrewTime(int minutes) {
	   
    brewTime = minutes;
    
    startBrew.setText(String.valueOf(brewTime) + " сек");
    startBrew.setBackgroundColor(getResources().getColor(com.shahadazub.hat_the_game.R.color.HatAppBackColor));
    startBrew.setTextColor(getResources().getColor(R.color.black));
    if (mp.isPlaying()){
    	mp.pause();
    }      
    
    
  }
  

  

  public void startBrew() {
	  
    // Create a new CountDownTimer to track the brew time
	  
	  
    brewCountDownTimer = new CountDownTimer((brewTime + 5) * 1000, 1000) {
      @Override
      public void onTick(long millisUntilFinished) {
    	q = millisUntilFinished;
    	if ((((millisUntilFinished - 100) / 1000) - 5) >= 0) {
       startBrew.setText(String.valueOf(((millisUntilFinished - 100) / 1000) - 5) + " сек");
    	}
    	
        if ((millisUntilFinished/1000) > 11){
        	
        	
        	startBrew.setBackgroundColor(getResources().getColor(com.shahadazub.hat_the_game.R.color.GreenColor));
        	
        	green = true;  
        } else if ((millisUntilFinished/1000) < 5) {
        	
        	startBrew.setText("Время!");
        	startBrew.setBackgroundColor(getResources().getColor(com.shahadazub.hat_the_game.R.color.DarkRedColor));
        	startBrew.setTextColor(getResources().getColor(R.color.black));
        	
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

      }
    };
    
    
    brewCountDownTimer.start();
    isBrewing = true;
    
    
  }
  
  

  public void pauseBrew() {
    if(brewCountDownTimer != null)
      brewCountDownTimer.cancel();
    
    isBrewing = false;
    
    
  }
  
  
  
  public void stopBrew() {
	    if(brewCountDownTimer != null)
	      brewCountDownTimer.cancel();
	    
	    isBrewing = false;
	    
	  }
  
    
  
  public void onClick(View v) {
	
    if(v == startBrew) {
      if(isBrewing){
    	  if ((q/1000) < 5){
    		  stopBrew();
    		  setBrewTime(brewTime);
    	  }else {
    		  
    		  pauseBrew();
    	  }
      }
      else{
       
        startBrew();
    	  }
    }else if (v == rebootBrew){
    	setBrewTime(brewTime);
    	stopBrew();
    	intent.setClass(this, ActivityMainMenu.class);
    	startActivity(intent);
    }
  }

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		switch (tab.getPosition()){
		case 0:
			
			stopBrew();
			setBrewTime(60);
			
			
			break;
			
		case 1:
			
			stopBrew();
			setBrewTime(30);	
			
			
			break;
			
		case 2:
			
			stopBrew();
			setBrewTime(20);
			
			
			break;
		}
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		switch (tab.getPosition()){
		case 0:
			
			stopBrew();
			setBrewTime(60);
			
			
			break;
			
		case 1:
			
			stopBrew();
			setBrewTime(30);
			
			
			break;
			
		case 2:
			
			stopBrew();
			setBrewTime(20);		
			
			
			break;
			
		}
	}
	
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
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