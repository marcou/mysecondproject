package com.gra.gra;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * 
 * @author Szpada
 *
 * Klasa opisuj¹ca ziemie (okr¹g³y obiekt posiadaj¹cy grawitacje, po jego powierzchni porusza siê gracz)
 */
public class Earth {
	
	private GameView view;
	
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
	}
}
