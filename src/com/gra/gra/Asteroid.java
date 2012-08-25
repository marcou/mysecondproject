package com.gra.gra;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Asteroid extends FlyingObject {
	
	private Paint paint;
	
	public Asteroid(GameView view, List<FlyingObject> objects, float x, float y, double speed, int angle,
			int mass, int radius) {
		super(view, objects, x, y, speed, angle, mass, radius);
		
		this.paint = new Paint();
		paint.setColor(Color.RED);
	}
	@Override
	public void resolveCollision(FlyingObject object){
		//kolizja asteroidy z inna asteroida lub graczem
		if(object instanceof Asteroid || object == null){
			super.setLife(0);
			((Asteroid) object).setLife(0);
		}
		//kolizja asteroidy z pieniedzmi
		else if(object instanceof Money){
			//nic sie nie dzieje
		}
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
}
