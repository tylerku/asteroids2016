package edu.byu.cs.superasteroids.database;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.Cannon;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.Engine;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.ExtraPart;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.MainBody;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.PowerCore;

/**
 * Created by tyudy on 10/27/16.
 */

public class Ship_DAO {

    //String constants
    private static final String SELECT_ALL_MAINBODY_INFORMATION = "select * from mainBodies";
    private static final String SELECT_ALL_CANNON_INFORMATION = "select * from cannons";
    private static final String SELECT_ALL_EXTRAPARTS_INFORMATION = "select * from extraParts";
    private static final String SELECT_ALL_ENGINE_INFORMATION = "select * from engines";
    private static final String SELECT_ALL_POWERCORE_INFORMATION = "select * from powerCores";
    private static final String[] EMPTY_ARRAY_OF_STRINGS = {};

    public static final Ship_DAO SINGLETON = new Ship_DAO();
    private SQLiteDatabase db;

    private Ship_DAO(){ db = null; }

    public void setDb(SQLiteDatabase database){
        db = database;
    }
    public SQLiteDatabase getDb(){ return db; }


    /* Add Functions -------------------------------
    ……(\_/)
    ……( ‘_’)
    …./”"”"”"”"”"”"\======░  o    o    o    o    o
    /”"”"”"”"”"”"”"”"”"”"\
    \_@_@_@_@_@_@_@_@_@_@_/
     Each will return true if successfully added false if not
    */
    public boolean addMainBody(MainBody mainBody, String cannonAttach, String engineAttach, String extraAttach){

        ContentValues values = new ContentValues();

        values.put("cannonAttach", cannonAttach);
        values.put("engineAttach", engineAttach);
        values.put("extraAttach", extraAttach);
        values.put("image", mainBody.getImageURL());
        values.put("imageWidth", mainBody.getImageWidth());
        values.put("imageHeight", mainBody.getImageHeight());

        return insertRowAndCheck(values, "mainBodies");
    }
    public boolean addCannon(Cannon cannon, String attachPoint, String emitPoint){

        ContentValues values = new ContentValues();

        values.put("attachPoint", attachPoint);
        values.put("emitPoint", emitPoint);
        values.put("image", cannon.getImageURL());
        values.put("imageWidth", cannon.getImageWidth());
        values.put("imageHeight", cannon.getImageHeight());
        values.put("attackImage", cannon.getAttackImageURL());
        values.put("attackImageWidth", cannon.getAttackImageWidth());
        values.put("attackImageHeight", cannon.getAttackImageHeight());
        values.put("attackSound", cannon.getAttackSound());
        values.put("damage", cannon.getDamage());

        return insertRowAndCheck(values, "cannons");
    }
    public boolean addExtraPart(ExtraPart extraPart, String attachPoint){

        ContentValues values = new ContentValues();

        values.put("attachPoint", attachPoint);
        values.put("image", extraPart.getImageURL());
        values.put("imageWidth", extraPart.getImageWidth());
        values.put("imageHeight", extraPart.getImageHeight());

        return insertRowAndCheck(values, "extraParts");

    }
    public boolean addEngine(Engine engine, String attachPoint){

        ContentValues values = new ContentValues();

        values.put("baseSpeed", engine.getBaseSpeed());
        values.put("baseTurnRate", engine.getBaseTurnRate());
        values.put("attachPoint", attachPoint);
        values.put("image", engine.getImageURL());
        values.put("imageWidth", engine.getImageWidth());
        values.put("imageHeight", engine.getImageHeight());

        return insertRowAndCheck(values, "engines");
    }
    public boolean addPowerCore(PowerCore powerCore){

        ContentValues values = new ContentValues();

        values.put("cannonBoost", powerCore.getCannonBoost());
        values.put("engineBoost", powerCore.getEngineBoost());
        values.put("image", powerCore.getImageURL());

        return insertRowAndCheck(values, "powerCores");
    }

    /* Fetch Functions -------------------------------

         __.--**"""**--...__..--**""""*-.
    .'                                `-.
  .'                         _           \
 /                         .'        .    \   _._
:                         :          :`*.  :-'.' ;
;    `                    ;          `.) \   /.-'
:     `                             ; ' -*   ;
       :.    \           :       :  :        :
 ;     ; `.   `.         ;     ` |  '
 |         `.            `. -*"*\; /        :
 |    :     /`-.           `.    \/`.'  _    `.
 :    ;    :    `*-.__.-*""":`.   \ ;  'o` `. /
       ;   ;                ;  \   ;:       ;:   ,/
  |  | |                       /`  | ,      `*-*'/
  `  : :  :                /  /    | : .    ._.-'
   \  \ ,  \              :   `.   :  \ \   .'
    :  *:   ;             :    |`*-'   `*+-*
    `**-*`""               *---*
      Each will retrieve all objects in the database of the specified type
     */

