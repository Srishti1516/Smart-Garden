package com.androidmads.navdraweractivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter_new extends RecyclerView.Adapter<RecyclerViewAdapter_new.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mGroupname = new ArrayList<>();
    private ArrayList<String> mImage = new ArrayList<>();
    private Context mContext;
    private ImageView mImage_add;
    String a="join";

    public RecyclerViewAdapter_new(Context context, ArrayList<String> groupNames, ArrayList<String> images){
        mImage = images;
        mGroupname = groupNames;
        mContext = context;
        //mImage_add = image_add;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_new, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

       /* Glide.with(mContext)
              .asBitmap()
              .load(mImage.get(position))
                .into(holder.image);*/
        StorageReference ref= FirebaseStorage.getInstance().getReferenceFromUrl("gs://project-71c1c.appspot.com/images").child(mGroupname.get(position).trim());
        try {

            final File file=File.createTempFile("image","jpeg");
            ref.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    holder.image.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //    Toast.makeText(RecyclerViewAdapter2.this, "Failed To Load Image", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        holder.group_name.setText(mGroupname.get(position));
        holder.group_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,join_group.class);
                intent.putExtra("message_key2",mGroupname.get(position));
                intent.putExtra("number",a);
                mContext.startActivity(intent);
            }
        });
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,join_group.class);
                intent.putExtra("message_key2",mGroupname.get(position));
                intent.putExtra("number",a);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mGroupname.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView group_name;
        //ImageView image_add;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image1);
            group_name = itemView.findViewById(R.id.group_name1);
            parentLayout = itemView.findViewById(R.id.parent_layout1);
            //image_add = itemView.findViewById(R.id.image_add);
        }
    }
}
