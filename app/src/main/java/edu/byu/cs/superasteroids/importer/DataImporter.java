package edu.byu.cs.superasteroids.importer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PointF;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import edu.byu.cs.superasteroids.database.GameDbOpenHelper;
import edu.byu.cs.superasteroids.database.Level_DAO;
import edu.byu.cs.superasteroids.database.Ship_DAO;
import edu.byu.cs.superasteroids.model.Level;
import edu.byu.cs.superasteroids.model.positioned_objects.BgObject;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.Asteroid;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.Cannon;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.Engine;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.ExtraPart;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.MainBody;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.PowerCore;

/**
 * Created by tyudy on 10/26/16.
 */

public class DataImporter implements IGameDataImporter {

    private Context context;
    private SQLiteDatabase db;

    // String literals for accessing different objects in the JSON file
    private static final String asteroidsGame = "asteroidsGame";
    private static final String objects = "objects";
    private static final String asteroids = "asteroids";
    private static final String levels = "levels";
    private static final String mainBodies = "mainBodies";
    private static final String cannons = "cannons";
    private static final String extraParts = "extraParts";
    private static final String engines = "engines";
    private static final String powerCores = "powerCores";

    private static final String levelObjects = "levelObjects";
    private static final String levelAsteroids = "levelAsteroids";



    public DataImporter(Context cntext){
        context = cntext;
    }

    /**
     * Imports the data from the .json file the given InputStreamReader is connected to. Imported data
     * should be stored in a SQLite database for use in the ship builder and the game.
     * @param dataInputReader The InputStreamReader connected to the .json file needing to be imported.
     * @return TRUE if the data was imported successfully, FALSE if the data was not imported due
     * to any error.
     */
    @Override
    public boolean importData(InputStreamReader dataInputReader) {


        //clear database with SQLiteOpenHelper function
            //should this be a static function in GameDbOpenHelper?
        //create database with SQLiteOpenHelper function
            //should this be a static function in GameDbOpenHelper?
        prepareForImport();


        try {
            JSONObject rootOBjeect = new JSONObject(makeString(dataInputReader));
            JSONObject asteroidsGameObject = rootOBjeect.getJSONObject(asteroidsGame);

            // Get all the arrays from the JSON file that are inside the asteroidsGame object
            JSONArray JSONobjects = asteroidsGameObject.getJSONArray(objects);
            JSONArray JSONasteroids = asteroidsGameObject.getJSONArray(asteroids);
            JSONArray JSONlevels = asteroidsGameObject.getJSONArray(levels);
            JSONArray JSONmainBodies = asteroidsGameObject.getJSONArray(mainBodies);
            JSONArray JSONcannons = asteroidsGameObject.getJSONArray(cannons);
            JSONArray JSONextraParts = asteroidsGameObject.getJSONArray(extraParts);
            JSONArray JSONengines = asteroidsGameObject.getJSONArray(engines);
            JSONArray JSONpowerCores = asteroidsGameObject.getJSONArray(powerCores);

            //if any of the imports fails return false
           if(!importBgObjectImages(JSONobjects)||
              !importAsteroids(JSONasteroids)||
              !importLevels(JSONlevels)||
              !importMainBodies(JSONmainBodies)||
              !importCannons(JSONcannons)||
              !importExtraParts(JSONextraParts)||
              !importEngines(JSONengines)||
              !importPowerCores(JSONpowerCores)){

               return false;
           }

            return true;


        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }


        return false;
    }

