package com.gra.menu;

import com.gra.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

enum achievementType{novice, apprentice, adept, master, isdp, dead, lover, casanova, alien, collector, duck, secret};

public class OptionsTab3 extends Activity implements OnClickListener{
	
	//obrazki przedtsawiajace achievementy
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
	private ImageView achievement42;
	private ImageView achievement43;
	//tablica imageViewsow
	private ImageView imageArray[][];
	
	//podglad wybranego achievementu
	private ImageView preview;
	//informacje o acheivemencie
	private TextView info;
	//nazwa achievementu
	private TextView title;
	//tablica zweirajaca id obrazow achievementow
	private int array[][];
	
	
	
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
        achievement42 = (ImageView) findViewById(R.id.tab3_row4col2);
        achievement43 = (ImageView) findViewById(R.id.tab3_row4col3);
        
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
        achievement42.setOnClickListener(this);
        achievement43.setOnClickListener(this);
        
        
        //achievementy nie zdobyte (czarnobiale)
        achievement11.setDrawingCacheEnabled(true);
        achievement11.buildDrawingCache();
       
        //zapelnij tablice danymi 
        fillArray();
        for(int i = 0; i < 4; i++){
        	for(int j = 0; j < 3; j++){
        		disableAchievement(i, j);
        	}
        }
        
        //podlgad achievementu
        preview = (ImageView) findViewById(R.id.tab3_achievementPreview);
        info = (TextView) findViewById(R.id.tab3_achievementInfo);
        title = (TextView) findViewById(R.id.tab3_achievementTitle);
        
