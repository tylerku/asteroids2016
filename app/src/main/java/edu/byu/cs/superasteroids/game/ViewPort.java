package edu.byu.cs.superasteroids.game;

import android.graphics.PointF;

import edu.byu.cs.superasteroids.model.AsteroidsGame;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.Asteroid;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.Ship;

/**
 * Created by tyudy on 11/9/16.
 */

public class ViewPort {

    private static float height;
    private static float width;
    private static PointF shipPosInLevel; // This is where(the center) the view is at in the level.
    private static PointF screenCenter; //This is where the center is in level coordinates

    private ViewPort(){
        height = 0;
        width = 0;
        shipPosInLevel = new PointF();
        screenCenter = new PointF();
    }

    // Getters
    public static float getHeight(){ return height; }
    public static float getWidth(){ return width; }
    public static PointF getPosInLevel(){ return shipPosInLevel; }
    public static PointF getScreenCenter(){ return screenCenter; }
    public static PointF getShipPosOnScreen(){


        // Initialize to the value if the ship isnt near any edges
        float x = width/2;
        float y = height/2;

        // Adjust the x or y depending on if the ship is near an edge
        if(nearBottomEdge()) {
            y = shipPosInLevel.y - (AsteroidsGame.SINGLETON.getCurrentLevel().getLevelHeight() - height);
        }

        if(nearTopEdge()){
            y = shipPosInLevel.y;
        }

        if(nearRightEdge()){
            x = shipPosInLevel.x - (AsteroidsGame.SINGLETON.getCurrentLevel().getLevelWidth() - width);
        }

        if(nearLeftEdge()){
            x = shipPosInLevel.x;
        }

        return new PointF(x,y);
    }
    //WARNING: All of these need to be called set before using this classes static functions
    // Setters
    public static void setHeight(float h){ height = h; }
    public static void setWidth(float w){ width = w; }
    public static void setPosInLevel(PointF pos) { shipPosInLevel = pos; }
    public static void setScreenCenter(PointF center){ screenCenter = new PointF(center.x, center.y); }

    /**
     * sets the new ship location in level coordinates and changes the viewPort
     * coordinates.
     *
     * WARNING: must initialize AsteroidsGame before using this function
     *
     * @param newShipLocation - This is the location in level coordinates
     */
    public static void updateShipLocationAndMoveView(PointF newShipLocation) {
        float deltax = (newShipLocation.x - shipPosInLevel.x);
        float deltay = (newShipLocation.y - shipPosInLevel.y);

        boolean leftBoundReached = Ship.SINGLETON.getWidth()/2 > newShipLocation.x;
        boolean rightBoundReached = newShipLocation.x + Ship.SINGLETON.getWidth()/2 > AsteroidsGame.SINGLETON.getCurrentLevel().getLevelWidth();
        boolean topBoundReached = Ship.SINGLETON.getHeight()/2 > newShipLocation.y;
        boolean bottomBoundReached = newShipLocation.y + Ship.SINGLETON.getHeight()/2 > AsteroidsGame.SINGLETON.getCurrentLevel().getLevelHeight();

        // update ship position if it is not touching the edge
        if(!leftBoundReached && !rightBoundReached) {
            shipPosInLevel.x = newShipLocation.x;
        }
        if(!topBoundReached && !bottomBoundReached){
            shipPosInLevel.y = newShipLocation.y;
        }

        // update screen center if it is not touching the edge
        if(!nearLeftEdge() && !nearRightEdge()) {
            screenCenter.x += deltax;
        }
        if(!nearTopEdge() && !nearBottomEdge()) {
            screenCenter.y += deltay;
        }

    }
    public static PointF convertToScreenCoordinates(PointF levelCoords){
        float x = levelCoords.x - (screenCenter.x - width/2);
        float y = levelCoords.y - (screenCenter.y - height/2);
        return new PointF(x,y);
    }
    public static PointF convertToWorldCoordinates(PointF screenCoords){
        float y = screenCenter.y + (screenCoords.y - height/2);
        float x = screenCenter.x + (screenCoords.x - width/2);
        return new PointF(x,y);
    }

    // Reset the ship back to the center of the level
    public static void resetShipToLevelCenter(){
        screenCenter.x = width/2;
        screenCenter.y = height/2;
        shipPosInLevel.x = width/2;
        shipPosInLevel.y = height/2;
    }

    public static boolean nearLeftEdge(){ return shipPosInLevel.x - ViewPort.width/2 <= 0; }
    public static boolean nearRightEdge(){ return shipPosInLevel.x + ViewPort.width/2 >= AsteroidsGame.SINGLETON.getCurrentLevel().getLevelWidth(); }
    public static boolean nearTopEdge(){ return shipPosInLevel.y - ViewPort.height/2 <= 0; }
    public static boolean nearBottomEdge(){ return  shipPosInLevel.y + ViewPort.height/2 >= AsteroidsGame.SINGLETON.getCurrentLevel().getLevelHeight(); }


}