    public ArrayList<MainBody> getMainBodies(){

        ArrayList<MainBody> resultMainBodies = new ArrayList<>();
        Cursor cursor = db.rawQuery(SELECT_ALL_MAINBODY_INFORMATION, EMPTY_ARRAY_OF_STRINGS);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {

                String strCannonAttach = cursor.getString(0);
                String strEngineAttach = cursor.getString(1);
                String strExtraAttach = cursor.getString(2);
                String image = cursor.getString(3);
                int imageWidth = cursor.getInt(4);
                int imageHeight = cursor.getInt(5);

                //convert the string coordinates to PointF types taken by MainBody()
                PointF cannonAttach = convertCoordinatesToPointF(strCannonAttach);
                PointF engineAttach = convertCoordinatesToPointF(strEngineAttach);
                PointF extraAttach = convertCoordinatesToPointF(strExtraAttach);

                MainBody mainBody = new MainBody(cannonAttach, engineAttach, extraAttach, image, imageHeight, imageWidth);
                resultMainBodies.add(mainBody);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return resultMainBodies;
    }
    public ArrayList<Cannon> getCannons(){

        ArrayList<Cannon> resultCannons = new ArrayList<>();
        Cursor cursor = db.rawQuery(SELECT_ALL_CANNON_INFORMATION, EMPTY_ARRAY_OF_STRINGS);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){

                String strAttachPoint = cursor.getString(0);
                String strEmitPoint = cursor.getString(1);
                String image = cursor.getString(2);
                int imageWidth = cursor.getInt(3);
                int imageHeight = cursor.getInt(4);
                String attackImage = cursor.getString(5);
                int attackImageWidth = cursor.getInt(6);
                int attackImageHeight = cursor.getInt(7);
                String attackSound = cursor.getString(8);
                int damage = cursor.getInt(9);

                //convert the string coordinates to PointF types taken by Cannon()
                PointF attachPoint = convertCoordinatesToPointF(strAttachPoint);
                PointF emitPoint = convertCoordinatesToPointF(strEmitPoint);

                Cannon cannon = new Cannon(attachPoint, emitPoint, image, imageWidth, imageHeight,
                                           attackImage, attackImageWidth, attackImageHeight, attackSound, damage );
                resultCannons.add(cannon);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return resultCannons;
    }
    public ArrayList<ExtraPart> getExtraParts(){

        ArrayList<ExtraPart> resultExtraParts = new ArrayList<>();
        Cursor cursor = db.rawQuery(SELECT_ALL_EXTRAPARTS_INFORMATION, EMPTY_ARRAY_OF_STRINGS);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){

                String strAttachPoint = cursor.getString(0);
                String image = cursor.getString(1);
                int imageWidth = cursor.getInt(2);
                int imageHeight = cursor.getInt(3);

                //convert the string coordinates to PointF types taken by Cannon()
                PointF attachPoint = convertCoordinatesToPointF(strAttachPoint);
                ExtraPart extraPart = new ExtraPart(attachPoint, image, imageWidth, imageHeight);
                resultExtraParts.add(extraPart);
                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }
        return resultExtraParts;
    }
    public ArrayList<Engine> getEngines(){

        ArrayList<Engine> resultEngines = new ArrayList<>();
        Cursor cursor = db.rawQuery(SELECT_ALL_ENGINE_INFORMATION, EMPTY_ARRAY_OF_STRINGS);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){

                int baseSpeed = cursor.getInt(0);
                int baseTurnRate = cursor.getInt(1);
                String strAttachPoint = cursor.getString(2);
                String image = cursor.getString(3);
                int imageWidth = cursor.getInt(4);
                int imageHeight = cursor.getInt(5);

                //convert the string coordinates to PointF types taken by MainBody()
                PointF attachPoint = convertCoordinatesToPointF(strAttachPoint);

                //    public Engine(int bSpeed, int bTurnRate, PointF atchPoint, String URLimage, int h, int w ){

                Engine engine = new Engine(baseSpeed, baseTurnRate, attachPoint, image, imageHeight, imageWidth);
                resultEngines.add(engine);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return resultEngines;
    }
    public ArrayList<PowerCore> getPowerCores(){

        ArrayList<PowerCore> resultPowerCores = new ArrayList<>();
        Cursor cursor = db.rawQuery(SELECT_ALL_POWERCORE_INFORMATION, EMPTY_ARRAY_OF_STRINGS);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){

                int cannonBoost = cursor.getInt(0);
                int engineBoost = cursor.getInt(1);
                String image = cursor.getString(2);

                PowerCore powerCore = new PowerCore(cannonBoost, engineBoost, image);
                resultPowerCores.add(powerCore);
                cursor.moveToNext();
            }
        }finally{
            cursor.close();
        }
        return resultPowerCores;
    }

    //Helper Functions
    private boolean insertRowAndCheck(ContentValues values, String tableName ){
        long id = db.insert(tableName, null, values);

        boolean result = false;
        if(id >= 0){
            result = true;
        }
        return result;
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
