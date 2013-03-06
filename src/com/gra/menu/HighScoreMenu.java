package com.gra.menu;

import com.gra.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class HighScoreMenu extends Activity{
	
	private TableLayout score;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.highscore);
        
        score = (TableLayout)findViewById(R.id.highScoreTable);
        
        addRecord(1, "pies", 100000);
        addRecord(2, "kon", 9999);
        addRecord(3, "okon", 2345);
        addRecord(4, "wieloryb", 2338);
        addRecord(5, "plaszczka", 107);
        addRecord(6, "rys", 85);
        addRecord(7, "bawol", 76);
        addRecord(8, "mysz", 13);
        addRecord(9, "golab", 9);
        addRecord(10, "mis", 6);
        addRecord(0, "to", 0);
        addRecord(0, "jest", 0);
        addRecord(0, "tylko", 0);
        addRecord(0, "test", 0);
        addRecord(0, "zeby", 0);
        addRecord(0, "sparwdzic", 0);
        addRecord(0, "czy", 0);
        addRecord(0, "scrollowanie", 0);
        addRecord(0, "dziala", 0);
        addRecord(0, ".", 0);
        addRecord(0, "Jak", 0);
        addRecord(0, "widac", 0);
        addRecord(0, "dziala", 0);
        addRecord(0, "i", 0);
        addRecord(0, "to", 0);
        addRecord(0, "calkiem", 0);
        addRecord(0, "niezle", 0);
        addRecord(0, "bo wczytal", 0);
        addRecord(0, "dokladnie", 0);
        addRecord(0, "31", 0);
        addRecord(0, "rekordow", 0);
        
	}
	
	public void addRecord(int rank, String name, long points){
		
		TableRow tr = new TableRow(this);
        LayoutParams rankParamas = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT | Gravity.LEFT);
        LayoutParams otherParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        tr.setLayoutParams(otherParams);

        TextView tvLeft = new TextView(this);
        rankParamas.setMargins(80, 0, 0, 0);
        tvLeft.setLayoutParams(rankParamas);
        tvLeft.setTextSize(15.0f);
        tvLeft.setText(Integer.toString(rank));
        //tvLeft.setText("1");
        
        TextView tvCenter = new TextView(this);
        otherParams.setMargins(180, 0, 0, 0);
        tvCenter.setLayoutParams(otherParams);
        tvCenter.setTextSize(15.0f);
        tvCenter.setText(name);
        //tvCenter.setText("2");
        
        TextView tvRight = new TextView(this);
        otherParams.setMargins(220, 0, 0, 0);
        tvRight.setLayoutParams(otherParams);
        tvRight.setTextSize(15.0f);
        tvRight.setText(Long.toString(points));
        //tvRight.setText("3");

        tr.addView(tvLeft);
        tr.addView(tvCenter);
        tr.addView(tvRight);

        score.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}
}
