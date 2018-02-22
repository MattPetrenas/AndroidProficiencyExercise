package com.matt.androidproficiencyexercise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.matt.androidproficiencyexercise.Models.FeedRow;
import com.matt.androidproficiencyexercise.UserInterfaceUtils.FeedRowListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Matthew Petrenas on 22/02/2018.
 * This Application's only activity. It will display an info feed from and API request.
 */

public class MainActivity extends AppCompatActivity {
    private TextView feedTitleView;
    private ListView feedRowsListView;
    private FeedRowListAdapter feedRowListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialise the UI Elements before making API calls.
        feedTitleView = (TextView) findViewById(R.id.feed_title);
        feedRowsListView = (ListView) findViewById(R.id.feed_list_view);
        downloadFeed();
    }

    private void downloadFeed() {
        //This function will make an API call via the API Service via a broadcast.

        //Temp Code for placeholder:
        //This Code Parses the JSON resposne.
        JSONObject json = APIService.getJson();
        Log.d("TEST", json.toString());
        try {
            //Convert from jSONArray  to ArrayList<Model>
            ArrayList<FeedRow> listdata = new ArrayList<FeedRow>();
            JSONArray jArray = json.getJSONArray("rows");
            if (jArray != null) {
                for (int i = 0; i < jArray.length(); i++) {
                    listdata.add(new FeedRow(jArray.getJSONObject(i)));
                }
            }
            loadFeed(json.getString("title"), listdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadFeed(String title, ArrayList<FeedRow> rows) {
        //This function takes the required data and updates the UI.
        feedTitleView.setText(title);
        feedRowListAdapter = new FeedRowListAdapter(rows, this);
        feedRowsListView.setAdapter(feedRowListAdapter);

    }
}
