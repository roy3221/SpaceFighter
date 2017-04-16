package example.com.spacefighter.Presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;

import example.com.spacefighter.model.Boom;
import example.com.spacefighter.model.ModelInterface;
import example.com.spacefighter.model.Star;
import example.com.spacefighter.model.flights.Enemy;
import example.com.spacefighter.model.flights.Friend;
import example.com.spacefighter.model.flights.Player;
import example.com.spacefighter.view.GameView;
import example.com.spacefighter.view.GameViewInterface;

/**
 * Created by churong1 on 4/16/17.
 */
public class GamePresenter implements Runnable{
    private GameViewInterface gameView;
    private ModelInterface player;
    private ModelInterface[] enemies;
    private ModelInterface[] stars;
    private ModelInterface[] friends;
    private ModelInterface boom;
    private final int STAR_NUM = 100;
    private final int ENEMY_NUM = 3;
    private final int FRINEND_NUM = 2;
    //indicator that the enemy has just entered the game screen
    private boolean enermyEnter ;
    //an indicator if the game is Over
    private boolean isGameOver ;
    //the score holder
    private int score;
    //the high Scores Holder
    private int highScore[] = new int[4];
    //Shared Prefernces to store the High Scores
    private SharedPreferences sharedPreferences;
    private int screenX;
    private int countMisses;
    //boolean variable to track if the game is playing or not
    volatile boolean playing;
    private Thread gameThread = null;

    public GamePresenter(Context context, GameViewInterface gameView) {
        this.screenX = gameView.getScreenX();
        this.gameView = gameView;
        int screenY = gameView.getScreenY();
        countMisses = 0;
        isGameOver = false;
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
            gameView.draw();

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
        for (ModelInterface s : stars) {
            s.update(player.getSpeed());
        }



        //updating the enemy coordinate with respect to player speed
        for(int i=0; i<ENEMY_NUM; i++) {
            //setting the flag true when the enemy just enters the screen
            if (enemies[i].getX() == screenX) {
                enermyEnter = true;
            }
            enemies[i].update(player.getSpeed());
            //if collision occurrs with player
            if (Rect.intersects(player.getDetectCollision(), enemies[i].getDetectCollision())) {
                //moving enemy outside the left edge
                //displaying boom at that location
                //playing a sound at the collision between player and the enemy
                gameView.playKilledEnemySound(true);
                boom.setX(enemies[i].getX());
                boom.setY(enemies[i].getY());
                enemies[i].setX(-350);

            } else {
                //if the enemy has just entered
                if (enermyEnter) {
                    //if player's x coordinate is more than the enemies's x coordinate.i.e. enemy has just passed across the player
                    if (player.getDetectCollision().exactCenterX() >= enemies[i].getDetectCollision().exactCenterX()) {
                        //increment countMisses
                        countMisses++;

                        //setting the flag false so that the else part is executed only when new enemy enters the screen
                        enermyEnter = false;
                        //if no of Misses is equal to 3, then game is over.
                        if (countMisses == 3) {
                            //setting playing false to stop the game.
                            playing = false;
                            isGameOver = true;
                            //stopping the gameon music
                            gameView.playGameOnSound(false);
                            gameView.playGameOverSound(true);
                            GameView.stopMusic();
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
                gameView.playGameOnSound(false);
                //play the game over sound
                gameView.playGameOverSound(true);
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

    public ModelInterface[] getEnemies() {
        return enemies;
    }

    public ModelInterface getPlayer() {
        return player;
    }

    public ModelInterface[] getStars() {
        return stars;
    }

    public ModelInterface[] getFriends() {
        return friends;
    }

    public ModelInterface getBoom() {
        return boom;
    }

    public int getENEMY_NUM() {
        return ENEMY_NUM;
    }

    public int getSTAR_NUM() {
        return STAR_NUM;
    }

    public int getFRINEND_NUM() {
        return FRINEND_NUM;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isEnermyEnter() {
        return enermyEnter;
    }

    public int getScore() {
        return score;
    }

    public int[] getHighScore() {
        return highScore;
    }

    public int getCountMisses() {
        return countMisses;
    }
}
