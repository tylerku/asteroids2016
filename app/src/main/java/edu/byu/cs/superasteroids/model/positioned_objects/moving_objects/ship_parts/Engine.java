package edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts;

import android.graphics.PointF;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by tylerku on 10/19/16.
 *
 * Engine to be used in a ship
 */
public class Engine {

    private int baseSpeed;
    private int baseTurnRate;
    private PointF attachPoint;

    private String imageURL;
    private int imageHeight;
    private int imageWidth;


    /**
     * Constructor for the engine of a ship
     *
     * @param bSpeed - base speed of the engine
     * @param bTurnRate - base turn rate of the engine
     * @param atchPoint - point to attach the engine to the ship
     * @param URLimage - string holding a url of the image to use
     * @param h - width of the iamge
     * @param w - height of the image
     */
    public Engine(int bSpeed, int bTurnRate, PointF atchPoint, String URLimage, int h, int w ){
        baseSpeed = bSpeed;
        baseTurnRate = bTurnRate;
        attachPoint = atchPoint;
        imageURL = URLimage;
        imageHeight = h;
        imageWidth = w;
    }

    public Engine(JSONObject JsonEngine) throws JSONException{
        String attachCoords = JsonEngine.getString("attachPoint");

        baseSpeed = JsonEngine.getInt("baseSpeed");
        baseTurnRate = JsonEngine.getInt("baseTurnRate");
        attachPoint = convertCoordinatesToPointF(attachCoords);
        imageURL = JsonEngine.getString("image");
        imageHeight = JsonEngine.getInt("imageHeight");
        imageWidth = JsonEngine.getInt("imageWidth");

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

    //Getters
    public int getBaseSpeed() { return baseSpeed; }
    public int getBaseTurnRate() { return baseTurnRate; }
    public PointF getAttachPoint() { return attachPoint; }
    public String getImageURL() { return imageURL; }
    public int getImageHeight() { return imageHeight; }
    public int getImageWidth() { return imageWidth; }
}
