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

import com.example.student.exploreauckland.database.AttractionDAOImplementation;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Fragment with list of attractions
 * Created by Aleksandr on 10.08.2015.
 */
public class FragmentListPlace extends Fragment {
    //progress dialog is shown while performing a database query
    private ProgressDialog progressDialog;
    private OnItemSelectedListener mListener;
    private GetAttractionList mGetAttractionsList;
    private ListView mListView;
    private AttractionDAOImplementation mAttractionDAOImplementation;
    private ArrayList<Attraction> items;
    private int categoryId;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            //Interface's object
            mListener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewHierarchy = inflater.inflate(R.layout.list_view_places, container, false);
        mAttractionDAOImplementation = new AttractionDAOImplementation(getActivity()
                .getApplicationContext());
        mListView = (ListView)viewHierarchy.findViewById(R.id.lvPlaces);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            //get id of chosen in previous fragment category from bundle
            categoryId = bundle.getInt("ID");
        }
        //Async task to get List of Categories from DB
        mGetAttractionsList = new GetAttractionList();
        mGetAttractionsList.execute(categoryId);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //start method of interface described in MainActivity
                //method changes fragments
                mListener.onRssItemSelected("place", items.get(position).getId());
            }
        });

        return viewHierarchy;
    }

    /**
     * Class extends async task to get List of attractions to display in the fragment
     */
    class GetAttractionList extends AsyncTask<Integer, Void, ArrayList<Attraction>>{

        //creation and showing of progress dialog
        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle("Loading...");
            progressDialog.setMessage("Loading List of Categories, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected ArrayList<Attraction> doInBackground(Integer... id) {
            ArrayList<Attraction> attractionsList = null;
            try {
                //open DB connection
                mAttractionDAOImplementation.open();
                if(id[0] == 7){
                    //get list of all favorites attractions
                    attractionsList = mAttractionDAOImplementation.findAllFavorite();
                }else {
                    //get list of all attraction by category
                    attractionsList = mAttractionDAOImplementation.findByCategory(id[0]);
                }
                //close DB connection
                mAttractionDAOImplementation.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return attractionsList;
        }

        @Override
        protected void onPostExecute(ArrayList<Attraction> attractionsList){
            //close progress dialog
            progressDialog.dismiss();
            items = attractionsList;
            //set adapter for listView
            mListView.setAdapter(new PlaceListAdapter(getActivity(), R.layout.list_view_places, items));
        }
    }
}

