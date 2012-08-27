package com.gra.gra;

import java.util.List;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Asteroid extends FlyingObject {
	
	private Paint paint;
	
	private int size = 1;
	
	public Asteroid(GameView view, List<FlyingObject> objects, float x, float y, double speed, double angle,
			int mass, int radius, int size) {
		super(view, objects, x, y, speed, angle, mass, radius * size);
		
		this.size = size;
		
		this.paint = new Paint();
		paint.setColor(Color.RED);
	}
	@Override
	public void resolveCollision(FlyingObject object){
		//kolizja asteroidy z inna asteroida lub graczem
		if(object instanceof Asteroid){
			//zmniejsz ich rozmiary
			setSize(getSize() - 1);
			((Asteroid) object).setSize(((Asteroid) object).getSize() - 1);
			
			Random rand = new Random();
			super.setAngle(super.getAngle() - rand.nextInt(90) - 90);
			((Asteroid) object).setAngle(((Asteroid) object).getAngle() - rand.nextInt(90) - 90);
		}
		//kolizja asteroidy z pieniedzmi
		else if(object instanceof Money){
			((Money) object).setLife(0);
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
		if(size < 1){
			super.setLife(0);
		}
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
}
