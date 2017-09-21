package edu.byu.cs.superasteroids.model.positioned_objects.moving_objects;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.Display;
import android.view.InflateException;
import android.view.View;
import android.view.inputmethod.InputBinding;

import edu.byu.cs.superasteroids.base.GameDelegate;
import edu.byu.cs.superasteroids.content.ContentManager;
import edu.byu.cs.superasteroids.core.GraphicsUtils;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;
import edu.byu.cs.superasteroids.game.InputManager;
import edu.byu.cs.superasteroids.game.ViewPort;
import edu.byu.cs.superasteroids.model.AsteroidsGame;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.Cannon;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.Engine;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.ExtraPart;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.MainBody;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.PowerCore;

/**
 * Created by tylerku on 10/19/16.
 *
 * The ship that the player will control
 */
public class Ship {

    public static final Ship SINGLETON = new Ship();

    Cannon mCannon;
    Engine mEngine;
    ExtraPart mExtraPart;
    MainBody mMainBody;
    PowerCore mPowerCore;
    float speed;  // This will always be the same, later I can make it engine specific
    float rotationalDegrees; // angle that the ship is rotates in degrees
    double angle; // angle that the ship is facing in radians
    float opacity;
    int lives;
    boolean resettingShip;
    double resetTime;

    private Ship(){

        mCannon = null;
        mEngine = null;
        mExtraPart = null;
        mMainBody = null;
        mPowerCore = null;
        speed = AsteroidsGame.SHIP_SPEED;
        rotationalDegrees = 0;
        angle = 0;
        opacity = 255;
        lives = 5;
        resettingShip = false;
        resetTime = 5;

    }

    /**
     * Sets the number of lives of the ship to the original amount
     */
    public void initializeLives(){
        lives = (int)AsteroidsGame.SHIP_LIVES;
    }

    /**
     * Function to make the ship react when hit.
     */
    public void collide(Asteroid asteroid){
        // This makes the ship "safe" for a time
        if(resettingShip){
            return;
        }
        opacity = 155;
        lives--;
        resettingShip = true;
        resetTime = 0;
    }


    /**
     * creates a new projectiles that will fly across the screen
     */
    public void shoot(){
        Projectile shot = new Projectile(mCannon.getAttackImageURL(), mCannon.getAttackImageHeight(), mCannon.getAttackImageWidth(), (float)(angle), getShootPoint(), mCannon.getDamage());
        AsteroidsGame.SINGLETON.getCurrentLevel().addProjectile(shot);
    }


    /**
     * @return - the location where bullets should be fired from (tip of the cannon) in level coordinates
     */
    public PointF getShootPoint(){
        //start location of projectile
        //calculate for when the ship is in a different position in screen
        float xEmitPointOffset = AsteroidsGame.SHIP_SCALE * ((mMainBody.getCannonAttach().x - mMainBody.getImageWidth()/2f) + (mCannon.getEmitPoint().x - mCannon.getAttachPoint().x));
        float yEmitPointOffset = AsteroidsGame.SHIP_SCALE * ((mMainBody.getCannonAttach().y - mMainBody.getImageHeight()/2f) + (mCannon.getEmitPoint().y - mCannon.getAttachPoint().y));
        PointF emitPointOffset = GraphicsUtils.rotate(new PointF(xEmitPointOffset, yEmitPointOffset), angle);
        PointF returnPoint =  new PointF (ViewPort.getShipPosOnScreen().x + emitPointOffset.x,
                           ViewPort.getShipPosOnScreen().y + emitPointOffset.y);
        returnPoint = ViewPort.convertToWorldCoordinates(returnPoint);
        return returnPoint;
    }

    /**
     * Function to make the ship rotate/turn.
     */
    public void rotate(){

    }

