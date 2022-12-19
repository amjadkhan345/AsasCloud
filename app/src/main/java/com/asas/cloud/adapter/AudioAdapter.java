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

import com.asas.cloud.Activity.PlayVideoActivity;
import com.asas.cloud.Model.AudioModel;
import com.asas.cloud.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.SimpleDateFormat;

public class AudioAdapter extends FirebaseRecyclerAdapter<AudioModel, AudioAdapter.ViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AudioAdapter(@NonNull FirebaseRecyclerOptions<AudioModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AudioAdapter.ViewHolder holder, int position, @NonNull AudioModel model) {
        holder.filename.setText(model.getVideo_Name());
        //Glide.with(holder.img.getContext()).load(model.()).into(holder.img);
        //Bitmap bmp = BitmapFactory.decodeByteArray(model.getThambnel(), 0, model.getThambnel().length);
        //ImageView imageView = new ImageView(ConversationsActivity.this);
        //holder.img.setImageBitmap(ImageResize.baytetobitmap(model.getThambnel()));
        //Bitmap bitmap = ImageResize.stringtobitmap(model.getThambnel());
        holder.img.setImageResource(R.drawable.ic_audio);
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
        String strDt = simpleDate.format(model.getData());
        holder.data.setText(strDt);



        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.layout.getContext(), PlayVideoActivity.class);
                intent.putExtra("id", model.getId());
                intent.putExtra("type", "audio");
                intent.putExtra("url", model.getAudioPath());
                holder.layout.getContext().startActivity(intent);

            }
        });
    }

    @NonNull
    @Override
    public AudioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new AudioAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView filename, data;
        ConstraintLayout layout;
        //ContactsContract.Data data;


        public ViewHolder(@NonNull View itemView) {


            super(itemView);
            img = itemView.findViewById(R.id.image11);
            filename = itemView.findViewById(R.id.name11);
            data = itemView.findViewById(R.id.data11);
            layout = itemView.findViewById(R.id.l_view11);


        }
    }
}
