package edu.byu.cs.superasteroids.core;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import java.util.ArrayList;
import edu.byu.cs.superasteroids.database.GameDbOpenHelper;
import edu.byu.cs.superasteroids.database.Level_DAO;
import edu.byu.cs.superasteroids.model.Level;
import edu.byu.cs.superasteroids.model.positioned_objects.BgObject;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.Asteroid;

/**
 * Created by tyudy on 11/15/16.
 */

public class Level_DAOTests extends AndroidTestCase {
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

    public void testGetAsteroids(){

        Level_DAO testLevel_DAO = Level_DAO.SINGLETON;
        ArrayList<Asteroid> testAsteroids = testLevel_DAO.getAsteroids();
        Asteroid testAsteroid = testAsteroids.get(0);

        // check first asteroid for accuracy
        assertEquals("images/asteroids/asteroid.png", testAsteroid.getImageURL());
        assertEquals(169, testAsteroid.getImageWidth());
        assertEquals(153, testAsteroid.getImageHeight());
        assertEquals("regular", testAsteroid.getName());

        testAsteroid = testAsteroids.get(1);
        // check second asteroid for accuracy
        assertEquals("images/asteroids/blueasteroid.png", testAsteroid.getImageURL());
        assertEquals(161, testAsteroid.getImageWidth());
        assertEquals(178, testAsteroid.getImageHeight());
        assertEquals("growing", testAsteroid.getName());

        testAsteroid = testAsteroids.get(2);
        // check the third asteroid for accuracy
        assertEquals("images/asteroids/asteroid.png", testAsteroid.getImageURL());
        assertEquals(169, testAsteroid.getImageWidth());
        assertEquals(153, testAsteroid.getImageHeight());
        assertEquals("octeroid", testAsteroid.getName());
    }

    public void testGetLevels(){

        Level_DAO testLevel_DAO = Level_DAO.SINGLETON;
        ArrayList<Level> testLevels = testLevel_DAO.getLevels();
        Level testLevel = testLevels.get(0);

        // check first level for accuracy
        assertEquals(1, testLevel.getNumber());
        assertEquals("Level 1", testLevel.getTitle());
        assertEquals("Destroy 1 Asteroid", testLevel.getHint());
        assertEquals(3000, testLevel.getLevelWidth());
        assertEquals(3000, testLevel.getLevelHeight());
        assertEquals("sounds/SpyHunter.ogg", testLevel.getMusicURL());

        testLevel = testLevels.get(1);
        // check second level for accuracy
        assertEquals(2, testLevel.getNumber());
        assertEquals("Level 2", testLevel.getTitle());
        assertEquals("Destroy 5 Asteroids", testLevel.getHint());
        assertEquals(3000, testLevel.getLevelWidth());
        assertEquals(3000, testLevel.getLevelHeight());
        assertEquals("sounds/SpyHunter.ogg", testLevel.getMusicURL());

        testLevel = testLevels.get(2);
        // check third level for accuracy
        assertEquals(3, testLevel.getNumber());
        assertEquals("Level 3", testLevel.getTitle());
        assertEquals("Destroy 10 Asteroids", testLevel.getHint());
        assertEquals(4056, testLevel.getLevelWidth());
        assertEquals(4056, testLevel.getLevelHeight());
        assertEquals("sounds/SpyHunter.ogg", testLevel.getMusicURL());

        testLevel = testLevels.get(3);
        // check fourth level for accuracy
        assertEquals(4, testLevel.getNumber());
        assertEquals("Level 4", testLevel.getTitle());
        assertEquals("Destroy 20 Asteroids", testLevel.getHint());
        assertEquals(4056, testLevel.getLevelWidth());
        assertEquals(4056, testLevel.getLevelHeight());
        assertEquals("sounds/SpyHunter.ogg", testLevel.getMusicURL());

        testLevel = testLevels.get(4);
        // check fifth level for accuracy
        assertEquals(5, testLevel.getNumber());
        assertEquals("Level 5", testLevel.getTitle());
        assertEquals("Destroy 20 Asteroids", testLevel.getHint());
        assertEquals(3000, testLevel.getLevelWidth());
        assertEquals(3000, testLevel.getLevelHeight());
        assertEquals("sounds/SpyHunter.ogg", testLevel.getMusicURL());

    }

    public void testGetLevelBgObjects(){

        Level_DAO testLevel_DAO = Level_DAO.SINGLETON;
        ArrayList<BgObject> testBgObjects = new ArrayList<>();

        // initialize the backgournd objects to test...
        for(int i = 0; i < testLevel_DAO.getLevels().size(); i++){
            Level currentLevel = testLevel_DAO.getLevels().get(i);
            // remove duplicates then add the new BgObjects
            testBgObjects.removeAll(currentLevel.getLevelBackgroundObjects());
            testBgObjects.addAll(currentLevel.getLevelBackgroundObjects());
        }

        BgObject obj = testBgObjects.get(0);

        assertEquals("1000,1000", obj.getPositionAsString());
        assertEquals(1.5 ,obj.getScale());

    }

