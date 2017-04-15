package example.com.spacefighter.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import example.com.spacefighter.controller.MainActivity;
import example.com.spacefighter.R;
import example.com.spacefighter.model.Boom;
import example.com.spacefighter.model.flights.Enemy;
import example.com.spacefighter.model.flights.Friend;
import example.com.spacefighter.model.flights.Player;
import example.com.spacefighter.model.Star;

/**
 * Created by churong1 on 4/14/17.
 */
public class GameView  extends SurfaceView implements Runnable {

    //boolean variable to track if the game is playing or not
    volatile boolean playing;

    //the game thread
    private Thread gameThread = null;

    private Player player;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private Star[] stars;
    private final int STAR_NUM = 100;
    //Adding enemies object array
    private Enemy[] enemies;

    //Adding 3 enemies you may increase the size
    private final int ENEMY_NUM = 3;
    private Boom boom;
    private final int FRINEND_NUM = 2;
    private Friend[] friends;
    //a screenX holder
    int screenX;

    //to count the number of Misses
    int countMisses;

    //indicator that the enemy has just entered the game screen
    boolean flag ;

    //an indicator if the game is Over
    private boolean isGameOver ;

    //the score holder
    int score;

    //the high Scores Holder
    int highScore[] = new int[4];

    //Shared Prefernces to store the High Scores
    SharedPreferences sharedPreferences;
    //the mediaplayer objects to configure the background music
    static MediaPlayer gameOnsound;
    private final MediaPlayer killedEnemysound;
    private final MediaPlayer gameOversound;
    //context to be used in onTouchEvent to cause the activity transition from GameAvtivity to MainActivity.
    Context context;

    //Class constructor
    public GameView(Context context, int screenX, int screenY) {
        super(context);
        this.screenX = screenX;
        this.context = context;
        //initializing the media players for the game sounds
        gameOnsound = MediaPlayer.create(context, R.raw.gameon);
        killedEnemysound = MediaPlayer.create(context,R.raw.killedenemy);
        gameOversound = MediaPlayer.create(context,R.raw.gameover);
        countMisses = 0;
//starting the game music as the game starts
        gameOnsound.start();
        isGameOver = false;
        //initializing player object
        //this time also passing screen size to player constructor
        player = new Player(context, screenX, screenY);
        stars = new Star[STAR_NUM];
        for(int i = 0; i < STAR_NUM; i++) {
            stars[i] = new Star(screenX, screenY);
        }
        enemies = new Enemy[ENEMY_NUM];
        for(int i = 0; i < ENEMY_NUM; i++) {
            enemies[i] = new Enemy(context, screenX, screenY);
        }
        friends = new Friend[FRINEND_NUM];
        for(int i = 0; i < FRINEND_NUM; i++) {
            friends[i] = new Friend(context, screenX, screenY);
        }
        //initializing drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();
        boom = new Boom(context);
        //setting the score to 0 initially
        score = 0;

        sharedPreferences = context.getSharedPreferences("SCORES",Context.MODE_PRIVATE);

//initializing the array high scores with the previous values
        highScore[0] = sharedPreferences.getInt("score1",0);
        highScore[1] = sharedPreferences.getInt("score2",0);
        highScore[2] = sharedPreferences.getInt("score3",0);
        highScore[3] = sharedPreferences.getInt("score4",0);


    }

    @Override
    public void run() {
        while (playing) {
            //to update the frame
            update();

            //to draw the frame
            draw();

            //to control
            control();
        }
    }


