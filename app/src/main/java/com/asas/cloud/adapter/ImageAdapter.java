package com.asas.cloud.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.asas.cloud.Activity.ShowImageActivity;
import com.asas.cloud.Model.ImageModel;
import com.asas.cloud.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ImageModel model) {
        //holder.filename.setText(model.getVideo_Name());
        Glide.with(holder.img.getContext()).load(model.getImage()).into(holder.img);
        //holder.img.setImageBitmap(ImageResize.baytetobitmap(model.getImage()));
        //Bitmap bitmap = ImageResize.stringtobitmap(model.getImage());
        //holder.img.setImageBitmap(bitmap);
        holder.filename.setText(model.getVideo_Name());
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String date = format.format(model.getData());
        holder.data.setText(date);


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
        ConstraintLayout layout;
        //ContactsContract.Data data;


        public ViewHolder(@NonNull View itemView) {


            super(itemView);
            img = itemView.findViewById(R.id.image11);
            filename = itemView.findViewById(R.id.name11);
            data = itemView.findViewById(R.id.data11);
            layout=itemView.findViewById(R.id.l_view11);


        }
    }
}
