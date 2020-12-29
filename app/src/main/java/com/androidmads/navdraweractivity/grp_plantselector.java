package com.androidmads.navdraweractivity;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

import java.io.InputStream;
import java.util.Iterator;

public class grp_plantselector extends BaseActivity {
    String TAG ="main";
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef,mDatabaseReference;
    private ImageView image;
    ImageButton sd,distant,temp,moist,phh,day,outdor,indor,suit;
    private TextView Name,Sow_depth,Distance,Temperature,Moisture,Ph,Days,Outdoor,Indoor,Suitable;
    String name[] = new String[42];
    String sow_depth[] = new String[42];
    String distance[] = new String[42];
    String temperature[] = new String[42];
    String moisture[] = new String[42];
    Button button;
    Button buttona;
    String ph[] = new String[42];
    String uid="";
    String days[] = new String[42];
    String outdoor[] = new String[42];
    String indoor[] = new String[42];
    String suitable[] = new String[42];
    private int flags[] = {R.drawable.acorn_squash, R.drawable.artichoke, R.drawable.asparagus, R.drawable.banana_pepper, R.drawable.beetroot, R.drawable.bell_pepper , R.drawable.broccoli, R.drawable.cabbage, R.drawable.carrot, R.drawable.cauliflower, R.drawable.celery, R.drawable.cherry_tomato, R.drawable.chickpea, R.drawable.chilli_pepper, R.drawable.chinese_cabbage, R.drawable.corn, R.drawable.cucumber, R.drawable.eggplant, R.drawable.fennel,  R.drawable.garlic, R.drawable.ghost_pepper, R.drawable.green_beans,R.drawable.green_onions, R.drawable.jalapeno, R.drawable.leek_summer, R.drawable.leek_winter, R.drawable.lentils, R.drawable.lettuce, R.drawable.onion, R.drawable.peas, R.drawable.potato, R.drawable.pumpkin, R.drawable.radish, R.drawable.red_cabbage, R.drawable.red_onion, R.drawable.runner_beans, R.drawable.soybean, R.drawable.sweet_potato, R.drawable.tomato, R.drawable.turnip, R.drawable.wild_garlic, R.drawable.zucchini};
    int i=0;
    int c=0,num=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_grp_plantselector, null, false);
        drawer.addView(contentView, 0);
        // navigationView.setCheckedItem(R.id.nav_activity2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hello Second Activity", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        sd=(ImageButton) findViewById(R.id.imageButton2);
        sd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(grp_plantselector.this, "Sow Depth", Toast.LENGTH_SHORT).show();
            }
        });
        distant=(ImageButton) findViewById(R.id.imageButton4);
        distant.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(grp_plantselector.this, "Distance", Toast.LENGTH_SHORT).show();
            }
        });
        temp=(ImageButton) findViewById(R.id.imageButton5);
        temp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(grp_plantselector.this, "Sunlight", Toast.LENGTH_SHORT).show();
            }
        });
        moist=(ImageButton) findViewById(R.id.imageButton6);
        moist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(grp_plantselector.this, "moisture", Toast.LENGTH_SHORT).show();
            }
        });
        phh=(ImageButton) findViewById(R.id.imageButton7);
        phh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(grp_plantselector.this, "PH", Toast.LENGTH_SHORT).show();
            }
        });
        day=(ImageButton) findViewById(R.id.imageButton8);
        day.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(grp_plantselector.this, "Days To Harvest", Toast.LENGTH_SHORT).show();
            }
        });
        outdor=(ImageButton) findViewById(R.id.imageButton9);
        outdor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(grp_plantselector.this, "Can It Be Planted Outdoors", Toast.LENGTH_SHORT).show();
            }
        });
        indor=(ImageButton) findViewById(R.id.imageButton11);
        indor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //startActivity(new Intent(SecondActivity.this,Main9Activity.class));
                Toast.makeText(grp_plantselector.this, "Can It Be Planted Indoors", Toast.LENGTH_SHORT).show();
            }
        });
        suit=(ImageButton) findViewById(R.id.imageButton12);
        suit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(grp_plantselector.this, "Suitable Temperature Range", Toast.LENGTH_SHORT).show();
            }
        });
        image=(ImageView) findViewById(R.id.imageView5);
        Name = (TextView) findViewById(R.id.textView12);
        Sow_depth = (TextView) findViewById(R.id.textView13);
        Distance = (TextView) findViewById(R.id.textView14);
        Temperature= (TextView) findViewById(R.id.textView15);
        Moisture = (TextView) findViewById(R.id.textView16);
        Ph = (TextView) findViewById(R.id.textView17);
        Days = (TextView) findViewById(R.id.textView18);
        Outdoor = (TextView) findViewById(R.id.textView19);
        Indoor = (TextView) findViewById(R.id.textView20);
        Suitable = (TextView) findViewById(R.id.textView21);
        readExcelFileFromAssets();

        Intent intent = getIntent();
        c = intent.getIntExtra("message_key2",c);
        image.setImageResource(flags[c]);
        Name.setText(name[c]);
        Sow_depth.setText(sow_depth[c]);
        Distance.setText(distance[c]);
        Temperature.setText(temperature[c]);
        Moisture.setText(moisture[c]);
        Ph.setText(ph[c]);
        Days.setText(days[c]);
        Outdoor.setText(outdoor[c]);
        Indoor.setText(indoor[c]);
        Suitable.setText(suitable[c]);
        button = (Button) findViewById(R.id.button5);
        buttona = (Button) findViewById(R.id.button6);
        mDatabase = FirebaseDatabase.getInstance();
        mDbRef = mDatabase.getReference("Donor");
        uid = intent.getExtras().getString("NAMESS");
        button.setText("Add to Favourite");
        buttona.setText("Add To MyPlants");
        mDbRef.child("favourite").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String z = ds.getValue().toString();// ab[z] = Integer.parseInt(name[z]);
                    if (c == Integer.parseInt(z)) {
                        button.setText("Delete From Favourite");
                    }
                }
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String a = Name.getText().toString();
                        if (button.getText().toString() == "Delete From Favourite") {
                            mDbRef.child("favourite").child(uid).child(a).removeValue();
                            button.setText("Add To Favourite");
                            Toast.makeText(grp_plantselector.this, "Removed From Favourite", Toast.LENGTH_SHORT).show();
                        } else {
                            mDbRef.child("favourite").child(uid).child(a).setValue(c);
                            Toast.makeText(grp_plantselector.this, "Added To Favourite", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
        mDbRef.child("Plants").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String y = ds.getValue().toString();// ab[z] = Integer.parseInt(name[z]);
                    if (c == Integer.parseInt(y)) {
                        buttona.setText("Delete From MyPlants");
                    }
                }
                buttona.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        String x = Name.getText().toString();

                        if (buttona.getText().toString() == "Delete From MyPlants")
                        {
                            mDbRef.child("Plants").child(uid).child(x).removeValue();
                            Toast.makeText(grp_plantselector.this, "Removed From My Plants", Toast.LENGTH_SHORT).show();
                            buttona.setText("Add To MyPlants");
                        } else
                        {
                            mDbRef.child("Plants").child(uid).child(x).setValue(c);
                            Toast.makeText(grp_plantselector.this, "Added To My Plants", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_menu_second) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    String sno = "", date = "", det = "",ab="",ac="",ad="",ae="",af="",ag="",ah="",ai="",aj="",ak="";
                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (colno == 0) {
                            sno = myCell.toString();
                        } else if (colno == 1) {
                            date = myCell.toString();
                        } else if (colno == 2) {
                            det = myCell.toString();
                        }
                        else if (colno == 3) {
                            ab = myCell.toString();
                        }
                        else if (colno == 4) {
                            ac = myCell.toString();
                        }
                        else if (colno == 5) {
                            ad = myCell.toString();
                        }else if (colno == 6) {
                            ae = myCell.toString();
                        }else if (colno == 7) {
                            af = myCell.toString();
                        }else if (colno == 8) {
                            ag = myCell.toString();
                        }else if (colno == 9) {
                            ah = myCell.toString();
                        }
                        else if (colno == 10) {
                            ai= myCell.toString();
                        }

                        colno++;
                        Log.e(TAG, " Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());
                    }
                    name[i]=sno;
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
            Log.e(TAG, "error "+ e.toString());
        }

    }
}
