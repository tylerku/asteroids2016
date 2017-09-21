package edu.byu.cs.superasteroids.model.positioned_objects.moving_objects;

import android.graphics.PointF;

import edu.byu.cs.superasteroids.model.positioned_objects.PositionedObject;

/**
 * Created by tylerku on 10/20/16.
 *
 * An object that moves on screen
 */
public class MovingObject extends PositionedObject {


    /**
     * Constructor for MovingObject
     *
     * @param URLimage - image for the object to be drawn
     * @param h - height of the object
     * @param w - width of the object
     */
    public MovingObject(String URLimage, int h, int w){
        super(URLimage, h, w);
    }

    /**
     * updates the object when changes are made (e.g. changes its position)
     */
    public void update(PointF newPosition){

    }
}
