package edu.byu.cs.superasteroids.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import edu.byu.cs.superasteroids.model.Level;
import edu.byu.cs.superasteroids.model.positioned_objects.BgObject;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.Asteroid;

/**
 * Created by tyudy on 10/27/16.
 */

public class Level_DAO {

    private static final String SELECT_ALL_ASTEROID_INFORMATION = "select * from asteroids";
    private static final String SELECT_ALL_LEVEL_INFORMATION = "select * from levels";
    private static final String SELECT_ALL_BG_OBJECT_IMAGE_INFORMATION = "select * from backgroundObjectImages";
    private static final String SELECT_ALL_BG_LEVEL_OBJECTS_WITH_LEVELID = "select * from levelBackgroundObjects where levelId=";
    private static final String SELECT_ALL_LEVEL_ASTEROIDS_WITH_LEVELID = "select * from levelAsteroids where levelId=";
    private static final String SELECT_BG_IMAGE_WITH_ID = "select * from backgroundObjectImages where id=";
    private static final String SELECT_ASTEROID_WITH_ID = "select * from asteroids where id=";
    private static final String[] EMPTY_ARRAY_OF_STRINGS = {};

    public static final Level_DAO SINGLETON = new Level_DAO();
    private SQLiteDatabase db;

    private Level_DAO(){ db = null; }

    public void setDb(SQLiteDatabase database){
        db = database;
    }
    public SQLiteDatabase getDb(){
        return db;
    }


