package edu.byu.cs.superasteroids.ship_builder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.Display;
import android.widget.Toast;

import java.util.ArrayList;

import edu.byu.cs.superasteroids.base.IView;
import edu.byu.cs.superasteroids.content.ContentManager;
import edu.byu.cs.superasteroids.database.GameDbOpenHelper;
import edu.byu.cs.superasteroids.database.Level_DAO;
import edu.byu.cs.superasteroids.database.Ship_DAO;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.Ship;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.Cannon;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.Engine;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.ExtraPart;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.MainBody;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.PowerCore;


/**
 * Created by tyudy on 11/5/16.
 *
 *
 *
 */

/*
             ,-"     "-.              ______________
            / o       o \            /              |
           /   \     /   \        __/               |
          /     )-"-(     \      / BUILD MY SHIP!   |
         /     ( 6 6 )     \    /___________________|
        /       \ " /       \
       /         )=(         \
      /   o   .--"-"--.   o   \
     /    I  /  -   -  \  I    \
 .--(    (_}y/\       /\y{_)    )--.
(    ".___l\/__\_____/__\/l___,"    )
 \                                 /
  "-._      o O o O o O o      _,-"
      `--Y--.___________.--Y--'
         |==.___________.==| hjw
         `==.___________.==' `97

 */

public class ShipBuildingController implements IShipBuildingController {

    ShipBuildingActivity shipBuildingActivity;
    SQLiteDatabase database;

    ArrayList<Cannon> cannons;
    ArrayList<Engine> engines;
    ArrayList<ExtraPart> extraParts;
    ArrayList<PowerCore> powerCores;
    ArrayList<MainBody> mainBodies;
    IShipBuildingView.PartSelectionView currentView;
    float rotationalDegrees;


    public ShipBuildingController(ShipBuildingActivity sba){
        shipBuildingActivity = sba;
        setDatabase(shipBuildingActivity);

        cannons = new ArrayList<>();
        engines = new ArrayList<>();
        extraParts = new ArrayList<>();
        powerCores = new ArrayList<>();
        mainBodies = new ArrayList<>();

        currentView = IShipBuildingView.PartSelectionView.MAIN_BODY;
        rotationalDegrees = 0;


    }

    // Initialize the database from the existing activity
    private void setDatabase(Context context){
        GameDbOpenHelper openHelper = new GameDbOpenHelper(context);
        database = openHelper.getReadableDatabase();
        Ship_DAO.SINGLETON.setDb(database);
        Level_DAO.SINGLETON.setDb(database);
    }

    /**
     * The ship building view calls this function when a part selection view is loaded. This function
     * should be used to configure the part selection view. Example: Set the arrows for the view in
     * this function.
     * @param partView
     */
    @Override
    public void onViewLoaded(IShipBuildingView.PartSelectionView partView) {

        IShipBuildingView.PartSelectionView previousView = getPreviousPartView();
        IShipBuildingView.PartSelectionView nextView = getNextPartView();

        shipBuildingActivity.setArrow(currentView, IShipBuildingView.ViewDirection.LEFT, true, nextView.toString());
        shipBuildingActivity.setArrow(currentView, IShipBuildingView.ViewDirection.RIGHT, true, previousView.toString());
        shipBuildingActivity.setArrow(currentView, IShipBuildingView.ViewDirection.UP, false, "");
        shipBuildingActivity.setArrow(currentView, IShipBuildingView.ViewDirection.DOWN, false, "");

    }

    /**
     * Updates the game delegate. The game engine will call this function 60 times a second
     * once it enters the game loop.
     * @param elapsedTime Time since the last update. For this game, elapsedTime is always
     *                    1/60th of second
     */
    @Override
    public void update(double elapsedTime) {
        //not important
    }

    /**
     * Loads content such as image and sounds files and other data into the game. The ShipBuildingView
     * calls this function as it is created. Load ship building content in this function.
     * @param content An instance of the content manager. This should be used to load images and sound.
     */
    @Override
    public void loadContent(ContentManager content) {


        //I don't think I actually need these
        cannons = Ship_DAO.SINGLETON.getCannons();
        engines = Ship_DAO.SINGLETON.getEngines();
        powerCores = Ship_DAO.SINGLETON.getPowerCores();
        extraParts = Ship_DAO.SINGLETON.getExtraParts();
        mainBodies = Ship_DAO.SINGLETON.getMainBodies();


        ArrayList<Integer> partIds = new ArrayList();

        //Add all images to the imagesForContent ArrayList
        for(int i = 0; i < mainBodies.size(); i++){
            String mainBodyImage = mainBodies.get(i).getImageURL();
            Integer id = content.loadImage(mainBodyImage);
            partIds.add(id);
        }

        shipBuildingActivity.setPartViewImageList(IShipBuildingView.PartSelectionView.MAIN_BODY, partIds);
        partIds.clear();

        for(int i = 0; i < engines.size(); i++){
            String engineImage = engines.get(i).getImageURL();
            Integer id = content.loadImage(engineImage);
            partIds.add(id);
        }

        shipBuildingActivity.setPartViewImageList(IShipBuildingView.PartSelectionView.ENGINE, partIds);
        partIds.clear();

        for(int i = 0; i < cannons.size(); i++){
            String cannonImage = cannons.get(i).getImageURL();
            Integer id = content.loadImage(cannonImage);
            partIds.add(id);
        }

        shipBuildingActivity.setPartViewImageList(IShipBuildingView.PartSelectionView.CANNON, partIds);
        partIds.clear();

        for(int i = 0; i < extraParts.size(); i++){
            String extraPartImage = extraParts.get(i).getImageURL();
            Integer id = content.loadImage(extraPartImage);
            partIds.add(id);
        }

        shipBuildingActivity.setPartViewImageList(IShipBuildingView.PartSelectionView.EXTRA_PART, partIds);
        partIds.clear();

        for(int i = 0; i < powerCores.size(); i++){
            String powerCoreImage = powerCores.get(i).getImageURL();
            Integer id = content.loadImage(powerCoreImage);
            partIds.add(id);
        }

        shipBuildingActivity.setPartViewImageList(IShipBuildingView.PartSelectionView.POWER_CORE, partIds);
        partIds.clear();

        activateStartButtonIfReady();
        if (Ship.SINGLETON.getMainBody() == null) {
            CharSequence toastText = "To Start, Select a Main Body!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(shipBuildingActivity, toastText, duration);
            toast.show();
        }

    }


