package com.gra.menu;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.gra.gra.TimeGameView;

import com.gra.zapisy.SaveContainer;
import com.gra.zapisy.SaveService;

public class TimeAttack extends Activity {
	//PlayerCreatorView view;
	TimeGameView view;
	
	SaveService saver;
	
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
				
				view = new TimeGameView(this,w_factor, h_factor);
				
				
			}
			else {
				view = new TimeGameView(this,w_factor, h_factor);
			}
        }
        else {
        	view = new TimeGameView(this,w_factor, h_factor);
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
    
}