
package edu.byu.cs.superasteroids.model.positioned_objects;

import android.graphics.PointF;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import edu.byu.cs.superasteroids.content.ContentManager;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;
import edu.byu.cs.superasteroids.game.ViewPort;

/**
 * Created by tylerku on 10/19/16.
 *
 * Object that makes up part of the background in a level
 */
public class BgObject extends PositionedObject {

    private float scale;
    private PointF position;

    /**
     * BgObject Constructor
     * @param URLimage - image for the object to be drawn
     */
    public BgObject(String URLimage, float sc, PointF pos){
        super(URLimage, 0, 0);
        scale = sc;
        position = pos;
    }

    /*
        Constructor used for importing background image URLs from the JsonObject
     */
    public BgObject(String URLimage){

        super(URLimage, 0, 0);

    }

    /**
     * Draw the backgorund image on screen
     */
    public void draw(){
        int objectId = ContentManager.getInstance().getImageId(this.getImageURL());
        PointF screenPos = ViewPort.convertToScreenCoordinates(position);
        DrawingHelper.drawImage(objectId, screenPos.x, screenPos.y, 0, scale, scale, 255);
    }


    // Getters and Setters ---------------------------------------------------------------


    public String getPositionAsString(){
        return position.toString();
    }
    public PointF getPosition(){ return position; }
    public float getScale(){
        return scale;
    }
    private PointF convertCoordinatesToPointF (String stringCoords){
        String[] coordinates = stringCoords.split(",");
        if(coordinates.length != 2){
            Log.e("Invalid Params", "invalid params from JSON object when creating MainBody Object");
        }
        Integer x = Integer.parseInt(coordinates[0]);
        Integer y = Integer.parseInt(coordinates[1]);

        return new PointF(x, y);
    }



}
