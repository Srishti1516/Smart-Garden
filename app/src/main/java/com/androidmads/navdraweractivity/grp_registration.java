package com.androidmads.navdraweractivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

public class grp_registration extends BaseActivity {
    TextView name;
    ArrayList<MainModel> mainModels;
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    MainAdapter mainAdapter;
    RecyclerView recyclerView;
    public static EditText a,b,c;

    FirebaseStorage storage;
    StorageReference storageReference;
    ImageView select,imageView;
    int z=0;
    String uid = "";
    public static Button registerbtn,locatio;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_grp_registration, null, false);
        drawer.addView(contentView, 0);
        imageView = findViewById(R.id.image2);
        select=findViewById(R.id.choose);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });
        registerbtn=findViewById(R.id.button4);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        locatio=findViewById(R.id.button10);
        locatio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),grp_map.class));
            }
        });
        a=findViewById(R.id.editText2);
        b=findViewById(R.id.editText3);
        c=findViewById(R.id.editText4);
       // recyclerView = findViewById(R.id.add_plants);
        Integer[] plants = {R.drawable.add, R.drawable.add};

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance();

                mDbRef = mDatabase.getReference("Donor");

                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                uid = user1.getUid();
                mDbRef.child("community").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            if(ds.getKey().trim().equals(a.getText().toString().trim()))
                            {
                                z=3;
                            }

                        }
                        if(z!=3) {
                            mDbRef.child("community").child(a.getText().toString()).child("Max").setValue(b.getText().toString());
                            mDbRef.child("community").child(a.getText().toString()).child("location").setValue(c.getText().toString());
                            mDbRef.child("memberscommunity").child(a.getText().toString()).child(uid).child("name").setValue(BaseActivity.headername.getText());
                            mDbRef.child("memberscommunity").child(a.getText().toString()).child(uid).child("type").setValue("Admin");
                            mDbRef.child("mycommunity").child(uid).child(a.getText().toString()).setValue("join");
                            uploadImage();
                            startActivity(new Intent(getApplicationContext(), groups.class));
                        }
                        else if(z==3)
                        {
                            Toast.makeText(grp_registration.this, "COMMUNITY EXISTS WITH SAME NAME", Toast.LENGTH_SHORT).show();
                            z=0;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w("TAG", "Failed to read value.", error.toException());
                    }
                });


            }
            });
    /*    mainModels = new ArrayList<>();
        for (int i=0;i<plants.length;i++)
        {
            MainModel model= new MainModel((plants[i]));
            mainModels.add(model);
        }
     /*   LinearLayoutManager layoutManager = new LinearLayoutManager(grp_registration.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mainAdapter = new MainAdapter(grp_registration.this, mainModels);

        recyclerView.setAdapter(mainAdapter);*/
    }
    private void SelectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {
        super.onActivityResult(requestCode,
                resultCode,
                data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            Toast.makeText(this, "safasfas", Toast.LENGTH_SHORT).show();
    }
    private void uploadImage()
    {
        if (filePath != null) {
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String uid=(a.getText().toString().trim());
            //FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
          //  uid = user1.getUid();
            StorageReference ref = storageReference.child("images/"+uid);
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(grp_registration.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(grp_registration.this,"Check: "+filePath,Toast.LENGTH_SHORT).show();
                            Toast
                                    .makeText(grp_registration.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }
}
