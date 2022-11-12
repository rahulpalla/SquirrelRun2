package com.example.squirrelrun2;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View{

    Bitmap background, ground, squirrel;
    Rect rectBackground, rectGround;
    Context context;
    Handler handler;
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    float TEXT_SIZE = 120;
    int points = 0;
    int life = 3;
    static int dWidth, dHeight;
    Random random;
    float squirrelX, squirrelY;
    float oldX;
    float oldSquirrelX;
    ArrayList<Wolf> wolves;
    ArrayList<Explosion> explosions;



    public GameView(Context context){
        super(context);
        this.context = context;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background_home);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        squirrel = BitmapFactory.decodeResource(getResources(), R.drawable.squirrel2);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rectBackground = new Rect(0,0,dWidth, dHeight);
        rectGround = new Rect(0,dHeight-ground.getHeight(), dWidth, dHeight);
        handler = new Handler();
        runnable = new Runnable(){
            @Override
            public void run(){
                invalidate();
            }
        };
        textPaint.setColor(Color.rgb(255, 165, 0));
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.magz));
        healthPaint.setColor(Color.GREEN);
        random = new Random();
        squirrelX= dWidth/2 - squirrel.getWidth()/2;
        squirrelY = dHeight - ground.getHeight() - squirrel.getHeight();
        wolves = new ArrayList<>();
        explosions = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            Wolf wolf = new Wolf(context);
            wolves.add(wolf);
        }
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(ground, null, rectGround, null);
        canvas.drawBitmap(squirrel, squirrelX, squirrelY, null);
        for(int i = 0; i < wolves.size(); i++){
            canvas.drawBitmap(wolves.get(i).getWolf(wolves.get(i).wolfFrame), wolves.get(i).wolfX, wolves.get(i).wolfY, null);
            wolves.get(i).wolfFrame++;
            if(wolves.get(i).wolfFrame > 2){
                wolves.get(i).wolfFrame = 0;
            }
            wolves.get(i).wolfY += wolves.get(i).wolfVelocity;
            if(wolves.get(i).wolfY + wolves.get(i).getWolfHeight() >= dHeight - ground.getHeight()){
                points+= 10;
                Explosion explosion = new Explosion(context);
                explosion.explosionX = wolves.get(i).wolfX;
                explosion.explosionY = wolves.get(i).wolfY;
                explosions.add(explosion);
                wolves.get(i).resetPosition();

            }
        }

        for(int i = 0; i < wolves.size(); i++){
            if(wolves.get(i).wolfX + wolves.get(i).getWolfWidth() >= squirrelX &&
                    wolves.get(i).wolfX <= squirrelX + squirrel.getWidth() &&
                    wolves.get(i).wolfY + wolves.get(i).getWolfWidth() >= squirrelY &&
            wolves.get(i).wolfY + wolves.get(i).getWolfWidth() <= squirrelY + squirrel.getHeight()){
                life--;
                wolves.get(i).resetPosition();
                if(life==0){
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("points", points);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        }
        for(int i =0; i < explosions.size(); i++){
            canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionFrame),
                    explosions.get(i).explosionX, explosions.get(i).explosionY, null);
            explosions.get(i).explosionFrame++;
            if(explosions.get(i).explosionFrame > 3){
                explosions.remove(i);
            }
        }

        if(life == 2){
            healthPaint.setColor(Color.YELLOW);
        }
        else if(life == 1){
            healthPaint.setColor(Color.RED);
        }
        canvas.drawRect(dWidth-200, 30, dWidth-200+60*life, 80, healthPaint);
        canvas.drawText("" + points, 20, TEXT_SIZE, textPaint);
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        if(touchY >= squirrelY){
            int action = event.getAction();
            if(action == MotionEvent.ACTION_DOWN){
                oldX = event.getX();
                oldSquirrelX = squirrelX;
            }
            if(action == MotionEvent.ACTION_MOVE){
                float shift = oldX - touchX;
                float newSquirrelX = oldSquirrelX - shift;
                if(newSquirrelX <= 0){
                    squirrelX = 0;
                }
                else if(newSquirrelX >= dWidth - squirrel.getWidth()){
                    squirrelX = dWidth - squirrel.getWidth();
                }
                else{
                    squirrelX = newSquirrelX;
                }
            }
        }
        return true;
    }
}