    /**
     * @return Returns true if all parts of the ship have been set, false otherwise
     */
    public boolean isComplete(){
        if(mCannon == null ||
           mEngine == null ||
           mExtraPart == null ||
           mMainBody == null ||
           mPowerCore == null){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Set the ship back back to its original state like it was after it was constructed
     */
    public void setPartsToNull(){
        mCannon = null;
        mEngine = null;
        mExtraPart = null;
        mMainBody = null;
        mPowerCore = null;
        speed = AsteroidsGame.SHIP_SPEED;
        rotationalDegrees = 0;
        angle = 0;
    }

    public void update(double elapsedTime, PointF movePoint ){


        if(resettingShip){
            resetTime += elapsedTime; // When this is 300 we want to set ressetting ship back to false
            if(resetTime >= 1){
                resettingShip = false;
            }
        }

        if(movePoint == null){
            return;
        }

        RectF shipBounds = Ship.SINGLETON.getBounds();
        GameDelegate.TouchTriangle.SINGLETON.setTouch(movePoint);
        GraphicsUtils.MoveObjectResult shipMoveResult = GraphicsUtils.moveObject(ViewPort.getPosInLevel(), shipBounds, Ship.SINGLETON.getSpeed(),
                GameDelegate.TouchTriangle.SINGLETON.angelCos(), GameDelegate.TouchTriangle.SINGLETON.angelSin(), elapsedTime );
        ViewPort.updateShipLocationAndMoveView(shipMoveResult.getNewObjPosition());




    }

    public void draw(float scale){



        int mainBodyId;
        int cannonId;
        int extraPartId;
        int engineId;

        if(lives <= 0){
            DrawingHelper.drawCenteredText("GAME OVER", 500, Color.RED);
            return;
        }

        //prepare variables for drawing
        boolean gameIsSet = AsteroidsGame.SINGLETON.getCurrentLevel() != null;
        PointF drawCenter = new PointF();
        if(gameIsSet) {
            drawCenter = ViewPort.getShipPosOnScreen();
        } else {
            drawCenter.x = DrawingHelper.getGameViewWidth()/2;
            drawCenter.y = DrawingHelper.getGameViewHeight()/2;
        }

        float xScale = scale;
        float yScale = scale;

        // Set all to false so that when no point is clicked the below if statements dont run
        boolean leftBelowClick = false;
        boolean leftAboveClick = false;
        boolean rightAboveClick = false;
        boolean rightBelowClick = false;

        if(InputManager.movePoint != null) {
            PointF click = InputManager.movePoint;

            leftBelowClick = click.y > ViewPort.getShipPosOnScreen().y &&
                             click.x < ViewPort.getShipPosOnScreen().x;
            leftAboveClick = click.y < ViewPort.getShipPosOnScreen().y &&
                             click.x < ViewPort.getShipPosOnScreen().x;
            rightAboveClick = click.y < ViewPort.getShipPosOnScreen().y &&
                              click.x > ViewPort.getShipPosOnScreen().x;
            rightBelowClick = click.y > ViewPort.getShipPosOnScreen().y &&
                              click.x > ViewPort.getShipPosOnScreen().x;

            if(leftAboveClick || leftBelowClick){
                angle = (270*Math.PI/180) - Math.asin(GameDelegate.TouchTriangle.SINGLETON.angelSin());
            }else if(rightAboveClick || rightBelowClick){
                angle = Math.asin(GameDelegate.TouchTriangle.SINGLETON.angelSin()) + (90*Math.PI/180);

            }

        }

        rotationalDegrees = (float)GraphicsUtils.radiansToDegrees(angle);



        if(mMainBody != null) {
            String s = mMainBody.getImageURL();
            mainBodyId = ContentManager.getInstance().getImageId(mMainBody.getImageURL());
            DrawingHelper.drawImage(mainBodyId, drawCenter.x, drawCenter.y, rotationalDegrees, xScale, yScale, 255);
        } else {
            //There will be a toast on screen in the shipBuilder notifying the user that they need to select a main body
            // to continue this is because we use the main bodies dimensions to do further calculations, we need it to continue
            return;
        }

        if(mCannon != null) {
            float xCannonOffset = xScale * ((mMainBody.getCannonAttach().x - mMainBody.getImageWidth()/2) + (mCannon.getImageWidth()/2 - mCannon.getAttachPoint().x));
            float yCannonOffset = yScale * ((mMainBody.getCannonAttach().y - mMainBody.getImageHeight()/2) + (mCannon.getImageHeight()/2 - mCannon.getAttachPoint().y));
            PointF cannonOffset = GraphicsUtils.rotate(new PointF(xCannonOffset, yCannonOffset), angle);

            cannonId = ContentManager.getInstance().getImageId(mCannon.getImageURL());
            DrawingHelper.drawImage(cannonId, drawCenter.x + cannonOffset.x, drawCenter.y + cannonOffset.y,
                                    rotationalDegrees, xScale, yScale, 255);
        }

        if(mExtraPart != null) {
            float xExtraPartOffset = xScale * ((mMainBody.getExtraAttach().x - mMainBody.getImageWidth()/2f) + (mExtraPart.getImageWidth()/2 - mExtraPart.getAttachPoint().x));
            float yExtraPartOffset = yScale * ((mMainBody.getExtraAttach().y - mMainBody.getImageHeight()/2) + (mExtraPart.getImageHeight()/2 - mExtraPart.getAttachPoint().y));
            PointF extraPartOffset = GraphicsUtils.rotate(new PointF(xExtraPartOffset, yExtraPartOffset), angle);

            extraPartId = ContentManager.getInstance().getImageId(mExtraPart.getImageURL());
            DrawingHelper.drawImage(extraPartId, drawCenter.x + extraPartOffset.x, drawCenter.y + extraPartOffset.y,
                                    rotationalDegrees, xScale, yScale, 255);
        }

        if(mEngine != null) {
            float xEngineOffset = xScale * ((mMainBody.getEngineAttach().x - mMainBody.getImageWidth()/2) + (mEngine.getImageWidth()/2 - mEngine.getAttachPoint().x));
            float yEngineOffset = yScale * ((mMainBody.getEngineAttach().y - mMainBody.getImageHeight()/2) + (mEngine.getImageHeight()/2 - mEngine.getAttachPoint().y));
            PointF engineOffset = GraphicsUtils.rotate(new PointF(xEngineOffset, yEngineOffset), angle);

            engineId = ContentManager.getInstance().getImageId(mEngine.getImageURL());
            DrawingHelper.drawImage(engineId, drawCenter.x + engineOffset.x, drawCenter.y + engineOffset.y,
                                    rotationalDegrees, xScale, yScale, 255);
        }

        if(resettingShip){
            DrawingHelper.drawPoint(ViewPort.getShipPosOnScreen(), getWidth(), Color.RED, 120);
        }

    }

    // Add ship parts
    public void addCannon(Cannon cannon){ mCannon = cannon; }
    public void addEngine(Engine engine){ mEngine = engine; }
    public void addExtraPart(ExtraPart extraPart){ mExtraPart = extraPart; }
    public void addMainBody(MainBody mainBody){ mMainBody = mainBody; }
    public void addPowerCore(PowerCore powerCore){ mPowerCore = powerCore; }

    // Getters
    public Cannon getCannon(){ return mCannon; }
    public Engine getEngine(){ return mEngine; }
    public ExtraPart getExtraPart(){ return mExtraPart; }
    public MainBody getMainBody(){ return mMainBody; }
    public PowerCore getPowerCore(){ return mPowerCore; }
    public float getSpeed(){ return speed; }
    public int getLives(){ return lives; }

    // Setters
    public void setAngle(float theta){ angle = theta; }

    // Calculate non existent member variables
    public float getHeight(){
        float engineOverlap = mEngine.getImageHeight() - (mEngine.getImageHeight() - mEngine.getAttachPoint().y);
        float mainBodyOverlap = mMainBody.getImageHeight() - mMainBody.getEngineAttach().y;
        float overlap = engineOverlap + mainBodyOverlap;
        float height = mMainBody.getImageHeight() + mEngine.getImageHeight() - overlap;
        return AsteroidsGame.SHIP_SCALE*height;
    }
    public float getWidth(){
        float extraPartOverlap = (mExtraPart.getImageWidth() - mExtraPart.getAttachPoint().x);
        float mainBodyOverlap = mMainBody.getImageWidth() - ((mMainBody.getImageWidth() - mMainBody.getExtraAttach().x) -
                                (mMainBody.getImageWidth() - mMainBody.getCannonAttach().x));
        float cannonOverlap = mCannon.getImageWidth() - (mCannon.getImageWidth() - mCannon.getAttachPoint().x);
        float overlap = extraPartOverlap + mainBodyOverlap + cannonOverlap;
        float width = (mExtraPart.getImageWidth() + mMainBody.getImageWidth() + mCannon.getImageWidth()) - overlap;
        return AsteroidsGame.SHIP_SCALE*width;
    }
    public RectF getBounds(){
        float leftBound = ViewPort.getPosInLevel().x - this.getWidth()/2f;
        float rightBound = ViewPort.getPosInLevel().x + this.getWidth()/2f;
        float topBound = ViewPort.getPosInLevel().y - this.getHeight()/2f;
        float bottomBound = ViewPort.getPosInLevel().y + this.getHeight()/2f;

        return new RectF(leftBound, topBound, rightBound, bottomBound);
    }


}
