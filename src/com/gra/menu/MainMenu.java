package com.gra.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.gra.R;
import com.gra.zapisy.SaveContainer;
import com.gra.zapisy.SaveService;
import com.gra.zapisy.UserSettings;

public class MainMenu extends Activity {
	
	private SaveService saver;
	SaveContainer savedstate;
	UserSettings settings;
	private boolean canresume = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.mainmenu);
        
        
        
        
        readState();
        Log.d("MainMenu","tworzenie menu");
       
        Button ShowOptions = (Button)findViewById(R.id.button1);
        ShowOptions.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent ShowOptionsIntent = new Intent(MainMenu.this,Options.class);
				startActivity(ShowOptionsIntent);
			}
		});
        
        
        
        Button PlayGameButton = (Button)findViewById(R.id.Resume);
        PlayGameButton.setOnClickListener(new OnClickListener() {
        	
        	//@Override
			public void onClick(View v) {
        		Intent PlayGameIntent = new Intent(MainMenu.this,GameStart.class);
        		if (canresume) {
        			PlayGameIntent.putExtra("RESUME", true);
        			PlayGameIntent.putExtra("SAVE", savedstate);
        			Log.d("MainMenu","save przekazany");
        		}
        		else {
        			PlayGameIntent.putExtra("RESUME", false);
        			Log.d("MainMenu","nie ma sava");
        		}
        		//passing the settings to the main game screen
        		PlayGameIntent.putExtra("SETTINGS", settings);
        			
        		
        		startActivity(PlayGameIntent);
        	}
        });
        
        
        
        Button OptionsButton = (Button)findViewById(R.id.StartNew);
        OptionsButton.setOnClickListener(new OnClickListener() {
        	
        	//@Override
			public void onClick(View v) {
        		Intent NewGameIntent = new Intent(MainMenu.this,GameStart.class);
        		NewGameIntent.putExtra("RESUME", false);
        		NewGameIntent.putExtra("SETTINGS", settings);
        		startActivity(NewGameIntent);
        	}
        });
        


 

    }

private void readState() {
	saver = new SaveService(MainMenu.this);
	saver.existing();
    savedstate = saver.readLastState();
    settings = saver.readSettings("user_settings");
//    if (settings == null) settings = new UserSettings(); //in case it's a first game or something -> przeniesione do gameview
    canresume = (savedstate!=null);
	}

protected void onResume() {
	super.onResume();
	readState();
	Log.d("MainMenu","resume menu");
	

}

}