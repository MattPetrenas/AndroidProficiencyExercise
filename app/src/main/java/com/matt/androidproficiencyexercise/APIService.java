package com.matt.androidproficiencyexercise;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class APIService extends IntentService {
    private static final String ACTION_GET_FEED = "com.matt.androidproficiencyexercise.action.getFeed";

    private static final String EXTRA_RESPONSE_RECIEVER = "com.matt.androidproficiencyexercise.extra.ResponseReciever";

    //Request Status Broadcast Extras
    public static final String STATUS = "status";
    public static final String RESPONSE = "response";

    //Request Statuses
    public static final String ERROR = "error";
    public static final String IN_PROGRESS = "in progress";
    public static final String FINISHED = "finished";

    public static final String FACTS_URI = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json";

    private RequestQueue requestQueue;

    public APIService() {
        super("APIService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionGetFeed(Context context, String responseReciever) {
        Intent intent = new Intent(context, APIService.class);
        intent.setAction(ACTION_GET_FEED);
        intent.putExtra(EXTRA_RESPONSE_RECIEVER, responseReciever);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (requestQueue == null){requestQueue = Volley.newRequestQueue(this);}
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_FEED.equals(action)) {
                final String reciever = intent.getStringExtra(EXTRA_RESPONSE_RECIEVER);
                handleActionGetFeed(reciever);
            }
        }
    }

    private void handleActionGetFeed(final String reciever) {
            final String uri = FACTS_URI;
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, uri, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Response", response.toString());
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.setAction(reciever);
                                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                                broadcastIntent.putExtra(STATUS, FINISHED);
                                broadcastIntent.putExtra(RESPONSE, response.toString());
                                sendBroadcast(broadcastIntent);
                            }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(error.networkResponse!=null) {
                                Log.d("Error Body", error.networkResponse.toString());
                            }
                            else{
                                Log.d("Error", error.getMessage());
                            }
                            broadcastStatusOnly(reciever, ERROR);
                        }
                    });
            requestQueue.add(jsObjRequest);
        }

    private void broadcastStatusOnly(String reciever, String value) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(reciever);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(STATUS, value);
        sendBroadcast(broadcastIntent);
    }
}
