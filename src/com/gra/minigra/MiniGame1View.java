package com.gra.minigra;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
/**
 * 
 * @author Szpada
 * 
 * Minigra1 - pojawiaja siê monety ktore nalezy jak najszybciej zbierac
 *
 */

public class MiniGame1View extends SurfaceView{
	private MiniGame1LoopThread thread;
	private List<Ball> balls = new ArrayList<Ball>();
	private Paint paint;
	
	private static float world_gravity = 9.8f;
	
    public MiniGame1View(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        //this.setOnTouchListener(this);
    	thread = new MiniGame1LoopThread(this);
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
            	   thread.setRunning(true);
            	   thread.start();
	            }
	            //@Override
	            public void surfaceChanged(SurfaceHolder holder, int format,int width, int height) {
	            	
	            }
        }); 
    }
    public void createSprites(){
    	paint = new Paint();
    	paint.setColor(Color.BLACK);
    	
    	Ball ball = new Ball(this, this.balls, 240, 20, 12, -26, 25, 5, 0.1f, 10);
    	ball.setGarvity(world_gravity);
    	this.balls.add(ball);
    }
    
    @Override
    public void onDraw(Canvas canvas) {
    	canvas.drawRect(0, 0, 480, 800, this.paint);
    	for(int i = balls.size()-1; i >=0; i--){
    		balls.get(i).onDraw(canvas);
    	}
    	checkCollision();
    }
    public void checkCollision(){
    	for(int i = 0; i < balls.size(); i++){
    		for(int j = i + 1; j < balls.size(); j++){
    			if(balls.get(i).checkBallCollision(balls.get(j).getX(), balls.get(j).getX(), balls.get(j).getRadius())){
    				
    			}
    		}
    	}
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	float x = event.getX();
        float y = event.getY();
//        for(int i = balls.size()-1; i >=0 ; i--){
//    		if(balls.get(i).checkCollision((int)x, (int)y)){
//    			balls.remove(i);
//    		}
//    	}
        Ball ball = new Ball(this, this.balls, x, y, 3, -24, 15, 5, 0.5f, 10);
    	ball.setGarvity(world_gravity);
    	this.balls.add(ball);
        return true;
    }
}
    