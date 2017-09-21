package edu.byu.cs.superasteroids.model;

import java.util.ArrayList;

import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.Ship;

/**
 * Created by tyudy on 11/10/16.
 */

public class AsteroidsGame {

    public static AsteroidsGame SINGLETON = new AsteroidsGame();
    public static final float SHIP_SCALE = (float).25;
    public static final float SHIP_SPEED = 600;
    public static final float SHIP_LIVES = 5;
    public static final float PROJECTILE_SPEED = 5000;
    public static final float ASTEROID_SCALE = 2;
    public static final float ASTEROID_GROWTH_RATE = (float)1.0025;
    public static final float ASTEROID_SPEED = 400;
    public static final float MAP_TO_LEVEL_RATIO = 1/10f;

    Space mSpace;
    Ship mShip;
    int mProjectileHeight;
    int mProjectileWidth;
    int mProjectileDamage;

    ArrayList<Level> mLevels;
    Level currentLevel;

    private AsteroidsGame(){
        mLevels = new ArrayList<>();
    }

    public void setSpace(Space space) { mSpace = space; }
    public void setShip(Ship ship) { mShip = ship; }
    public void setLevels(ArrayList<Level> levels){ mLevels = levels; }
    public void setCurrentLevel(Level level){ currentLevel = level; }
    public void setProjectileHeight(int h){ mProjectileHeight = h; }
    public void setProjectileWidth(int w){ mProjectileWidth = w; }
    public void setProjectileDamage(int d){ mProjectileDamage = d; }

    public Level getCurrentLevel(){ return currentLevel; }
    public Space getSpace(){ return mSpace; }



}
