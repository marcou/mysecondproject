package com.gra.gra;

import java.util.List;

import com.gra.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

enum spriteType{coin};

public class GameSprite {
	private GameView gameView;
    private Bitmap bmp;
    
    private int x;
    private int y;
    private int width;
    private int height;
    
    private spriteType st;
    
    private int life;
    private int currentLife;
    
    private int currentframe = 0;
    private int frames;
    private boolean animated = false;
    
    private List<GameSprite> sprites;

    public GameSprite(GameView gameView, List<GameSprite> sprites, int x, int y,int MaxLife, spriteType st) {
    	this.st = st;
    	this.gameView = gameView;
    	this.sprites = sprites;
		this.x = x;
		this.y = y;
		this.currentframe = 0;
		switch(st){
		case coin:
			this.bmp = BitmapFactory.decodeResource(this.gameView.getResources(), R.drawable.moneta);
			this.frames = 1;
			this.width = bmp.getWidth();
			this.height = bmp.getHeight();
			this.animated = true;
			break;
		}
		if(this.frames > 1){
			this.animated = true;
		}
		this.life = MaxLife;
		this.currentLife = MaxLife;
	}
    public void onDraw(Canvas canvas) {
    	if(this.animated){
    		update();
    	}
    	int srcX = 0;
    	int srcY = 0;
		if(this.animated){
			srcX = currentframe * this.width;
		}
		Rect src = new Rect(srcX, srcY, srcX + this.width, srcY + this.height);
		Rect dst = new Rect(this.x, this.y, this.x + this.width, this.y + this.height);
		canvas.drawBitmap(this.bmp, src, dst, null);
		if(this.animated){
			this.currentframe++;
		}
	}
    
    private void update(){
    	if(this.currentLife < 1){
    		sprites.remove(this);
    	}
    	if(this.currentframe < 0){
			this.currentframe = 0;
		}
		else if(this.currentframe > frames-1){
			this.currentframe = 0;
		}
    	this.currentLife--;
    }
    
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	public void updateStats(int life){
		this.currentLife = life;
	}
	public boolean checkCollision(int x, int y){
		Rect rect = new Rect(this.x, this.y, this.x + this.width, this.y + this.height);
		if(rect.contains(x, y)){
			return true;
		}
		else{
			return false;
		}
	}
}
