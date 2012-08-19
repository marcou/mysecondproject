package com.gra.minigra;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * 
 * @author Szpada
 *
 *	gracz a wlasciwie kulka ktora kontroluje gracz
 */
public class Player {

	private GameView view;

	private int x;
	private int y;
	
	private int mass;
	private int radius;			//promien naszego obiektu
	private int angle;			//kat - godzina 6 to 90stopni, 12 270
	
	private int earth_x;		//pozycja X srodka ziemi
	private int earth_y;		//pozycja Y srodka ziemi
	private int earth_radius;	//promien okregu po jakim ma siê poruszaæ nasz obiekt
	private boolean on_ground = true;	//czy dotyka ziemi
	
	private Paint paint;
	
	public Player(GameView view, int x, int y, int radius, int mass, int degree){
		this.view = view;
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.mass = mass;
		this.angle = degree;
		
		paint = new Paint();
		paint.setColor(Color.GREEN);
	}
	
	public void onDraw(Canvas canvas){
		canvas.drawCircle(x, y, radius, paint);
	}
	
	public void set_earth(int x, int y, int radius){
		this.earth_x = x;
		this.earth_y = y;
		this.earth_radius = radius;
	}
	
	//Ruch				zgodnie lub przeciwnie do wskazowek zegara
	public void move(boolean clockwise){
		//zgodnie z ruchem wskazowek zegara
		if(clockwise){
			//gracz jest na ziemi
			if(on_ground){
				if(angle < 360){
					this.angle++;
				}
				else if(angle == 360){
					this.angle = 0;
				}
				this.x = (int)(		Math.cos(Math.toRadians(this.angle)) * (this.radius + this.earth_radius) + this.earth_x		);
				this.y = (int)(		Math.sin(Math.toRadians(this.angle)) * (this.radius + this.earth_radius) + this.earth_y		);
			}
			//gracz jest w powietrzu
			else{
				
			}
		}
		//przeciwnie do ruchu wskazowek zegara
		else{
			//gracz jest na ziemi
			if(on_ground){
				if(angle > 0){
					this.angle--;
				}
				else if(angle == 0){
					this.angle = 360;
				}
				this.x = (int)(		Math.cos(Math.toRadians(this.angle)) * (this.radius + this.earth_radius) + this.earth_x		);
				this.y = (int)(		Math.sin(Math.toRadians(this.angle)) * (this.radius + this.earth_radius) + this.earth_y		);
			}
			//gracz jest w powietrzu
			else{
				
			}
		}
	}
	
	public GameView getView() {
		return view;
	}

	public void setView(GameView view) {
		this.view = view;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
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

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public int getEarth_x() {
		return earth_x;
	}

	public void setEarth_x(int earth_x) {
		this.earth_x = earth_x;
	}

	public int getEarth_y() {
		return earth_y;
	}

	public void setEarth_y(int earth_y) {
		this.earth_y = earth_y;
	}

	public int getEarth_radius() {
		return earth_radius;
	}

	public void setEarth_radius(int earth_radius) {
		this.earth_radius = earth_radius;
	}

	public boolean isOn_ground() {
		return on_ground;
	}

	public void setOn_ground(boolean on_ground) {
		this.on_ground = on_ground;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}
}
