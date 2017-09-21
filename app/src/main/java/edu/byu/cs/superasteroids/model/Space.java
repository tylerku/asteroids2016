package edu.byu.cs.superasteroids.model;


import android.graphics.Point;
import android.view.Display;

import edu.byu.cs.superasteroids.content.ContentManager;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;
import edu.byu.cs.superasteroids.game.ViewPort;

/**
 * Created by tylerku on 10/19/16.
 *
 * The still background image of space
 */
public class Space extends VisibleObject {

    /**
     *
     * @param URLimage - String holding the URL for the space image
     */
    public Space(String URLimage){
        super(URLimage);
    }

    /**
     * Draw the space on screen
     */
    public static void draw(){
        int spaceId = ContentManager.getInstance().loadImage("images/space.bmp");
        float spaceWidth = ContentManager.getInstance().getImage(spaceId).getWidth();
        float spaceHeight = ContentManager.getInstance().getImage(spaceId).getHeight();

        float xSpaceScale = ViewPort.getWidth()/spaceWidth;
        float ySpaceScale = ViewPort.getHeight()/spaceHeight;

        DrawingHelper.drawImage(spaceId,ViewPort.getWidth()/2,ViewPort.getHeight()/2,0,xSpaceScale,ySpaceScale,255);
    }

}
