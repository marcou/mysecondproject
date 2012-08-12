package com.gra.minigra;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author Szpdada
 * activity do kreatora gracza
 */
public class MiniGame1Activity extends Activity {
	    MiniGame1View view;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        // Set full screen view
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);

	        view = new MiniGame1View(this);
	        setContentView(view);
	        view.requestFocus();
	    }
}

