package edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by tylerku on 10/19/16.
 *
 * Main power source of the ship
 */
public class PowerCore {

    private int cannonBoost;
    private int engineBoost;
    private String imageURL;


    /**
     * Constructor for the power core of a ship
     *
     * @param cnnBoost - amount of power boost the powerCore gives to the ship
     * @param engnBoost - amount of boost the powerCore gives to the engine
     * @param URLimage - URL that holds the power core image
     */
    public PowerCore(int cnnBoost, int engnBoost, String URLimage){
        cannonBoost = cnnBoost;
        engineBoost = engnBoost;
        imageURL = URLimage;
    }

    public PowerCore(JSONObject JsonPowerCore)throws JSONException{
        cannonBoost = JsonPowerCore.getInt("cannonBoost");
        engineBoost = JsonPowerCore.getInt("engineBoost");
        imageURL = JsonPowerCore.getString("image");
    }

    public int getCannonBoost() { return cannonBoost; }
    public int getEngineBoost() { return engineBoost; }
    public String getImageURL() { return imageURL; }
}
