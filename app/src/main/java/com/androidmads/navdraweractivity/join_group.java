package com.androidmads.navdraweractivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import de.hdodenhof.circleimageview.CircleImageView;
public class join_group extends BaseActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef;
    NavigationView navigationView;
    TextView headername,headername1;
    TextView comname,comname1,comname2;
    String name;
    ImageView chat,plants;
    Button join;
    int ij=0;
    String c="0";
    private ArrayList<String> data12 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        mDatabase = FirebaseDatabase.getInstance();
        mDbRef = mDatabase.getReference("Donor");
        join = (Button) findViewById(R.id.button);
        Intent i = getIntent();
        name = i.getExtras().getString("message_key2");
        comname1=(TextView)findViewById(R.id.textView4);
        comname=(TextView)findViewById(R.id.textView);
        comname2=(TextView)findViewById(R.id.textView2);
        plants=(ImageView)findViewById(R.id.imageView10);
        chat=(ImageView)findViewById(R.id.imageView5) ;
        StorageReference ref= FirebaseStorage.getInstance().getReferenceFromUrl("gs://project-71c1c.appspot.com/images").child(name.trim());
        try {

            final File file=File.createTempFile("image","jpeg");
            ref.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    plants.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(join_group.this, "Failed To Load Image", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),grp_main.class);
                i.putExtra("message_key2",name);
                startActivity(i);
            }
        });
        c = i.getExtras().getString("number");
        if(c.equals("join"))
        {
            join.setText("JOIN COMMUNITY");
            plants.setEnabled(false);
        }
        else
        {
            join.setText("LEAVE COMMUNITY");
            mDbRef.child("memberscommunity").child(name).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String type="";
                            type=dataSnapshot.child("type").getValue().toString();
                    //join.setText(type);
                    if(type.equals("Admin"))
                    {
                        join.setText("DELETE COMMUNITY");
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("TAG", "Failed to read value.", error.toException());
                }
            });
        }
        comname.setText(name);
        mDbRef.child("community").child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                  comname2.setText(ds.getValue().toString());
                }
               join.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = user1.getUid();
                        if(join.getText().toString()=="JOIN COMMUNITY") {

                            mDbRef.child("mycommunity").child(uid).child(name).setValue("join");
                            mDbRef.child("memberscommunity").child(name).child(uid).child("name").setValue(BaseActivity.headername.getText());
                            mDbRef.child("memberscommunity").child(name).child(uid).child("type").setValue("Member");
                        }
                        else if(join.getText().toString()=="LEAVE COMMUNITY")
                        {
                            mDbRef.child("mycommunity").child(uid).child(name).removeValue();
                            mDbRef.child("memberscommunity").child(name).child(uid).removeValue();
                            startActivity(new Intent(getApplicationContext(),groups.class));
                        }
                        else
                        {

                            mDbRef.child("mycommunity").child(uid).child(name).removeValue();
                            mDbRef.child("memberscommunity").child(name).child(uid).removeValue();
                            mDbRef.child("Plants").child(name).removeValue();
                            mDbRef.child("community").child(name).removeValue();
                            startActivity(new Intent(getApplicationContext(),groups.class));
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
}
