package com.asas.cloud.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asas.cloud.Activity.VideoDatiles;
import com.asas.cloud.Model.FileModel;
import com.asas.cloud.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.SimpleDateFormat;

public class FileAdapter extends FirebaseRecyclerAdapter<FileModel, FileAdapter.ViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FileAdapter(@NonNull FirebaseRecyclerOptions<FileModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull FileModel model) {

        holder.filename.setText(model.getFile_Name());
        //Glide.with(holder.img.getContext()).load(Dr).into(holder.img);
        //holder.img.;
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        String strDt = simpleDate.format(model.getData());
        holder.data.setText(strDt);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new  Intent(holder.layout.getContext(), VideoDatiles.class);
                intent.putExtra("id", model.getId());
                intent.putExtra("type", 1);
                holder.layout.getContext().startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
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
