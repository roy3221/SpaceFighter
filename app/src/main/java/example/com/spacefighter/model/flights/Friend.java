package example.com.spacefighter.model.flights;

import android.content.Context;
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
}
