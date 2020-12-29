package com.androidmads.navdraweractivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Iterator;

public class Main5Activity extends  BaseActivity {

    private TextView txtProgress, txtProgress1, txtProgress2, txtProgress3, namesensor;
    private ImageView first,second,third,sun;
    private ProgressBar progressBar, progressBar1, progressBar2;
    String name[] = new String[42];

    private int flags[] = {R.drawable.acorn_squash, R.drawable.artichoke, R.drawable.asparagus, R.drawable.banana_pepper, R.drawable.beetroot, R.drawable.bell_pepper , R.drawable.broccoli, R.drawable.cabbage, R.drawable.carrot, R.drawable.cauliflower, R.drawable.celery, R.drawable.cherry_tomato, R.drawable.chickpea, R.drawable.chilli_pepper, R.drawable.chinese_cabbage, R.drawable.corn, R.drawable.cucumber, R.drawable.eggplant, R.drawable.fennel,  R.drawable.garlic, R.drawable.ghost_pepper, R.drawable.green_beans,R.drawable.green_onions, R.drawable.jalapeno, R.drawable.leek_summer, R.drawable.leek_winter, R.drawable.lentils, R.drawable.lettuce, R.drawable.onion, R.drawable.peas, R.drawable.potato, R.drawable.pumpkin, R.drawable.radish, R.drawable.red_cabbage, R.drawable.red_onion, R.drawable.runner_beans, R.drawable.soybean, R.drawable.sweet_potato, R.drawable.tomato, R.drawable.turnip, R.drawable.wild_garlic, R.drawable.zucchini};
    int i = 0;
    private int pStatus = 0;
    int b[];
    private ImageView a;
    String days[] = new String[42];
    String outdoor[] = new String[42];
    String indoor[] = new String[42];
    String suitable[] = new String[42];
    String ph[] = new String[42];
    //String name[] = new String[42];
    String sow_depth[] = new String[42];
    String distance[] = new String[42];
    String temperature[] = new String[42];
    String moisture[] = new String[42];
    private ImageButton u;
    int c = -1;
    //private Handler handler = new Handler();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_main5, null, false);
        drawer.addView(contentView, 0);
        //navigationView.setCheckedItem(R.id.nav_activity4);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hello First Activity", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Intent i = getIntent();
        int position = i.getExtras().getInt("message_key2");

        readExcelFileFromAssets();
        a = (ImageView) findViewById(R.id.imageView2);
        u = (ImageButton) findViewById(R.id.imageButton2);
        u.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        namesensor = (TextView) findViewById(R.id.textView4);
        txtProgress2 = (TextView) findViewById(R.id.txtProgress);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //txtProgress3 = (TextView) findViewById(R.id.txtProgress2);
        //progressBar1 = (ProgressBar) findViewById(R.id.progressBar3);
        txtProgress = (TextView) findViewById(R.id.textView6);
        //progressBar2 = (ProgressBar) findViewById(R.id.tempGauge);
        txtProgress1 = (TextView) findViewById(R.id.textView7);
        first=(ImageView)findViewById(R.id.imageView9);
         second=(ImageView)findViewById(R.id.imageView11);
         third=(ImageView)findViewById(R.id.imageView15);
        sun=(ImageView)findViewById(R.id.imageView20);
        namesensor.setText(name[position]);
        a.setImageResource(flags[position]);
        new FetchThingspeakTask().execute();


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
            int rowno = 0;

            while (rowIter.hasNext()) {
                Log.e(TAG, " row no " + rowno);
                HSSFRow myRow = (HSSFRow) rowIter.next();
                if (rowno != 0) {

                    Iterator<Cell> cellIter = myRow.cellIterator();
                    int colno = 0;
                    String sno = "", date = "", det = "", ab = "", ac = "", ad = "", ae = "", af = "", ag = "", ah = "", ai = "", aj = "", ak = "";
                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (colno == 0) {
                            sno = myCell.toString();
                        } else if (colno == 1) {
                            date = myCell.toString();
                        } else if (colno == 2) {
                            det = myCell.toString();
                        } else if (colno == 3) {
                            ab = myCell.toString();
                        } else if (colno == 4) {
                            ac = myCell.toString();
                        } else if (colno == 5) {
                            ad = myCell.toString();
                        } else if (colno == 6) {
                            ae = myCell.toString();
                        } else if (colno == 7) {
                            af = myCell.toString();
                        } else if (colno == 8) {
                            ag = myCell.toString();
                        } else if (colno == 9) {
                            ah = myCell.toString();
                        } else if (colno == 10) {
                            ai = myCell.toString();
                        }

                        colno++;
                        Log.e(TAG, " Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());
                    }
                    name[i] = sno;
                    sow_depth[i] = date;
                    distance[i] = det;
                    temperature[i] = ab;
                    moisture[i] = ac;
                    ph[i] = ad;
                    days[i] = ae;
                    outdoor[i] = af;
                    indoor[i] = ag;
                    suitable[i] = (ah+"-"+ai);
                                        i++;
                }
                rowno++;
            }
        } catch (Exception e) {
            Log.e(TAG, "error " + e.toString());
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
                Toast.makeText(Main5Activity.this, "There was an error", Toast.LENGTH_SHORT).show();
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
                int ma=(int)Double.parseDouble(w);
                if(ma==1)
                {
                    txtProgress1.setText("SHADOW");
                    sun.setImageResource(R.drawable.cloud);
                }
                else
                {
                    txtProgress1.setText("FULL SUN");
                    sun.setImageResource(R.drawable.sunny);

                }
                //progressBar.setProgress(100);
                int za=(int)Double.parseDouble(t);
                txtProgress.setText(za+"    °C");
               // progressBar1.setProgress(50);
               // txtProgress1.setText(w);
                // progressBar2.setProgress(60);
                 za=(int)Double.parseDouble(u);
                txtProgress2.setText(za+" %");

                int ya=(int)Double.parseDouble(v);

                if(ya<=50)
                {
                    first.setImageResource(R.drawable.water);
                    second.setImageResource(R.drawable.water);
                    third.setImageResource(R.drawable.water);

                }
                else if(ya>50&&ya<80)
                {
                    first.setImageResource(R.drawable.drop);
                    second.setImageResource(R.drawable.water);
                    third.setImageResource(R.drawable.water);
                }
                else
                {
                    first.setImageResource(R.drawable.drop);
                    second.setImageResource(R.drawable.drop);
                    third.setImageResource(R.drawable.water);
                }
                refresh(1000);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void refresh(int milliseconds) {
        final Handler handler=new Handler();
        final Runnable runnable=new Runnable() {
            @Override
            public void run() {
                new FetchThingspeakTask().execute();
            }
        };
        handler.postDelayed(runnable,1000);
    }
}