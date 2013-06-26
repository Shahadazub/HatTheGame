package com.shahadazub.hat_the_game;

import com.shahadazub.hat_the_game.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class ActivityMainMenu extends Activity {

	public Intent intent = new Intent();
	public static final int SPLASHSTOP = 0;
	public static long SPLASHTIME = 1;//3000;
	public ImageView splash;


	@Override
    public void onCreate(Bundle savedInstanceState) {
        
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        
        
        setContentView(R.layout.mainmenu);
        
        //Button ButRules = (Button) findViewById(R.id.ButRules);
       // Button ButTimer = (Button) findViewById(R.id.ButTimer);
       // Button ButWords = (Button) findViewById(R.id.ButWords);
        
       
        
        super.onCreate(savedInstanceState);             
    }
	
	
	
	
    
	
    public void onClick(View v){
    	
    	switch (v.getId()){
    	
    	case R.id.ButRules:    		 
    		intent.setClass(this, Activity_Rules.class);
    		startActivity(intent);
    		break;
    	
    	case R.id.ButWords:    		
    		intent.setClass(this, ActivityWords.class);
    		startActivity(intent);
    		break;
    		
    	case R.id.ButTimer:
    		intent.setClass(this, ActivityTimer.class);    	
    		startActivity(intent);    		
    		break;
    		
    	case R.id.ButGame:
    		intent.setClass(this, ActivityCountPlayers.class);
    		startActivity(intent);
    		break;
    	case R.id.ButSettings:
    		intent.setClass(this, ActivitySettings.class);
    		startActivity(intent);
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_activity_main_menu, menu);
        return true;
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
