package com.androidmads.navdraweractivity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

@SuppressWarnings("deprecation")
public class grp_main extends TabActivity {
    TabHost TabHostWindow;
    ImageView image;
    TextView namess,locationn,members;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef;
    int ij=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_grp_main);
        mDatabase = FirebaseDatabase.getInstance();
        mDbRef = mDatabase.getReference("Donor");
        Intent i = getIntent();
        image=(ImageView)findViewById(R.id.imageView3);
        locationn=(TextView)findViewById(R.id.textView3);
        String name = i.getExtras().getString("message_key2");
        namess=(TextView)findViewById(R.id.textView2);
        members=(TextView)findViewById(R.id.textView5);
        namess.setText(name);
        StorageReference ref= FirebaseStorage.getInstance().getReferenceFromUrl("gs://project-71c1c.appspot.com/images").child(name.trim());
        try {

            final File file=File.createTempFile("image","jpeg");
            ref.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    image.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(grp_main.this, "Failed To Load Image", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        Glide.with(grp_main.this)
                .load(ref)
                .into(image);

        mDbRef.child("community").child(name).child("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            locationn.setText(dataSnapshot.getValue().toString());
            }
                    @Override
                    public void onCancelled(DatabaseError error) {
// Failed to read value
                        Log.w("TAG", "Failed to read value.", error.toException());
                    }
                });

        mDbRef.child("memberscommunity").child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    ij++;
                }
               members.setText(ij+" garderner");
            }

            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
        TabHostWindow = (TabHost)findViewById(android.R.id.tabhost);
        TabSpec TabMenu1 = TabHostWindow.newTabSpec("First tab");
        TabSpec TabMenu2 = TabHostWindow.newTabSpec("Second Tab");
        TabSpec TabMenu3 = TabHostWindow.newTabSpec("Third Tab");
        TabMenu1.setIndicator("Chat");
        TabMenu1.setContent(new Intent(this,TabActivity_1.class));
        TabMenu2.setIndicator("Members");
        Intent iz = new Intent(getApplicationContext(),grp_users.class);
        iz.putExtra("NAMESS",name);
        TabMenu2.setContent(iz);
        TabMenu3.setIndicator("Plants");
        Intent intent = new Intent(getApplicationContext(), grp_myplants.class);
        intent.putExtra("message_key2",name);
        TabMenu3.setContent(intent);
        TabHostWindow.addTab(TabMenu1);
        TabHostWindow.addTab(TabMenu2);
        TabHostWindow.addTab(TabMenu3);

    }
}