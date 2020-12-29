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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

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

import java.io.InputStream;
import java.util.Iterator;

public class myplants extends BaseActivity {


    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef;
    int ab[];
    private static final String TAG = "TAG";
    private ImageButton a;
    Button add;
    ListView simpleList;
    String name[]=new String[5];
    String countryList[]=new String[42];
    private int flags[] = {R.drawable.acorn_squash, R.drawable.artichoke, R.drawable.asparagus, R.drawable.banana_pepper, R.drawable.beetroot, R.drawable.bell_pepper , R.drawable.broccoli, R.drawable.cabbage, R.drawable.carrot, R.drawable.cauliflower, R.drawable.celery, R.drawable.cherry_tomato, R.drawable.chickpea, R.drawable.chilli_pepper, R.drawable.chinese_cabbage, R.drawable.corn, R.drawable.cucumber, R.drawable.eggplant, R.drawable.fennel,  R.drawable.garlic, R.drawable.ghost_pepper, R.drawable.green_beans,R.drawable.green_onions, R.drawable.jalapeno, R.drawable.leek_summer, R.drawable.leek_winter, R.drawable.lentils, R.drawable.lettuce, R.drawable.onion, R.drawable.peas, R.drawable.potato, R.drawable.pumpkin, R.drawable.radish, R.drawable.red_cabbage, R.drawable.red_onion, R.drawable.runner_beans, R.drawable.soybean, R.drawable.sweet_potato, R.drawable.tomato, R.drawable.turnip, R.drawable.wild_garlic, R.drawable.zucchini};

    int b=0,z=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_myplants);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_myplants, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_activity5);
        readExcelFileFromAssets();
        a = (ImageButton) findViewById(R.id.imageView8);
        a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        add=(Button)findViewById(R.id.button7) ;
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Main8Activity.class));
            }
        });
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
                         int  number[]=new int[z];
                        String abc[]=new String[z];
                        for(int i=0;i<z;i++)
                        {
                            number[i]=flags[Integer.parseInt(name[i])];
                            abc[i]=countryList[Integer.parseInt((name[i]))];
                        }
                        GridView gridview = (GridView) findViewById(R.id.gridView);
                        ImageAdapter customAdapter = new ImageAdapter (getApplicationContext(),abc,number);
                        gridview.setAdapter(customAdapter);
                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                                //LauncherActivity.ListItem user = (LauncherActivity.ListItem) simpleList.getItemAtPosition(position);
                                // Intent intent = new Intent(getApplicationContext(), Main5Activity.class);
                                Intent intent = new Intent(getApplicationContext(),Main5Activity.class);
                                intent.putExtra("myArr1", countryList);
                                intent.putExtra("myArr2",ab);
                                intent.putExtra("message_key2",Integer.parseInt(name[position]));
                                startActivity(intent);
                            }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w("TAG", "Failed to read value.", error.toException());
                    }
                });


            }
    public void readExcelFileFromAssets() {
        int i=0;
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


    }
        }