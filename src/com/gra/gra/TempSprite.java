package com.gra.gra;

import java.io.Serializable;
import java.util.List;

/**
 * @author Maciej
 * bonusy wyswietlajace sie na ekranie - np. extra mana, extra cosie
 */

enum tempType{smoke, explosion}

public class TempSprite implements Serializable{
       private float x;
       private float y;

       private int life = 16;	//zmienna przechowujaca zycie podzielone przez liczbe wierszy (potrzebne do animacji)
       private int currentLife;
       private List<TempSprite> temps;

       private int currentFrame = 0;
       private int frames = 15;
 
       private tempType type;
       
//       public TempSprite(List<TempSprite> temps, float x,float y, double angle, int radius, int temp_sprite_size) {
//		   this.temps = temps;
//		   
//		   this.x = (float)(Math.cos(Math.toRadians(angle)) * (radius + temp_sprite_size) + x);
//		   this.y = (float)(Math.sin(Math.toRadians(angle)) * (radius + temp_sprite_size) + y);
//		   
//		   this.currentLife = life;
//       }
       
       public TempSprite(List<TempSprite> temps, float x,float y, tempType type) {
		   this.temps = temps;
		   
		   this.type = type;
		   
		   
		   this.x = x;
		   this.y = y;
		   
		   this.currentLife = life;
       }

       public void update() {
    	   if(currentFrame >= frames){
    		   currentFrame = 0;
    	   }
           if (--currentLife < 1) {
        	   temps.remove(this);
           }
           currentFrame++;
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

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getCurrentLife() {
		return currentLife;
	}

	public void setCurrentLife(int currentLife) {
		this.currentLife = currentLife;
	}

	public List<TempSprite> getTemps() {
		return temps;
	}

	public void setTemps(List<TempSprite> temps) {
		this.temps = temps;
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

	public tempType getType() {
		return type;
	}

	public void setType(tempType type) {
		this.type = type;
	} 

	public int getID(){
		if(type == tempType.explosion) return 18;
		else if(type == tempType.smoke) return 6;
		return -1;
	}
}