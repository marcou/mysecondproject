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
		points = rnd.nextInt(5) + 1;
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
			//NIC NIE ROB CHUJU
//			if(checkDistance(object)){
//				points *= ((Money) object).getPoints();
//				((Money) object).setPoints(0);
//				((Money) object).setLife(0);
//			}
//			else{
//				points *= ((Money) object).getPoints();
//				((Money) object).setPoints(points);
//				points = 0;
//				super.setLife(0);
//				
//			}
			//((Money) object).getObjects().remove(object);
		}
	}
	
	//metoda sprawdzajaca ktory obiekt jest blizej ziemi - ten obiekt pozostanie a drugi zniknie
	public boolean checkDistance(FlyingObject object){
		double distance1 = Math.pow(Math.pow(super.getX() - super.getEarth_x(),2) + Math.pow(super.getY() - super.getEarth_y(),2), 0.5);
		double distance2 = Math.pow(Math.pow(object.getX() - object.getEarth_x(),2) + Math.pow(object.getY() - object.getEarth_y(),2), 0.5);
		
		if(distance1 <= distance2){
			return true;
		}
		else return false;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		update();
		canvas.drawCircle(super.getX(), super.getY(), super.getRadius(), paint);
	}
	
	public void update(){
		//jesli obiekt dotyka ziemi usun go po czasie "life_timer"
		if(super.isOn_ground()){
			super.setLife_timer(super.getLife_timer() - 1);
			if(super.getLife_timer() < 0){
				super.setLife(0);
			}
		}
	}
	
	public int getPoints(){
		return points;
	}
	
	public void setPoints(int points){
		this.points = points;
	}
}
