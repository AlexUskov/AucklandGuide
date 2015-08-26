package com.example.student.exploreauckland.database;

import android.content.Context;
import android.util.Log;

import com.example.student.exploreauckland.Category;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * Class for reading data from XML and write it to DB
 * Created by Aleksandr on 06.08.2015.
 */
public class ReadingCategoriesData implements Runnable{

    XmlPullParserFactory pullParserFactory;
    XmlPullParser parser;

    private CategoryDAOImplementation mCategoryDAOImplementation;
    private Category mCategory = null;
    //status of insert data to DB operation
    boolean status = false;

    public ReadingCategoriesData(Context context) throws XmlPullParserException, IOException {

        mCategoryDAOImplementation = new CategoryDAOImplementation(context);
        pullParserFactory = XmlPullParserFactory.newInstance();
        parser = pullParserFactory.newPullParser();
        //input stream from xml file
        InputStream in_s = context.getAssets().open("dataCategories.xml");
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in_s, null);
    }

    /**
     * Parse data from XML to datastore
     * @param parser
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        int eventType = parser.getEventType();
        //open DB connection
        try {
            mCategoryDAOImplementation.open();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("SQL Exception", e.getMessage());
        }

        //loop through xml file
        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if(name.equals("item")){
                        mCategory = new Category();
                    } else if (mCategory != null){
                        if (name.equals("category_id")){
                            mCategory.setId(Integer.valueOf(parser.nextText()));
                        } else if(name.equals("category_name")){
                            mCategory.setName(parser.nextText());
                        } else if(name.equals("category_picture")){
                            mCategory.setIcon(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                   if(name.equalsIgnoreCase("item") && mCategory != null){
                        status = mCategoryDAOImplementation.insertCategory(mCategory);
                        if(status) Log.d("Add Category", "Success");
                        else Log.d("Add Category", "Fail");
                    }
            }
            eventType = parser.next();
        }
        mCategoryDAOImplementation.close();
    }

    //implemented method (class implements runnable)
    @Override
    public void run() {
        try {
            parseXML(parser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}