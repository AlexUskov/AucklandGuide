package com.example.student.exploreauckland;

import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.student.exploreauckland.database.AttractionDAOImplementation;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Aleksandr on 10.08.2015.
 */
public class PlaceListAdapter extends ArrayAdapter implements CompoundButton.OnCheckedChangeListener {

    private ArrayList<Attraction> placesList;
    private Context context;
    private Attraction p;
    private AttractionDAOImplementation mAttractionDAOImplementation;
    //array of check boxes statuses
    private SparseBooleanArray mCheckStates;

    //Constructor
    public PlaceListAdapter(Context context, int textViewResourceId, ArrayList<Attraction> places) {
        super(context, textViewResourceId, places);
        this.placesList = places;
        this.context = context;
        mAttractionDAOImplementation = new AttractionDAOImplementation(context);
        mCheckStates = new SparseBooleanArray(places.size());//
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return placesList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.place_list_item, null);
            vh = new ViewHolder();
            vh.placeTitle = (TextView) convertView.findViewById(R.id.place_title);
            vh.placeDescription = (TextView)convertView.findViewById(R.id.place_description);
            vh.favorite = (CheckBox)convertView.findViewById(R.id.favorite_checkBox);
            vh.placeIco = (ImageView)convertView.findViewById(R.id.place_icon);
            vh.mapButton = (ImageButton)convertView.findViewById(R.id.mapButton);
            vh.mapButton.setFocusable(false);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        p = placesList.get(position);

        vh.placeTitle.setText(p.getName());
        vh.placeDescription.setText(p.getShortDescription());
        vh.placeIco.setImageResource(context.getResources().getIdentifier(p.getPicture(),
                "drawable", context.getPackageName()));

        vh.favorite.setTag(position);
        vh.favorite.setChecked(mCheckStates.get(position, p.getFavorite() == 1));
        vh.favorite.setOnCheckedChangeListener(this);
        vh.mapButton.setVisibility(View.VISIBLE);

        //map button unable for festivas
        if(p.getCategory() == 3){
            vh.mapButton.setEnabled(false);
            vh.mapButton.setVisibility(View.INVISIBLE);
        }

        vh.mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Attraction a = placesList.get(position);
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("type", "place");
                intent.putExtra("id", a.getId());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView placeTitle, placeDescription;
        private ImageView placeIco;
        private CheckBox favorite;
        private ImageButton mapButton;
    }

    //get info about adding to favorite
    public boolean isChecked(int position) {
        return mCheckStates.get(position, false);
    }

    //add attraction to
    public void setChecked(int position, boolean isChecked) {
        mCheckStates.put(position, isChecked);
    }

//    public void toggle(int position) {
//        setChecked(position, !isChecked(position));
//    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {
        mCheckStates.put((Integer) buttonView.getTag(), isChecked);

        try {
            mAttractionDAOImplementation.open();
            Attraction a = placesList.get((Integer) buttonView.getTag());
            if(isChecked) {
                a.setFavorite(1);
            }
            else{
                a.setFavorite(0);
            }
            //update info in DB
            mAttractionDAOImplementation.updateAttraction(a);
            mAttractionDAOImplementation.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
