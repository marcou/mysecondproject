package com.gra.kreator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author Szpdada
 * activity do kreatora gracza
 */
public class PlayerCreatorActivity extends Activity {
	    PlayerCreatorView view;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        // Set full screen view
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);

	        view = new PlayerCreatorView(this);
	        setContentView(view);
	        view.requestFocus();
	    }
}

