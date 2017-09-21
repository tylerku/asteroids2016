package edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts;

import android.graphics.PointF;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tylerku on 10/19/16.
 *
 * Cannon that goes on a ship
 */
public class Cannon {

    private PointF attachPoint;
    private PointF emitPoint;
    private String imageURL;
    private int imageWidth;
    private int imageHeight;
    private String attackImageURL;
    private int attackImageWidth;
    private int attackImageHeight;
    private String attackSound;
    private int damage;


    /**
     * Constructor for the cannon of a ship
     *
     * @param atchPoint - point to attach cannon on the ship
     * @param URLimage - String holding url to the cannons image
     * @param iW - width of the image
     * @param iH - height of the image
     * @param atckImage - string to url of image of the cannon when it is attacking
     * @param atckIW - width of attack image
     * @param atckIH - height of attack image
     * @param atckSound - String to sound file that the cannon makes when it shoots
     * @param dmg - damage that the cannon does
     */
    public Cannon(PointF atchPoint, PointF ePoint, String URLimage, int iW, int iH, String atckImage, int atckIW, int atckIH, String atckSound, int dmg){
        attachPoint = atchPoint;
        emitPoint = ePoint;
        imageURL = URLimage;
        imageWidth = iW;
        imageHeight = iH;
        attackImageURL = atckImage;
        attackImageHeight = atckIH;
        attackImageWidth = atckIW;
        attackSound = atckSound;
        damage = dmg;
    }

    public Cannon(JSONObject JsonCannon) throws JSONException{
        String attachCoords = JsonCannon.getString("attachPoint");
        String emitCoords = JsonCannon.getString("emitPoint");

        attachPoint = convertCoordinatesToPointF(attachCoords);
        emitPoint = convertCoordinatesToPointF(emitCoords);
        imageURL = JsonCannon.getString("image");
        imageWidth = JsonCannon.getInt("imageWidth");
        imageHeight = JsonCannon.getInt("imageHeight");
        attackImageURL = JsonCannon.getString("attackImage");
        attackImageHeight = JsonCannon.getInt("attackImageHeight");
        attackImageWidth = JsonCannon.getInt("attackImageWidth");
        attackSound = JsonCannon.getString("attackSound");
        damage = JsonCannon.getInt("damage");

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
    public int getDamage() { return damage; }
    public PointF getAttachPoint() { return attachPoint; }
    public PointF getEmitPoint() { return emitPoint; }
    public String getImageURL() { return imageURL; }
    public int getImageWidth() { return imageWidth; }
    public int getImageHeight() { return imageHeight; }
    public String getAttackImageURL() { return attackImageURL; }
    public int getAttackImageWidth() { return attackImageWidth; }
    public int getAttackImageHeight() { return attackImageHeight; }
    public String getAttackSound() { return attackSound; }
}


