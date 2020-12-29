package com.androidmads.navdraweractivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static TextView headername,headername1;
    DrawerLayout drawer;

    FloatingActionButton fab;
    NavigationView navigationView;
    FirebaseAuth fAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef;
    public static final String TAG = "TAG";
    String uid="";
    ImageView profile_pic;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setVisibility(View.GONE);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        headername= (TextView) headerView.findViewById(R.id.headername);
        headername1= (TextView) headerView.findViewById(R.id.headername1);
         profile_pic=(ImageView)headerView.findViewById(R.id.profile);
         headerView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(BaseActivity.this,Main9Activity.class));
             }
         });
                profile_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(BaseActivity.this,Main9Activity.class));
                    }
                });
        mDatabase = FirebaseDatabase.getInstance();
        mDbRef = mDatabase.getReference("Donor/Name");


        fAuth = FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() != null) {
            FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
            uid = user1.getUid();
            mDbRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Users user = dataSnapshot.getValue(Users.class);
                    headername1.setText(user.getEmail());
                    headername.setText(user.getName());
                    StorageReference ref= FirebaseStorage.getInstance().getReferenceFromUrl("gs://project-71c1c.appspot.com/images").child(uid);

                    try {

                        final File file=File.createTempFile("image","jpeg");
                        ref.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                profile_pic.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(BaseActivity.this, "Failed To Load Image", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                     Glide.with(BaseActivity.this)
                            .load(ref)
                            .into(profile_pic);

                    Log.d(TAG, "User name: " + user.getName() + ", email " + user.getEmail());
                }

                @Override
                public void onCancelled(DatabaseError error) {
// Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_activity1) {
            startAnimatedActivity(new Intent(getApplicationContext(), FirstActivity.class));
        //} else if (id == R.id.nav_activity3) {
          //  startAnimatedActivity(new Intent(getApplicationContext(), Main4Activity.class));
        }else if (id == R.id.nav_activity5) {
            startAnimatedActivity(new Intent(getApplicationContext(), myplants.class));
        }else if (id == R.id.nav_activity6) {
            startAnimatedActivity(new Intent(getApplicationContext(), Main6Activity.class));
        }else if (id == R.id.nav_activity7) {
            startAnimatedActivity(new Intent(getApplicationContext(), Main8Activity.class));
        }else if (id == R.id.nav_activity8) {
            startAnimatedActivity(new Intent(getApplicationContext(), groups.class));
        }else if (id == R.id.nav_activity9) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(BaseActivity.this,Main2Activity.class));
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void startAnimatedActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}
