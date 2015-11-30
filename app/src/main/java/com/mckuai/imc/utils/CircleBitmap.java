package com.mckuai.imc.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.NonNull;

/**
 * Created by kyly on 2015/10/19.
 */
public class CircleBitmap {


    static public Bitmap getCircleBitmap(@NonNull Bitmap bitmap){

        int width = bitmap.getWidth();
        int radius = width / 2;
        Bitmap output = Bitmap.createBitmap(width,width,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0,0,width,width);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(radius, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
//        bitmap.recycle();

        return  output;
    }

    static public Bitmap getCircleBitmap(@NonNull Bitmap bitmap,int width){
        int radius = width / 2;
        Bitmap output = Bitmap.createBitmap(width,width,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rectold = new Rect(0,0,bitmap.getWidth(),bitmap.getWidth());
        Rect rectnew = new Rect(0,0,width,width);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(radius, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rectold, rectnew, paint);
//        bitmap.recycle();
        return  output;
    }

    static public Bitmap getCircleBitmapWithStroke(@NonNull Context context,@NonNull Bitmap src,int width,int color){
        int BORDER_WIDTH = 2;
        int strokeRadius = width / 2;
        int imageRadius = strokeRadius - 2;

        Rect srcRect = new Rect(0,0,src.getWidth(),src.getHeight());
        Rect dstRect = new Rect(0,0,width - 4,width - 4);
        Paint paint = new Paint();
//        paint.setStrokeWidth(5);
        paint.setColor(0xff0000);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setDither(true);

//        Bitmap output = getCircleBitmap(src,width - 4);
        Bitmap output = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(output);
        canvas.drawARGB(0, 0, 0, 0);

        canvas.drawCircle(imageRadius, imageRadius, imageRadius, paint);
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(src, srcRect, dstRect, paint);
       canvas.save();

        return  output;
    }

}
