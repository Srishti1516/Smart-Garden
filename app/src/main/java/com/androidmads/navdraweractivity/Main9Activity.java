package com.androidmads.navdraweractivity;




import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Main9Activity extends BaseActivity {
    private Button  btnUpload;
    public ImageView btnSelect,imageView, imageretreive,backbtn;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef;
    private Uri filePath;
    FirebaseUser user1;
    String uid="";
    private final int PICK_IMAGE_REQUEST = 22;
    private EditText name,email,location,password;
    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_main9, null, false);
        drawer.addView(contentView, 0);
        //setContentView(R.layout.activity_main9);
        btnSelect = findViewById(R.id.Choose);
        btnUpload = findViewById(R.id.upload);
        backbtn = findViewById(R.id.imageView2);
        imageView = findViewById(R.id.image1);
        name=findViewById(R.id.editText);
        email=findViewById(R.id.editText5);
        location=findViewById(R.id.editText6);
        password=findViewById(R.id.editText7);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mDatabase = FirebaseDatabase.getInstance();
        mDbRef = mDatabase.getReference("Donor/Name");
         user1 = FirebaseAuth.getInstance().getCurrentUser();
        uid = user1.getUid();
        mDbRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                StorageReference ref= FirebaseStorage.getInstance().getReferenceFromUrl("gs://project-71c1c.appspot.com/images").child(uid);
                name.setText(user.getName());
                email.setText(user.getEmail());
                location.setText(user.getPhone());
               // password.setText(dataSnapshot.child("password").getValue().toString());
                try {

                    final File file=File.createTempFile("image","jpeg");
                    ref.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            imageView.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Main9Activity.this, "Failed To Load Image", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
               /* Glide.with(Main9Activity.this)
                        .load(ref)
                        .into(imageView);
*/
                ///profile_pic.setImageResource(R.drawable.water);
                Log.d("TAG", "User name: " + user.getName() + ", email " + user.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SelectImage();
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
                // mDbRef = mDatabase.getReference("Donor/Name");
                mDbRef.child(uid).child("email").setValue(email.getText().toString());
                mDbRef.child(uid).child("name").setValue(name.getText().toString());
                mDbRef.child(uid).child("phone").setValue(location.getText().toString());
                if (password.getText().toString().trim().length()>=6 ) {
                    user1.updatePassword(password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Main9Activity.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(Main9Activity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                }
                Toast.makeText(Main9Activity.this, "UPDATED", Toast.LENGTH_SHORT).show();
            }
        });
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
            String uid;
            FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
            uid = user1.getUid();
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + uid);
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
                                            .makeText(Main9Activity.this,
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
                            Toast.makeText(Main9Activity.this,"Check: "+filePath,Toast.LENGTH_SHORT).show();
                            Toast
                                    .makeText(Main9Activity.this,
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

