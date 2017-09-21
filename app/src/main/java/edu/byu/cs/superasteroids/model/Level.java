package edu.byu.cs.superasteroids.model;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.byu.cs.superasteroids.model.positioned_objects.BgObject;
import edu.byu.cs.superasteroids.model.positioned_objects.MiniMap;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.Asteroid;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.Projectile;

/**
 * Created by tylerku on 10/20/16.
 *
 * The area of game play that the ship interacts with.
 */
public class Level extends VisibleObject {

    private int number;
    private String title;
    private String hint;
    private String musicURL;
    private int levelHeight;
    private int levelWidth;

    private ArrayList<Asteroid> levelAsteroids;
    private ArrayList<BgObject> levelBackgroundObjects;
    private ArrayList<Projectile> projectiles;
    private MiniMap miniMap;


    /**
     * constructor for creating part of the level.
     * NOTE: levelAsteroids, backgroundObjects and miniMap are not being set here
     *
     * @param h - height of the level
     * @param w - width of the level
     * @param num - current level number (level 1, level2, etc...)
     * @param help - a hint that can be given
     * @param music - music for the level
     */
    public Level(int h, int w, int num, String help, String music, String ttl) {
        super("");

        levelHeight = h;
        levelWidth = w;
        number = num;
        hint = help;
        musicURL = music;
        title = ttl;
        levelAsteroids = new ArrayList<>();
        levelBackgroundObjects = new ArrayList<>();
        projectiles = new ArrayList<>();
    }

    /*
        Constructor used for importing Json version of Level into database

        "number": 1,
        "title": "Level 1 - The Regular Asteroid",
        "hint": "Destroy a regular asteroid",
        "width": 3000,
        "height": 3000,
        "music": "sounds/SpyHunter.ogg",
     */
    public Level(JSONObject JsonLevel) throws JSONException {
        super("");

        number = JsonLevel.getInt("number");
        title = JsonLevel.getString("title");
        hint = JsonLevel.getString("hint");
        musicURL = JsonLevel.getString("music");
        levelWidth = JsonLevel.getInt("width");
        levelHeight = JsonLevel.getInt("height");

    }

    public void addBgObject(BgObject object){ levelBackgroundObjects.add(object); }
    public void addAsteroid(Asteroid asteroid){ levelAsteroids.add(asteroid); }
    public void addProjectile(Projectile prjtl){ projectiles.add(prjtl); }

    public void removeProjectile(int index){ projectiles.remove(index); }

    public int getNumber(){ return number; }
    public String getTitle(){ return title; }
    public String getMusicURL(){ return musicURL; }
    public String getHint(){ return hint; }
    public int getLevelHeight(){ return levelHeight; }
    public int getLevelWidth(){ return levelWidth; }
    public ArrayList<Asteroid> getLevelAsteroids(){ return levelAsteroids; }
    public ArrayList<BgObject> getLevelBackgroundObjects(){ return levelBackgroundObjects; }
    public ArrayList<Projectile> getProjectiles(){ return projectiles; }
}
