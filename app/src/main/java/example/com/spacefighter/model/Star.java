package example.com.spacefighter.model;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by churong1 on 4/14/17.
 */
public class Star implements ModelInterface{

    private int x;
    private int y;
    private int speed;

    private int maxX;
    private int maxY;
    private int minX;
    private int minY;



    public Star(int screenX, int screenY) {
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        Random generator = new Random();
        speed = generator.nextInt(10);

        //generating a random coordinate
        //but keeping the coordinate inside the screen size
        x = generator.nextInt(maxX);
        y = generator.nextInt(maxY);
    }

    public void update(int playerSpeed) {
        //animating the star horizontally left side
        //by decreasing x coordinate with player speed
        x -= playerSpeed;
        x -= speed;
        //if the star reached the left edge of the screen
        if (x < 0) {
            //again starting the star from right edge
            //this will give a infinite scrolling background effect
            x = maxX;
            Random generator = new Random();
            y = generator.nextInt(maxY);
            speed = generator.nextInt(15);
        }
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
        //Making the star width random so that
        //it will give a real look
        float minX = 1.0f;
        float maxX = 4.0f;
        Random rand = new Random();
        float finalX = rand.nextFloat() * (maxX - minX) + minX;
        return finalX;
    }

    @Override
    public void setX(int x) {

    }

    @Override
    public void setY(int y) {

    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

    @Override
    public void setBitmap(Bitmap bitmap) {

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
}
