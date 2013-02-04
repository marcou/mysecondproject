package com.gra.gra;

import java.util.List;
import java.util.Random;

import android.graphics.Canvas;

public class Asteroid extends FlyingObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int size = 1;
	
	private int basic_radius;
	
	private boolean exploded = false;	//przy kontakcie z ziemia ma wybuchnac, zmienna ktora blokuje wielokrotne wybuchanie
	
	public Asteroid(List<FlyingObject> objects, float x, float y, double speed, double angle,
			int mass, int radius, int size) {
		super( objects, x, y, speed, angle, mass, radius * size);
		
		this.size = size;
		
		basic_radius = radius;

	}
	@Override
	public void resolveCollision(FlyingObject object){
		//kolizja asteroidy z inna asteroida lub graczem
		if(object instanceof Asteroid){
			//zmniejsz ich rozmiary
			setSize(getSize() - 1);
			((Asteroid) object).setSize(((Asteroid) object).getSize() - 1);
			
			if(getSize() > 0){
				super.setRadius(basic_radius * getSize());
			}
			if(((Asteroid) object).getSize() > 0){
				((Asteroid) object).setRadius(((Asteroid) object).getBasic_radius() * ((Asteroid) object).getSize());
			}
			Random rand = new Random();
			super.setAngle(super.getAngle() - 180);//rand.nextInt(90) - 90);
			((Asteroid) object).setAngle(((Asteroid) object).getAngle() - 180);//rand.nextInt(90) - 90);
		}
		//kolizja z upgradeami
		else if(object instanceof Upgrade){
			((Upgrade) object).setLife(0);
		}
		//kolizja asteroidy z pieniedzmi
		else if(object instanceof Money){
			((Money) object).setLife(0);
		}
	}
	@Override
	public void onDraw(Canvas canvas){
		update();
//		canvas.drawCircle(super.getX(), super.getY(), super.getRadius(), paint);
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
		
		super.setCurrentFrame(super.getCurrentFrame() + 1);
		
		if(super.getCurrentFrame() > super.getFrames()){
			super.setCurrentFrame(0);
		}
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getBasic_radius() {
		return basic_radius;
	}
	public void setBasic_radius(int basic_radius) {
		this.basic_radius = basic_radius;
	}
	public boolean isExploded() {
		return exploded;
	}
	public void setExploded(boolean exploded) {
		this.exploded = exploded;
	}
}