    // Create model objects from JSON arrays
    private boolean importBgObjectImages(JSONArray JSONObjects) throws JSONException{

        for(int i = 0; i < JSONObjects.length(); i++){
            String objectURL = JSONObjects.getString(i);
            BgObject backgroundObject = new BgObject(objectURL);
            if(!Level_DAO.SINGLETON.addBgObjectImages(backgroundObject)){
                return false;
            }
        }
        return true;
    }
    private boolean importAsteroids(JSONArray JSONAsteroids) throws JSONException{

        for(int i = 0; i < JSONAsteroids.length(); i++){
            JSONObject JsonAsteroid = JSONAsteroids.getJSONObject(i);
            Asteroid asteroid = new Asteroid(JsonAsteroid);
            if(!Level_DAO.SINGLETON.addAsteroid(asteroid)){
                return false;
            }
        }

        return true;
    }
    private boolean importLevels(JSONArray JSONLevels) throws JSONException{


        for(int i = 0; i < JSONLevels.length(); i++){
            JSONObject JsonLevel = JSONLevels.getJSONObject(i);
            JSONArray levelBgObjects = JsonLevel.getJSONArray(levelObjects);
            JSONArray levelRoids = JsonLevel.getJSONArray(levelAsteroids);

            //create list of BgObjects
            for(int j = 0; j < levelBgObjects.length(); j++){
                //create BgObject
                JSONObject JsonLevelBgObject = levelBgObjects.getJSONObject(j);
                String position = JsonLevelBgObject.getString("position");
                double scale = JsonLevelBgObject.getDouble("scale");
                int objectId = JsonLevelBgObject.getInt("objectId");
                int levelId = JsonLevel.getInt("number");
                //add it to the running list of BgObjects
                if(!Level_DAO.SINGLETON.addBgLevelObject(position, (float) scale, objectId, levelId)){
                    return false;
                }
            }
            //create list of Asteroids by just passing params to the DAO instead of Asteroid Object
            for(int j = 0; j < levelRoids.length(); j++){
                JSONObject JsonLevelAsteroid = levelRoids.getJSONObject(j);
                int asteroidNumber = JsonLevelAsteroid.getInt("number");
                int asteroidId = JsonLevelAsteroid.getInt("asteroidId");
                int levelId = JsonLevel.getInt("number");
                if(!Level_DAO.SINGLETON.addLevelAsteroid(asteroidNumber, asteroidId, levelId)){
                    return false;
                }

            }
            //create level using those parameters
            Level level = new Level(JsonLevel);
            if(!Level_DAO.SINGLETON.addLevel(level)){
                return false;
            }
        }
        return true;
    }
    private boolean importMainBodies(JSONArray JSONMainBodies) throws JSONException {

        for(int i = 0; i < JSONMainBodies.length(); i++){
            JSONObject JsonMainBody = JSONMainBodies.getJSONObject(i);
            MainBody mainBody = new MainBody(JsonMainBody);
            String cannonAttach = JsonMainBody.getString("cannonAttach");
            String engineAttach = JsonMainBody.getString("engineAttach");
            String extraAttach = JsonMainBody.getString("extraAttach");
            if(!Ship_DAO.SINGLETON.addMainBody(mainBody, cannonAttach, engineAttach, extraAttach)){
                return false;
            }
        }
        return true;
    }
    private boolean importCannons(JSONArray JSONCannons) throws JSONException{
        for(int i = 0; i < JSONCannons.length(); i++){
            JSONObject JsonCannon = JSONCannons.getJSONObject(i);
            Cannon cannon = new Cannon(JsonCannon);
            String attachPoint = JsonCannon.getString("attachPoint");
            String emitPoint = JsonCannon.getString("emitPoint");
            if(!Ship_DAO.SINGLETON.addCannon(cannon, attachPoint, emitPoint)){
                return false;
            }
        }
        return true;
    }
    private boolean importExtraParts(JSONArray JSONExtraParts) throws JSONException{
        for(int i = 0; i < JSONExtraParts.length(); i++){
            JSONObject JsonExtraPart = JSONExtraParts.getJSONObject(i);
            ExtraPart extraPart = new ExtraPart(JsonExtraPart);
            String attachPoint = JsonExtraPart.getString("attachPoint");
            if(!Ship_DAO.SINGLETON.addExtraPart(extraPart, attachPoint)){
                return false;
            }
        }
        return true;
    }
    private boolean importEngines(JSONArray JSONEngines) throws JSONException {
        for(int i = 0; i < JSONEngines.length(); i++){
            JSONObject JsonEngine = JSONEngines.getJSONObject(i);
            Engine engine = new Engine(JsonEngine);
            String attachPoint = JsonEngine.getString("attachPoint");
            if(!Ship_DAO.SINGLETON.addEngine(engine, attachPoint)){
                return false;
            }
        }
        return true;
    }
    private boolean importPowerCores(JSONArray JSONPowerCores) throws JSONException {
        for(int i = 0; i < JSONPowerCores.length(); i++){
            JSONObject JsonPowerCore = JSONPowerCores.getJSONObject(i);
            PowerCore powerCore = new PowerCore(JsonPowerCore);
            if(!Ship_DAO.SINGLETON.addPowerCore(powerCore)){
                return false;
            }
        }
        return true;
    }

    private String makeString(Reader reader) throws IOException {

        StringBuilder sb = new StringBuilder();
        char[] buf = new char[512];

        int n = 0;
        while ((n = reader.read(buf)) > 0) {
            sb.append(buf, 0, n);
        }

        return sb.toString();
    }
    private void prepareForImport(){
        GameDbOpenHelper openHelper = new GameDbOpenHelper(context);

        // Tables will be dropped and created becuase getWritableDatabase invokes
        // onCreate() in the GameDbOpenHelper which will do the dropping and creating
        db = openHelper.getWritableDatabase();
        openHelper.dropTables(db);
        openHelper.createTables(db);

        Ship_DAO.SINGLETON.setDb(db);
        Level_DAO.SINGLETON.setDb(db);

    }
    public SQLiteDatabase getDatabase(){
        if (db == null){
            GameDbOpenHelper openHelper = new GameDbOpenHelper(context);
            return openHelper.getReadableDatabase();
        } else {
            return db;
        }

    }

}
