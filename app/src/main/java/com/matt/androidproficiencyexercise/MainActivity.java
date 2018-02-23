package com.matt.androidproficiencyexercise;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private TextView feedTitleView, requestStatusView;
    private ListView feedRowsListView;
    private FeedRowListAdapter feedRowListAdapter;

    private SyncReceiver syncReceiver;
    private boolean recieverRegistered = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialise the UI Elements before making API calls.
        feedTitleView = (TextView) findViewById(R.id.feed_title);
        requestStatusView = (TextView) findViewById(R.id.request_status);
        feedRowsListView = (ListView) findViewById(R.id.feed_list_view);
        downloadFeed();
        if(!recieverRegistered) {
            IntentFilter filter = new IntentFilter(SyncReceiver.receiver);
            filter.addCategory(Intent.CATEGORY_DEFAULT);
            SyncReceiver syncReceiver = new SyncReceiver();
            registerReceiver(syncReceiver, filter);
            recieverRegistered=true;
        }
    }

    private void downloadFeed() {
        //This function will make an API call via the API Service via a broadcast.
        APIService.startActionGetFeed(this, SyncReceiver.receiver);
        //Temp Code for placeholder:
        //This Code Parses the JSON resposne.
    }

    private void loadFeed(String title, ArrayList<FeedRow> rows) {
        //This function takes the required data and updates the UI.
        feedTitleView.setText(title);
        feedRowListAdapter = new FeedRowListAdapter(rows, this);
        feedRowsListView.setAdapter(feedRowListAdapter);

    }

    public class SyncReceiver extends BroadcastReceiver {
        public static final String receiver =  "MainActivity.ReponseReciever";
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = intent.getStringExtra(APIService.STATUS);
            switch(status){
                case APIService.ERROR:
                    processError();
                    break;
                case APIService.IN_PROGRESS:
                    showProgress();
                    break;
                case APIService.FINISHED:
                    processResult(context, intent);
                    break;
                default:
                    break;
            }
        }
        private void processError(){
            requestStatusView.setText("ERROR!");
        }
        private void showProgress(){
            requestStatusView.setText("Syncing...");
        }

        private void processResult(Context context, Intent intent){
            requestStatusView.setText("Done.");
            String rawPayload = intent.getStringExtra(APIService.RESPONSE);
            try {
                JSONObject jsonPayload = new JSONObject(rawPayload);
                //Convert from jSONArray  to ArrayList<Model>
                ArrayList<FeedRow> listdata = new ArrayList<FeedRow>();
                JSONArray jArray = jsonPayload.getJSONArray("rows");
                if (jArray != null) {
                    for (int i = 0; i < jArray.length(); i++) {
                        listdata.add(new FeedRow(jArray.getJSONObject(i)));
                    }
                }
                loadFeed(jsonPayload.getString("title"), listdata);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause()
    {
        recieverRegistered = false;
        unregisterReceiver(syncReceiver);
        super.onStop();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(!recieverRegistered) {
            IntentFilter filter = new IntentFilter(SyncReceiver.receiver);
            filter.addCategory(Intent.CATEGORY_DEFAULT);
            syncReceiver = new SyncReceiver();
            registerReceiver(syncReceiver, filter);
            recieverRegistered=true;
        }

    }
}
