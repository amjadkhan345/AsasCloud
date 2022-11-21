package com.asas.cloud.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.asas.cloud.R;

public class ProductActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    TextView p1,p2,p3;
    private BillingProcessor bp;
    //private TextView tvStatus;
    //private Button btnPremium;
    private TransactionDetails purchaseTransactionDetails = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        p1=findViewById(R.id.p_1);
        p2=findViewById(R.id.p_2);
        p3=findViewById(R.id.p_3);

        bp = new BillingProcessor(this, getResources().getString(R.string.play_console_license), this);
        bp.initialize();


    }

    private boolean hasSubscription() {
        if (purchaseTransactionDetails != null) {
            return purchaseTransactionDetails.purchaseInfo != null;
        }
        return false;
    }

    @Override
    public void onBillingInitialized() {

        Log.d("MainActivity", "onBillingInitialized: ");



        //1
        String premium1 = getResources().getString(R.string.premium1);
        purchaseTransactionDetails = bp.getSubscriptionTransactionDetails(premium1);


        //2
        String premium2 = getResources().getString(R.string.premium1);
        purchaseTransactionDetails = bp.getSubscriptionTransactionDetails(premium2);
        //3
        String premium3 = getResources().getString(R.string.premium1);
        purchaseTransactionDetails = bp.getSubscriptionTransactionDetails(premium3);

        bp.loadOwnedPurchasesFromGoogle();

        p1.setOnClickListener(v -> {
            if (bp.isSubscriptionUpdateSupported()) {
                bp.subscribe(this, premium1);
            } else {
                Log.d("MainActivity", "onBillingInitialized: Subscription updated is not supported");
            }
        });


        p2.setOnClickListener(v -> {
            if (bp.isSubscriptionUpdateSupported()) {
                bp.subscribe(this, premium2);
            } else {
                Log.d("MainActivity", "onBillingInitialized: Subscription updated is not supported");
            }
        });


        p3.setOnClickListener(v -> {
            if (bp.isSubscriptionUpdateSupported()) {
                bp.subscribe(this, premium3);
            } else {
                Log.d("MainActivity", "onBillingInitialized: Subscription updated is not supported");
            }
        });

        if (hasSubscription()) {
            String productId= purchaseTransactionDetails.purchaseInfo.purchaseData.productId.toString();
            if (productId.equals(premium1)){
                Toast.makeText(this, "you subscribe for one mounth,  Your product id is " + productId, Toast.LENGTH_SHORT).show();
            }else if(productId.equals(premium2)){
                Toast.makeText(this, "you subscribe for six mounth,  Your product id is " + productId, Toast.LENGTH_SHORT).show();


            }else if(productId.equals(premium3)){
                Toast.makeText(this, "you subscribe for one year,  Your product id is " + productId, Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(this, "you in free version", Toast.LENGTH_SHORT).show();
            }
            //tvStatus.setText("Status: Premium");
        } else {
            //tvStatus.setText("Status: Free");
        }
    }



    //@Override
    //public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {

    //}
    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        Log.d("MainActivity", "onProductPurchased: ");
    }

    @Override
    public void onPurchaseHistoryRestored() {
        Log.d("MainActivity", "onPurchaseHistoryRestored: ");

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Log.d("MainActivity", "onBillingError: ");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }

}