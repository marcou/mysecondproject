package com.gra.gra;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TimeGameView extends SurfaceView{
	
	private TimeGameLoopThread thread;
	
	private float h_factor;
	private float w_factor;
	
	private ArrayList<Earth> planets = new ArrayList<Earth>();
	
	private Player player;
	
	private boolean playermoving = false;
	private boolean playerjumping = false;
	private boolean clockwisedirection;
	
	public TimeGameView(Context context, double w_factor, double h_factor) {
        super(context);
        
        this.h_factor = (float)h_factor;
	   	this.w_factor = (float)w_factor;
        
        setFocusable(true);
        setFocusableInTouchMode(true);
        setLongClickable(true);
        //this.setOnTouchListener(this);
    	thread = new TimeGameLoopThread(this);
        getHolder().addCallback(new SurfaceHolder.Callback() {
               //@Override
               public void surfaceDestroyed(SurfaceHolder holder) {
                      boolean retry = true;
                      thread.setRunning(false);
                      while (retry) {
                             try {
                            	 thread.join();
                                   retry = false;
                             } catch (InterruptedException e) {}
                      }
               }
               //@Override
               public void surfaceCreated(SurfaceHolder holder) {
            	   createSprites();
            	   createGenerator();
            	   thread.setRunning(true);
            	   thread.start();
	            }
	            //@Override
	            public void surfaceChanged(SurfaceHolder holder, int format,int width, int height) {
	            	
	            }
        }); 
    }
	
	@Override
	public void onDraw(Canvas canvas){
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		canvas.drawRect(0, 0, 480, 800, paint);
		
		//informacje o graczu
    	paint.setColor(Color.GREEN);
    	canvas.drawText("X : " + player.getX(), 240, 10, paint);
    	canvas.drawText("Y : " + player.getY(), 240, 20, paint);
    	canvas.drawText("Angle : " + player.getAngle(), 240, 30, paint);
    	canvas.drawText("On_Ground : " + player.isOn_ground(), 240, 40, paint);
		
    	if(!player.isOn_ground()){
			//oblicz sily grawitacyjne
			resolveGravity();
		}
		//rysuj planety
		for(int i = planets.size()-1; i >= 0; i--){
			planets.get(i).onDraw(canvas);
			//sprawdz kolizje z graczem
			player.checkPlanetCollision(planets.get(i).getX(),planets.get(i).getY(),planets.get(i).getRadius());
			//przesun wszystkie planety
			//...
		}
		player.onDraw(canvas);
		if (playermoving) {
    		movePlayer();
    	}
	}
	
	public void createGenerator(){
		
	}
	
	public void createSprites(){
		planets.add(new Earth(240, 400, 3500, 50, 7));
		planets.add(new Earth(40, 300, 3500, 50, 6));
		player = new Player	(240, 	290, 	1, 		10, 	270);
		player.setEarth_radius(planets.get(0).getRadius());
		player.setEarth_x(planets.get(0).getX());
		player.setEarth_y(planets.get(0).getY());
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
    	
    	//Log.d(VIEW_LOG_TAG, "Pointer Num:" + Integer.toString(event.getPointerId(0)));
    	
    	float x = event.getX();
        float y = event.getY();
        //SKALOWANIE
        x = x / this.w_factor;
        y = y / this.h_factor;
        //jesli player zyje
        if(player.getLife() > 0){
        	
	        if(event.getAction()==MotionEvent.ACTION_DOWN){
	        	Log.d(VIEW_LOG_TAG, "Touch DOWN");
	        	if(y > 400){
	        		if(player.isOn_ground()){
	        			player.jump();
	        			playerjumping = true;
	        			Log.d(VIEW_LOG_TAG, "Jump pressed");
	        		}
	        	}
	        	else{
	        		playermoving = true;
		        	if(x < 240){
			        	//player.move(false);
		        		clockwisedirection = false;
			        	Log.d(VIEW_LOG_TAG, "less than");
		        	}
		        	if(x > 240){
		        		clockwisedirection = true;
			        	//player.move(true);
			        	Log.d(VIEW_LOG_TAG, "more than");
		        	}
	        	}
	    	 }
        
        
	        else if(event.getAction()==MotionEvent.ACTION_POINTER_2_DOWN) {
	        	Log.d(VIEW_LOG_TAG, "SECOND pointer down");
	        	event.getX(1);
	        	float yy = event.getY(1);
	        	if(yy > 400){
	        		if(player.isOn_ground()){
	        			player.jump();
	        			playerjumping = true;
	        			Log.d(VIEW_LOG_TAG, "Jump pressed");
	        		}
	        	}
	        }
	        
	        else if(event.getAction()==MotionEvent.ACTION_UP){
	    		 Log.d(VIEW_LOG_TAG, "Touch UP");
	    		 if (y<400) {
	    			 playermoving = false;
	    			 Log.d(VIEW_LOG_TAG, "Player stopped");
	    		 }
	    		 else {
	    			 playerjumping = false;
	    		 }
	    		 //
	    	 }
	    	 
	        else if(event.getAction()==MotionEvent.ACTION_POINTER_2_UP) {
	         	Log.d(VIEW_LOG_TAG, "SECOND pointer UP");
	         	float yy = event.getY(1);
	        	if (yy>400) {
	        		playerjumping = false;
	        	}
	         	
	         }
	        
	        else if (event.getAction()==MotionEvent.ACTION_MOVE) {
	        	
	        	
	        }
	        
	        
	        else {
	        	Log.d(VIEW_LOG_TAG,"Sth else Action " + Integer.toString(event.getAction()));
	        	Log.d(VIEW_LOG_TAG, "Sth else Index" + Integer.toString(event.getActionIndex()));
	        	 
	        }
        }
		return super.onTouchEvent(event);		
    }
	
	public void movePlayer() {
		player.move(clockwisedirection);
    }
	
	public void resolveGravity(){
		float x_power = 0;
		float y_power = 0;
		Gravity temp;
		for(int i = planets.size()-1; i >= 0; i--){
			temp = planets.get(i).countPowers(player.getX(), player.getY());
			x_power += temp.getX_power();
			y_power += temp.getY_power();
			Log.d("Gravity", "x_power : " + x_power);
			Log.d("Gravity", "y_power : " + y_power);
		}
		player.resolveGravityPowers(x_power, y_power);
	}
}
