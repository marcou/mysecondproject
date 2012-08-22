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
	public void resolveCollision(){
		super.getObjects().remove(this);
	}
	@Override
	public void onDraw(Canvas canvas){
		canvas.drawCircle(super.getX(), super.getY(), super.getRadius(), paint);
	}
}
