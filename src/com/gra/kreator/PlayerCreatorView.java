package com.gra.kreator;

import java.util.ArrayList;
import java.util.List;

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
 * klasa sluzaca do tworzenia gracza - nazwa, symbol (painter) i 3 podstawowe dogmaty,
 * po stworzeniu bedzie odpala³a siê gra (gracz przekazany w argumencie).
 *
 */

public class PlayerCreatorView extends SurfaceView implements OnTouchListener {
	private LogoPainter painter;
	private PlayerCreatorLoopThread thread;
	
    public PlayerCreatorView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        painter = new LogoPainter(100,200);
    	thread = new PlayerCreatorLoopThread(this);
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
            	   thread.setRunning(true);
            	   thread.start();
	            }
	            //@Override
	            public void surfaceChanged(SurfaceHolder holder, int format,int width, int height) {
	            	
	            }
        }); 
    }

    @Override
    public void onDraw(Canvas canvas) {
        painter.onDraw(canvas);
    }

    public boolean onTouch(View view, MotionEvent event) {
    	float x = event.getX();
        float y = event.getY();
        painter.collision((int)x, (int)y);
        return true;
    }
}