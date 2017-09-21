package edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts;

import android.graphics.PointF;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tylerku on 10/19/16.
 *
 * The Main body of a ship
 */
public class MainBody {

    PointF cannonAttach;
    PointF engineAttach;
    PointF extraAttach;

    private String imageURL;
    private int imageHeight;
    private int imageWidth;

    /**
     * Main Body Constructor
     *
     * @param cannonPoint - location to put cannon on the ship
     * @param enginePoint - location to put engine on the ship.
     * @param extraPoint - location to put the extraPart on the ship.
     * @param URLimage - String holding the location of the image used to represent the body
     * @param h - height of URLimage
     * @param w - width of URLimage
     */

    public MainBody(PointF cannonPoint, PointF enginePoint, PointF extraPoint, String URLimage, int h, int w){

        cannonAttach = cannonPoint;
        engineAttach = enginePoint;
        extraAttach = extraPoint;
        imageURL = URLimage;
        imageHeight = h;
        imageWidth = w;
    }

    public MainBody(JSONObject JsonMainBody) throws JSONException {
        String cannonAttachCoords = JsonMainBody.getString("cannonAttach");
        String engineAttachCoords = JsonMainBody.getString("engineAttach");
        String extraAttachCoords = JsonMainBody.getString("extraAttach");

        cannonAttach = convertCoordinatesToPointF(cannonAttachCoords);
        engineAttach = convertCoordinatesToPointF(engineAttachCoords);
        extraAttach = convertCoordinatesToPointF(extraAttachCoords);
        imageURL = JsonMainBody.getString("image");
        imageHeight = JsonMainBody.getInt("imageHeight");
        imageWidth = JsonMainBody.getInt("imageWidth");

    }

    public String getImageURL(){ return imageURL; }
    public int getImageHeight(){ return imageHeight; }
    public int getImageWidth(){ return imageWidth; }
    public PointF getCannonAttach(){ return cannonAttach; }
    public PointF getEngineAttach(){ return engineAttach; }
    public PointF getExtraAttach(){ return extraAttach; }



    /**
     *
     * @param stringCoords - coordinates in the form of (ex. "199,200")
     * @return
     */
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
