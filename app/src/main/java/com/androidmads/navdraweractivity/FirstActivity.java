package com.androidmads.navdraweractivity;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class FirstActivity extends BaseActivity {
    private static final String TAG = "UsingThingspeakAPI";
    private static final String THINGSPEAK_CHANNEL_ID = "974910";
    private static final String THINGSPEAK_API_KEY = "WPU34ZTOHE00C36P";
    private static final String THINGSPEAK_API_KEY_STRING = "WPU34ZTOHE00C36P";
    private static final String THINGSPEAK_FIELD1 = "field1";
    String uid="";
    private static final String THINGSPEAK_FIELD2 = "field2";
    private static final String THINGSPEAK_FIELD3 = "field3";
    private static final String THINGSPEAK_FIELD4 = "field4";
    private static final String THINGSPEAK_UPDATE_URL = "https://api.thingspeak.com/update?";
    private static final String THINGSPEAK_CHANNEL_URL = "https://api.thingspeak.com/channels/";
    private static final String THINGSPEAK_FEEDS_LAST = "/feeds/last?";
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef;
    private ImageButton a;
    ListView listView;
    LayoutInflater inflater;
    String name[]=new String[100];
    int z=0;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mNamesall = new ArrayList<>();
    public ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<Integer> mImagess = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>(
            Arrays.asList(R.drawable.acorn_squash, R.drawable.artichoke, R.drawable.asparagus, R.drawable.banana_pepper, R.drawable.beetroot, R.drawable.bell_pepper , R.drawable.broccoli, R.drawable.cabbage, R.drawable.carrot, R.drawable.cauliflower, R.drawable.celery, R.drawable.cherry_tomato, R.drawable.chickpea, R.drawable.chilli_pepper, R.drawable.chinese_cabbage, R.drawable.corn, R.drawable.cucumber, R.drawable.eggplant, R.drawable.fennel,  R.drawable.garlic, R.drawable.ghost_pepper, R.drawable.green_beans,R.drawable.green_onions, R.drawable.jalapeno, R.drawable.leek_summer, R.drawable.leek_winter, R.drawable.lentils, R.drawable.lettuce, R.drawable.onion, R.drawable.peas, R.drawable.potato, R.drawable.pumpkin, R.drawable.radish, R.drawable.red_cabbage, R.drawable.red_onion, R.drawable.runner_beans, R.drawable.soybean, R.drawable.sweet_potato, R.drawable.tomato, R.drawable.turnip, R.drawable.wild_garlic, R.drawable.zucchini));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
         View contentView = inflater.inflate(R.layout.activity_first1, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_activity1);
        scheduleJob();
        mDatabase = FirebaseDatabase.getInstance();
        mDbRef = mDatabase.getReference("Donor");
        //arrayList.add("TOO MOIST");
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        uid = user1.getUid();
        readExcelFileFromAssets();
        getImages();
        a = (ImageButton) findViewById(R.id.imageView2);
        a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
    });
        listView=(ListView)findViewById(R.id.list);
        new FetchThingspeakTask().execute();
        for(int h=0;h<=2000000;h++)
        {

        }
    /*    mDbRef.child("Data").child(uid).child("moisture").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                  if(Double.parseDouble(dataSnapshot.getValue().toString())<80)
                {
                    arrayList.add("TOO Moist");
                }
            }

        public void onCancelled(DatabaseError error) {
            Log.w("TAG", "Failed to read value.", error.toException());
        }
    });*/
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);
        getExplore();
    }
    private void getImages(){
        mDatabase = FirebaseDatabase.getInstance();
        mDbRef = mDatabase.getReference("Donor");
        String uid = "";
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        uid = user1.getUid();
        mDbRef.child("Plants").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    name[z] = ds.getValue().toString();
                    // ab[z] = Integer.parseInt(name[z]);
                   z++;
                }
              // int  number[]=new int[z];
                //String abc[]=new String[z];
                for(int i=0;i<z;i++)
                {
                 mNames.add(mNamesall.get(Integer.parseInt(name[i])));
                    mImagess.add(mImages.get(Integer.parseInt(name[i])));
                  //  number[i]=flags[Integer.parseInt(name[i])];
                //    abc[i]=countryList[Integer.parseInt((name[i]))];
                }

            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        initRecyclerView();
    }
    public void getExplore(){

        initRecyclerView1();

    }
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView:init recyclerView");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames,mImagess,name,this);
        recyclerView.setAdapter(adapter) ;
    }
    private void initRecyclerView1(){
        Log.d(TAG, "initRecyclerView:init recyclerView");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter1 adapter = new RecyclerViewAdapter1(mNamesall,mImages,this);
        recyclerView.setAdapter(adapter) ;
    }
    public void cancelJob() {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d("FirstActivity", "Job cancelled");
    }
    public void scheduleJob() {
        ComponentName componentName = new ComponentName(this, BGJobService.class);


        JobInfo info = new JobInfo.Builder(123, componentName)
                .setPersisted(true)
                .setPeriodic(15*60*1000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("TAG", "Job scheduled");
        } else {
            Log.d("TAG", "Job scheduling failed");
        }
    }
    public void readExcelFileFromAssets() {
        try {
            InputStream myInput;
            // initialize asset manager
            AssetManager assetManager = getAssets();
            //  open excel sheet
            myInput = assetManager.open("myexcelsheet.xls");
            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            // We now need something to iterate through the cells.
            Iterator<Row> rowIter = mySheet.rowIterator();
            int rowno =0;

            while (rowIter.hasNext()) {
                Log.e(TAG, " row no "+ rowno );
                HSSFRow myRow = (HSSFRow) rowIter.next();
                if(rowno !=0) {

                    Iterator<Cell> cellIter = myRow.cellIterator();
                    int colno = 0;
                    String sno = "", date = "", det = "";
                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (colno == 0) {
                            sno = myCell.toString();
                        } else if (colno == 1) {
                            date = myCell.toString();
                        } else if (colno == 2) {
                            det = myCell.toString();
                        }
                        colno++;
                        Log.e(TAG, " Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());
                    }
                    mNamesall.add(sno);

                }
                rowno++;
            }
        } catch (Exception e) {
            Log.e(TAG, "error "+ e.toString());
        }
    }
    class FetchThingspeakTask extends AsyncTask<Void, Void, String> {
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
                Toast.makeText(FirstActivity.this, "There was an error", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject channel = (JSONObject) new JSONTokener(response).nextValue();
                double v1 = channel.getDouble(THINGSPEAK_FIELD1);
                double v2 = channel.getDouble(THINGSPEAK_FIELD2);
                double v3 = channel.getDouble(THINGSPEAK_FIELD3);
                double v4 = channel.getDouble(THINGSPEAK_FIELD4);
                String temperature = String.valueOf(v1);
                String t="temperature";
                String humidity = String.valueOf(v2);
                String soil_moisture = String.valueOf(v3);

                String light = String.valueOf(v4);
                //int abc=Integer.parseInt(temperature);

                mDbRef.child("Data").child(uid).child("moisture").setValue(v3);
                if(v3<80)
                {
                    arrayList.add("TOO MOIST");
                }
                /*if(Integer.parseInt(temperature)!=25)
                {
                    arrayList.add("Temperature under 25");
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
