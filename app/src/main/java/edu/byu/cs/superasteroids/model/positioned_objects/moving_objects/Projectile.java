package edu.byu.cs.superasteroids.model.positioned_objects.moving_objects;

import android.graphics.PointF;
import android.graphics.RectF;

import edu.byu.cs.superasteroids.content.ContentManager;
import edu.byu.cs.superasteroids.core.GraphicsUtils;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;
import edu.byu.cs.superasteroids.game.ViewPort;
import edu.byu.cs.superasteroids.model.AsteroidsGame;


/**
 * Created by tylerku on 10/19/16.
 *
 * The projectiles that will be shot from the ship
 */
public class Projectile extends MovingObject {

    private float angle; // Angle in radians that the projectile is traveling
    private float speed;
    private PointF position; // Point where the projectile is on screen in level coordinates
    private int damage;
    /**
     * Constructor for Projectile
     *
     * @param URLimage - image for the object to be drawn
     * @param h        - height of the object
     * @param w        - width of the object
     * */
    public Projectile(String URLimage, int h, int w, float theta, PointF pos, int shotPower) {
        super(URLimage, h, w);
        angle = theta;
        speed = AsteroidsGame.PROJECTILE_SPEED;
        position = pos;
        damage = shotPower;

    }

    /**
     * Function to change to location of the projectile on screen
     */
    public void update(double elapsedTime){

        RectF projectileBounds = new RectF();
        GraphicsUtils.MoveObjectResult projectileMoveResult = GraphicsUtils.moveObject(position, projectileBounds, speed, (angle - (Math.PI/2)), elapsedTime);
        position = projectileMoveResult.getNewObjPosition();
    }

    /**
     * @return - true of the object is past the bound of the level, false otherwise
     */
    public boolean isOutOfLevel(){
        if(position.x < (0 - getImageWidth()/2) || position.x > AsteroidsGame.SINGLETON.getCurrentLevel().getLevelWidth() + getImageWidth()/2 ||
           position.y < (0 - getImageHeight()/2) || position.y > AsteroidsGame.SINGLETON.getCurrentLevel().getLevelHeight() + getImageHeight()/2){
            return true;
        }
        return false;
    }

    /**
     * Function to make the projectile explode
     */
    public void collide(int index){
        AsteroidsGame.SINGLETON.getCurrentLevel().getProjectiles().remove(index);
    }

    /**
     * Draw the projectile on screen
     */
    public void draw(){
        int projectileId = ContentManager.getInstance().getImageId(this.getImageURL());
        float scale = AsteroidsGame.SHIP_SCALE;
        PointF screenPos = ViewPort.convertToScreenCoordinates(position);
        float rotationalDegrees = (float)(angle * (180/Math.PI)) % 360;
        DrawingHelper.drawImage(projectileId, screenPos.x, screenPos.y, rotationalDegrees, scale, scale, 255);
    }

    // Setters
    public void setCenter(PointF point){ position = point; }

    // Getters
    public float getAngle(){ return angle; }
    public float getSpeed(){ return speed; }
    public int getDamage(){ return damage; }
    public PointF getPosition(){ return position; }
    public RectF getBounds(){
        float leftBound = position.x - getImageWidth()/2f;
        float rightBound = position.x + getImageWidth()/2f;
        float topBound = position.y - getImageHeight()/2f;
        float bottomBound = position.y + getImageHeight()/2f;

        return new RectF(leftBound, topBound, rightBound, bottomBound);
    }

}
