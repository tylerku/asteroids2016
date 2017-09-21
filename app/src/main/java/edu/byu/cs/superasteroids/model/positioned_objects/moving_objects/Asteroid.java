package edu.byu.cs.superasteroids.model.positioned_objects.moving_objects;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;

import edu.byu.cs.superasteroids.content.ContentManager;
import edu.byu.cs.superasteroids.core.GraphicsUtils;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;
import edu.byu.cs.superasteroids.game.ViewPort;
import edu.byu.cs.superasteroids.model.AsteroidsGame;
import edu.byu.cs.superasteroids.ship_builder.IShipBuildingView;

/**
 * Created by tylerku on 10/19/16.
 *
 * Represents and asteroid that flys around on the screen on a level
 */
public class Asteroid extends MovingObject {


    private long id;
    private String name;
    PointF posInLevel; // Where the center of the asteroid is in level coordinates

    int speed;
    float scale;
    float rotationalDegrees;
    float angle; // Angle at which the asteroid is traveling in radians
    int hp;
    int splitCount;





    /**
     * Asteroid Constructor
     *
     * @param URLimage - image for the object to be drawn
     * @param h - height of the object
     * @param w - width of the object
     */
    public Asteroid(String URLimage, int h, int w, String n){
        super(URLimage, h, w);
        name = n;
        posInLevel = new PointF();
        scale = AsteroidsGame.ASTEROID_SCALE;
        setSpecifics();
        splitCount = 0;
    }

    // Copy Constructor
    public Asteroid(Asteroid asteroid, float s){

        super(new String(asteroid.getImageURL()), asteroid.getImageHeight(), asteroid.getImageWidth());
        name = new String(asteroid.getName());
        posInLevel = new PointF(new Point((int)asteroid.getPosInLevel().x,(int)asteroid.getPosInLevel().y));
        splitCount = asteroid.splitCount;
        setSpecifics();
        setScale(scale);

    }

    /**
     *
     * @param asteroid - JSON asteroid that cointains all the data we need to create an asteroid
     * @throws JSONException
     */
    public Asteroid(JSONObject asteroid)
     throws JSONException {

        super(asteroid.getString("image"),
                asteroid.getInt("imageHeight"),
                asteroid.getInt("imageWidth"));

        name = asteroid.getString("name");

    }

    public void setId(long Id){ id = Id; }
    public void setPosInLevel(PointF pos){ posInLevel = pos; }
    public void setAngle(float theta){ angle = theta; }
    public void setScale(float newScale){ scale = newScale; }
    /**
     * this is private because it should only be used by the constructor.
     * Sets the speed of the asteroid depending on its name
     * Sets the rotationalDegrees to a random value from 0 - 360;
     * Sets the scale to the scale specified by AsteroidsGame
     * Sets angle to some random angle between 0 and 2Pi
     *
     */
    private void setSpecifics(){
        Random rand = new Random();
        speed = (int)AsteroidsGame.ASTEROID_SPEED;
        rotationalDegrees = rand.nextInt(360);
        angle = (float)(rand.nextFloat() * 2 * Math.PI);

        // I can change these later if I want
        if(name == "regular"){
            hp = 3;
        } else if(name == "growing"){
            hp = 3;
        } else if(name == "octeroid"){
            hp = 3;
        } else {
            hp = 3;
        }
    }
    public String getName(){ return name; }
    public PointF getPosInLevel(){ return posInLevel; }
    public RectF getBounds(){

        float topBound = posInLevel.y - getImageHeight()/2;
        float bottomBound = posInLevel.y + getImageHeight()/2;
        float leftBound = posInLevel.x - getImageWidth()/2;
        float rightBound = posInLevel.x + getImageWidth()/2;

        return new RectF(leftBound, topBound, rightBound, bottomBound);
    }
    public float getScale(){ return scale; }
    public int getSplitCount(){ return splitCount; }

    public ArrayList<Asteroid> collide(Projectile laser){
        //Do damage to the asteroid if it can still take the damage
        if(hp > laser.getDamage()){
            hp -= laser.getDamage();
            return null;
          // Split the asteroid if its hp is too low
        } else {
            return this.split();
        }


    }

    public ArrayList<Asteroid> collide(Ship ship){
        hp--;
        if(hp <= 0){
            return this.split();
        }
        return null;
    }

    private ArrayList<Asteroid> split(){

        ArrayList<Asteroid> newAsteroids = new ArrayList<>();
        int countAfterSplit;
        if(this.name.equals("octeroid")){
            countAfterSplit = 8;
        } else {
            countAfterSplit = 2;
        }

        angle += Math.PI/4;
        scale = scale / countAfterSplit;
        this.setImageWidth(getImageWidth()/countAfterSplit);
        this.setImageHeight(getImageHeight()/countAfterSplit);
        splitCount++;

        for(int i = 0; i < countAfterSplit - 1; i++){
            Asteroid fragment = new Asteroid(this, scale/countAfterSplit);
            fragment.setAngle((float) (angle - Math.PI/4));
            fragment.setScale(scale);
            fragment.setImageWidth(getImageWidth()/countAfterSplit);
            fragment.setImageHeight(getImageHeight()/countAfterSplit);
            fragment.setSpecifics();
            newAsteroids.add(fragment);
        }

        return newAsteroids;
    }