    private void update() {
        score++;
        player.update();
        //setting boom outside the screen
        boom.setX(-350);
        boom.setY(-350);
        //Updating the stars with player speed
        for (Star s : stars) {
            s.update(player.getSpeed());
        }



        //updating the enemy coordinate with respect to player speed
        for(int i=0; i<ENEMY_NUM; i++) {
            //setting the flag true when the enemy just enters the screen
            if (enemies[i].getX() == screenX) {
                flag = true;
            }
            enemies[i].update(player.getSpeed());
            //if collision occurrs with player
            if (Rect.intersects(player.getDetectCollision(), enemies[i].getDetectCollision())) {
                //moving enemy outside the left edge
                //displaying boom at that location
                //playing a sound at the collision between player and the enemy
                killedEnemysound.start();
                boom.setX(enemies[i].getX());
                boom.setY(enemies[i].getY());
                enemies[i].setX(-350);

            } else {
                //if the enemy has just entered
                if (flag) {
                    //if player's x coordinate is more than the enemies's x coordinate.i.e. enemy has just passed across the player
                    if (player.getDetectCollision().exactCenterX() >= enemies[i].getDetectCollision().exactCenterX()) {
                        //increment countMisses
                        countMisses++;

                        //setting the flag false so that the else part is executed only when new enemy enters the screen
                        flag = false;
                        //if no of Misses is equal to 3, then game is over.
                        if (countMisses == 3) {
                            //setting playing false to stop the game.
                            playing = false;
                            isGameOver = true;
                            //stopping the gameon music
                            gameOnsound.stop();
                            //play the game over sound
                            gameOversound.start();
                        }
                    }
                }
            }
        }
        for(int i = 0; i < FRINEND_NUM; i++) {
            friends[i].update(player.getSpeed());
            //checking for a collision between player and a friend
            if(Rect.intersects(player.getDetectCollision(),friends[i].getDetectCollision())){

                //displaying the boom at the collision
                boom.setX(friends[i].getX());
                boom.setY(friends[i].getY());
                //setting playing false to stop the game
                playing = false;
                //setting the isGameOver true as the game is over
                isGameOver = true;
                //stopping the gameon music
                gameOnsound.stop();
                //play the game over sound
                gameOversound.start();
            }
        }
        if(isGameOver) {
            //Assigning the scores to the highscore integer array
            for(int i=0;i<4;i++){
                if(highScore[i]<=score){

                    final int finalI = i;
                    highScore[i] = score;
                    break;
                }
            }

            //storing the scores through shared Preferences
            SharedPreferences.Editor e = sharedPreferences.edit();
            for(int i=0;i<4;i++){
                int j = i+1;
                e.putInt("score"+j,highScore[i]);
            }
            e.apply();
        }
    }

    private void draw() {
        //checking if surface is valid
        if (surfaceHolder.getSurface().isValid()) {
            //locking the canvas
            canvas = surfaceHolder.lockCanvas();
            //drawing a background color for canvas
            canvas.drawColor(Color.BLACK);

            //setting the paint color to white to draw the stars
            paint.setColor(Color.WHITE);

            //drawing all stars
            for (Star s : stars) {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }

            //drawing the score on the game screen
            paint.setTextSize(30);
            canvas.drawText("Score:"+score,100,50,paint);
            canvas.drawText("miss:"+countMisses,500,50,paint);



            //Drawing the player
            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);
            //drawing the enemies
            for (int i = 0; i < ENEMY_NUM; i++) {
                canvas.drawBitmap(
                        enemies[i].getBitmap(),
                        enemies[i].getX(),
                        enemies[i].getY(),
                        paint
                );
            }

            //drawing boom image
            canvas.drawBitmap(
                    boom.getBitmap(),
                    boom.getX(),
                    boom.getY(),
                    paint
            );
            //drawing friends image
            for(int i = 0; i < FRINEND_NUM; i++){
                canvas.drawBitmap(

                        friends[i].getBitmap(),
                        friends[i].getX(),
                        friends[i].getY(),
                        paint
                );
            }

            //draw game Over when the game is over
            if(isGameOver){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);

                int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over",canvas.getWidth()/2,yPos,paint);
            }

            //Unlocking the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        //when the game is paused
        //setting the variable to false
        playing = false;
        try {
            //stopping the thread
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        //when the game is resumed
        //starting the thread again
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {// why do not have default?
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                player.setBoosting();
                break;
        }
        //if the game's over, tappin on game Over screen sends you to MainActivity
        if(isGameOver){
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
