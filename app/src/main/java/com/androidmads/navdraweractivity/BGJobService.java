package com.androidmads.navdraweractivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BGJobService extends JobService {
    Boolean jobcancel=false;
    private static final String TAG = "UsingThingspeakAPI";
    private static final String THINGSPEAK_CHANNEL_ID = "974910";
    private static final String THINGSPEAK_API_KEY = "WPU34ZTOHE00C36P";
    private static final String THINGSPEAK_API_KEY_STRING = "WPU34ZTOHE00C36P";
    /* Be sure to use the correct fields for your own app*/
    private static final String THINGSPEAK_FIELD1 = "field1";
    private static final String THINGSPEAK_FIELD2 = "field2";
    private static final String THINGSPEAK_FIELD3 = "field3";
    private static final String THINGSPEAK_FIELD4 = "field4";
    private static final String THINGSPEAK_UPDATE_URL = "https://api.thingspeak.com/update?";
    private static final String THINGSPEAK_CHANNEL_URL = "https://api.thingspeak.com/channels/";
    private static final String THINGSPEAK_FEEDS_LAST = "/feeds/last?";
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        if(jobcancel==true)
        {
            return true;
        }
        Log.d("TAg","ENter Job");
        new BGJobService.FetchThingspeakTask().execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        jobcancel=true;
        return false;
    }
    class FetchThingspeakTask extends AsyncTask<Void, Void, String> {
        private FirebaseDatabase mDatabase;
        private DatabaseReference mDbRef;
        protected void onPreExecute() {
            //t2.setText("Fetching Data from Server.Please Wait...");
        }
        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(THINGSPEAK_CHANNEL_URL + THINGSPEAK_CHANNEL_ID +
                        THINGSPEAK_FEEDS_LAST + THINGSPEAK_API_KEY_STRING + "=" +
                        THINGSPEAK_API_KEY + "");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }
        protected void onPostExecute(String response) {
            if(response == null) {
                Toast.makeText(BGJobService.this, "There was an error", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject channel = (JSONObject) new JSONTokener(response).nextValue();
                double v1 = channel.getDouble(THINGSPEAK_FIELD1);
                double v2 = channel.getDouble(THINGSPEAK_FIELD2);
                double v3 = channel.getDouble(THINGSPEAK_FIELD3);
                double v4 = channel.getDouble(THINGSPEAK_FIELD4);
                String t = String.valueOf(v1);
                String u = String.valueOf(v2);
                String v = String.valueOf(v3);
                String w = String.valueOf(v4);
                mDatabase = FirebaseDatabase.getInstance();
                mDbRef = mDatabase.getReference("Donor");
                String uid = "";
                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                uid = user1.getUid();
                mDbRef.child("Data").child(uid).child("temperature").setValue(v1);
                mDbRef.child("Data").child(uid).child("humidity").setValue(v2);
                mDbRef.child("Data").child(uid).child("moisture").setValue(v3);
                mDbRef.child("Data").child(uid).child("Light").setValue(v4);
                Log.d("INSERT","DATA INSERTED");
                if(v3<80) {
                    String message = "Excess Water";
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(
                            BGJobService.this, notification.CHANNEL_ID
                    )
                            .setSmallIcon(R.drawable.alerts)
                            .setContentTitle("Alert")
                            .setContentText(message)
                            .setAutoCancel(true);

                    Intent in = new Intent(BGJobService.this, FirstActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    in.putExtra("message", message);

                    PendingIntent pendingIntent = PendingIntent.getActivity(BGJobService.this,
                            0, in, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(
                            Context.NOTIFICATION_SERVICE
                    );

                    notificationManager.notify(0, builder.build());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
