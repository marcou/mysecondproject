package com.gra.gra;

import java.util.List;
import java.util.Random;

import com.gra.R;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Money extends FlyingObject {

	private int life_timer = 200;
	
	private int points;	//liczba punktow ktora daje ten obiekt
	
	public Money(List<FlyingObject> objects, float x, float y, double speed, double angle,
			int mass, int radius) {
		super(objects, x, y, speed, angle, 80, 5);
		
		super.setLife_timer(life_timer);
		
		Random rnd = new Random();
		//points = rnd.nextInt(mass/2) + mass/2;
		points = rnd.nextInt(mass);
		if(points < 1){
			points = 1;
		}
		
		//super.setBmpData(BitmapFactory.decodeResource(view.getResources(), R.drawable.gemstone), 1, 1);
	}
	
	@Override
	public void resolveCollision(FlyingObject object){
		//kolizja pieniedzy z asteroida lub graczem
		if(object instanceof Asteroid){
			super.setLife(0);
		}
		//kolizja miedzy pieniedzmi
		else if(object instanceof Money){
			//NIC NIE ROB
//			if(checkDistance(object)){
//				points *= ((Money) object).getPoints();
//				((Money) object).setPoints(0);
//				((Money) object).setLife(0);
//			}
//			else{
//				points *= ((Money) object).getPoints();
//				((Money) object).setPoints(points);
//				points = 0;
//				super.setLife(0);
//				
//			}
			//((Money) object).getObjects().remove(object);
		}
	}
	
	//metoda sprawdzajaca ktory obiekt jest blizej ziemi - ten obiekt pozostanie a drugi zniknie
	public boolean checkDistance(FlyingObject object){
		double distance1 = Math.pow(Math.pow(super.getX() - super.getEarth_x(),2) + Math.pow(super.getY() - super.getEarth_y(),2), 0.5);
		double distance2 = Math.pow(Math.pow(object.getX() - object.getEarth_x(),2) + Math.pow(object.getY() - object.getEarth_y(),2), 0.5);
		
		if(distance1 <= distance2){
			return true;
		}
		else return false;
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
		super.setCurrentFrame(super.getCurrentFrame() + 1);
		
		if(super.getCurrentFrame() > super.getFrames()){
			super.setCurrentFrame(0);
		}
	}
	
	//przyciaganie monety przez gracza
	public void resolvePlayerGravity(float x, float y, double angle) {
		
		double gravity = 5000;	//stala grawirtacji potrzebna do "miodnosci"
		double distance = (Math.pow(super.getX() - x,2) + Math.pow(super.getY() - y,2));//odleglosc do kwadratu
		double power = gravity/(distance);	//wzor na sile grawitacji
		
		//ruch monety zalezny od kata
		if(angle < super.getAngle()){
			power = -power;
		}
		else if(angle == super.getAngle()){
			return;
		}
		//przy przejsciu 0 - 360 metoda nie dziala bo moneta majaca kat 359 powinna byc przyciagana do gracza ze stopniem 1 a jest odpychana
		//dlatego to sprawdzenie powinno sprawic ze wszystko bedzie smigac
		if(Math.abs(angle - super.getAngle()) > 180){
			power = -power;
		}
//		//jelsi na przeciwnych polowach to odwroc kierunek
//		if(super.getY() > super.getEarth_y() && y < super.getEarth_y() ){
//			//power = -power;
//		}
		super.moveOnGround((int)power);
		
	}
	
	public int getPoints(){
		return points;
	}
	
	public void setPoints(int points){
		this.points = points;
	}

	@Override
	public int getID() {
		return 1;
	}
}
