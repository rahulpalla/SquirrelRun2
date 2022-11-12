package com.example.squirrelrun2;

import android.graphics.Bitmap;
import android.content.Context;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Wolf {
    Bitmap wolf[] = new Bitmap[3];
    int wolfFrame = 0;
    int wolfX, wolfY, wolfVelocity;
    Random random;

    public Wolf(Context context){
        wolf[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wolf2);
        wolf[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wolf2);
        wolf[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wolf2);
        random = new Random();
        resetPosition();
    }

    public Bitmap getWolf(int wolfFrame){
         return wolf[wolfFrame];
    }

    public int getWolfWidth(){
        return wolf[0].getWidth();
    }

    public int getWolfHeight(){
        return wolf[0].getHeight();
    }

    public void resetPosition(){
        wolfX = random.nextInt(GameView.dWidth - getWolfWidth());
        wolfY = -200 + random.nextInt(600)*-1;
        wolfVelocity = 35 + random.nextInt(16);
    }



}
