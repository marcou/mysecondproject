package com.gra.kreator;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 
 * @author Szpada
 *
 * klasa w ktorej bedziemy rysowali logo
 */
public class LogoPainter {
	private int x; //polozenie okna paintera
	private int y; //polozenie okna paintera
	
	private static int width = 128;	//szerokosc okna
	private static int height = 128;//wysokosc okna
	
	private Paint paint = new Paint();
	
	private Point point;
	
	List<Point> points = new ArrayList<Point>();
	
	public LogoPainter(int x, int y) {
		this.x = x;
		this.y = y;
		
	}

    public void onDraw(Canvas canvas) {
		this.paint.setColor(Color.WHITE);
		canvas.drawRect(this.x, this.y, this.x + LogoPainter.width, this.y + LogoPainter.height, this.paint);
		this.paint.setColor(Color.GREEN);
        canvas.drawCircle(point.x, point.y, 5, paint);
    }

    public void collision(int x, int y){
    	if(x > this.x && x < this.x + LogoPainter.width && y > this.y && y < this.y + LogoPainter.height){
    		point = new Point();
            point.x = x;
            point.y = y;
            //points.add(point);
    	}
    }
}

class Point {
    float x, y;

    @Override
    public String toString() {
        return x + ", " + y;
    }
}
