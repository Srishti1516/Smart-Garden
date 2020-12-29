package com.androidmads.navdraweractivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class start extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef;
    FirebaseAuth fAuth;
    Button signout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        fAuth = FirebaseAuth.getInstance();
       mDatabase = FirebaseDatabase.getInstance();
        mDbRef = mDatabase.getReference("Donor");
        signout=findViewById(R.id.button8);
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user1.getUid();
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });
      /*  if(fAuth.getCurrentUser() != null /*&& fAuth.getCurrentUser().isEmailVerified()){
            startActivity(new Intent(getApplicationContext(),FirstActivity.class));
            // finish();
        }
        else
        {
            Toast.makeText(start.this, "Email Not Verified", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),Main2Activity.class));
        }*/
        mDbRef.child("Name").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            startActivity(new Intent(start.this,FirstActivity.class));
            }

            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

    }
}
