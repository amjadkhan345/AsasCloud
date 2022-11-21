package com.asas.cloud.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.asas.cloud.Activity.ShowImageActivity;
import com.asas.cloud.Activity.VideoDatiles;
import com.asas.cloud.Model.FileModel;
import com.asas.cloud.Model.ImageModel;
import com.asas.cloud.R;
import com.asas.cloud.classes.ImageResize;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public  class ImageAdapter extends FirebaseRecyclerAdapter<ImageModel,ImageAdapter.ViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ImageAdapter(@NonNull FirebaseRecyclerOptions<ImageModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ImageModel model) {
        holder.filename.setText(model.getVideo_Name());
        Glide.with(holder.img.getContext()).load(model.getImage()).into(holder.img);
        //holder.img.setImageBitmap(ImageResize.baytetobitmap(model.getImage()));
        //Bitmap bitmap = ImageResize.stringtobitmap(model.getImage());
        //holder.img.setImageBitmap(bitmap);
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        String strDt = simpleDate.format(model.getData());
        holder.data.setText(strDt);


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new  Intent(holder.layout.getContext(), ShowImageActivity.class);
                intent.putExtra("url", model.getImage());
                intent.putExtra("id", model.getId());

                holder.layout.getContext().startActivity(intent);
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView filename, data;
        LinearLayout layout;
        //ContactsContract.Data data;


        public ViewHolder(@NonNull View itemView) {


            super(itemView);
            img = itemView.findViewById(R.id.imageview);
            filename = itemView.findViewById(R.id.video_name);
            data = itemView.findViewById(R.id.video_time);
            layout=itemView.findViewById(R.id.linear_layout);


        }
    }
}