    public void testGetLevelAsteroids(){

        Level_DAO testLevel_DAO = Level_DAO.SINGLETON;
        ArrayList<Asteroid> testLevelAsteorids = testLevel_DAO.getLevelAsteroids(0);

        assertEquals(testLevelAsteorids.get(0).getName(), "regular");
        assertEquals(testLevelAsteorids.get(1).getName(), "regular");
        assertEquals(testLevelAsteorids.get(2).getName(), "regular");
        assertEquals(testLevelAsteorids.get(3).getName(), "regular");
        assertEquals(testLevelAsteorids.get(4).getName(), "growing");
        assertEquals(testLevelAsteorids.get(5).getName(), "growing");
        assertEquals(testLevelAsteorids.get(6).getName(), "growing");
        assertEquals(testLevelAsteorids.get(7).getName(), "growing");

        testLevelAsteorids = testLevel_DAO.getLevelAsteroids(1);
        assertEquals(testLevelAsteorids.get(0).getName(), "regular");
        assertEquals(testLevelAsteorids.get(1).getName(), "regular");
        assertEquals(testLevelAsteorids.get(2).getName(), "regular");
        assertEquals(testLevelAsteorids.get(3).getName(), "regular");
        assertEquals(testLevelAsteorids.get(4).getName(), "regular");

        testLevelAsteorids = testLevel_DAO.getLevelAsteroids(2);
        assertEquals(testLevelAsteorids.get(0).getName(), "regular");
        assertEquals(testLevelAsteorids.get(1).getName(), "regular");
        assertEquals(testLevelAsteorids.get(2).getName(), "regular");
        assertEquals(testLevelAsteorids.get(3).getName(), "regular");
        assertEquals(testLevelAsteorids.get(4).getName(), "regular");
        assertEquals(testLevelAsteorids.get(5).getName(), "regular");
        assertEquals(testLevelAsteorids.get(6).getName(), "regular");
        assertEquals(testLevelAsteorids.get(7).getName(), "regular");
        assertEquals(testLevelAsteorids.get(8).getName(), "regular");
        assertEquals(testLevelAsteorids.get(9).getName(), "regular");

        testLevelAsteorids = testLevel_DAO.getLevelAsteroids(3);
        assertEquals(testLevelAsteorids.get(0).getName(), "regular");
        assertEquals(testLevelAsteorids.get(1).getName(), "regular");
        assertEquals(testLevelAsteorids.get(2).getName(), "regular");
        assertEquals(testLevelAsteorids.get(3).getName(), "regular");
        assertEquals(testLevelAsteorids.get(4).getName(), "regular");
        assertEquals(testLevelAsteorids.get(5).getName(), "regular");
        assertEquals(testLevelAsteorids.get(6).getName(), "regular");
        assertEquals(testLevelAsteorids.get(7).getName(), "regular");
        assertEquals(testLevelAsteorids.get(8).getName(), "regular");
        assertEquals(testLevelAsteorids.get(9).getName(), "regular");
        assertEquals(testLevelAsteorids.get(10).getName(), "regular");
        assertEquals(testLevelAsteorids.get(11).getName(), "regular");
        assertEquals(testLevelAsteorids.get(12).getName(), "regular");
        assertEquals(testLevelAsteorids.get(13).getName(), "regular");
        assertEquals(testLevelAsteorids.get(14).getName(), "regular");
        assertEquals(testLevelAsteorids.get(15).getName(), "regular");
        assertEquals(testLevelAsteorids.get(16).getName(), "regular");
        assertEquals(testLevelAsteorids.get(17).getName(), "regular");
        assertEquals(testLevelAsteorids.get(18).getName(), "regular");
        assertEquals(testLevelAsteorids.get(19).getName(), "regular");

        testLevelAsteorids = testLevel_DAO.getLevelAsteroids(4);
        assertEquals(testLevelAsteorids.get(0).getName(), "regular");
        assertEquals(testLevelAsteorids.get(1).getName(), "regular");
        assertEquals(testLevelAsteorids.get(2).getName(), "regular");
        assertEquals(testLevelAsteorids.get(3).getName(), "regular");
        assertEquals(testLevelAsteorids.get(4).getName(), "regular");
        assertEquals(testLevelAsteorids.get(5).getName(), "regular");
        assertEquals(testLevelAsteorids.get(6).getName(), "regular");
        assertEquals(testLevelAsteorids.get(7).getName(), "regular");
        assertEquals(testLevelAsteorids.get(8).getName(), "regular");
        assertEquals(testLevelAsteorids.get(9).getName(), "regular");
        assertEquals(testLevelAsteorids.get(10).getName(), "regular");
        assertEquals(testLevelAsteorids.get(11).getName(), "regular");
        assertEquals(testLevelAsteorids.get(12).getName(), "regular");
        assertEquals(testLevelAsteorids.get(13).getName(), "regular");
        assertEquals(testLevelAsteorids.get(14).getName(), "regular");
        assertEquals(testLevelAsteorids.get(15).getName(), "regular");
        assertEquals(testLevelAsteorids.get(16).getName(), "regular");
        assertEquals(testLevelAsteorids.get(17).getName(), "regular");
        assertEquals(testLevelAsteorids.get(18).getName(), "regular");
        assertEquals(testLevelAsteorids.get(19).getName(), "regular");
    }


}
