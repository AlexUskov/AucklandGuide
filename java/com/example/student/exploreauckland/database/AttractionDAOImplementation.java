package com.example.student.exploreauckland.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.student.exploreauckland.Attraction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of methods described in interface
 * Created by Aleksandr on 03.08.2015.
 */
public class AttractionDAOImplementation implements AttractionDAO {

    private DatabaseHandler dbHandler;
    private SQLiteDatabase db;

    /**
     * Constructor
     * @param context
     */
    public AttractionDAOImplementation(Context context){
        Log.d("Attraction impl", "Started");
        dbHandler = new DatabaseHandler(context, "pointsOfInterest.db", null, 1);
    }

    /**
     * open DB connection
     * @throws SQLException
     */
    public void open() throws SQLException {
        db = dbHandler.getWritableDatabase();
    }

    /**
     * close DB connection
     */
    public void close() {
        dbHandler.close();
    }

    /**
     * Auxiliary method for data request from Datastore
     * @param whereStatement parameters for DB request
     * @return List of attractions according to where statement
     */
    public ArrayList<Attraction> findAttractions(String whereStatement, String[] param){
        ArrayList<Attraction> attractionList = new ArrayList<Attraction>();
        // Select Query
        String selectQuery = "SELECT * FROM " + dbHandler.getTablePlaces() + whereStatement;
        try {
            //getting cursor
            Cursor cursor = db.rawQuery(selectQuery, param);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Attraction attraction = new Attraction();
                    attraction.setId(cursor.getInt(0));
                    attraction.setCategory(cursor.getInt(1));
                    attraction.setName(cursor.getString(2));
                    attraction.setDescription(cursor.getString(3));
                    attraction.setPicture(cursor.getString(4));
                    attraction.setLatitude(cursor.getDouble(5));
                    attraction.setLongitude(cursor.getDouble(6));
                    attraction.setNote(cursor.getString(7));
                    attraction.setFavorite(cursor.getInt(8));

                    // Adding attraction to list
                    attractionList.add(attraction);
                } while (cursor.moveToNext());
            }
            db.close();
        }catch(Exception e){
            Log.d("Exception Get", e.getMessage());
        }
        return attractionList;
    }

    /**
     * Get all companies
     * @return list of all attractions in DB
     */
    @Override
    public ArrayList<Attraction> findAll() {
        return findAttractions("", null);
    }

    /**
     * Get all attractions of chosen category
     * @param category - category of attraction
     * @return list of attraction
     */
    @Override
    public ArrayList<Attraction> findByCategory(int category) {
        return findAttractions(" WHERE " + dbHandler.getPlaceCategory() + " = ?",
                new String[]{String.valueOf(category)});
    }

    /**
     * Get all attractions added to favorite
     * @return attractions from favorite list
     */
    @Override
    public ArrayList<Attraction> findAllFavorite() {
        return findAttractions(" WHERE " + dbHandler.getPlaceFavorite() + " = ?", new String[]{String.valueOf(1)});
    }

    /**
     * Get attraction by it's id
     * @param id - id of attraction in DB
     * @return - attraction with chosen id
     */
    @Override
    public Attraction findById(int id) {
        List<Attraction> attractionList = findAttractions(" WHERE " + dbHandler.getPlaceId() + " = ?",  new String[]{String.valueOf(id)});
        if (attractionList.size() > 0) {
            return attractionList.get(0);
        }
        return null;
    }

    /**
     *
     * @param attraction - new attraction
     * @return is inserted successful or not
     */
    @Override
    public boolean insertAttraction(Attraction attraction) {
        try {
            ContentValues values = new ContentValues();

            values.put(dbHandler.getPlaceId(), attraction.getId());
            values.put(dbHandler.getPlaceCategory(), attraction.getCategory());
            values.put(dbHandler.getPlaceDescription(), attraction.getDescription());
            values.put(dbHandler.getPlaceName(), attraction.getName());
            values.put(dbHandler.getPlaceLatitude(), attraction.getLatitude());
            values.put(dbHandler.getPlaceLongitude(), attraction.getLongitude());
            values.put(dbHandler.getPlacePicture(), attraction.getPicture());
            values.put(dbHandler.getPlaceNote(), attraction.getNote());
            values.put(dbHandler.getPlaceFavorite(), attraction.getFavorite());

            //insert row
            db.insert(dbHandler.getTablePlaces(), null, values);

        }catch (Exception ex){
            Log.d("Exception Insert", ex.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Update attraction in DB
     * @param attraction - chosen attraction
     * @return is updated successful or not
     */
    @Override
    public boolean updateAttraction(Attraction attraction) {
        try {
            ContentValues values = new ContentValues();

            values.put(dbHandler.getPlaceCategory(), attraction.getCategory());
            values.put(dbHandler.getPlaceDescription(), attraction.getDescription());
            values.put(dbHandler.getPlaceName(), attraction.getName());
            values.put(dbHandler.getPlaceLatitude(), attraction.getLatitude());
            values.put(dbHandler.getPlaceLongitude(), attraction.getLongitude());
            values.put(dbHandler.getPlacePicture(), attraction.getPicture());
            values.put(dbHandler.getPlaceNote(), attraction.getNote());
            values.put(dbHandler.getPlaceFavorite(), attraction.getFavorite());

            // updating row
            db.update(dbHandler.getTablePlaces(), values, dbHandler.getPlaceId() + " = ?",
                    new String[]{String.valueOf(attraction.getId())});
        }catch (Exception ex){
            Log.d("Exception Update", ex.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Delete chosen attraction
     * @param attraction - chosen attraction
     * @return is deleted successful or not
     */
    @Override
    public boolean deleteAttraction(Attraction attraction) {
        try {
            db.delete(dbHandler.getTablePlaces(), dbHandler.getPlaceId() + " = ?",
                    new String[] { String.valueOf(attraction.getId()) });
        }catch (Exception ex){
            Log.d("Exception Delete", ex.getMessage());
            return false;
        }
        return true;
    }
}