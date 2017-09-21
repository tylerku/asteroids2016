package edu.byu.cs.superasteroids.model.positioned_objects;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.View;

import edu.byu.cs.superasteroids.drawing.DrawingHelper;
import edu.byu.cs.superasteroids.game.ViewPort;
import edu.byu.cs.superasteroids.model.AsteroidsGame;
import edu.byu.cs.superasteroids.model.Level;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.Asteroid;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.Ship;

/**
 * Created by tylerku on 10/19/16
 *
 * A small map showing the entire asteroid level.
 */
public class MiniMap extends PositionedObject {

    public static final MiniMap SINGLETON = new MiniMap();


    /**
     * MiniMap Constructor
     *
     */
    public MiniMap(){
        super("", 0, 0);
    }

    /**
     *
     * @param levelHeight - Height of the current level
     * @param levelWidth - Width of the current level
     */
    public void setHeightandWidth(float levelHeight, float levelWidth){
        this.setImageHeight((int)(levelHeight * AsteroidsGame.MAP_TO_LEVEL_RATIO));
        this.setImageWidth((int)(levelWidth * AsteroidsGame.MAP_TO_LEVEL_RATIO));
    }


    /**
     * Draw the mini map according to whats happening in the level
     */
    public void draw(Level currentLevel, Ship ship){
        int left = 0;
        int top = 0;
        int right = getImageWidth();
        int bottom = getImageHeight();
        // Draw a light grey background to represent the level in the top left corner of the screen
        DrawingHelper.drawFilledRectangle(new Rect(left, top, right, bottom), Color.WHITE, 225);

        // Draw red dots on the Minimap for asteroids
        for(Asteroid asteroid: currentLevel.getLevelAsteroids()){
            float xCoord = asteroid.getPosInLevel().x * AsteroidsGame.MAP_TO_LEVEL_RATIO;
            float yCoord = asteroid.getPosInLevel().y * AsteroidsGame.MAP_TO_LEVEL_RATIO;
            /**
             * Draws a point to the game view using the provided parameters.  Will not draw if the current canvas is null.
             * @param location The location of the point in view coordinates
             * @param width The width of the point
             * @param color The color of the point
             * @param alpha The point alpha value (0 - 255, 0 = completely transparent, 255 = completely opaque)
             */
            DrawingHelper.drawPoint(new PointF(xCoord,yCoord), asteroid.getImageWidth() * AsteroidsGame.MAP_TO_LEVEL_RATIO, Color.RED, 215);
        }

        // Draw the ship as a green dot in the Minimap
        float xShipCoord = ViewPort.getPosInLevel().x * AsteroidsGame.MAP_TO_LEVEL_RATIO;
        float yShipCoord = ViewPort.getPosInLevel().y * AsteroidsGame.MAP_TO_LEVEL_RATIO;
        PointF shipPosInMiniMap = new PointF(xShipCoord, yShipCoord);
        DrawingHelper.drawPoint(shipPosInMiniMap, ship.getWidth() * AsteroidsGame.MAP_TO_LEVEL_RATIO, Color.GREEN, 215);
    }

    public PointF getPosInLevel(){
        float x = ViewPort.getScreenCenter().x - ViewPort.getWidth()/2 + getImageWidth()/2;
        float y = ViewPort.getScreenCenter().y - ViewPort.getHeight()/2 + getImageHeight();
        return new PointF(x,y);
    }



}
