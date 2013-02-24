package com.gra.gra;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * 
 * @author Szpada
 *
 *	klasa jednego z wrogow - kolca poruszajacego sie zgodnie z ruchem wskazowek zegara, dziedziczy po FlyingObject bo tam juz jest wszystko skodzone
 */

public class GroundEnemy extends FlyingObject{

	private Paint paint;
	
	private int speed;
	
	public GroundEnemy(List<FlyingObject> objects, float x,
			float y, double speed, double angle, int mass, int radius) {
		super(objects, x, y, speed, angle, mass, radius);
		
		this.speed = (int)speed;
		
		this.paint = new Paint();
		paint.setColor(Color.RED);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resolveCollision(FlyingObject object) {
		//kolizja z asteroida
		if(object instanceof Asteroid){
			((Asteroid) object).setLife(0);
		}
		//kolizja z upgradem
		else if(object instanceof Upgrade){
			((Upgrade) object).setLife(0);
		}
		//kolizja asteroidy z pieniedzmi
		else if(object instanceof Money){
			((Money) object).setLife(0);
		}
	}
	
	@Override
	public void resolveGravity(double gravity, int mass, int radius){
		super.moveOnGround(speed);
	}
	
	@Override
	public void onDraw(Canvas canvas){
		update();
		canvas.drawCircle(super.getX(), super.getY(), super.getRadius(), paint);
	}

	@Override
	public int getID() {
		return 4;
	}
}
