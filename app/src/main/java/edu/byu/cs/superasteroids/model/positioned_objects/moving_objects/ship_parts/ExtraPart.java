package edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts;

import android.graphics.PointF;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by tylerku on 10/19/16.
 *
 * Extra part that the ship holds
 */
public class ExtraPart {
    private PointF attachPoint;
    private String imageURL;
    private int imageWidth;
    private int imageHeight;

    /**
     * Constructor for the extra part of a ship
     *
     * @param atchPnt - point where the extra part attaches to the ship
     * @param URLimage - String holding URL to the image of the extra part
     * @param w - width of the extra part
     * @param h - height of the extra part
     */
    public ExtraPart(PointF atchPnt, String URLimage, int w, int h){
        attachPoint = atchPnt;
        imageURL = URLimage;
        imageWidth = w;
        imageHeight = h;
    }

    public ExtraPart(JSONObject JsonExtraPart) throws JSONException{
        String attachCoords = JsonExtraPart.getString("attachPoint");

        attachPoint = convertCoordinatesToPointF(attachCoords);
        imageURL = JsonExtraPart.getString("image");
        imageWidth = JsonExtraPart.getInt("imageWidth");
        imageHeight = JsonExtraPart.getInt("imageHeight");

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
    public PointF getAttachPoint() { return attachPoint; }
    public String getImageURL() { return imageURL; }
    public int getImageWidth() { return imageWidth; }
    public int getImageHeight() { return imageHeight; }
}
