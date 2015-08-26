package com.example.student.exploreauckland;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.exploreauckland.database.AttractionDAOImplementation;

import java.sql.SQLException;

/**
 * Fragment with description of attraction in details
 * Created by Aleksandr on 13.08.2015.
 */
public class FragmentPlaceDetails extends Fragment{
    //progress dialog is shown while performing a database query
    private ProgressDialog progressDialog;
    private TextView mPlaceName, mPlaceDescription, mPlaceNote;
    private ImageButton mMapButton, mNoteButton;
    private ImageView mPlaceImage;
    private OnItemSelectedListener mListener;
    private CheckBox placeFavorite;
    private final static int REQUEST_CODE = 1;

    private AttractionDAOImplementation mAttractionDAOImplementation;
    private Attraction mAttraction;
    private int placeId;

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
        View viewHierarchy = inflater.inflate(R.layout.place_description_fragment, container, false);
        mAttractionDAOImplementation = new AttractionDAOImplementation(getActivity()
                .getApplicationContext());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            //get id of chosen in previous fragment attraction from bundle
            placeId = bundle.getInt("ID");
        }

        mPlaceName = (TextView)viewHierarchy.findViewById(R.id.textView_placeName_description);
        mPlaceDescription = (TextView)viewHierarchy.findViewById(R.id.textView_placeDescription_description);
        mPlaceNote = (TextView)viewHierarchy.findViewById(R.id.textView_placeNote_description);
        mMapButton = (ImageButton)viewHierarchy.findViewById(R.id.imageButton_placeMap_description);
        mNoteButton = (ImageButton)viewHierarchy.findViewById(R.id.imageButton_placeNote_description);
        mPlaceImage = (ImageView)viewHierarchy.findViewById(R.id.imageView_placeImage_description);
        placeFavorite = (CheckBox)viewHierarchy.findViewById(R.id.checkBox_placeFavorite_description);

        GetAttraction getAttraction = new GetAttraction();
        getAttraction.execute(placeId);

        //starts activity with google maps
        mMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("type", "place");
                intent.putExtra("id", mAttraction.getId());
                getActivity().startActivity(intent);
            }
        });

        //add attraction to favorite
        placeFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        mAttraction.setFavorite(1);
                    }
                    else{
                        mAttraction.setFavorite(0);
                    }
                //update attraction in the DB
                updateAttraction(mAttraction);
            }
        });

        mNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
        return viewHierarchy;
    }

    /**
     * open input dialog for enter or modify user note
     */
    private void showInputDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        InputDialogFragment inputNoteDialog = new InputDialogFragment();
        inputNoteDialog.setTargetFragment(this, REQUEST_CODE);
        inputNoteDialog.setCancelable(false);
        inputNoteDialog.setDialogTitle("Enter Note");
        Bundle bundle = new Bundle();
        //send existing note to dialog fragment
        bundle.putString("existingNote", mAttraction.getNote());
        inputNoteDialog.setArguments(bundle);
        inputNoteDialog.show(fragmentManager, "Input Dialog");
    }

    /**
     * Method to get note from Dialog Fragment
     * @param requestCode
     * @param resultCode
     * @param data intent data (new note)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            String note = data.getStringExtra("note");
            Toast.makeText(getActivity(), "Input Note:  " + note,
                    Toast.LENGTH_SHORT).show();
            mAttraction.setNote(note);
            //call method to update attraction in DB
            updateAttraction(mAttraction);
        }
    }

    private void updateAttraction (Attraction attraction){
        try {
            //open DB connection
            mAttractionDAOImplementation.open();
            //update attraction in DB
            mAttractionDAOImplementation.updateAttraction(attraction);
            //close DB connection
            mAttractionDAOImplementation.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateView();
    }

    //Update View attributes of this fragment
    public void updateView(){
        mPlaceName.setText(mAttraction.getName());
        mPlaceDescription.setText(mAttraction.getDescription());
        mPlaceImage.setImageResource(getActivity().getResources().getIdentifier(mAttraction.getPicture(),
                "drawable", getActivity().getPackageName()));
        placeFavorite.setChecked(mAttraction.getFavorite() == 1);
        mPlaceNote.setText(mAttraction.getNote());
        if(mAttraction.getCategory() == 3 & mMapButton.getVisibility() == View.VISIBLE){
            //delete map button for festivals
            mMapButton.setVisibility(View.GONE);
        }
    }

    /**
     * Class extends async task to get Attraction from DB
     */
    class GetAttraction extends AsyncTask<Integer, Void, Attraction> {

        //Show progress dialog
        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle("Loading...");
            progressDialog.setMessage("Loading of Attraction details, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Attraction doInBackground(Integer... id) {
            Attraction attraction = null;
            try {
                //open DB connection
                mAttractionDAOImplementation.open();
                //get attraction from DB
                attraction = mAttractionDAOImplementation.findById(placeId);
                //close DB connection
                mAttractionDAOImplementation.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return attraction;
        }

        @Override
        protected void onPostExecute(Attraction attraction){
            //close progress dialog
            progressDialog.dismiss();
            mAttraction = attraction;
            updateView();
        }

    }
}
