package edu.byu.cs.superasteroids.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.method.Touch;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import edu.byu.cs.superasteroids.content.ContentManager;
import edu.byu.cs.superasteroids.core.GraphicsUtils;
import edu.byu.cs.superasteroids.database.GameDbOpenHelper;
import edu.byu.cs.superasteroids.database.Level_DAO;
import edu.byu.cs.superasteroids.database.Ship_DAO;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;
import edu.byu.cs.superasteroids.game.GameActivity;
import edu.byu.cs.superasteroids.game.InputManager;
import edu.byu.cs.superasteroids.game.ViewPort;
import edu.byu.cs.superasteroids.model.AsteroidsGame;
import edu.byu.cs.superasteroids.model.Level;
import edu.byu.cs.superasteroids.model.Space;
import edu.byu.cs.superasteroids.model.positioned_objects.BgObject;
import edu.byu.cs.superasteroids.model.positioned_objects.MiniMap;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.Asteroid;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.Projectile;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.Ship;

/**
 * Created by tyudy on 11/10/16.
 */

public class GameDelegate implements IGameDelegate {

    GameActivity mGameActivity;
    SQLiteDatabase mDatabase;
    PointF shipCenter;
    boolean shipGotHit;
    int currentLevelIndex;
    ArrayList<Level> levels;
    boolean userWon;
    int timeCounter;




    public GameDelegate(GameActivity gameActivity){
        mGameActivity = gameActivity;
        setDatabase(mGameActivity);
        shipCenter = new PointF();
        shipGotHit = false;
        currentLevelIndex = 0;
        levels = new ArrayList<>();
        userWon = false;
        timeCounter = 0;
    }

    // Sync the database with the database that has been loaded.
    private void setDatabase(Context context){
        GameDbOpenHelper openHelper = new GameDbOpenHelper(context);
        mDatabase = openHelper.getReadableDatabase();
        Ship_DAO.SINGLETON.setDb(mDatabase);
        Level_DAO.SINGLETON.setDb(mDatabase);
    }

    /**
     * Updates the game delegate. The game engine will call this function 60 times a second
     * once it enters the game loop.
     * @param elapsedTime Time since the last update. For this game, elapsedTime is always
     *                    1/60th of second
     */
    @Override
    public void update(double elapsedTime) {

        if(userWon){
            return;
        }

        // Go to the next level if all the asteroids are gone
        if(AsteroidsGame.SINGLETON.getCurrentLevel().getLevelAsteroids().size() == 0){
            // Go to next level
            currentLevelIndex++;

            if(currentLevelIndex == levels.size()){
                //GAME OVER AND THEY WON!!!
                userWon = true;
            } else {
                AsteroidsGame.SINGLETON.setCurrentLevel(levels.get(currentLevelIndex));
                MiniMap.SINGLETON.setImageHeight((int)(AsteroidsGame.MAP_TO_LEVEL_RATIO * AsteroidsGame.SINGLETON.getCurrentLevel().getLevelHeight()));
                MiniMap.SINGLETON.setImageWidth((int)(AsteroidsGame.MAP_TO_LEVEL_RATIO * AsteroidsGame.SINGLETON.getCurrentLevel().getLevelWidth()));
                for(Asteroid asteroid: AsteroidsGame.SINGLETON.getCurrentLevel().getLevelAsteroids()){
                    asteroid.generateRandomStartPoint();
                }
            }
        }

        // If fire pressed
        if(InputManager.firePressed){
            Ship.SINGLETON.shoot();
            playFireSound();
        }

        PointF movePoint = InputManager.movePoint;

        // Update all the shots that have been fired and are still in the level
        for(int i = 0; i < AsteroidsGame.SINGLETON.getCurrentLevel().getProjectiles().size(); i++){
            Projectile shot = AsteroidsGame.SINGLETON.getCurrentLevel().getProjectiles().get(i);
            shot.update(elapsedTime);
            if(shot.isOutOfLevel()){
                AsteroidsGame.SINGLETON.getCurrentLevel().removeProjectile(i);
            }
        }

        // Update each asteroid in the level
        for(Asteroid asteroid: AsteroidsGame.SINGLETON.getCurrentLevel().getLevelAsteroids()){
            asteroid.update(elapsedTime);
        }

        // React to any collisions if there are any
        detectCollisions();

        Ship.SINGLETON.update(elapsedTime, movePoint);

        if(movePoint == null){
            return;
        }

        // Update ship location
        RectF shipBounds = Ship.SINGLETON.getBounds();
        TouchTriangle.SINGLETON.setTouch(movePoint);
        GraphicsUtils.MoveObjectResult shipMoveResult = GraphicsUtils.moveObject(ViewPort.getPosInLevel(), shipBounds, Ship.SINGLETON.getSpeed(),
                                                                             TouchTriangle.SINGLETON.angelCos(), TouchTriangle.SINGLETON.angelSin(), elapsedTime );
        ViewPort.updateShipLocationAndMoveView(shipMoveResult.getNewObjPosition());



    }

