package com.gra.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gra.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class HighScoreMenu extends Activity{
	
	private TableLayout score;
	private List<Record> records;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.highscore);
        
        score = (TableLayout)findViewById(R.id.highScoreTable);
        
        records = new ArrayList<Record>();
        
        records.add(new Record("pies", 431));
        records.add(new Record("kon", 5351));
        records.add(new Record("okon", 17436));
        records.add(new Record("wieloryb", 164));
        records.add(new Record("plaszczka", 64164));
        records.add(new Record("rys", 7781));
        for(int i = 0; i < 94; i++){
        	records.add(new Record("test" + i, i + 1));
        }
        
        sortRecords();
        
        for(int i = 0; i < records.size(); i ++){
        	addRecord(i+1, records.get(i).getName(), records.get(i).getPoints());
        }
	}

	@SuppressWarnings("unchecked")
	private void sortRecords() {
		Collections.sort(records);
	}
	
	public void addRecord(int rank, String name, long points){
		
		TableRow tr = new TableRow(this);
        LayoutParams rankParamas = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT | Gravity.RIGHT);
        LayoutParams otherParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        tr.setLayoutParams(otherParams);

        TextView tvLeft = new TextView(this);
        rankParamas.weight = 1.0f;
        //rankParamas.setMargins(20, 0, 40, 0);
        tvLeft.setLayoutParams(rankParamas);
        tvLeft.setTextSize(15.0f);
        tvLeft.setText(Integer.toString(rank));
        //tvLeft.setText("1");
        
        TextView tvCenter = new TextView(this);
        //rankParamas.setMargins(140, 0, 0, 0);
        tvCenter.setLayoutParams(rankParamas);
        tvCenter.setTextSize(15.0f);
        tvCenter.setText(name);
        //tvCenter.setText("2");
        
        TextView tvRight = new TextView(this);
        //rankParamas.setMargins(200, 0, 0, 0);
        tvRight.setLayoutParams(rankParamas);
        tvRight.setTextSize(15.0f);
        tvRight.setText(Long.toString(points));
        //tvRight.setText("3");

        tr.addView(tvLeft);
        tr.addView(tvCenter);
        tr.addView(tvRight);

        //score.addView(tr);
        score.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT | Gravity.LEFT));
	}
	
	/**
	 * 
	 * @author Szpada
	 * Klasa zawierajaca nazwe gracza wraz z punkatmi ktore uzyskal
	 */
	@SuppressWarnings("rawtypes")
	public class Record implements Comparable{
		private String name;
		private long points;
		
		public Record(String name, long points){
			this.name = name;
			this.points = points;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public long getPoints() {
			return points;
		}

		public void setPoints(long points) {
			this.points = points;
		}

		public int compareTo(Object another) {
			Record record = (Record) another;
			
			if(this.points > record.getPoints()) return -1;	//normalnie powinno byc 1 ale mamy odwrotna kolejnosc
			else if(this.points < record.getPoints()) return 1; //a tu -1
			else return 0;
		}
	}
}
