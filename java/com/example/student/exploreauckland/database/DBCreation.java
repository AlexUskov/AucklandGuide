package com.example.student.exploreauckland.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Aleksandr on 15.08.2015.
 */
public class DBCreation {
    private DatabaseHandler mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    Context context;

    public DBCreation(Context context) {
        this.context = context;
        mDatabaseHelper = new DatabaseHandler(context, "pointsOfInterest.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }


    public void createDB() {

        try {
            ReadingAttractionData readAttractions = new ReadingAttractionData(context);
            ReadingCategoriesData readCategories = new ReadingCategoriesData(context);
            Thread t1 = new Thread(readAttractions);
            Thread t2 = new Thread(readCategories);
            t1.start();
            t2.start();
        } catch (Exception e) {
            Log.d("DBCreation", "Unsuccessful parsing");
            e.printStackTrace();
        }
    }

    public void reCreateDB(){
        mDatabaseHelper.dropTables(mSqLiteDatabase);
        mDatabaseHelper.onCreate(mSqLiteDatabase);
        createDB();
    }
}