    /**
     * Loads content such as image and sounds files and other data into the game. The GameActivty will
     * call this once right before entering the game engine enters the game loop.
     * @param content An instance of the content manager. This should be used to load images and sound
     *                files.
     */
    @Override
    public void loadContent(ContentManager content) {

        // Prepare Space Object
        AsteroidsGame.SINGLETON.setSpace(new Space("image/space.bmp"));

        // Prepare the Ship
        AsteroidsGame.SINGLETON.setShip(Ship.SINGLETON);
        Ship.SINGLETON.initializeLives();

        // Prepare the levels
        levels = Level_DAO.SINGLETON.getLevels();
        AsteroidsGame.SINGLETON.setLevels(levels);
        AsteroidsGame.SINGLETON.setCurrentLevel(levels.get(currentLevelIndex));
        userWon = false;
        currentLevelIndex = 0;

        // Prepare the Projectile
        AsteroidsGame.SINGLETON.setProjectileHeight(Ship.SINGLETON.getCannon().getAttackImageHeight());
        AsteroidsGame.SINGLETON.setProjectileWidth(Ship.SINGLETON.getCannon().getAttackImageWidth());
        AsteroidsGame.SINGLETON.setProjectileDamage(Ship.SINGLETON.getCannon().getDamage());

        // Prepare the MiniMap
        MiniMap.SINGLETON.setHeightandWidth(AsteroidsGame.SINGLETON.getCurrentLevel().getLevelHeight(), AsteroidsGame.SINGLETON.getCurrentLevel().getLevelWidth());
        MiniMap.SINGLETON.setImageHeight((int)(AsteroidsGame.MAP_TO_LEVEL_RATIO * AsteroidsGame.SINGLETON.getCurrentLevel().getLevelHeight()));
        MiniMap.SINGLETON.setImageWidth((int)(AsteroidsGame.MAP_TO_LEVEL_RATIO * AsteroidsGame.SINGLETON.getCurrentLevel().getLevelWidth()));

        // Prepare coordinates, heights, and widths
        DisplayMetrics displaymetrics = new DisplayMetrics();
        mGameActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        ViewPort.setHeight(displaymetrics.heightPixels);
        ViewPort.setWidth(displaymetrics.widthPixels);
        ViewPort.setPosInLevel(new PointF(AsteroidsGame.SINGLETON.getCurrentLevel().getLevelWidth()/2,
                                          AsteroidsGame.SINGLETON.getCurrentLevel().getLevelHeight()/2));
        ViewPort.setScreenCenter(ViewPort.getPosInLevel());


        ArrayList<Asteroid> roids = AsteroidsGame.SINGLETON.getCurrentLevel().getLevelAsteroids();

        // Set the start position of all the asteroids (this must be done after the levels have been initialized)
        for(Asteroid asteroid: AsteroidsGame.SINGLETON.getCurrentLevel().getLevelAsteroids()){
            asteroid.generateRandomStartPoint();
        }

        // Load ship images into content manager
        ContentManager.getInstance().loadImage(Ship.SINGLETON.getEngine().getImageURL());
        ContentManager.getInstance().loadImage(Ship.SINGLETON.getMainBody().getImageURL());
        ContentManager.getInstance().loadImage(Ship.SINGLETON.getExtraPart().getImageURL());
        ContentManager.getInstance().loadImage(Ship.SINGLETON.getCannon().getImageURL());
        ContentManager.getInstance().loadImage(Ship.SINGLETON.getCannon().getAttackImageURL());


        try {
            ContentManager.getInstance().loadSound(Ship.SINGLETON.getCannon().getAttackSound());
        } catch(IOException e){
            e.printStackTrace();
        }

        int backgrounMusicId = 0;


        // Load the music, asteroids, and backgroundObjects into the content manager
        for(Level lvl: levels){
            try {
                backgrounMusicId = ContentManager.getInstance().loadLoopSound(lvl.getMusicURL());
            } catch (IOException e){
                e.printStackTrace();
                Log.d("GameDelegate", "The sound failed to upload in the loadContent function in the GameDelegate class.");
            }

            for(Asteroid asteroid: lvl.getLevelAsteroids()){
                ContentManager.getInstance().loadImage(asteroid.getImageURL());
            }

            for(BgObject bgObject: lvl.getLevelBackgroundObjects()){
                ContentManager.getInstance().loadImage(bgObject.getImageURL());
            }
        }

        // Play the sound for the game
        ContentManager.getInstance().playLoop(backgrounMusicId);







    }

    @Override
    public void unloadContent(ContentManager content) {

    }

