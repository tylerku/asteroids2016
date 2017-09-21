package edu.byu.cs.superasteroids.main_menu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import edu.byu.cs.superasteroids.base.IView;
import edu.byu.cs.superasteroids.database.GameDbOpenHelper;
import edu.byu.cs.superasteroids.database.Level_DAO;
import edu.byu.cs.superasteroids.database.Ship_DAO;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.Ship;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.Cannon;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.Engine;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.ExtraPart;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.MainBody;

/**
 * Created by tyudy on 11/9/16.
 */

public class MainMenuController implements IMainMenuController {

    SQLiteDatabase mDatabase;
    MainActivity mMainActivity;


    public MainMenuController(MainActivity mainActivity){
        mMainActivity = mainActivity;
        setDatabase(mMainActivity);
    }

    // Sync the database with the database that has been loaded.
    private void setDatabase(Context context){
        GameDbOpenHelper openHelper = new GameDbOpenHelper(context);
        mDatabase = openHelper.getReadableDatabase();
        Ship_DAO.SINGLETON.setDb(mDatabase);
        Level_DAO.SINGLETON.setDb(mDatabase);
    }
    /**
     * The MainActivity calls this function when the "quick play" button is pressed.
     */
    @Override
    public void onQuickPlayPressed() {
        int boringShipIndex = 0;
        int coolShipIndex = 1;

        constructShip(coolShipIndex);
        mMainActivity.startGame();
    }

    @Override
    public IView getView() {
        return null;
    }

    @Override
    public void setView(IView view) {

    }

    /**
      * creates a ship that is stored in the Ship.SINGLETON class
      * @param index - index of which ship parts are being requested to build the ship
     */
    private void constructShip(int index){
        //Get the images from the database
        try {
            MainBody mainBody = Ship_DAO.SINGLETON.getMainBodies().get(index);
            Cannon cannon = Ship_DAO.SINGLETON.getCannons().get(index);
            ExtraPart extraPart = Ship_DAO.SINGLETON.getExtraParts().get(index);
            Engine engine = Ship_DAO.SINGLETON.getEngines().get(index);

            Ship.SINGLETON.addMainBody(mainBody);
            Ship.SINGLETON.addCannon(cannon);
            Ship.SINGLETON.addExtraPart(extraPart);
            Ship.SINGLETON.addEngine(engine);

        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
            Log.d("MainMenuController", "index out of bounds in Construct ship function. Index probably isn't 0 or 1");
        }

    }
}
