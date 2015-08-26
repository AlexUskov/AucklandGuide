package com.example.student.exploreauckland.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Aleksandr on 03.08.2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 11;

    // Database Name
    private static final String DATABASE_NAME = "pointsOfInterest.db";

    //Tables names
    private static final String TABLE_PLACES = "places";
    private static final String TABLE_CATEGORIES = "categories";

    // Places table columns names
    private static final String PLACE_ID = "place_id";
    private static final String PLACE_CATEGORY = "place_category";
    private static final String PLACE_NAME = "place_name";
    private static final String PLACE_DESCRIPTION = "place_description";
    private static final String PLACE_PICTURE = "place_picture";
    private static final String PLACE_LATITUDE = "place_latitude";
    private static final String PLACE_LONGITUDE = "place_longitude";
    private static final String PLACE_NOTE = "place_note";
    private static final String PLACE_FAVORITE = "place_favorite";

    // Categories table columns names
    private static final String CATEGORY_ID = "category_id";
    private static final String CATEGORY_NAME = "category_name";
    private static final String CATEGORY_PICTURE = "category_picture";

    //String for creation table Attractions
    final String CREATE_ATTRACTION_TABLE = "CREATE TABLE " + TABLE_PLACES + "("
            + PLACE_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,"
            + PLACE_CATEGORY + " INTEGER,"
            + PLACE_NAME + " TEXT NOT NULL,"
            + PLACE_DESCRIPTION + " TEXT,"
            + PLACE_PICTURE + " TEXT,"
            + PLACE_LATITUDE + " REAL,"
            + PLACE_LONGITUDE + " REAL,"
            + PLACE_NOTE + " TEXT,"
            + PLACE_FAVORITE + " INTEGER" + ")";
    //String for creation table Categories
    final String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
            + CATEGORY_ID + " INTEGER PRIMARY KEY UNIQUE,"
            + CATEGORY_NAME + " TEXT,"
            + CATEGORY_PICTURE + " TEXT" + ")";

    /**
     * constructor
     * @param context - app context
     */
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("Database handler", "Started");
    }

    /**
     * overloaded constructor
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    /**
     * Overloaded constructor
     * @param context
     * @param name
     * @param factory
     * @param version
     * @param errorHandler
     */
    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory,
                           int version, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION, errorHandler);
    }

    /**
     * On create database method
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Database on create", "Started");
        db.execSQL(CREATE_ATTRACTION_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
    }

    /**
     * on upgrade database method
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
        onCreate(db);
    }

    /**
     * drop tables in DB
     * @param db
     */
    public void dropTables(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
    }

    // getter methods for all name of tables colons
    public static String getCategoryName() {
        return CATEGORY_NAME;
    }

    public static String getPlaceId() {
        return PLACE_ID;
    }

    public static String getPlaceCategory() {
        return PLACE_CATEGORY;
    }

    public static String getPlaceName() {
        return PLACE_NAME;
    }

    public static String getTableCategories() {
        return TABLE_CATEGORIES;
    }

    public static String getTablePlaces() {
        return TABLE_PLACES;
    }

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    public static String getPlaceDescription() {
        return PLACE_DESCRIPTION;
    }

    public static String getPlacePicture() {
        return PLACE_PICTURE;
    }

    public static String getPlaceLongitude() {
        return PLACE_LONGITUDE;
    }

    public static String getCategoryPicture() {
        return CATEGORY_PICTURE;
    }

    public static String getPlaceLatitude() {
        return PLACE_LATITUDE;
    }

    public static String getPlaceNote() {
        return PLACE_NOTE;
    }

    public static String getPlaceFavorite() {
        return PLACE_FAVORITE;
    }

    public static String getCategoryId() {
        return CATEGORY_ID;
    }
}
