package com.example.student.exploreauckland.database;

import com.example.student.exploreauckland.Attraction;

import java.util.ArrayList;

/**
 * methods for access to data for attractions list
 * Created by Aleksandr on 03.08.2015.
 */
public interface AttractionDAO {
    /**
     * Get all companies
     * @return list of all attractions in DB
     */
    ArrayList<Attraction> findAll();

    /**
     * Get all attractions of chosen category
     * @param category - category of attraction
     * @return list of attraction
     */
    ArrayList<Attraction> findByCategory(int category);

    /**
     * Get all attractions added to favorite
     * @return attractions from favorite list
     */
    ArrayList<Attraction> findAllFavorite();

    /**
     * Get attraction by it's id
     * @param id - id of attraction in DB
     * @return - attraction with chosen id
     */
    Attraction findById(int id);

    /**
     * Insert new attraction to DB
     * @param attraction - new attraction
     * @return true if insert was successful
     */
    boolean insertAttraction(Attraction attraction);

    /**
     * Update the attraction in the DB
     * @param attraction - chosen attraction
     * @return true if update was successful
     */
    boolean updateAttraction(Attraction attraction);

    /**
     * Delete the attraction in the DB
     * @param attraction - chosen attraction
     * @return true if delete was successful
     */
    boolean deleteAttraction(Attraction attraction);
}
