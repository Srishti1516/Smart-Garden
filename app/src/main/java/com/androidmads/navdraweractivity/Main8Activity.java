package com.androidmads.navdraweractivity;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class Main8Activity extends BaseActivity {
    String TAG ="main";
    int i=0;
    public TextView textView;
    Button add;
    ListView simpleList;
    String countryList[] = new String[42];
    private int flags[] = {R.drawable.acorn_squash, R.drawable.artichoke, R.drawable.asparagus, R.drawable.banana_pepper, R.drawable.beetroot, R.drawable.bell_pepper , R.drawable.broccoli, R.drawable.cabbage, R.drawable.carrot, R.drawable.cauliflower, R.drawable.celery, R.drawable.cherry_tomato, R.drawable.chickpea, R.drawable.chilli_pepper, R.drawable.chinese_cabbage, R.drawable.corn, R.drawable.cucumber, R.drawable.eggplant, R.drawable.fennel,  R.drawable.garlic, R.drawable.ghost_pepper, R.drawable.green_beans,R.drawable.green_onions, R.drawable.jalapeno, R.drawable.leek_summer, R.drawable.leek_winter, R.drawable.lentils, R.drawable.lettuce, R.drawable.onion, R.drawable.peas, R.drawable.potato, R.drawable.pumpkin, R.drawable.radish, R.drawable.red_cabbage, R.drawable.red_onion, R.drawable.runner_beans, R.drawable.soybean, R.drawable.sweet_potato, R.drawable.tomato, R.drawable.turnip, R.drawable.wild_garlic, R.drawable.zucchini};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main8);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_main8, null, false);
        drawer.addView(contentView, 0);
        ImageButton a = (ImageButton) findViewById(R.id.imageView8);
        a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });


        navigationView.setCheckedItem(R.id.nav_activity7);
       // textView = findViewById(R.id.textview);
        readExcelFileFromAssets();
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
                        countryList[i] = sno;
                        i++;

                }
                rowno++;
            }
        } catch (Exception e) {
            Log.e(TAG, "error "+ e.toString());
        }

        GridView gridview = (GridView) findViewById(R.id.gridView);
        ImageAdapter customAdapter = new ImageAdapter (getApplicationContext(),countryList,flags);
        gridview.setAdapter(customAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
               // LauncherActivity.ListItem user = (LauncherActivity.ListItem) simpleList.getItemAtPosition(position);
               // Intent intent = new Intent(getApplicationContext(), Main5Activity.class);
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                intent.putExtra("myArr1", countryList);
                intent.putExtra("myArr", flags);
                intent.putExtra("message_key2", position);
                startActivity(intent);
            }
        });
    }
}