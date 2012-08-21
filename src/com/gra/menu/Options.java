package com.gra.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.gra.R;
import com.gra.gra.GameView;
import com.gra.kreator.PlayerCreatorView;

public class Options extends Activity {
	//PlayerCreatorView view;
	GameView view;
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
        view = new GameView(this);
        setContentView(view);
        view.requestFocus();
    }
}