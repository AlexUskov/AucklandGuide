package com.example.student.exploreauckland.database;

import com.example.student.exploreauckland.Category;

import java.util.ArrayList;

/**
 * methods for access to data for categories list
 * Created by Aleksandr on 05.08.2015.
 */
public interface CategoryDAO {

    /**
     * Get list of all categories
     * @return - list of categories
     */
    ArrayList<Category> findAll();

    /**
     * Get category by it's id
     * @param id - id of category in DB
     * @return - category with chosen id
     */
    Category findById(int id);

    /**
     * Insert new category to DB
     * @param category - new category
     * @return true if insert was successful
     */
    boolean insertCategory(Category category);

    /**
     * Update the category in the DB
     * @param category - chosen category
     * @return true if update was successful
     */
    boolean updateCategory(Category category);

    /**
     * Delete the category in the DB
     * @param category - chosen category
     * @return true if delete was successful
     */
    boolean deleteCategory(Category category);
}
