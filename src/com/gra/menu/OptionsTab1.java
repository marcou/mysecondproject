package com.gra.menu;

import com.gra.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class OptionsTab1 extends Activity{
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1layout);
        
        //przycisk przezroczstych przyciskow
        final CheckBox transparentControlls = (CheckBox) findViewById(R.id.tab1_transparentControlls);
        
        //grupa wyboru sterowania 
        RadioGroup radioButtons = (RadioGroup) findViewById(R.id.radioGroup1);
        
        //sterowanie akcelerometrem
        RadioButton accelerometer = (RadioButton) findViewById(R.id.tab1_accelerometer);
        //sterowanie przyciskami
        RadioButton controllsButtons = (RadioButton) findViewById(R.id.tab1_controllButton);
        
        //zmiana wybranego sterowania
        radioButtons.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
		        case R.id.tab1_accelerometer:
		        	transparentControlls.setEnabled(false);
		        	break;
		        case R.id.tab1_controllButton:
		        	transparentControlls.setEnabled(true);
		        	break;
		        }
			}
		});
    }
}
