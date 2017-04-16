package example.com.spacefighter.model.flights;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import example.com.spacefighter.R;

/**
 * Created by churong1 on 4/14/17.
 */
public class Friend extends Flights {


    public Friend(Context context, int screenX, int screenY) {
        super(context, screenX, screenY);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.friend);
        y = generator.nextInt(maxY) - bitmap.getHeight();
        detectCollision =  new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
        speed = 1;

    }

    @Override
    public void setX(int x) {

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
