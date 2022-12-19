package com.asas.cloud.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.aemerse.iap.DataWrappers;
import com.aemerse.iap.IapConnector;
import com.aemerse.iap.PurchaseServiceListener;
import com.aemerse.iap.SubscriptionServiceListener;
import com.asas.cloud.R;
import com.asas.cloud.classes.References;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import games.moisoni.google_iab.BillingConnector;

public class ProductActivity extends AppCompatActivity {
    private BillingConnector billingConnector;
    MutableLiveData<Boolean> isBillingClientConnected = new MutableLiveData<>();

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user= auth.getCurrentUser();

    String user_id, size;

    TextView a10, a30, a50, a100;

    //create a list with consumable ids
    private final List<String> consumableIds = new ArrayList<>();
    private final ArrayList<String> purchaseItemDisplay = new ArrayList<>();
    private ArrayAdapter arrayAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        user_id= user.getUid();
        a10= findViewById(R.id.a10);
        a30= findViewById(R.id.a30);
        a50= findViewById(R.id.a50);
        a100= findViewById(R.id.a100);
        References.User_Reference.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    size = snapshot.child("user_storage").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        isBillingClientConnected.setValue(false);

        List<String> nonConsumablesList = Collections.singletonList("subscription");//Arrays.asList("10_gb_space", "30_gb_space", "50_gb_space", "100_gb_space");
        List<String> consumablesList = Arrays.asList("10_gb_space", "30_gb_space", "50_gb_space", "100_gb_space");
        //Collections.singletonList("subscription");
        List<String> subsList = Collections.singletonList("subscription");

        IapConnector iapConnector = new IapConnector(
                this,
                nonConsumablesList,
                consumablesList,
                subsList,
                getResources().getString(R.string.licince_key),
                true
        );
        iapConnector.addBillingClientConnectionListener((status, billingResponseCode) -> {
            Log.d("KSA", "This is the status: "+status+" and response code is: "+billingResponseCode);
            isBillingClientConnected.setValue(status);
        });
        iapConnector.addPurchaseListener(new PurchaseServiceListener() {
            public void onPricesUpdated(@NotNull Map iapKeyPrices) {

            }
            public void onProductPurchased(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
                if (purchaseInfo.getSku().equals("10_gb_space")) {

                    long user_stoarge=Long.parseLong(size);
                    long purcheasegb = 10000000;
                    long totel = user_stoarge + purcheasegb;
                    References.User_Reference.child(user_id).child("user_storage").setValue(totel);
                    Toast.makeText(getApplicationContext(), "Thanks For purchase 10 GB space", Toast.LENGTH_SHORT).show();

                }else if (purchaseInfo.getSku().equals("30_gb_space")) {
                    long user_stoarge=Long.parseLong(size);
                    long purcheasegb = 30000000;
                    long totel = user_stoarge + purcheasegb;
                    References.User_Reference.child(user_id).child("user_storage").setValue(totel);
                    Toast.makeText(getApplicationContext(), "Thanks For purchase 30 GB space", Toast.LENGTH_SHORT).show();

                } else if (purchaseInfo.getSku().equals("50_gb_space")) {
                    long user_stoarge=Long.parseLong(size);
                    long purcheasegb = 50000000;
                    long totel = user_stoarge + purcheasegb;
                    References.User_Reference.child(user_id).child("user_storage").setValue(totel);
                    Toast.makeText(getApplicationContext(), "Thanks For purchase 50 GB space", Toast.LENGTH_SHORT).show();

                }else if (purchaseInfo.getSku().equals("100_gb_space")) {
                    long user_stoarge=Long.parseLong(size);
                    long purcheasegb = 100000000;
                    long totel = user_stoarge + purcheasegb;
                    References.User_Reference.child(user_id).child("user_storage").setValue(totel);
                    Toast.makeText(getApplicationContext(), "Thanks For purchase 100 GB space", Toast.LENGTH_SHORT).show();

                }
            }

            public void onProductRestored(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {

            }
        });

        iapConnector.addSubscriptionListener(new SubscriptionServiceListener() {
            public void onSubscriptionRestored(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
            }

            public void onSubscriptionPurchased(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
                if (purchaseInfo.getSku().equals("subscription")) {

                }
            }

            public void onPricesUpdated(@NotNull Map iapKeyPrices) {

            }
        });
        a10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iapConnector.purchase(ProductActivity.this, "10_gb_space");
                //startActivity(new Intent(ProductActivity.this, ProductActivity.class));
            }
        });
        a30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iapConnector.purchase(ProductActivity.this, "30_gb_space");
            }
        });
        a50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iapConnector.purchase(ProductActivity.this, "50_gb_space");
            }
        });
        a100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iapConnector.purchase(ProductActivity.this, "100_gb_space");
            }
        });



    }


}