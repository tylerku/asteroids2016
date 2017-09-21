package edu.byu.cs.superasteroids.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by tyudy on 10/28/16.
 */

public class GameDataBase{



    public static final GameDataBase SINGLETON = new GameDataBase();
    private SQLiteDatabase database;

    public GameDataBase(){

    }

    public GameDataBase(SQLiteDatabase db){
        database = db;
    }

    //SQLiteFunctions

    public long insert(String table, String nullColumnHack, ContentValues values){
        return database.insert(table, nullColumnHack, values);
    }

}
