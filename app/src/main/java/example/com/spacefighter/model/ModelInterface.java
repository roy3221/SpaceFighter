package example.com.spacefighter.model;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by churong1 on 4/16/17.
 */
public interface ModelInterface {

    //setters for x and y to make it visible at the place of collision
    public void setX(int x);

    public void setY(int y);

    //getters
    public Bitmap getBitmap();

    public void setBitmap(Bitmap bitmap);

    public int getX();

    public int getY();

    public int getSpeed();

    public void update();

    public void update(int speed);

    public Rect getDetectCollision();

    public void stopBoosting();
    public void setBoosting();
    public float getStarWidth();


}
