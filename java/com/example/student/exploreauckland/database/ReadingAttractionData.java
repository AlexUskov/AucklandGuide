package com.example.student.exploreauckland.database;

import android.content.Context;
import android.util.Log;

import com.example.student.exploreauckland.Attraction;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * Class for reading data from XML and write it to DB
 * Created by Aleksandr on 05.08.2015.
 */
public class ReadingAttractionData implements Runnable{

    private XmlPullParserFactory pullParserFactory;
    private XmlPullParser parser;

    private AttractionDAOImplementation mAttractionDAOImplementation;
    private Attraction mAttraction = null;
    //status of insert to DB operation
    boolean status = false;



    /**
     * Constructor
     * @param context
     * @throws XmlPullParserException
     * @throws IOException
     */
    public ReadingAttractionData(Context context) throws XmlPullParserException, IOException {
        mAttractionDAOImplementation = new AttractionDAOImplementation(context);

        pullParserFactory = XmlPullParserFactory.newInstance();
        parser = pullParserFactory.newPullParser();
        InputStream in_s = context.getAssets().open("dataPlaces.xml");
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in_s, null);
    }

    /**
     * Parse data from XML to Datastore
     * @param parser
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        int eventType = parser.getEventType();

        //open db connection
        try {
            mAttractionDAOImplementation.open();
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
                    if (name.equals("place")) {
                        mAttraction = new Attraction();
                    } else if (mAttraction != null){
                        if(mAttraction != null) {
                            if (name.equals("id")) {
                                mAttraction.setId(Integer.valueOf(parser.nextText()));
                            } else if (name.equals("name")) {
                                mAttraction.setName(parser.nextText());
                            } else if (name.equals("category")) {
                                mAttraction.setCategory(Integer.valueOf(parser.nextText()));
                            } else if (name.equals("description")) {
                                mAttraction.setDescription(parser.nextText());
                            } else if (name.equals("picture")) {
                                mAttraction.setPicture(parser.nextText());
                            } else if (name.equals("latitude")) {
                                mAttraction.setLatitude(Double.valueOf(parser.nextText()));
                            } else if(name.equals("longitude")){
                                mAttraction.setLongitude(Double.valueOf(parser.nextText()));
                            } else if (name.equals("note")) {
                                mAttraction.setNote(parser.nextText());
                            } else if (name.equals("favorite")) {
                                mAttraction.setFavorite(Integer.valueOf(parser.nextText()));
                            }
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    //if place tag closed add new attraction to DB
                    if (name.equalsIgnoreCase("place") && mAttraction != null){
                        status = mAttractionDAOImplementation.insertAttraction(mAttraction);
                        if(status) Log.d("Add Place", "Success");
                        else Log.d("Add Place", "Fail");
                    }
            }
            eventType = parser.next();
        }
        //close DB connection
        mAttractionDAOImplementation.close();
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