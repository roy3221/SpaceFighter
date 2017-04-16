package example.com.spacefighter.view;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by churong1 on 4/16/17.
 */
public interface GameViewInterface {
    public void draw();
    public int getScreenX();
    public int getScreenY();

    public void playGameOnSound(boolean play);

    public void playGameOverSound(boolean play);

    public void playKilledEnemySound(boolean play);

    public void pause();

    public void resume();

}
