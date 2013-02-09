package com.gra.menu;

import com.gra.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

enum achievementType{acziwment1};

public class OptionsTab3 extends Activity implements OnClickListener{
	
	private ImageView achievement11;
	private ImageView achievement12;
	private ImageView achievement13;
	private ImageView achievement21;
	private ImageView achievement22;
	private ImageView achievement23;
	private ImageView achievement31;
	private ImageView achievement32;
	private ImageView achievement33;
	private ImageView achievement41;
	
	private ImageView preview;
	private TextView info;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab3layout);
        
        //inicjalizacja obrazkow
        achievement11 = (ImageView) findViewById(R.id.tab3_row1col1);
        achievement12 = (ImageView) findViewById(R.id.tab3_row1col2);
        achievement13 = (ImageView) findViewById(R.id.tab3_row1col3);
        achievement21 = (ImageView) findViewById(R.id.tab3_row2col1);
        achievement22 = (ImageView) findViewById(R.id.tab3_row2col2);
        achievement23 = (ImageView) findViewById(R.id.tab3_row2col3);
        achievement31 = (ImageView) findViewById(R.id.tab3_row3col1);
        achievement32 = (ImageView) findViewById(R.id.tab3_row3col2);
        achievement33 = (ImageView) findViewById(R.id.tab3_row3col3);
        achievement41 = (ImageView) findViewById(R.id.tab3_row4col1);
        
        //nadanie nasluchu
        achievement11.setOnClickListener(this);
        achievement12.setOnClickListener(this);
        achievement13.setOnClickListener(this);
        achievement21.setOnClickListener(this);
        achievement22.setOnClickListener(this);
        achievement23.setOnClickListener(this);
        achievement31.setOnClickListener(this);
        achievement32.setOnClickListener(this);
        achievement33.setOnClickListener(this);
        achievement41.setOnClickListener(this);
        
        //podlgad achievementu
        preview = (ImageView) findViewById(R.id.tab3_achievementPreview);
        info = (TextView) findViewById(R.id.tab3_achievementInfo);
    }
	
	private void showInfo(achievementType type, ImageView v) {
		switch(type){
		case acziwment1:
			preview.setImageDrawable(v.getDrawable());
			info.setText(R.string.acv_novice);
			break;
		}
	}



	public void onClick(View v) {
		switch(v.getId()){
		case R.id.tab3_row1col1:
			showInfo(achievementType.acziwment1, (ImageView)v);
			break;
		}
	}
}