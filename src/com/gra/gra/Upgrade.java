package com.gra.gra;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Upgrade extends FlyingObject {

	private Paint paint;
	
	public Upgrade(GameView view, List<FlyingObject> objects, float x, float y, double speed, int angle,
			int mass, int radius) {
		super(view, objects, x, y, speed, angle, mass, radius);
		
		this.paint = new Paint();
		paint.setColor(Color.GREEN);
	}
	
	@Override
	public void resolveCollision(FlyingObject object){
		//kolizja upgradeu z asteroida lub graczem
		if(object instanceof Asteroid || object == null){
			super.getObjects().remove(this);
		}
		//kolizja upgradeu z pieniedzmi
		else if(object instanceof Money){
			
		}
	}
	
	@Override
	public void onDraw(Canvas canvas){
		//super.update();
		canvas.drawCircle(super.getX(), super.getY(), super.getRadius(), paint);
	}
}