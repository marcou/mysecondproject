package com.gra.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.gra.R;
import com.gra.kreator.PlayerCreatorView;
import com.gra.minigra.MiniGame1View;

public class Options extends Activity {
	//PlayerCreatorView view;
	MiniGame1View view;
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
        view = new MiniGame1View(this);
        setContentView(view);
        view.requestFocus();
    }
}