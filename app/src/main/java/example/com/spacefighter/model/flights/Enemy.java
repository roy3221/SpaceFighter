package example.com.spacefighter.model.flights;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import example.com.spacefighter.R;
import example.com.spacefighter.model.ModelInterface;

/**
 * Created by churong1 on 4/14/17.
 */
public class Enemy extends Flights {


    public Enemy(Context context, int screenX, int screenY) {
        super(context, screenX, screenY);
        //getting bitmap from drawable resource
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        speed = 1;
        y = generator.nextInt(maxY) - bitmap.getHeight();
        //initializing rect object
        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());

    }

    //adding a setter to x coordinate so that we can change it after collision
    public void setX(int x){
        this.x = x;
    }

    @Override
    public void setY(int y) {

    }

    @Override
    public void setBitmap(Bitmap bitmap) {

    }

    @Override
    public void update() {

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
