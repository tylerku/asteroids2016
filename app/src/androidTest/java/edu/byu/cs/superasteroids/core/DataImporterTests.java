package edu.byu.cs.superasteroids.core;

import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.io.InputStream;
import java.io.InputStreamReader;

import edu.byu.cs.superasteroids.database.GameDbOpenHelper;
import edu.byu.cs.superasteroids.importer.DataImporter;

/**
 * Created by tyudy on 11/15/16.
 */

public class DataImporterTests extends AndroidTestCase {
    SQLiteDatabase database;
    private static final String JSON_TESTFILE_NAME = "gamedata.json";

    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        {
            //initialize the database...
            GameDbOpenHelper openHelper = new GameDbOpenHelper(getContext());
            database = openHelper.getWritableDatabase();
        }
    }

    public void testImportDatabase() throws Exception {

        DataImporter testImporter = new DataImporter(getContext());

        AssetManager manager = getContext().getAssets();
        InputStream input = manager.open(JSON_TESTFILE_NAME);
        InputStreamReader reader = new InputStreamReader(input);

        // Attempt the import...
        assertTrue(testImporter.importData(reader));

        // Test for expected database content
        String[] EmptyArrayOfStrings = {};
        Cursor cursor = database.rawQuery("select * from engines", EmptyArrayOfStrings);
        try{
            cursor.moveToFirst();
            // check first engine for accuracy
            assertEquals(cursor.getInt(0), 350);
            assertEquals(cursor.getInt(1), 270);
            assertEquals(cursor.getString(2), "106,6");
            assertEquals(cursor.getString(3), "images/parts/engine1.png");
            assertEquals(cursor.getInt(4), 220);
            assertEquals(cursor.getInt(5), 160);

            cursor.moveToNext();
            // check second engine for accuracy
            assertEquals(cursor.getInt(0), 500);
            assertEquals(cursor.getInt(1), 360);
            assertEquals(cursor.getString(2), "107,7");
            assertEquals(cursor.getString(3), "images/parts/engine2.png");
            assertEquals(cursor.getInt(4), 208);
            assertEquals(cursor.getInt(5), 222);

        } finally {
            cursor.close();
        }

        cursor = database.rawQuery("select * from cannons", EmptyArrayOfStrings);
        try{
            cursor.moveToFirst();
            // check first cannon for accuracy
            assertEquals(cursor.getString(0), "14,240");
            assertEquals(cursor.getString(1), "104,36");
            assertEquals(cursor.getString(2), "images/parts/cannon1.png");
            assertEquals(cursor.getInt(3), 160);
            assertEquals(cursor.getInt(4), 360);
            assertEquals(cursor.getString(5), "images/parts/laser.png");
            assertEquals(cursor.getInt(6), 50);
            assertEquals(cursor.getInt(7), 250);
            assertEquals(cursor.getString(8), "sounds/laser.mp3");
            assertEquals(cursor.getInt(9), 1);

            cursor.moveToNext();
            // check second cannon for accuracy
            assertEquals(cursor.getString(0), "19,137");
            assertEquals(cursor.getString(1), "184,21");
            assertEquals(cursor.getString(2), "images/parts/cannon2.png");
            assertEquals(cursor.getInt(3), 325);
            assertEquals(cursor.getInt(4), 386);
            assertEquals(cursor.getString(5), "images/parts/laser2.png");
            assertEquals(cursor.getInt(6), 105);
            assertEquals(cursor.getInt(7), 344);
            assertEquals(cursor.getString(8), "sounds/laser.mp3");
            assertEquals(cursor.getInt(9), 2);

        } finally {
            cursor.close();
        }

        cursor = database.rawQuery("select * from mainBodies", EmptyArrayOfStrings);
        try{
            cursor.moveToFirst();
            // check first mainBody for accuracy
            assertEquals(cursor.getString(0), "190,227");
            assertEquals(cursor.getString(1), "102,392");
            assertEquals(cursor.getString(2), "6,253");
            assertEquals(cursor.getString(3), "images/parts/mainbody1.png");
            assertEquals(cursor.getInt(4), 200);
            assertEquals(cursor.getInt(5), 400);

            cursor.moveToNext();
            // check second mainBody for accuracy
            assertEquals(cursor.getString(0), "143,323");
            assertEquals(cursor.getString(1), "85,459");
            assertEquals(cursor.getString(2), "26,323");
            assertEquals(cursor.getString(3), "images/parts/mainbody2.png");
            assertEquals(cursor.getInt(4), 156);
            assertEquals(cursor.getInt(5), 459);
        } finally {
            cursor.close();
        }

        cursor = database.rawQuery("select * from powerCores", EmptyArrayOfStrings);
        try{
            cursor.moveToFirst();
            // check first powerCore for accuracy
            assertEquals(cursor.getInt(0), 10);
            assertEquals(cursor.getInt(1), 10);
            assertEquals(cursor.getString(2), "images/Ellipse.png");

            cursor.moveToNext();
            // check second powerCore for accuracy
            assertEquals(cursor.getInt(0), 10);
            assertEquals(cursor.getInt(1), 10);
            assertEquals(cursor.getString(2), "images/Triangle.png");
        } finally {
            cursor.close();
        }

        cursor = database.rawQuery("select * from extraParts", EmptyArrayOfStrings);
        try{
            cursor.moveToFirst();
            // check first extraPart for accuracy
            assertEquals(cursor.getString(0), "312,94");
            assertEquals(cursor.getString(1), "images/parts/extrapart1.png");
            assertEquals(cursor.getInt(2), 320);
            assertEquals(cursor.getInt(3), 240);

            cursor.moveToNext();
            // check second extraPart for accuracy
            assertEquals(cursor.getString(0), "310,124");
            assertEquals(cursor.getString(1), "images/parts/extrapart2.png");
            assertEquals(cursor.getInt(2), 331);
            assertEquals(cursor.getInt(3), 309);

        } finally {
            cursor.close();
        }

        //Levels
        //Asteroids
        //BgObjectImages
        cursor = database.rawQuery("select * from asteroids", EmptyArrayOfStrings);
        try{
            cursor.moveToFirst();
            // check first asteroid for accuracy
            int id = cursor.getInt(0);
            assertEquals(cursor.getString(1), "images/asteroids/asteroid.png");
            assertEquals(cursor.getInt(2), 169);
            assertEquals(cursor.getInt(3), 153);
            assertEquals(cursor.getString(4), "regular");

            cursor.moveToNext();
            // check second asteroid for accuracy
            id = cursor.getInt(0);
            assertEquals(cursor.getString(1), "images/asteroids/blueasteroid.png");
            assertEquals(cursor.getInt(2), 161);
            assertEquals(cursor.getInt(3), 178);
            assertEquals(cursor.getString(4), "growing");

            cursor.moveToNext();
            // check third asteroid for accuracy
            id = cursor.getInt(0);
            assertEquals(cursor.getString(1), "images/asteroids/asteroid.png");
            assertEquals(cursor.getInt(2), 169);
            assertEquals(cursor.getInt(3), 153);
            assertEquals(cursor.getString(4), "octeroid");
        } finally {
            cursor.close();
        }

        cursor = database.rawQuery("select * from levels", EmptyArrayOfStrings);
        try{
            cursor.moveToFirst();
            // check first level for accuracy
            int number = cursor.getInt(0);
            assertEquals(cursor.getString(1), "Level 1");
            assertEquals(cursor.getString(2), "Destroy 1 Asteroid");
            assertEquals(cursor.getInt(3), 3000);
            assertEquals(cursor.getInt(4), 3000);
            assertEquals(cursor.getString(5), "sounds/SpyHunter.ogg");

            cursor.moveToNext();
            // check second level for accuracy
            number = cursor.getInt(0);
            assertEquals(cursor.getString(1), "Level 2");
            assertEquals(cursor.getString(2), "Destroy 5 Asteroids");
            assertEquals(cursor.getInt(3), 3000);
            assertEquals(cursor.getInt(4), 3000);
            assertEquals(cursor.getString(5), "sounds/SpyHunter.ogg");

            cursor.moveToNext();
            // check third level for accuracy
            number = cursor.getInt(0);
            assertEquals(cursor.getString(1), "Level 3");
            assertEquals(cursor.getString(2), "Destroy 10 Asteroids");
            assertEquals(cursor.getInt(3), 4056);
            assertEquals(cursor.getInt(4), 4056);
            assertEquals(cursor.getString(5), "sounds/SpyHunter.ogg");

            cursor.moveToNext();
            // check fourth level for accuracy
            number = cursor.getInt(0);
            assertEquals(cursor.getString(1), "Level 4");
            assertEquals(cursor.getString(2), "Destroy 20 Asteroids");
            assertEquals(cursor.getInt(3), 4056);
            assertEquals(cursor.getInt(4), 4056);
            assertEquals(cursor.getString(5), "sounds/SpyHunter.ogg");

            cursor.moveToNext();
            // check fifth level for accuracy
            number = cursor.getInt(0);
            assertEquals(cursor.getString(1), "Level 5");
            assertEquals(cursor.getString(2), "Destroy 20 Asteroids");
            assertEquals(cursor.getInt(3), 3000);
            assertEquals(cursor.getInt(4), 3000);
            assertEquals(cursor.getString(5), "sounds/SpyHunter.ogg");
        } finally {
            cursor.close();
        }

        cursor = database.rawQuery("select * from backgroundObjectImages", EmptyArrayOfStrings);
        try{
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            assertEquals(cursor.getString(1), "images/planet1.png");

            cursor.moveToNext();
            id = cursor.getInt(0);
            assertEquals(cursor.getString(1), "images/planet2.png");

            cursor.moveToNext();
            id = cursor.getInt(0);
            assertEquals(cursor.getString(1), "images/planet3.png");

            cursor.moveToNext();
            id = cursor.getInt(0);
            assertEquals(cursor.getString(1), "images/planet4.png");

            cursor.moveToNext();
            id = cursor.getInt(0);
            assertEquals(cursor.getString(1), "images/planet5.png");

            cursor.moveToNext();
            id = cursor.getInt(0);
            assertEquals(cursor.getString(1), "images/planet6.png");

            cursor.moveToNext();
            id = cursor.getInt(0);
            assertEquals(cursor.getString(1), "images/station.png");

            cursor.moveToNext();
            id = cursor.getInt(0);
            assertEquals(cursor.getString(1), "images/nebula1.png");

            cursor.moveToNext();
            id = cursor.getInt(0);
            assertEquals(cursor.getString(1), "images/nebula2.png");

            cursor.moveToNext();
            id = cursor.getInt(0);
            assertEquals(cursor.getString(1), "images/nebula3.png");

            cursor.moveToNext();
            id = cursor.getInt(0);
            assertEquals(cursor.getString(1), "images/nebula4.png");

            cursor.moveToNext();
            id = cursor.getInt(0);
            assertEquals(cursor.getString(1), "images/nebula5.png");
        } finally {
            cursor.close();
        }
    }

}
