package com.matt.androidproficiencyexercise.UserInterfaceUtils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.matt.androidproficiencyexercise.Models.FeedRow;
import com.matt.androidproficiencyexercise.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Matthew Petrenas on 22/02/2018.
 * Array Adapter for displaying a list of FeedRow's in a ListView
 */

public class FeedRowListAdapter extends ArrayAdapter<FeedRow> {
    private ArrayList<FeedRow> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView titleView;
        TextView descriptionView;
        ImageView imageView;
    }

    public FeedRowListAdapter(ArrayList<FeedRow> data, Context context) {
        super(context, R.layout.feed_list_element, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FeedRow dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.feed_list_element, parent, false);
            viewHolder.titleView = convertView.findViewById(R.id.title_view);
            viewHolder.descriptionView = convertView.findViewById(R.id.description_view);
            viewHolder.imageView = convertView.findViewById(R.id.image_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.titleView.setText(dataModel.getTitle());
        viewHolder.descriptionView.setText(dataModel.getDescription());
        if(dataModel.getImageHRef()!="") {
            Picasso
                    .with(mContext)
                    .load(dataModel.getImageHRef())
                    .into(viewHolder.imageView);
        }
        // Return the completed view to render on screen
        return convertView;
    }
}