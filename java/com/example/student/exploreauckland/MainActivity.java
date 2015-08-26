package com.example.student.exploreauckland;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.student.exploreauckland.database.DBCreation;
import com.example.student.exploreauckland.database.DatabaseHandler;

/**
 * Main activity class with fragment container
 */
public class MainActivity extends AppCompatActivity implements
        OnItemSelectedListener{

    private ImageView mainImage;
    //Shred Preferences to check if it's the first start
    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("com.example.student.exploreauckland", MODE_PRIVATE);
        mainImage = (ImageView)findViewById(R.id.headerImage);
        mainImage.setImageResource(R.drawable.auck);

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.Fragment fragment = fm.findFragmentById(R.id.fragmentPlace);

//        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                //Call method on changing Fragments Back Stack
//                placeUpButton();
//            }
//        });

        //Checking if fragment is added to fragment space
        if(fragment == null){
            //Checking display orientation
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                onRssItemSelected("main", 0);
            }
            else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                onRssItemSelected("category", 1);
            }
        }
        else {
            //re-create navigation button for fragments after rotation
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                    getSupportFragmentManager().getBackStackEntryCount() == 0) {
                onRssItemSelected("category", 1);
            }
            placeUpButton();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //checking if it's the first run
        //and starts DB creation
        DatabaseHandler mDatabaseHelper = new DatabaseHandler(getApplicationContext());
        SQLiteDatabase mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        if (prefs.getBoolean("firstrun", true) ||
               mDatabaseHelper.getDatabaseVersion() != mSqLiteDatabase.getVersion()) {
            DBCreation dbc = new DBCreation(getApplicationContext());
            dbc.createDB();
            //put flag to preferences
            prefs.edit().putBoolean("firstrun", false).commit();
            //call method to start the fragment
            onRssItemSelected("main", 0);
        }
}

    /**
     * method of OnItemSelectedListener interface
     * using for change existing fragment by another fragment
     * @param link type of fragment to open
     * @param id
     */
    @Override
    public void onRssItemSelected(String link, int id) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.Fragment fragment = fm.findFragmentById(R.id.fragmentPlace);
        Fragment fragment1 = null;
        if(link.equals("main")){
            fragment1 = new FragmentListCategory();
        }else if(link.equals("category")){
            fragment1 = new FragmentListPlace();
        }else if(link.equals("place")){
            fragment1 = new FragmentPlaceDetails();
        }

        Bundle args = new Bundle();
        args.putInt("ID", id);
        fragment1.setArguments(args);

        //if no fragment added, new fragment adds
        //else new fragment replace old fragment and old fragment
        if(fragment == null){
            fm.beginTransaction().add(R.id.fragmentPlace, fragment1)
                    .commit();
        }else{
            fm.beginTransaction().replace(R.id.fragmentPlace, fragment1)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Show and hide Back button in The action bat
     */
    public void placeUpButton(){
        int stackHeight = getSupportFragmentManager().getBackStackEntryCount();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (stackHeight > 1 || (stackHeight == 1 &&
                    getResources().getConfiguration().orientation ==
                            Configuration.ORIENTATION_PORTRAIT)) {
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
            } else {
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setHomeButtonEnabled(false);
            }
        }
    }

    //back navigation through the BackStasck
    @Override
    public void onBackPressed(){
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //recreation of database
            //restarting the fragment
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.fragmentPlace);
            if(fragment != null){
                fm.beginTransaction().remove(fragment).commit();
            }
            DBCreation dbCreation = new DBCreation(getApplicationContext());
            dbCreation.reCreateDB();

            onRssItemSelected("main", 0);

            return true;
        }else if(id == R.id.action_about){
            android.app.FragmentManager fm = getFragmentManager();
            AboutDialog about = new AboutDialog();
            about.show(fm, null);
            return true;
        }

        if(id == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}
