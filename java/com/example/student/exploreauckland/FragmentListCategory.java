package com.example.student.exploreauckland;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.student.exploreauckland.database.CategoryDAOImplementation;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Fragment with list of categories
 * Created by Aleksandr on 10.08.2015.
 */
public class FragmentListCategory extends Fragment {
    //progress dialog is shown while performing a database query
    private ProgressDialog mProgressDialog;
    private OnItemSelectedListener mListener;
    private ListView mListView;
    private CategoryDAOImplementation mCategoryDAOImplementation;
    private ArrayList<Category> items;
    private GetCategoriesList mGetCategoriesList;

    /**
     * Initiation of interface's object
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            mListener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemSelectedListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewHierarchy = inflater.inflate(R.layout.list_view_fragment, container, false);
        mCategoryDAOImplementation = new CategoryDAOImplementation(getActivity()
                .getApplicationContext());
        mListView = (ListView)viewHierarchy.findViewById(R.id.lvMain);
        //Async task to get List of Categories from DB
        mGetCategoriesList = new GetCategoriesList();
        mGetCategoriesList.execute();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //start method of interface described in MainActivity
                //method changes fragments
                mListener.onRssItemSelected("category", items.get(position).getId());
            }
        });
        return viewHierarchy;
    }

    /**
     * Async task to get the list of categories
     */
    class GetCategoriesList extends AsyncTask<Void, Void, ArrayList<Category>>{

        //creation and showing of progress dialog
        @Override
        protected void onPreExecute(){
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setTitle("Loading...");
            mProgressDialog.setMessage("Loading List of Categories, please wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected ArrayList<Category> doInBackground(Void... params) {
            ArrayList<Category> categoriesList = null;
            try {
                //open DB connection
                mCategoryDAOImplementation.open();
                //get all categories from DB
                categoriesList = mCategoryDAOImplementation.findAll();
                //close DB connection
                mCategoryDAOImplementation.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return categoriesList;
        }

        @Override
        protected void onPostExecute(ArrayList<Category> categoriesList){
            //close progress dialog
            mProgressDialog.dismiss();
            items = categoriesList;
            //set adapter for ListView
            mListView.setAdapter(new CategoryListAdapter(getActivity(), R.layout.list_view_fragment, items));
        }
    }
}