        title.setTextColor(Color.GREEN);
        title.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        info.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
    }
	
	private void showInfo(achievementType type, ImageView v) {
		switch(type){
		case novice:
			preview.setImageDrawable(v.getDrawable());
			info.setText(R.string.acv_novice);
			title.setText(R.string.acv_novicetitle);
			break;
		case apprentice:
			preview.setImageDrawable(v.getDrawable());
			info.setText(R.string.acv_apprentice);
			title.setText(R.string.acv_apprenticetitle);
			break;
		case adept:
			preview.setImageDrawable(v.getDrawable());
			info.setText(R.string.acv_adept);
			title.setText(R.string.acv_adepttitle);
			break;
		case master:
			preview.setImageDrawable(v.getDrawable());
			info.setText(R.string.acv_master);
			title.setText(R.string.acv_mastertitle);
			break;
		case alien:
			preview.setImageDrawable(v.getDrawable());
			info.setText(R.string.acv_alien);
			title.setText(R.string.acv_alientitle);
			break;
		case lover:
			preview.setImageDrawable(v.getDrawable());
			info.setText(R.string.acv_lover);
			title.setText(R.string.acv_lovertitle);
			break;
		case casanova:
			preview.setImageDrawable(v.getDrawable());
			info.setText(R.string.acv_casanova);
			title.setText(R.string.acv_casanovatitle);
			break;
		case collector:
			preview.setImageDrawable(v.getDrawable());
			info.setText(R.string.acv_collector);
			title.setText(R.string.acv_collectortitle);
			break;
		case isdp:
			preview.setImageDrawable(v.getDrawable());
			info.setText(R.string.acv_isdp);
			title.setText(R.string.acv_isdptitle);
			break;
		case dead:
			preview.setImageDrawable(v.getDrawable());
			info.setText(R.string.acv_dead);
			title.setText(R.string.acv_deadtitle);
			break;
		case duck:
			preview.setImageDrawable(v.getDrawable());
			info.setText(R.string.acv_duck);
			title.setText(R.string.acv_ducktitle);
			break;
		case secret:
			preview.setImageDrawable(v.getDrawable());
			info.setText(R.string.acv_secret);
			title.setText(R.string.acv_secrettitle);
			break;
		}
	}

	public void onClick(View v) {
		switch(v.getId()){
		case R.id.tab3_row1col1:
			showInfo(achievementType.novice, (ImageView)v);
			disableAchievement(3, 1);
			break;
		case R.id.tab3_row1col2:
			showInfo(achievementType.duck, (ImageView)v);
			enableAchievement(3, 1);
			break;
		case R.id.tab3_row1col3:
			showInfo(achievementType.isdp, (ImageView)v);
			break;
		case R.id.tab3_row2col1:
			showInfo(achievementType.apprentice, (ImageView)v);
			break;
		case R.id.tab3_row2col2:
			showInfo(achievementType.lover, (ImageView)v);
			break;
		case R.id.tab3_row2col3:
			showInfo(achievementType.dead, (ImageView)v);
			break;
		case R.id.tab3_row3col1:
			showInfo(achievementType.adept, (ImageView)v);
			break;
		case R.id.tab3_row3col2:
			showInfo(achievementType.casanova, (ImageView)v);
			break;
		case R.id.tab3_row3col3:
			showInfo(achievementType.collector, (ImageView)v);
			break;
		case R.id.tab3_row4col1:
			showInfo(achievementType.master, (ImageView)v);
			break;
		case R.id.tab3_row4col2:
			showInfo(achievementType.secret, (ImageView)v);
			break;
		case R.id.tab3_row4col3:
			showInfo(achievementType.alien, (ImageView)v);
			break;
		}
	}
	
	private Bitmap convertColorIntoBlackAndWhiteImage(Bitmap orginalBitmap) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);

        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
                colorMatrix);

        Bitmap blackAndWhiteBitmap = orginalBitmap.copy(
                Bitmap.Config.ARGB_8888, true);

        Paint paint = new Paint();
        paint.setColorFilter(colorMatrixFilter);

        Canvas canvas = new Canvas(blackAndWhiteBitmap);
        canvas.drawBitmap(blackAndWhiteBitmap, 0, 0, paint);

        return blackAndWhiteBitmap;
    }
	
	public void fillArray(){
		array = new int[4][3];
        array[0][0] = R.drawable.ziemia3;
        array[0][1] = R.drawable.ziemia1;
        array[0][2] = R.drawable.ziemia3;
        array[1][0] = R.drawable.ziemia3;
        array[1][1] = R.drawable.ziemia3;
        array[1][2] = R.drawable.ziemia2;
        array[2][0] = R.drawable.ziemia3;
        array[2][1] = R.drawable.ziemia3;
        array[2][2] = R.drawable.ziemia3;
        array[3][0] = R.drawable.jez1;
        array[3][1] = R.drawable.jez2;
        array[3][2] = R.drawable.jez3;
        
        achievement11.setImageBitmap(BitmapFactory.decodeResource(getResources(), array[0][0]));
    	achievement12.setImageBitmap(BitmapFactory.decodeResource(getResources(), array[0][1]));
    	achievement13.setImageBitmap(BitmapFactory.decodeResource(getResources(), array[0][2]));
    	achievement21.setImageBitmap(BitmapFactory.decodeResource(getResources(), array[1][0]));
    	achievement22.setImageBitmap(BitmapFactory.decodeResource(getResources(), array[1][1]));
    	achievement23.setImageBitmap(BitmapFactory.decodeResource(getResources(), array[1][2]));
    	achievement31.setImageBitmap(BitmapFactory.decodeResource(getResources(), array[2][0]));
    	achievement32.setImageBitmap(BitmapFactory.decodeResource(getResources(), array[2][1]));
    	achievement33.setImageBitmap(BitmapFactory.decodeResource(getResources(), array[2][2]));
    	achievement41.setImageBitmap(BitmapFactory.decodeResource(getResources(), array[3][0]));
    	achievement42.setImageBitmap(BitmapFactory.decodeResource(getResources(), array[3][1]));
    	achievement43.setImageBitmap(BitmapFactory.decodeResource(getResources(), array[3][2]));
        
    	imageArray = new ImageView[4][3];
        imageArray[0][0] = achievement11;
        imageArray[0][1] = achievement12;
        imageArray[0][2] = achievement13;
        imageArray[1][0] = achievement21;
        imageArray[1][1] = achievement22;
        imageArray[1][2] = achievement23;
        imageArray[2][0] = achievement31;
        imageArray[2][1] = achievement32;
        imageArray[2][2] = achievement33;
        imageArray[3][0] = achievement41;
        imageArray[3][1] = achievement42;
        imageArray[3][2] = achievement43;
	}
	
	public void disableAchievement(int row, int column){
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), array[row][column]);
        imageArray[row][column].setImageBitmap(convertColorIntoBlackAndWhiteImage(bmp));
	}
	
	public void enableAchievement(int row, int column){
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), array[row][column]);
		imageArray[row][column].setImageBitmap((bmp));
	}
}