package example.com.spacefighter.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import example.com.spacefighter.R;

/**
 * Created by churong1 on 4/14/17.
 */
public class Boom implements ModelInterface{
    //bitmap object
    private Bitmap bitmap;

    //coordinate variables
    private int x;
    private int y;

    //constructor
    public Boom(Context context) {
        //getting boom image from drawable resource
        bitmap = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.boom);

        //setting the coordinate outside the screen
        //so that it won't shown up in the screen
        //it will be only visible for a fraction of second
        //after collission
        x = -250;
        y = -250;
    }

    //setters for x and y to make it visible at the place of collision
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    //getters
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public void update() {

    }

    @Override
    public void update(int speed) {

    }

    @Override
    public Rect getDetectCollision() {
        return null;
    }

    @Override
    public void stopBoosting() {

    }

    @Override
    public void setBoosting() {

    }

    @Override
    public float getStarWidth() {
        return 0;
    }

}
