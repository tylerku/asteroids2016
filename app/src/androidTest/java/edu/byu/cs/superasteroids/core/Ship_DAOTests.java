package edu.byu.cs.superasteroids.core;

import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PointF;
import android.test.AndroidTestCase;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.byu.cs.superasteroids.database.GameDbOpenHelper;
import edu.byu.cs.superasteroids.database.Ship_DAO;
import edu.byu.cs.superasteroids.importer.DataImporter;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.Cannon;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.Engine;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.ExtraPart;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.MainBody;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.PowerCore;

/**
 * Created by tyudy on 11/15/16.
 */

public class Ship_DAOTests extends AndroidTestCase {
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

    public void testGetMainBodies() {

        Ship_DAO testShip_DAO = Ship_DAO.SINGLETON;


        ArrayList<MainBody> testMainBodies = testShip_DAO.getMainBodies();
        MainBody testMainBody = testMainBodies.get(0);
        // check first mainBody for accuracy
        assertEquals("190,227", convertPoint(testMainBody.getCannonAttach()));
        assertEquals("102,392", convertPoint(testMainBody.getEngineAttach()));
        assertEquals("6,253", convertPoint(testMainBody.getExtraAttach()));
        assertEquals("images/parts/mainbody1.png", testMainBody.getImageURL());
        assertEquals(200, testMainBody.getImageWidth());
        assertEquals(400, testMainBody.getImageHeight());

        testMainBody = testMainBodies.get(1);
        // check second mainBody for accuracy
        assertEquals("143,323", convertPoint(testMainBody.getCannonAttach()));
        assertEquals("85,459", convertPoint(testMainBody.getEngineAttach()));
        assertEquals("26,323", convertPoint(testMainBody.getExtraAttach()));
        assertEquals("images/parts/mainbody2.png", testMainBody.getImageURL());
        assertEquals(156, testMainBody.getImageWidth());
        assertEquals(459, testMainBody.getImageHeight());

    }

    public void testGetCannons(){

        Ship_DAO testShip_DAO = Ship_DAO.SINGLETON;
        ArrayList<Cannon> testCannons = testShip_DAO.getCannons();
        Cannon testCannon = testCannons.get(0);
        // check first cannon for accuracy
        assertEquals("14,240", convertPoint(testCannon.getAttachPoint()));
        assertEquals("104,36", convertPoint(testCannon.getEmitPoint()));
        assertEquals("images/parts/cannon1.png", testCannon.getImageURL());
        assertEquals(160, testCannon.getImageWidth());
        assertEquals(360, testCannon.getImageHeight());
        assertEquals("images/parts/laser.png", testCannon.getAttackImageURL());
        assertEquals(50, testCannon.getAttackImageWidth());
        assertEquals(250, testCannon.getAttackImageWidth());
        assertEquals("sounds/laser.mp3", testCannon.getAttackSound());
        assertEquals(1, testCannon.getDamage());

        testCannon = testCannons.get(1);
        // check second cannon for accuracy
        assertEquals("19,137", convertPoint(testCannon.getAttachPoint()));
        assertEquals("184,21", convertPoint(testCannon.getEmitPoint()));
        assertEquals("images/parts/cannon2.png", testCannon.getImageURL());
        assertEquals(325, testCannon.getImageWidth());
        assertEquals(386, testCannon.getImageHeight());
        assertEquals("images/parts/laser2.png", testCannon.getAttackImageURL());
        assertEquals(105, testCannon.getAttackImageWidth());
        assertEquals(344, testCannon.getAttackImageWidth());
        assertEquals("sounds/laser.mp3", testCannon.getAttackSound());
        assertEquals(2, testCannon.getDamage());

    }

    public void testGetExtraParts(){
        Ship_DAO testShip_DAO = Ship_DAO.SINGLETON;
        ArrayList<ExtraPart> testExtraParts = testShip_DAO.getExtraParts();
        ExtraPart testExtraPart = testExtraParts.get(0);

        // check first extraPart for accuracy
        assertEquals("312,94", testExtraPart.getAttachPoint());
        assertEquals("images/parts/extrapart1.png", testExtraPart.getImageURL());
        assertEquals(320, testExtraPart.getImageWidth());
        assertEquals(240, testExtraPart.getImageHeight());

        testExtraPart = testExtraParts.get(1);
        // check second extraPart for accuracy
        assertEquals("310,124", testExtraPart.getAttachPoint());
        assertEquals("images/parts/extrapart2.png", testExtraPart.getImageURL());
        assertEquals(331, testExtraPart.getImageWidth());
        assertEquals(309, testExtraPart.getImageHeight());
    }

    public void testGetEngines(){
        Ship_DAO testShip_DAO = Ship_DAO.SINGLETON;
        ArrayList<Engine> testEngines = testShip_DAO.getEngines();
        Engine testEngine = testEngines.get(0);

        // check first engine for accuracy
        assertEquals(350, testEngine.getBaseSpeed());
        assertEquals(270, testEngine.getBaseTurnRate());
        assertEquals("106,6", convertPoint(testEngine.getAttachPoint()));
        assertEquals("images/parts/engine1.png", testEngine.getImageURL());
        assertEquals(220, testEngine.getImageWidth());
        assertEquals(160, testEngine.getImageHeight());

        testEngine = testEngines.get(1);
        // check second engine for accuracy
        assertEquals(500, testEngine.getBaseSpeed());
        assertEquals(360, testEngine.getBaseTurnRate());
        assertEquals("107,7", convertPoint(testEngine.getAttachPoint()));
        assertEquals("images/parts/engine2.png", testEngine.getImageURL());
        assertEquals(208, testEngine.getImageWidth());
        assertEquals(222, testEngine.getImageHeight());
    }

    public void testGetPowerCores(){
        Ship_DAO testShip_DAO = Ship_DAO.SINGLETON;
        ArrayList<PowerCore> testPowerCores = testShip_DAO.getPowerCores();
        PowerCore testPowerCore = testPowerCores.get(0);

        // check first powerCore for accuracy
        assertEquals(10, testPowerCore.getCannonBoost());
        assertEquals(10, testPowerCore.getEngineBoost());
        assertEquals("images/Ellipse.png", testPowerCore.getImageURL());

        // check second powerCore for accuracy
        assertEquals(10, testPowerCore.getCannonBoost());
        assertEquals(10, testPowerCore.getEngineBoost());
        assertEquals("images/Triangle.png", testPowerCore.getImageURL());
    }

    private String convertPoint(PointF p){
        String x = Float.toString(p.x);
        String y = Float.toString(p.y);
        return new String(x+","+y);
    }
}
