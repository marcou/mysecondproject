package com.gra.gra;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * 
 * @author Szpada
 *
 *	gracz a wlasciwie kulka ktora kontroluje gracz
 */
public class Player {

	private GameView view;

	private int speed = 3;
	private double jump_power = 14.5;
	private double current_jump_power;
	
	private double x_speed;		//predkosc w plaszczyznie X
	private double y_speed;		//predkosc w plaszczyznie Y
	
	private float x;
	private float y;
	
	private int mass;
	private int radius;			//promien naszego obiektu
	private int angle;			//kat - godzina 6 to 90stopni, 12 270
	
	private float earth_x;		//pozycja X srodka ziemi
	private float earth_y;		//pozycja Y srodka ziemi
	private int earth_radius;	//promien okregu po jakim ma siê poruszaæ nasz obiekt
	private boolean on_ground = true;	//czy dotyka ziemi
	
	private Paint paint;
	
	public Player(GameView view, float x, float y,  int mass, int radius, int degree){
		this.view = view;
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.mass = mass;
		this.angle = degree;
		
		paint = new Paint();
		paint.setColor(Color.YELLOW);
	}
	
	public void onDraw(Canvas canvas){
		canvas.drawCircle(x, y, radius, paint);
	}
	
	public void set_earth(float x, float y, int radius){
		this.earth_x = x;
		this.earth_y = y;
		this.earth_radius = radius;
	}
	
	//Ruch				zgodnie lub przeciwnie do wskazowek zegara
	public void move(boolean clockwise){
		double distance; 	//zmienna przechowujaca dystans kulki od planety (potrzebna do ruchu w powietrzu)
		
		//zgodnie z ruchem wskazowek zegara
		if(clockwise){
			//gracz jest na ziemi
			if(on_ground){
				if(angle + speed < 360){
					this.angle += this.speed;
				}
				else if(angle + speed >= 360){
					this.angle = angle - 360 + speed;
				}
				this.x = (float)(		Math.cos(Math.toRadians(this.angle)) * (this.radius + this.earth_radius) + this.earth_x		);
				this.y = (float)(		Math.sin(Math.toRadians(this.angle)) * (this.radius + this.earth_radius) + this.earth_y		);
			}
			//gracz jest w powietrzu
			else{
				
				if(angle + speed/4 + 1 < 360){
					this.angle += this.speed/4 + 1;
				}
				else if(angle + speed/4 + 1 >= 360){
					this.angle = angle - 360 + speed/4 + 1;
				}
				distance = Math.pow((Math.pow(this.earth_x - this.x,2) + Math.pow(this.earth_y - this.y,2)),0.5);
				this.x = (float)(		Math.cos(Math.toRadians(this.angle)) * (distance) + this.earth_x		);
				this.y = (float)(		Math.sin(Math.toRadians(this.angle)) * (distance) + this.earth_y		);
			}
		}
		//przeciwnie do ruchu wskazowek zegara
		else{
			//gracz jest na ziemi
			if(on_ground){
				if(angle - speed > 0){
					this.angle -= this.speed;
				}
				else if(angle - speed <= 0){
					this.angle = angle + 360 - speed;
				}
				this.x = (float)(		Math.cos(Math.toRadians(this.angle)) * (this.radius + this.earth_radius) + this.earth_x		);
				this.y = (float)(		Math.sin(Math.toRadians(this.angle)) * (this.radius + this.earth_radius) + this.earth_y		);
			}
			//gracz jest w powietrzu
			else{
				if(angle - speed/4 + 1 > 0){
					this.angle -= this.speed/4 + 1;
				}
				else if(angle - speed/4 + 1 <= 0){
					this.angle = angle + 360 - speed/4 + 1;
				}
				distance = Math.pow((Math.pow(this.earth_x - this.x,2) + Math.pow(this.earth_y - this.y,2)),0.5);
				this.x = (float)(		Math.cos(Math.toRadians(this.angle)) * (distance) + this.earth_x		);
				this.y = (float)(		Math.sin(Math.toRadians(this.angle)) * (distance) + this.earth_y		);
			}
		}
	}
	
	public void resolveGravity(double gravity, int mass, int radius){
		//ZREZYGNOWALEM Z GRAWITACJI ZALEZNEJ OD ODLEGLOSCI BO TA KURWA WYLATUJE W KOSMOS
		this.earth_radius = radius;
		double distance = (Math.pow(this.earth_x - this.x,2) + Math.pow(this.earth_y - this.y,2));
		double power = gravity;//(gravity * mass * this.mass)/(distance);	//wzor na sile grawitacji
		//Log.d("grawitacja", "sila grawitacji : " + power);
		//jesli sila wyrzutu jest mniejsza od grawitacji
		//Log.d("grawitacja", "sila podskoku : " + current_jump_power);
		if(current_jump_power <= 0){
			current_jump_power -= power;
			//power = power + current_jump_power;
			//current_jump_power = 0;
			//spadamy z sila power - current_jump_power
			resolvePower(-current_jump_power, false);
		}
		else{
			current_jump_power -= power;
			resolvePower(current_jump_power, true);
		}
	}
	
	public void jump(){
		current_jump_power = jump_power;
		on_ground = false;
	}
	//w pierwszej zmiennej znajduje sie sila z jaka
	//gracz sie unosi/opada (druga zmienna)
	//sila jest wypadkowa grawitacji i sily wyskoku
	public void resolvePower(double power, boolean move_up){	
		x_speed = Math.cos(Math.toRadians(angle)) * power;	//obliczenie skladowej X						
		y_speed = Math.sin(Math.toRadians(angle)) * power;	//obliczenie skladowej Y
		
		if(!move_up){
			x_speed = -x_speed;
			y_speed = -y_speed;
		}
		//Log.d("resolvePower", "X speed : " + x_speed);
		//Log.d("resolvePower", "Y speed : " + y_speed);
		
		this.x += x_speed;
		this.y += y_speed;
		
		//jesli kulka spadnie "za nisko" ustawaimy ja na wyjsciawa pozycje
		if(Math.pow((Math.pow(this.earth_x - this.x,2) + Math.pow(this.earth_y - this.y,2)),0.5) < this.radius + this.earth_radius){
			on_ground = true;
			this.x = (float) (		Math.cos(Math.toRadians(this.angle)) * (this.radius + this.earth_radius) + this.earth_x		);
			this.y = (float) (		Math.sin(Math.toRadians(this.angle)) * (this.radius + this.earth_radius) + this.earth_y		);
		}
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

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public float getEarth_x() {
		return earth_x;
	}

	public void setEarth_x(float earth_x) {
		this.earth_x = earth_x;
	}

	public float getEarth_y() {
		return earth_y;
	}

	public void setEarth_y(float earth_y) {
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
