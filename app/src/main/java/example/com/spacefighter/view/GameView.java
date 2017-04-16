package example.com.spacefighter.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import example.com.spacefighter.Presenter.GamePresenter;
import example.com.spacefighter.R;
import example.com.spacefighter.model.ModelInterface;

/**
 * Created by churong1 on 4/14/17.
 */
public class GameView  extends SurfaceView implements GameViewInterface {

    private GamePresenter gamePresenter;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    //the mediaplayer objects to configure the background music
    static MediaPlayer gameOnsound;
    private final MediaPlayer killedEnemysound;
    private final MediaPlayer gameOversound;
    //context to be used in onTouchEvent to cause the activity transition from GameAvtivity to MainActivity.
    Context context;
    private int screenX, screenY;

    //Class constructor
    public GameView(Context context, int screenX, int screenY) {
        super(context);
        this.context = context;
        this.screenX = screenX;
        this.screenY = screenY;
        //initializing the media players for the game sounds
        gameOnsound = MediaPlayer.create(context, R.raw.gameon);
        killedEnemysound = MediaPlayer.create(context,R.raw.killedenemy);
        gameOversound = MediaPlayer.create(context,R.raw.gameover);
//starting the game music as the game starts
        gameOnsound.start();
        //initializing drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();
        gamePresenter = new GamePresenter(context, this);

    }


    @Override
    public void draw() {
        //checking if surface is valid
        if (surfaceHolder.getSurface().isValid()) {
            //locking the canvas
            canvas = surfaceHolder.lockCanvas();
            //drawing a background color for canvas
            canvas.drawColor(Color.BLACK);

            //setting the paint color to white to draw the stars
            paint.setColor(Color.WHITE);

            //drawing all stars
            for (ModelInterface s : gamePresenter.getStars()) {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }

            //drawing the score on the game screen
            paint.setTextSize(30);
            canvas.drawText("Score:"+gamePresenter.getScore(),100,50,paint);
            canvas.drawText("miss:"+gamePresenter.getCountMisses(),500,50,paint);



            //Drawing the player
            canvas.drawBitmap(
                    gamePresenter.getPlayer().getBitmap(),
                    gamePresenter.getPlayer().getX(),
                    gamePresenter.getPlayer().getY(),
                    paint);
            //drawing the enemies
            for (int i = 0; i < gamePresenter.getENEMY_NUM(); i++) {
                canvas.drawBitmap(
                        gamePresenter.getEnemies()[i].getBitmap(),
                        gamePresenter.getEnemies()[i].getX(),
                        gamePresenter.getEnemies()[i].getY(),
                        paint
                );
            }

            //drawing boom image
            canvas.drawBitmap(
                    gamePresenter.getBoom().getBitmap(),
                    gamePresenter.getBoom().getX(),
                    gamePresenter.getBoom().getY(),
                    paint
            );
            //drawing friends image
            for(int i = 0; i < gamePresenter.getFRINEND_NUM(); i++){
                canvas.drawBitmap(

                        gamePresenter.getFriends()[i].getBitmap(),
                        gamePresenter.getFriends()[i].getX(),
                        gamePresenter.getFriends()[i].getY(),
                        paint
                );
            }

            //draw game Over when the game is over
            if(gamePresenter.isGameOver()){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);

                int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over",canvas.getWidth()/2,yPos,paint);
            }

            //Unlocking the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }

    @Override
    public int getScreenX() {
        return this.screenX;
    }

    @Override
    public int getScreenY() {
        return this.screenY;
    }

    @Override
    public void playGameOnSound(boolean play) {
        if(play) GameView.gameOnsound.start();
        else GameView.gameOnsound.stop();
    }

    @Override
    public void playGameOverSound(boolean play) {
        if(play) gameOversound.start();
        else gameOversound.stop();
    }

    @Override
    public void playKilledEnemySound(boolean play) {
        if(play) killedEnemysound.start();
        else killedEnemysound.stop();
    }

    @Override
    public void pause() {
        gamePresenter.pause();
    }

    @Override
    public void resume() {
        gamePresenter.resume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {// why do not have default?
            case MotionEvent.ACTION_UP:
                gamePresenter.getPlayer().stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                gamePresenter.getPlayer().setBoosting();
                break;
        }
        //if the game's over, tappin on game Over screen sends you to MainActivity
        if(gamePresenter.isGameOver()){
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                context.startActivity(new Intent(context,MainActivity.class));
            }
        }

        return true;
    }

    //stop the music on exit
    public static void stopMusic(){
        gameOnsound.stop();
    }
}
