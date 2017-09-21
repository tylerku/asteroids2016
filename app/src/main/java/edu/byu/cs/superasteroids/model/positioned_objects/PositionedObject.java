package edu.byu.cs.superasteroids.model.positioned_objects;

import android.graphics.PointF;
import edu.byu.cs.superasteroids.model.VisibleObject;


/**
 * Created by tylerku on 10/20/16.
 *
 * An object on screen that is on top of the background
 */
public class PositionedObject extends VisibleObject {

    private int imageHeight;
    private int imageWidth;
    /**
     * PositionedObject Constructor
     *
     * @param URLimage - image for the object to be drawn
     */
    public PositionedObject(String URLimage, int height, int width){

        super(URLimage);

        imageHeight = height;
        imageWidth = width;
    }

    public int getImageHeight(){ return imageHeight; }
    public int getImageWidth(){ return imageWidth; }

    public void setImageHeight(int h){ imageHeight = h; }
    public void setImageWidth(int w){ imageWidth = w; }

}
