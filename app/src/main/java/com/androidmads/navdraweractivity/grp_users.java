package com.androidmads.navdraweractivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class grp_users extends BaseActivity {
    private DatabaseReference mDbRef;
    private FirebaseDatabase mDatabase;
    private ArrayList<String> mImageURLs = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    TextView display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_grp_users, null, false);
        drawer.addView(contentView, 0);
        Intent i = getIntent();
        String name = i.getExtras().getString("NAMESS");
       // display=(TextView)findViewById(R.id.textView11);
        //display.setText("");

        mDatabase = FirebaseDatabase.getInstance();
        mDbRef = mDatabase.getReference("Donor");
        mDbRef.child("memberscommunity").child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    mNames.add(ds.getKey());
                }

                LinearLayoutManager layoutManager1 = new LinearLayoutManager(grp_users.this,LinearLayoutManager.VERTICAL,false);
                RecyclerView recyclerView1 = findViewById(R.id.recycler_view);
                recyclerView1.setLayoutManager(layoutManager1);
                grp_adapter adapter1 = new grp_adapter(grp_users.this, mNames, mImageURLs);
                recyclerView1.setLayoutManager(layoutManager1);
                recyclerView1.setAdapter(adapter1);
            }



            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }
}