    /**
     * Tells the Asteroid what to do when changes are made
     */
    public void update(double elapsedTime){


        RectF asteroidBounds = getBounds();
        rotationalDegrees = (rotationalDegrees + 1) % 360;

        // Correct the asteroids course if its at a wall
        if(isTouchingSideEdge()){
            angle = (float)(Math.PI/2 - (angle - (Math.PI/2)));
        } else if(isTouchingHorizontalEdge()){
            angle = (float)(Math.PI - (angle - (Math.PI)));
        }

        if(this.name.equals("growing")){
            float growScale = AsteroidsGame.ASTEROID_GROWTH_RATE;
            if(scale < 4) {
                scale *= growScale;
                setImageHeight((int) (getImageHeight() * growScale));
                setImageWidth((int) (getImageWidth() * growScale));
            }

        }

        GraphicsUtils.MoveObjectResult moveObjectResult = GraphicsUtils.moveObject(posInLevel, asteroidBounds, speed, angle, elapsedTime);

        posInLevel = moveObjectResult.getNewObjPosition();


    }

    public void draw(){

        int asteroidId = ContentManager.getInstance().getImageId(this.getImageURL());
        PointF screenCoords = ViewPort.convertToScreenCoordinates(posInLevel);
        PointF shipCoords = ViewPort.getPosInLevel();

        /**
         * Draws an image to the current game view centered at the specified view coordinates, with the specified
         * rotation in degrees, scale, and alpha. Will not draw if the current canvas is null.
         * @param imageId The ID of an image that has already been loaded through the content manager
         * @param x The x coordinate to draw the center of the image at
         * @param y The y coordinate to draw the center of the image at
         * @param rotationDegrees The rotation on the image in degrees
         * @param scaleX The image X scale
         * @param scaleY The image Y scale
         * @param alpha Image alpha (0 - 255, 0 = completely transparent, 255 = completely opaque)
         */
        DrawingHelper.drawImage(asteroidId, screenCoords.x, screenCoords.y, rotationalDegrees, scale, scale, 255);
    }
    public boolean isTouchingSideEdge(){

        float levelWidth = AsteroidsGame.SINGLETON.getCurrentLevel().getLevelWidth();

        PointF testPoint = posInLevel;
        float halfAsteroidWidth = this.getImageWidth()/2;

        //1100 , 1175

        if(this.posInLevel.x + halfAsteroidWidth - 0>= levelWidth ||
           this.posInLevel.x - halfAsteroidWidth + 0 <= 0){
            return true;
        }

        return false;
    }

    public boolean isTouchingHorizontalEdge(){

        float levelHeight = AsteroidsGame.SINGLETON.getCurrentLevel().getLevelHeight();
        float halfAsteroidHeight = this.getImageHeight()/2;

        PointF shipPos = ViewPort.getPosInLevel();


        //450 , 550

        if(this.posInLevel.y + halfAsteroidHeight - 0 >= levelHeight ||
           this.posInLevel.y - halfAsteroidHeight + 0 <= 0){
            return true;
        }

        return false;
    }
    /**
     * Sets the posInLevel of the asteroid to a randomly generate spot in the level
     * Does not allow the asteroid to be off of the screen
     * Does not allow the asteroid to be very close to the ship
     */
    public void generateRandomStartPoint(){
        Random rand = new Random();
        float xBound = AsteroidsGame.SINGLETON.getCurrentLevel().getLevelWidth();
        float yBound = AsteroidsGame.SINGLETON.getCurrentLevel().getLevelHeight();
        float extraSafeSpace = 50;

        int y = -1;
        int x = -1;

        boolean touchingLeftEdge = true;
        boolean touchingRightEdge = true;
        boolean touchingTopEdge = true;
        boolean touchingBottomEdge = true;
        boolean xIsInSafeSpace = true;
        boolean yIsInSafeSpace = true;

        //make sure it spawns all the way in the level and away from the ship
        while(touchingLeftEdge || touchingRightEdge || xIsInSafeSpace){
            x = rand.nextInt((int)xBound);
            touchingLeftEdge = x <= this.getImageWidth()/2;
            touchingRightEdge = x + this.getImageWidth()/2 >= xBound;
            xIsInSafeSpace = x + this.getImageWidth()/2 <= ViewPort.getScreenCenter().x - Ship.SINGLETON.getWidth()/2 - extraSafeSpace &&
                             x - this.getImageWidth()/2 >= ViewPort.getScreenCenter().x + Ship.SINGLETON.getWidth()/2 + extraSafeSpace;

        }

        while( touchingTopEdge || touchingBottomEdge || yIsInSafeSpace){
            y = rand.nextInt((int)yBound);
            touchingTopEdge = y <= this.getImageHeight()/2;
            touchingBottomEdge = y + this.getImageHeight()/2 >= yBound;
            yIsInSafeSpace = y + this.getImageHeight()/2 >= ViewPort.getScreenCenter().y - Ship.SINGLETON.getHeight()/2 - extraSafeSpace &&
                             y - this.getImageHeight()/2 <= ViewPort.getScreenCenter().y + Ship.SINGLETON.getHeight()/2 + extraSafeSpace;
        }

        posInLevel =  new PointF(x,y);
    }



}
