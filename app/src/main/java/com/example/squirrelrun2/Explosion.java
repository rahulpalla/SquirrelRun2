package com.example.squirrelrun2;

import android.graphics.Bitmap;
import android.content.Context;
import android.graphics.BitmapFactory;

public class Explosion {
    Bitmap explosion[] = new Bitmap[4];
    int explosionFrame = 0;
    int explosionX, explosionY;


    public Explosion(Context context){
        explosion[0]  = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion2);
        explosion[1]  = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion2);
        explosion[2]  = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion2);
        explosion[3]  = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion2);
    }

    public Bitmap getExplosion(int explosionFrame){
        return explosion[explosionFrame];
    }


}
