package com.gra.menu;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.gra.gra.GameView;

import com.gra.zapisy.SaveContainer;
import com.gra.zapisy.SaveService;
import com.gra.zapisy.UserSettings;

public class GameStart extends Activity {
	//PlayerCreatorView view;
	GameView view;
	
	SaveService saver;
	
	boolean resuming = false;
	
    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.options);
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set full screen view
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

//        view = new PlayerCreatorView(this);
//        setContentView(view);
//        view.requestFocus();
        
        
        
        saver = new SaveService(GameStart.this);
        
        //SKALOWANIE
        DisplayMetrics displaymetrics = new DisplayMetrics(); 
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics); 
        int height = displaymetrics.heightPixels; 
        int width = displaymetrics.widthPixels;
        double h_factor = height/800.0;
        double w_factor = width/480.0;
        
        
        
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
			Log.d("GameActivity","byly extrasy");
			
			if (extras.getBoolean("RESUME")) {
				Log.d("GameActivity","resume zapisanej gry");
				SaveContainer savedstate = (SaveContainer) extras.get("SAVE");
				
				view = new GameView(this,w_factor, h_factor,savedstate.getPlayer());
				view.setFlyingObjects(savedstate.getObjects());
				
				view.setThorn(true); //rozwiazanie tymczasowe, za bardzo mnie wkurwialy te kolce
				
			}
			else {
				view = new GameView(this,w_factor, h_factor);
			}
			
			view.setSettings((UserSettings) extras.get("SETTINGS")) ;
        }
        else {
        	view = new GameView(this,w_factor, h_factor);
        }
        
        
        setContentView(view);
        view.requestFocus();
    }
    
	protected void onStop() 
    {
        super.onStop();
        Log.d("GameActivity", "MYonStop is called");
        //saveState();
        finish();
    }
	
	
	protected void onPause() {
    	super.onPause();
    	Log.d("GameActivity", "MYonPause is called");
    	saveState();
    	resuming=true; //so that on resume you can read in last state
	}
	
	
	protected void onResume() {
        super.onResume();
        Log.d("GameActivity", "!jestem w GameActivity.onResume()");
        
        if (resuming) { //only if the game is being resumed, o/w the game that's sitting in the save file is not relevant
        	readSavedState();
        	Log.d("GameActivity", "read last saved state");
            
        }
		
		
    }
	
	

	private void readSavedState() {
//		SaveContainer savedstate = new SaveContainer(view.getPlayer(), view.getFlyingObjects());
		SaveContainer lastState = saver.readLastState();
		view.setFlyingObjects(lastState.getObjects());
		view.setPlayer(lastState.getPlayer());
		
	}

	private void saveState() {
		
		SaveContainer savedstate = new SaveContainer(view.getPlayer(), view.getFlyingObjects());
        saver.save(savedstate);
        UserSettings settings = view.getSettings();
        settings.setAchievements(view.getAchievements());
        saver.saveSettings(settings, "user_settings");
        Log.d("GameActivity", "SAVED STATE");
        
	}
    
    
    
}