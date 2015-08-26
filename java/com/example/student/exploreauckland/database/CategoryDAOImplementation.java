package com.example.student.exploreauckland.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.student.exploreauckland.Category;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of methods described in interface
 * Created by Aleksandr on 05.08.2015.
 */
public class CategoryDAOImplementation implements CategoryDAO {

    private DatabaseHandler dbHandler;
    private SQLiteDatabase db;

    /**
     * Constructor
     * @param context
     */
    public CategoryDAOImplementation(Context context){
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
     * @return List of categories according to where statement
     */
    public ArrayList<Category> findCategories(String whereStatement){
        ArrayList<Category> categoryList = new ArrayList<Category>();
        // Select Query
        String selectQuery = "SELECT * FROM " + dbHandler.getTableCategories() + whereStatement;
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Category category = new Category();
                    category.setId(cursor.getInt(0));
                    category.setName(cursor.getString(1));
                    category.setIcon(cursor.getString(2));

                    // Adding attraction to list
                    categoryList.add(category);
                } while (cursor.moveToNext());
            }
            db.close();
        }catch(Exception e){
            Log.d("Exception Get", e.getMessage());
        }
        return categoryList;
    }

    /**
     * Find list of all categories
     * @return - arraylist of all categories
     */
    @Override
    public ArrayList<Category> findAll() {
        return findCategories("");
    }

    /**
     * Find category by its ID
     * @param id - id of category in DB
     * @return Category with chosen id
     */
    @Override
    public Category findById(int id) {
        List<Category> categoryList = findCategories("WHERE " + dbHandler.getTableCategories() + " = " + id);
        if (categoryList.size() > 0) {
            return categoryList.get(0);
        }
        return null;
    }

    /**
     * Insert new category to DataBase
     * @param category - new category
     * @return is insertion successful
     */
    @Override
    public boolean insertCategory(Category category) {
        try {
            ContentValues values = new ContentValues();
            //put data co contentValue
            values.put(dbHandler.getCategoryId(), category.getId());
            values.put(dbHandler.getCategoryName(), category.getName());
            values.put(dbHandler.getCategoryPicture(), category.getIcon());
            //insert contentValue to DB
            db.insert(dbHandler.getTableCategories(), null, values);
        }catch (Exception ex){
            Log.d("Exception Insert", ex.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Update data of selected category
     * @param category - chosen category
     * @return is operation successful
     */
    @Override
    public boolean updateCategory(Category category) {
        try {
            ContentValues values = new ContentValues();
            values.put(dbHandler.getCategoryName(), category.getName());
            values.put(dbHandler.getCategoryPicture(), category.getIcon());
            // updating row
            db.update(dbHandler.getTableCategories(), values, dbHandler.getCategoryId() + " = ?",
                    new String[]{String.valueOf(category.getId())});
        }catch (Exception ex){
            Log.d("Exception Update", ex.getMessage());
            return false;
        }
        return true;
    }

    /**
     * delete selected category
     * @param category - chosen category
     * @return is operation successful
     */
    @Override
    public boolean deleteCategory(Category category) {
        try {
            db.delete(dbHandler.getTableCategories(), dbHandler.getCategoryId() + " = ?",
                    new String[] { String.valueOf(category.getId()) });
            db.close();
        }catch (Exception ex){
            Log.d("Exception Delete", ex.getMessage());
            return false;
        }
        return true;
    }
}
