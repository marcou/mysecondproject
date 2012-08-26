package com.gra.gra;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * 
 * @author Szpada
 *
 * Klasa opisuj�ca ziemie (okr�g�y obiekt posiadaj�cy grawitacje, po jego powierzchni porusza si� gracz)
 */
public class Earth {
	
	private GameView view;
	
	/*-----------------------------------------------------------------------------------------------------*
	 * Startowe wartosci do ktorych bedzie sie wracalo po uplynieciu czasu dzialania modyfikatora(upgradeu)*
	 *-----------------------------------------------------------------------------------------------------*/
	private int default_radius;
	private double default_gravity;
	
	private long timer = 0;	//czas po ktorym przestaja dzialac upgrady
	
	private boolean suck_my_stats = false;	//flaga oznaczajaca ze statystyki sie zmienily i trzeba je na nowo zassac
	
	private float x;			//pozycja X srodka
	private float y;			//pozycja Y srodka
	private int mass;		//masa ziemi
	private int radius; 	//promien ziemi
	private double gravity;	//stala grawitacyjna
	
	private Paint paint;
	
	public Earth(GameView view, float x, float y, int mass, int radius, double gravity){
		this.view = view;
		this.x = x;
		this.y = y;
		this.mass = mass;
		this.radius = radius;
		this.gravity = gravity;
		
		this.default_gravity = gravity;
		this.default_radius = radius;
		
		this.paint = new Paint();
		paint.setColor(Color.LTGRAY);
	}
	public GameView getView() {
		return view;
	}
	public void setView(GameView view) {
		this.view = view;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public int getMass() {
		return mass;
	}
	public void setMass(int mass) {
		this.mass = mass;
	}
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	public double getGravity() {
		return gravity;
	}
	public void setGravity(double gravity) {
		this.gravity = gravity;
	}
	public Paint getPaint() {
		return paint;
	}
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	public void onDraw(Canvas canvas){
		canvas.drawCircle(x, y, radius, paint);
		if(timer > 0){
			timer--;
		}
		else{
			timer = 0;
			this.gravity = default_gravity;
			this.radius = default_radius;
			this.suck_my_stats = true;
		}
		
	}
	
	public void setUpgrade(long time, double radius, double gravity){
		this.timer = time;
		this.radius *= radius;
		this.gravity *= gravity;
		suck_my_stats = true;
	}
	public boolean isSuck_my_stats() {
		return suck_my_stats;
	}
	public void setSuck_my_stats(boolean suck_my_stats) {
		this.suck_my_stats = suck_my_stats;
	}
	public void resetUpgrade(){
		this.gravity = default_gravity;
		this.radius = default_radius;
		this.suck_my_stats = true;
	}
	public long getTimer() {
		return timer;
	}
	public void setTimer(long timer) {
		this.timer = timer;
	}
	
}