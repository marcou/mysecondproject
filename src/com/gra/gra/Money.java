package com.gra.gra;

import java.util.List;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Money extends FlyingObject {

	private Paint paint;
	
	private int points;	//liczba punktow ktora daje ten obiekt
	
	public Money(GameView view, List<FlyingObject> objects, float x, float y, double speed, int angle,
			int mass, int radius) {
		super(view, objects, x, y, speed, angle, mass, radius);
		
		Random rnd = new Random();
		points = 5;//rnd.nextInt(5) + 1;
		//Log.d("money", "punkty : " + points);
		
		this.paint = new Paint();
		paint.setColor(Color.CYAN);
	}
	
	@Override
	public void resolveCollision(FlyingObject object){
		//kolizja pieniedzy z asteroida lub graczem
		if(object instanceof Asteroid || object == null){
			super.setLife(0);
		}
		//kolizja miedzy pieniedzmi
		else if(object instanceof Money){
			//Log.d("money collision", "punkty : " + ((Money) object).getPoints());
			points *= ((Money) object).getPoints();
			((Money) object).setPoints(0);
			((Money) object).setLife(0);
			//((Money) object).getObjects().remove(object);
		}
	}
	
	@Override
	public void onDraw(Canvas canvas){
		//super.update();
		canvas.drawCircle(super.getX(), super.getY(), super.getRadius(), paint);
	}
	
	public int getPoints(){
		return points;
	}
	
	public void setPoints(int points){
		this.points = points;
	}
}
