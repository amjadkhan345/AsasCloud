package com.asas.cloud.adapter;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.asas.cloud.Model.FileModel;
import com.asas.cloud.R;
import com.asas.cloud.classes.References;
import com.asas.cloud.classes.Uttilties;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
        //FirebaseDatabase database= FirebaseDatabase.getInstance();
        //DatabaseReference databaseReference=database.getReference().child()
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        String user_id=user.getUid();

        holder.filename.setText(model.getFile_Name());
        //Glide.with(holder.img.getContext()).load(Dr).into(holder.img);
        //holder.img.;
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        String strDt = simpleDate.format(model.getData());
        //holder.data.setText(model.getFile_Name());
        holder.img.setImageResource(R.drawable.pdf);
        holder.filename.setText(model.getFile_Name());
        holder.layout.setLongClickable(true);

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(holder.layout.getContext(), holder.layout);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.file_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.f_download:
                                Uttilties.downloadManager(holder.layout.getContext(), model.getFile_Path(), model.getFile_Name());
                                Toast.makeText(holder.layout.getContext(), "Download start....", Toast.LENGTH_SHORT).show();

                                break;
                                case R.id.f_delete:
                                    FirebaseStorage storage=FirebaseStorage.getInstance();
                                    StorageReference photoRef = storage.getReferenceFromUrl(model.getFile_Path());

                                    photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // File deleted successfully
                                            References.File_Reference.child(user_id).child(model.getId()).setValue(null);
                                            Toast.makeText(holder.layout.getContext(), "Delete "+ model.getFile_Name(), Toast.LENGTH_SHORT).show();

                                            //databaseReference.child(user_id).child(id).setValue(null);
                                            //Intent intent = new Intent(PlayVideoActivity.this, MainActivity.class);
                                            //startActivity(intent);
                                            //finish();
                                            Log.d(TAG, "onSuccess: deleted file");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Uh-oh, an error occurred!
                                            Log.d(TAG, "onFailure: did not delete file");
                                        }
                                    });

                                    break;
                        }
                        return true;
                    }
                });
                popup.show();
                return true;
            }
        });

       /* holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new  Intent(holder.layout.getContext(), VideoDatiles.class);
                intent.putExtra("id", model.getId());
                intent.putExtra("type", 1);
                holder.layout.getContext().startActivity(intent);
            }
        });*/

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new ViewHolder(view);
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
