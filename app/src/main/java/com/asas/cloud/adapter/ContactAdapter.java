package com.asas.cloud.adapter;

import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.asas.cloud.Model.ContactModel;
import com.asas.cloud.R;
import com.asas.cloud.classes.References;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ContactAdapter extends FirebaseRecyclerAdapter<ContactModel, ContactAdapter.ViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ContactAdapter(@NonNull FirebaseRecyclerOptions<ContactModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ContactModel model) {
        holder.name.setText(model.getContact_Name());
        holder.number.setText(model.getContact_Number());
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        String user_id=user.getUid();
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                PopupMenu popup = new PopupMenu(holder.view.getContext(), holder.view);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.contact_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.c_download:
                                // Creates a new Intent to insert a contact
                                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                                intent.putExtra(ContactsContract.Intents.Insert.NAME, model.getContact_Name());
                                intent.putExtra(ContactsContract.Intents.Insert.PHONE, model.getContact_Number());
                                v.getContext().startActivity(intent);
                                //Uttilties.downloadManager(holder.view.getContext(), model.(), model.getFile_Name());
                                //Toast.makeText(holder.layout.getContext(), "Download start....", Toast.LENGTH_SHORT).show();

                                break;
                            case R.id.c_delete:
                                References.CONTACT_Reference.child(user_id).child(model.getContact_Id()).setValue(null);
                                Toast.makeText(holder.view.getContext(), "Delete "+ model.getContact_Name(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
                return true;
            }
        });


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_row, parent, false);
        return new ContactAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //ImageView img;
        TextView name, number;
        View view;
        //LinearLayout layout;
        //ContactsContract.Data data;


        public ViewHolder(@NonNull View itemView) {


            super(itemView);
            view=itemView;
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.show_number);
            //data = itemView.findViewById(R.id.video_time);
            //layout = itemView.findViewById(R.id.linear_layout);


        }
    }
}
