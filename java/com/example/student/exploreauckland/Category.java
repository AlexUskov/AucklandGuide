package com.example.student.exploreauckland;

import android.content.Context;

/**
 * Created by Aleksandr on 05.08.2015.
 */
public class Category {

    //attributes of the category
    private int id;
    private String name;
    private String icon;

    /**
     * constructor
     * @param id - category id
     * @param name - category name
     * @param pictureName - picture name
     * @param context - app context
     */
    public Category(int id, String name, String pictureName, Context context) {
        icon = pictureName;
        this.id = id;
        this.name = name;
    }

    /**
     * Default constructor
     */
    public Category() {}

    /**
     *
     * @return id of category
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id - id of category
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return name of category
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name - name of category
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get category icon
     * @return icon name
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Set category icon
     * @param icon - picture name
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }
}