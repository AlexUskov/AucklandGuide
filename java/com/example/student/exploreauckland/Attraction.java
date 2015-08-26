package com.example.student.exploreauckland;

import android.content.Context;

/**
 * Created by Aleksandr on 03.08.2015.
 */
public class Attraction {

    //attributes of each attraction
    private int id;
    private int category;
    private String name;
    private String description;
    private String picture;
    private double latitude;
    private double longitude;
    private String note;
    private int favorite;

    /**
     * Constructor
     */
    public Attraction(){
    }
    /**
     * Overloaded constructor
     * @param id - id from DB
     * @param category - category of attraction
     * @param name - place name
     * @param description - place description
     * @param picture - picture name
     * @param latitude - data for GoogleMaps API
     * @param longitude - data for GoogleMaps API
     * @param note - user note
     * @param favorite - mark for added to favorite
     */

    public Attraction(int id, int category, String name, String description, String picture,
                      String note, int favorite, double latitude, double longitude, Context context){
        this.id = id;
        this.category = category;
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.latitude = latitude;
        this.longitude = longitude;
        this.note = note;
        this.favorite = favorite;
    }

    /**
     *
     * @return attraction's id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id - attraction's id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return user note
     */
    public String getNote() {
        return note;
    }

    /**
     *
     * @param note - user note for attraction
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     *
     * @return - latitude of attraction
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude latitude of attraction on the map
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return - longitude of attraction
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude - longitude of attraction
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    /**
     *
     * @return path for attraction's picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     *
     * @param picture - path for attraction's picture
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     *
     * @return place's description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description - place's description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return attraction's name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name - attraction's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return place's category
     */
    public int getCategory() {
        return category;
    }

    /**
     *
     * @param category - category of place
     */
    public void setCategory(int category) {
        this.category = category;
    }

    /**
     *
     * @return 1 if place added to favorite
     */
    public int getFavorite() {
        return favorite;
    }

    /**
     *
     * @param favorite - is place in favorite or not
     */
    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    /**
     * Get first 100 characters of attraction's description
     * @return short description of attraction
     */
    public String getShortDescription(){
        if(description.length() > 100){
            return description.substring(0, 99) + "...";
        }
        return description;
    }
}