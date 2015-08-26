package com.example.student.exploreauckland;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aleksandr on 10.08.2015.
 */
public class CategoryListAdapter extends ArrayAdapter {

    private ArrayList<Category> categoriesList;
    private Context context;
    private Category category;

    /**
     * Constructor
     * @param context - app context
     * @param textViewResourceId
     * @param categories - list of categories
     */
    public CategoryListAdapter(Context context, int textViewResourceId, ArrayList<Category> categories) {
        super(context, textViewResourceId, categories);
        this.categoriesList = categories;
        this.context = context;
    }

    /**
     * @param position - element position in listView
     * @param convertView
     * @param parent
     * @return element view
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        ViewHolder vh;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.category_list_item, null);
            vh = new ViewHolder();
            vh.categoryTitle = (TextView) v.findViewById(R.id.category_title);
            vh.categoryView = (ImageView) v.findViewById(R.id.category_icon);
            vh.mapButton = (ImageButton)v.findViewById(R.id.imageButton_map_category);
            vh.mapButton.setFocusable(false);
            v.setTag(vh);
        } else {
            vh = (ViewHolder) v.getTag();
        }
        //get category on current position from ArrayList
        category = categoriesList.get(position);

        vh.categoryTitle.setText(category.getName());
        vh.categoryView.setImageResource(context.getResources().getIdentifier(category.getIcon(),
                "drawable", context.getPackageName()));
        //set listener to open google maps with all category's items
        vh.mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category cat = categoriesList.get(position);
                Intent intent = new Intent(context, MapsActivity.class);
                if (cat.getId() == 7) {
                    intent.putExtra("type", "favorite");
                } else {
                    intent.putExtra("type", "category");
                }
                intent.putExtra("id", cat.getId());
                context.startActivity(intent);
            }
        });
        return v;
    }

    //view holder class
    class ViewHolder{
        private ImageButton mapButton;
        private ImageView categoryView;
        private TextView categoryTitle;
    }
}