    /* Add Functions -------------------------------
    ……(\_/)
    ……( ‘_’)
    …./”"”"”"”"”"”"\======░  o    o    o    o    o
    /”"”"”"”"”"”"”"”"”"\
    \_@_@_@_@_@_@_@_@_@_/
     Each will return true if successfully added false if not
    */
    public boolean addAsteroid(Asteroid asteroid){

        ContentValues values = new ContentValues();

        values.put("image", asteroid.getImageURL());
        values.put("imageWidth", asteroid.getImageWidth());
        values.put("imageHeight", asteroid.getImageHeight());
        values.put("name", asteroid.getName());
        //NOTE: We dont need to set the id becuase it is auto incramented

        return insertRowAndCheck(values, "asteroids");
    }
    public boolean addLevel(Level level){

        ContentValues values = new ContentValues();

        values.put("number", level.getNumber());
        values.put("title", level.getTitle());
        values.put("hint", level.getHint());
        values.put("width", level.getLevelWidth());
        values.put("height", level.getLevelHeight());
        values.put("music", level.getMusicURL());

        return insertRowAndCheck(values, "levels");
    }
    public boolean addBgObjectImages(BgObject bgObject){

        ContentValues values = new ContentValues();
        values.put("image", bgObject.getImageURL());

        return insertRowAndCheck(values, "backgroundObjectImages");
    }
    public boolean addBgLevelObject(String position, float scale, int objectId, int levelId){
        //create new instacne of ContentValues
        ContentValues values = new ContentValues();

        values.put("position", position);
        values.put("objectId", objectId);
        values.put("levelId", levelId);
        values.put("scale", scale);

        //inserts the row and returns true if success and false if failure
        return insertRowAndCheck(values, "levelBackgroundObjects");
    }
    public boolean addLevelAsteroid(int asteroidNumber, int asteroidId, int levelId){

        ContentValues values = new ContentValues();

        values.put("number", asteroidNumber);
        values.put("asteroidId", asteroidId);
        values.put("levelId", levelId);

        return insertRowAndCheck(values, "levelAsteroids");
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
    public ArrayList<Asteroid> getAsteroids(){

        ArrayList<Asteroid> resultAsteroids = new ArrayList<>();
        Cursor cursor = db.rawQuery(SELECT_ALL_ASTEROID_INFORMATION, EMPTY_ARRAY_OF_STRINGS);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){

                int id = cursor.getInt(0); // We don't use this but don't forget its here
                String image = cursor.getString(1);
                int imageWidth = cursor.getInt(2);
                int imageHeight = cursor.getInt(3);
                String name = cursor.getString(4);

                Asteroid asteroid = new Asteroid(image, imageHeight, imageWidth, name);
                resultAsteroids.add(asteroid);
                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }
        return resultAsteroids;
    }
    public ArrayList<Level> getLevels(){

        ArrayList<Level> resultLevels = new ArrayList<>();
        Cursor cursor = db.rawQuery(SELECT_ALL_LEVEL_INFORMATION, EMPTY_ARRAY_OF_STRINGS);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){

                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String hint = cursor.getString(2);
                int width = cursor.getInt(3);
                int height = cursor.getInt(4);
                String music = cursor.getString(5);

                Level level = new Level(height, width, id, hint, music, title);

                // Set this levels backgroundObjects
                for(BgObject object: getLevelBgObjects(id)){
                    level.addBgObject(object);
                }

                // Set this levels asteroids
                for(Asteroid asteroid: getLevelAsteroids(id)){
                    level.addAsteroid(asteroid);
                }

                resultLevels.add(level);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return resultLevels;
    }
    public ArrayList<String> getBgObjectImages(){

        ArrayList<String> resultImages = new ArrayList<>();
        Cursor cursor = db.rawQuery(SELECT_ALL_BG_OBJECT_IMAGE_INFORMATION, EMPTY_ARRAY_OF_STRINGS);

        try{

            int id = cursor.getInt(0); //not used but dont forget that it is here at the 0 index
            String image = cursor.getString(1);

            resultImages.add(image);
            cursor.moveToNext();

        } finally {
            cursor.close();
        }
        return resultImages;
    }
    // The following functions return a set of objects that belong to a specified level
    public ArrayList<BgObject> getLevelBgObjects(int level){ //use this level to get the objects that belong to a specific level

        ArrayList<BgObject> returnBgObjects = new ArrayList<>();
        String query = SELECT_ALL_BG_LEVEL_OBJECTS_WITH_LEVELID+level;//get the query for all the BGObjects in this level
        Cursor cursor = db.rawQuery(query, EMPTY_ARRAY_OF_STRINGS);

        try{

            cursor.moveToFirst();
            while(!cursor.isAfterLast()){

                PointF position =  convertCoordinatesToPointF(cursor.getString(0));
                int objectId = cursor.getInt(1);
                int levelId = cursor.getInt(2); // We dont use this but remember it's here
                float scale = (float) cursor.getDouble(3);

                //Get ImageURL for this BgObject
                String imageQuery = SELECT_BG_IMAGE_WITH_ID+objectId;
                Cursor imageCursor = db.rawQuery(imageQuery, EMPTY_ARRAY_OF_STRINGS);
                imageCursor.moveToFirst();
                String imageURL = imageCursor.getString(1);
                imageCursor.close();

                //Create the BgObject and add it to the list
                BgObject bgObject = new BgObject(imageURL, scale, position);
                returnBgObjects.add(bgObject);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return returnBgObjects;
    }
    public ArrayList<Asteroid> getLevelAsteroids(int level){
        ArrayList<Asteroid> returnAsteroids = new ArrayList<>();
        String levelAsteroidsQuery = SELECT_ALL_LEVEL_ASTEROIDS_WITH_LEVELID+level;
        Cursor cursor = db.rawQuery(levelAsteroidsQuery, EMPTY_ARRAY_OF_STRINGS);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){

                int number = cursor.getInt(0);
                int asteroidId = cursor.getInt(1);

                //Look up the desired asteroid in the database
                String asteroidQuery = SELECT_ASTEROID_WITH_ID+asteroidId;
                Cursor asteroidCursor = db.rawQuery(asteroidQuery, EMPTY_ARRAY_OF_STRINGS);
                asteroidCursor.moveToFirst();

                //Get asteroid fields
                int id = asteroidCursor.getInt(0); //Not used but good to remember it's here
                String image = asteroidCursor.getString(1);
                int imageWidth = asteroidCursor.getInt(2);
                int imageHeight = asteroidCursor.getInt(3);
                String name = asteroidCursor.getString(4);

                //Add asteroid to our list
                for(int i = 0; i < number; i++) {
                    Asteroid asteroid = new Asteroid(image, imageHeight, imageWidth, name);

                    returnAsteroids.add(asteroid);
                }
                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }

        return returnAsteroids;


    }

    /**
     * @param values - the values to be inserted into the row
     * @param tableName - the name of the table where the row will be inserted
     * @return - returns true if success or false if an error occurred
     */
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
