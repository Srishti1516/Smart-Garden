package com.androidmads.navdraweractivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
public class groups extends BaseActivity {
    RecyclerView recyclerView;
    private static final String TAG = "MainActivity";
    private Context context;
    private TextView a;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageURLs = new ArrayList<>();
    private ArrayList<Bitmap> mImageURL = new ArrayList<>();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef;
int i=0,z=0;
    private ArrayList<String> mNames1 = new ArrayList<>();
    private ImageView mImage_add;
    private Button create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_groups, null, false);
        drawer.addView(contentView, 0);
        mDatabase = FirebaseDatabase.getInstance();
        mDbRef = mDatabase.getReference("Donor");
        String uid = "";
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        uid = user1.getUid();
        //recyclerView = findViewById(R.id.recycler_view);
        create = (Button) findViewById(R.id.button3);
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),grp_registration.class));
            }
        });
        Log.d(TAG, "onCreate:  started. ");
      //  mImageURLs.add("https://www.google.com/imgres?imgurl=http%3A%2F%2Fwww.pngmart.com%2Ffiles%2F7%2FRed-Smoke-Transparent-Images-PNG.png&imgrefurl=http%3A%2F%2Fwww.pngmart.com%2Fimage%2F87450&tbnid=TuD68q0xzLzUnM&vet=12ahUKEwi695z50d_nAhWlMbcAHXkbC_sQMygBegUIARCGAg..i&docid=pehZnhYy8MUbgM&w=1600&h=1059&q=png%20image&ved=2ahUKEwi695z50d_nAhWlMbcAHXkbC_sQMygBegUIARCGAg");
        mDbRef.child("mycommunity").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String a=ds.getKey();
                    mNames.add(a);
                    mImageURLs.add("https://www.google.com/imgres?imgurl=http%3A%2F%2Fwww.pngmart.com%2Ffiles%2F7%2FRed-Smoke-Transparent-Images-PNG.png&imgrefurl=http%3A%2F%2Fwww.pngmart.com%2Fimage%2F87450&tbnid=TuD68q0xzLzUnM&vet=12ahUKEwi695z50d_nAhWlMbcAHXkbC_sQMygBegUIARCGAg..i&docid=pehZnhYy8MUbgM&w=1600&h=1059&q=png%20image&ved=2ahUKEwi695z50d_nAhWlMbcAHXkbC_sQMygBegUIARCGAg");
                    i++;
                }
                mDbRef.child("community").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            String ab=ds.getKey();
                            for(int j=0;j<mNames.size();j++)
                            {
                                if(ab.equals(mNames.get(j)))
                                {
                                    z=2;
                                                                }
                            }
                            if(z==0) {
                                mNames1.add(ab);
                                mImageURLs.add("https://www.google.com/imgres?imgurl=http%3A%2F%2Fwww.pngmart.com%2Ffiles%2F7%2FRed-Smoke-Transparent-Images-PNG.png&imgrefurl=http%3A%2F%2Fwww.pngmart.com%2Fimage%2F87450&tbnid=TuD68q0xzLzUnM&vet=12ahUKEwi695z50d_nAhWlMbcAHXkbC_sQMygBegUIARCGAg..i&docid=pehZnhYy8MUbgM&w=1600&h=1059&q=png%20image&ved=2ahUKEwi695z50d_nAhWlMbcAHXkbC_sQMygBegUIARCGAg");
                            }
                            else
                            {
                                z=0;
                            }
                        }
                        initRecyclerView();
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w("TAG", "Failed to read value.", error.toException());
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
                Log.d(TAG, "initialise:  started. ");
    }
    public void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init RecyclerView");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(groups.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter2 adapter = new RecyclerViewAdapter2(groups.this, mNames, mImageURL);

        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(groups.this,LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView1 = findViewById(R.id.recycler_view1);
        recyclerView1.setLayoutManager(layoutManager1);
        RecyclerViewAdapter_new adapter1 = new RecyclerViewAdapter_new(groups.this, mNames1, mImageURLs);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setAdapter(adapter1);

    }
}
