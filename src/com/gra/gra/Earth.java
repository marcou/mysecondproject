package com.gra.gra;

import com.gra.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * 
 * @author Szpada
 *
 * Klasa opisuj¹ca ziemie (okr¹g³y obiekt posiadaj¹cy grawitacje, po jego powierzchni porusza siê gracz)
 */
public class Earth {
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
	
//	private Bitmap bmp;
	
	private int currentFrame = 0;
	private int frames = 0;	//zmienic kiedy bedzie juz animowana kula ziemska
	
//	private Paint paint;
	
	
	public Earth(float x, float y, int mass, int radius, double gravity){
		this.x = x;
		this.y = y;
		this.mass = mass;
		this.radius = radius;
		this.gravity = gravity;
		
		this.default_gravity = gravity;
		this.default_radius = radius;
		
//		this.paint = new Paint();
//		paint.setColor(Color.LTGRAY);
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
//	public Paint getPaint() {
//		return paint;
//	}
//	public void setPaint(Paint paint) {
//		this.paint = paint;
//	}
	
	public boolean checkCollision(float x, float y){
//		Log.d("Earth", "this.x : " + this.x);
//		Log.d("Earth", "this.y : " + this.y);
//		Log.d("Earth", "x : " + x);
//		Log.d("Earth", "y : " + y);
//		Log.d("Earth", "radius : " + this.radius);
		if(Math.pow(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2), 0.5) <= this.radius){
			return true;
		}
		return false;
	}
	
	public void onDraw(Canvas canvas){
//		canvas.drawCircle(x, y, radius, paint);
		update();
//		
//		int srcX = 0;
//    	int srcY = 0;
//    	int row;
//    	
//    	srcX = (currentFrame % (this.columns)) * this.width;
//        row = currentFrame / (this.rows + 1);
//        srcY = row * this.height;
//        
//        Rect src = new Rect(srcX, srcY, srcX + this.width, srcY + this.height);
//		Rect dst = new Rect((int)x - width/2, (int)y - width * 2/5, (int)x + width /2, (int)y + width * 2/5);
//		canvas.drawBitmap(bmp, src, dst, paint);
		
		
	}
	
	public void update(){
		if(this.currentFrame > this.frames){
			currentFrame = 0;
		}
		if(timer > 0){
			timer--;
		}
		else{
			timer = 0;
			this.gravity = default_gravity;
			this.radius = default_radius;
			this.suck_my_stats = true;
		}
		//currentFrame++; odkomentowac jak bedzie wiecej klatek
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
	public int getCurrentFrame() {
		return currentFrame;
	}
	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}
	public int getFrames() {
		return frames;
	}
	public void setFrames(int frames) {
		this.frames = frames;
	}
	public int getDefault_radius() {
		return default_radius;
	}
	public void setDefault_radius(int default_radius) {
		this.default_radius = default_radius;
	}
	public double getDefault_gravity() {
		return default_gravity;
	}
	public void setDefault_gravity(double default_gravity) {
		this.default_gravity = default_gravity;
	}
	
	public int getID(){
		return 3;
	}

	/************************************************
	 *        METODY DO TRYBU TIME ATTACK			*
	 ************************************************/
	
	public Gravity countPowers(float x, float y){
		double distance = (Math.pow(this.x - x,2) + Math.pow(this.y - y,2));//odleglosc do kwadratu
		double power = (gravity * mass)/(distance);	//wzor na sile grawitacji
		
		distance = Math.pow(distance, 0.5);	//prawdziwa odleglosc
		
		double cos_beta =  Math.abs((this.x - x)/distance);
		double beta = Math.toDegrees(Math.acos((cos_beta)));
		
		double landing_angle = 0.0;// = 180 + beta;
		
		//pierwsza cwairtka
		if(x >= this.x && y >= this.y){
			landing_angle = 180 + beta;
		}
		//druga cwiartka
		else if(x < this.x && y >= this.y){
			landing_angle = 360 - beta;
		}
		//trzecia cwiartka
		else if(x < this.x && y < this.y){
			landing_angle = beta;
		}
		//czwarta cwiartka
		else if(x >= this.x && y < this.y){
			landing_angle = 180 - beta;
		}
		
		beta = landing_angle;
		
		//skladowa X grawitacji
		double x_power = Math.cos(Math.toRadians(beta)) * (power);
		//skladowa Y grawitacji
		double y_power = Math.sin(Math.toRadians(beta)) * (power);
		return new Gravity((float)x_power,(float)y_power);
	}
}
