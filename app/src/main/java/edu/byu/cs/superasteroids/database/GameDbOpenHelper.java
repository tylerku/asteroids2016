package edu.byu.cs.superasteroids.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tyudy on 10/31/16.
 */

public class GameDbOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "game_database.sqlite";
    private static final int DB_VERSION = 1;

    public GameDbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    //WILL NOT BE USED
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void dropTables(SQLiteDatabase db){

        db.execSQL("DROP TABLE IF EXISTS backgroundObjectImages");
        db.execSQL("DROP TABLE IF EXISTS asteroids");
        db.execSQL("DROP TABLE IF EXISTS levels");
        db.execSQL("DROP TABLE IF EXISTS levelBackgroundObjects");
        db.execSQL("DROP TABLE IF EXISTS levelAsteroids");
        db.execSQL("DROP TABLE IF EXISTS mainBodies");
        db.execSQL("DROP TABLE IF EXISTS cannons");
        db.execSQL("DROP TABLE IF EXISTS extraParts");
        db.execSQL("DROP TABLE IF EXISTS engines");
        db.execSQL("DROP TABLE IF EXISTS powerCores");
    }

    public void createTables(SQLiteDatabase db){
        db.execSQL(CREATE_BGOBJECT_IMAGES_TABLE);
        db.execSQL(CREATE_ASTEROIDS_TABLE);
        db.execSQL(CREATE_LEVELS_TABLE);
        db.execSQL(CREATE_LEVEL_BGOBJECTS_TABLE);
        db.execSQL(CREATE_LEVEL_ASTEROIDS_TABLE);
        db.execSQL(CREATE_MAIN_BODIES_TABLE);
        db.execSQL(CREATE_CANNONS_TABLE);
        db.execSQL(CREATE_ENGINES_TABLE);
        db.execSQL(CREATE_POWER_CORES_TABLE);
        db.execSQL(CREATE_EXTRA_PARTS_TABLE);
    }

    private static final String CREATE_BGOBJECT_IMAGES_TABLE =
           " CREATE TABLE backgroundObjectImages" +
           " (" +
           "    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
           "    image TEXT NOT NULL" +
           " );";

    private static final String CREATE_ASTEROIDS_TABLE =
            " CREATE TABLE asteroids" +
            " (" +
            "    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "    image TEXT NOT NULL," +
            "    imageWidth INTEGER NOT NULL," +
            "    imageHeight INTEGER NOT NULL," +
            "    name TEXT NOT NULL" +
            " );";

    private static final String CREATE_LEVELS_TABLE =
            " CREATE TABLE levels" +
            " (" +
            "    number INTEGER NOT NULL PRIMARY KEY," +
            "    title TEXT NOT NULL," +
            "    hint TEXT NOT NULL," +
            "    width INTEGER NOT NULL," +
            "    height INTEGER NOT NULL," +
            "    music TEXT NOT NULL" +
            " );";

    private static final String CREATE_LEVEL_BGOBJECTS_TABLE =
            " CREATE TABLE levelBackgroundObjects" +
            " (" +
            "    position TEXT NOT NULL," +
            "    objectId INTEGER REFERENCES backgroundObjectImages(id)," +
            "    levelId INTEGER REFERENCES levels(number)," +
            "    scale REAL NOT NULL" +
            " );";

    private static final String CREATE_LEVEL_ASTEROIDS_TABLE =
            " CREATE TABLE levelAsteroids" +
            " (" +
            "    number INTEGER NOT NULL," +
            "    asteroidId INTEGER REFERENCES asteroids(id)," +
            "    levelId INTEGER REFERENCES levels(number)" +
            " );";

    private static final String CREATE_MAIN_BODIES_TABLE =
            " CREATE TABLE mainBodies" +
            " (" +
            "    cannonAttach TEXT NOT NULL," +
            "    engineAttach TEXT NOT NULL," +
            "    extraAttach TEXT NOT NULL," +
            "    image TEXT NOT NULL," +
            "    imageWidth INTEGER NOT NULL," +
            "    imageHeight INTEGER NOT NULL" +
            " );";

    private static final String CREATE_CANNONS_TABLE =
            " CREATE TABLE cannons" +
            " (" +
            "    attachPoint TEXT NOT NULL," +
            "    emitPoint TEXT NOT NULL," +
            "    image TEXT NOT NULL," +
            "    imageWidth INTEGER NOT NULL," +
            "    imageHeight INTEGER NOT NULL," +
            "    attackImage TEXT NOT NULL," +
            "    attackImageWidth INTEGER NOT NULL," +
            "    attackImageHeight INTEGER NOT NULL," +
            "    attackSound TEXT NOT NULL," +
            "    damage INTEGER NOT NULL" +
            " );";

    private static final String CREATE_ENGINES_TABLE =
            " CREATE TABLE engines" +
            " (" +
            "    baseSpeed INTEGER NOT NULL," +
            "    baseTurnRate INTEGER NOT NULL," +
            "    attachPoint TEXT NOT NULL," +
            "    image TEXT NOT NULL," +
            "    imageWidth INTEGER NOT NULL," +
            "    imageHeight INTEGER NOT NULL" +
            " );";

    private static final String CREATE_POWER_CORES_TABLE =
            " CREATE TABLE powerCores" +
            " (" +
            "    cannonBoost INTEGER NOT NULL," +
            "    engineBoost INTEGER NOT NULL," +
            "    image TEXT NOT NULL" +
            " );";

    private static final String CREATE_EXTRA_PARTS_TABLE =
            " CREATE TABLE extraParts" +
            " (" +
            "    attachPoint TEXT NOT NULL," +
            "    image TEXT NOT NULL," +
            "    imageWidth INTEGER NOT NULL," +
            "    imageHeight INTEGER NOT NULL" +
            " );";


}
