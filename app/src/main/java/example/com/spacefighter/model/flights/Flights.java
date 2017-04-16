package example.com.spacefighter.model.flights;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Random;

import example.com.spacefighter.model.ModelInterface;

/**
 * Created by churong1 on 4/15/17.
 */
public abstract class Flights implements ModelInterface {
    protected Bitmap bitmap;
    protected int x;
    protected int y;
    protected int speed;

    protected int maxX;
    protected int minX;

    protected int maxY;
    protected int minY;
    protected Rect detectCollision;
    protected Random generator;


    public Flights(Context context, int screenX, int screenY)  {
        generator = new Random();
        //initializing min and max coordinates
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        //generating a random coordinate to add enemy
        speed = generator.nextInt(6) + 10;
        x = screenX;
    }

    @Override
    public void update(int playerSpeed) {
        //decreasing x coordinate so that enemy will move right to left
        x -= playerSpeed;
        x -= speed;
        //if the enemy reaches the left edge
        if (x < minX - bitmap.getWidth()) {
            //adding the enemy again to the right edge
            generator = new Random();
            speed = generator.nextInt(10) + 10;
            x = maxX;
            y = generator.nextInt(maxY) - bitmap.getHeight();
        }
        //Adding the top, left, bottom and right to the rect object
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
    }

    //getters
    @Override
    public Rect getDetectCollision() {
        return detectCollision;
    }
    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
    @Override
    public int getX() {
        return x;
    }
    @Override
    public int getY() {
        return y;
    }
    @Override
    public int getSpeed() {
        return speed;
    }

}