    /**
     * Unloads content from the game. The GameActivity will call this function after the game engine
     * exits the game loop. The ShipBuildingActivity will call this function after the "Start Game"
     * button has been pressed.
     * @param content An instance of the content manager. This should be used to unload image and
     *                sound files.
     */
    @Override
    public void unloadContent(ContentManager content) {
        //Dont worry about this one
    }

    /**
     * Draws the game delegate. This function will be 60 times a second.
     */
    @Override
    public void draw() {

       Ship.SINGLETON.draw(1);

    }

    /**
     * The ShipBuildingView calls this function when the user makes a swipe/fling motion in the
     * screen. Respond to the user's swipe/fling motion in this function.
     * @param direction The direction of the swipe/fling.
     */
    @Override
    public void onSlideView(IShipBuildingView.ViewDirection direction) {
       // shipBuildingActivity.animateToView();
        IShipBuildingView.ViewDirection animateDirection;
        switch (direction){
            case RIGHT:
                currentView = getNextPartView();
                animateDirection = IShipBuildingView.ViewDirection.LEFT;
                break;
            case LEFT:
                currentView = getPreviousPartView();
                animateDirection = IShipBuildingView.ViewDirection.RIGHT;
                break;
            case DOWN:
            case UP:
                return;
            default:
                return;
        }
        shipBuildingActivity.animateToView(currentView, animateDirection);
        activateStartButtonIfReady();
    }

    /**
     * The part selection fragments call this function when a part is selected from the parts list. Respond
     * to the part selection in this function.
     * @param index The list index of the selected part.
     */
    @Override
    public void onPartSelected(int index) {
        //know what the current on screen part is
        //get that specific part from that parts ArrayList
        switch(currentView){
            case MAIN_BODY:
                Ship.SINGLETON.addMainBody(mainBodies.get(index));
                break;
            case CANNON:
                Ship.SINGLETON.addCannon(cannons.get(index));
                break;
            case EXTRA_PART:
                Ship.SINGLETON.addExtraPart(extraParts.get(index));
                break;
            case ENGINE:
                Ship.SINGLETON.addEngine(engines.get(index));
                break;
            case POWER_CORE:
                Ship.SINGLETON.addPowerCore(powerCores.get(index));
                break;
            default:
                break;
        }

        activateStartButtonIfReady();
    }

    /**
     * The ShipBuildingView calls this function is called when the start game button is pressed.
     */
    @Override
    public void onStartGamePressed() {
        shipBuildingActivity.startGame();
    }

    /**
     * The ShipBuildingView calls this function when ship building has resumed. Reset the Camera and
     * the ship position as needed when this is called.
     */
    @Override
    public void onResume() {

    }

    @Override
    public IView getView() {
        return null;
    }

    @Override
    public void setView(IView view) {

    }

    private IShipBuildingView.PartSelectionView getNextPartView(){
        switch (currentView){
            case MAIN_BODY:
                return IShipBuildingView.PartSelectionView.ENGINE;
            case ENGINE:
                return IShipBuildingView.PartSelectionView.CANNON;
            case CANNON:
                return IShipBuildingView.PartSelectionView.EXTRA_PART;
            case EXTRA_PART:
                return IShipBuildingView.PartSelectionView.POWER_CORE;
            case POWER_CORE:
                return IShipBuildingView.PartSelectionView.MAIN_BODY;
            default:
                return IShipBuildingView.PartSelectionView.MAIN_BODY;
        }
    }

    private IShipBuildingView.PartSelectionView getPreviousPartView(){
        switch (currentView){
            case MAIN_BODY:
                return IShipBuildingView.PartSelectionView.POWER_CORE;
            case POWER_CORE:
                return IShipBuildingView.PartSelectionView.EXTRA_PART;
            case EXTRA_PART:
                return IShipBuildingView.PartSelectionView.CANNON;
            case CANNON:
                return IShipBuildingView.PartSelectionView.ENGINE;
            case ENGINE:
                return IShipBuildingView.PartSelectionView.MAIN_BODY;
            default:
                return IShipBuildingView.PartSelectionView.MAIN_BODY;
        }
    }

    private void activateStartButtonIfReady(){
        if(Ship.SINGLETON.isComplete()){
            shipBuildingActivity.setStartGameButton(true);
        }
    }
}