    /**
     * Draws the game delegate. This function will be 60 times a second.
     */
    @Override
    public void draw() {

        // Draw Space
        AsteroidsGame.SINGLETON.getSpace().draw();

        // If user Won then say so
        if(userWon){
            DrawingHelper.drawCenteredText("YOU WON!", 500, Color.GREEN);
            return;
        }

        //Draw background objects
        for(BgObject obj: AsteroidsGame.SINGLETON.getCurrentLevel().getLevelBackgroundObjects()){
           obj.draw();
        }

        // Draw projectiles
        for(Projectile shot: AsteroidsGame.SINGLETON.getCurrentLevel().getProjectiles()){
            shot.draw();
        }

        // Draw Asteroids
        for(Asteroid asteroid: AsteroidsGame.SINGLETON.getCurrentLevel().getLevelAsteroids()){
            asteroid.draw();
        }

        //Draw the ship
        Ship.SINGLETON.draw(AsteroidsGame.SHIP_SCALE);

        // Draw the mini map
        MiniMap.SINGLETON.draw(AsteroidsGame.SINGLETON.getCurrentLevel(), Ship.SINGLETON);


    }

    /**
     * Handles collisions in the map.
     */
    private void detectCollisions(){

        ArrayList<Asteroid> asteroidsToAdd = new ArrayList<>();

        // Check for every possible type of collision
        for(int i = 0; i < AsteroidsGame.SINGLETON.getCurrentLevel().getLevelAsteroids().size(); i++){
            Asteroid asteroid = AsteroidsGame.SINGLETON.getCurrentLevel().getLevelAsteroids().get(i);

            // check if the asteroid collides with ship
            if(asteroid.getBounds().intersect(Ship.SINGLETON.getBounds())){
                Ship.SINGLETON.collide(asteroid);
                ArrayList<Asteroid> newAsteroids = asteroid.collide(Ship.SINGLETON);

                if(newAsteroids != null) addAsteroidArrays(asteroidsToAdd, newAsteroids);

                shipGotHit = true;
            }
            // check if the asteroid collides with any projectiles
            for(int j = 0; j < AsteroidsGame.SINGLETON.getCurrentLevel().getProjectiles().size(); j++){
                Projectile shot = AsteroidsGame.SINGLETON.getCurrentLevel().getProjectiles().get(j);
                if(asteroid.getBounds().intersect(shot.getBounds())){
                    shot.collide(j);
                    ArrayList<Asteroid> newAsteroids = asteroid.collide(shot);
                    if(newAsteroids != null) addAsteroidArrays(asteroidsToAdd, newAsteroids);
                }
            }
        }

        // Add asteroids that were split
        for(Asteroid asteroid: asteroidsToAdd){
            AsteroidsGame.SINGLETON.getCurrentLevel().getLevelAsteroids().add(asteroid);
        }

        // Remove the asteroids that are too small
        for(int i = 0; i < AsteroidsGame.SINGLETON.getCurrentLevel().getLevelAsteroids().size(); i++){
            Asteroid asteroid = AsteroidsGame.SINGLETON.getCurrentLevel().getLevelAsteroids().get(i);

            if(asteroid.getSplitCount() >= 2 && asteroid.getName().equals("octeroid")){
                AsteroidsGame.SINGLETON.getCurrentLevel().getLevelAsteroids().remove(i);
            } else if (asteroid.getSplitCount() >= 3){
                AsteroidsGame.SINGLETON.getCurrentLevel().getLevelAsteroids().remove(i);
            }
        }
    }


    /**
     * class to manage angels.
     * To use just call the set touch function to set the point that was touched.
     */
    public static class TouchTriangle{

        public static final TouchTriangle SINGLETON = new TouchTriangle();
        PointF touch; //The point that was touched on the screen

        private TouchTriangle(){
            touch = new PointF();
        }

        public void setTouch(PointF touchPoint){ touch = touchPoint; }
        public PointF getTouchPoint(){ return touch; }

        public double getHypotenuse(){ return GraphicsUtils.distance(ViewPort.getShipPosOnScreen(), touch); }
        public double getOpposite(){ return (touch.y - ViewPort.getShipPosOnScreen().y); }
        public double getAdjacent(){ return (touch.x - ViewPort.getShipPosOnScreen().x); }
        public double angelSin(){ return getOpposite()/getHypotenuse(); }
        public double angelCos(){ return getAdjacent()/getHypotenuse(); }
    }

    /**
     *
     * @param listOne - List that will be added to
     * @param listTwo - List that will have its elements added to listOne
     * @return an array list with ALL the elements of the two arrays
     */
    private ArrayList<Asteroid> addAsteroidArrays(ArrayList<Asteroid> listOne, ArrayList<Asteroid> listTwo){

        for(int i = 0; i < listTwo.size(); i++){
            listOne.add(listTwo.get(i));
        }
        return listOne;
    }

    /**
     * plays the sound made by the projectile
     */
    private void playFireSound(){
        int soundId = 0;
        try {
            soundId = ContentManager.getInstance().loadSound(Ship.SINGLETON.getCannon().getAttackSound());
            ContentManager.getInstance().playSound(soundId, 1, 1);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
