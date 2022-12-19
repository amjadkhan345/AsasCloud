package com.asas.cloud.adapter;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.asas.cloud.Model.ContactModel;
import com.asas.cloud.R;
import com.asas.cloud.classes.References;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ContactMain extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW = 0;
    private static final int AD_VIEW = 1;
    private static final int ITEM_FEED_COUNT = 5;
    private final Activity activity;
    private final List<ContactModel> mainList;

    public ContactMain(Activity activity, List<ContactModel> mainList) {
        this.activity = activity;
        this.mainList = mainList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        if (viewType == ITEM_VIEW) {
            View view = layoutInflater.inflate(R.layout.contacts_row, parent, false);
            return new ContactMain.ImageViewHolder(view);
        } else if (viewType == AD_VIEW) {
            View view = layoutInflater.inflate(R.layout.layout_ad, parent, false);
            return new ContactMain.AdViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == ITEM_VIEW) {
            int pos = position - Math.round(position / ITEM_FEED_COUNT);
            ((ContactMain.ImageViewHolder) holder).bindData(mainList.get(pos));
        } else if (holder.getItemViewType() == AD_VIEW) {
            ((ContactMain.AdViewHolder) holder).bindAdData();
        }

    }

    @Override
    public int getItemCount() {
        if (mainList.size() > 0) {
            return mainList.size() + Math.round(mainList.size() / ITEM_FEED_COUNT);
        }
        return mainList.size();
    }
    @Override
    public int getItemViewType(int position) {
        if ((position + 1) % ITEM_FEED_COUNT == 0) {
            return AD_VIEW;
        }
        return ITEM_VIEW;
    }

    private void populateNativeADView(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view.
        adView.setMediaView(adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        TextView name, number;
        View view;
        //LinearLayout layout;
        //ContactsContract.Data data;


        public ImageViewHolder(@NonNull View itemView) {


            super(itemView);
            view=itemView;
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.show_number);
            //data = itemView.findViewById(R.id.video_time);
            //layout = itemView.findViewById(R.id.linear_layout);


        }


        private void bindData(ContactModel main) {
            name.setText(main.getContact_Name());
            number.setText(main.getContact_Number());
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user=auth.getCurrentUser();
            String user_id=user.getUid();
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    PopupMenu popup = new PopupMenu(view.getContext(), view);
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
                                    intent.putExtra(ContactsContract.Intents.Insert.NAME, main.getContact_Name());
                                    intent.putExtra(ContactsContract.Intents.Insert.PHONE, main.getContact_Number());
                                    v.getContext().startActivity(intent);
                                    //Uttilties.downloadManager(holder.view.getContext(), model.(), model.getFile_Name());
                                    //Toast.makeText(holder.layout.getContext(), "Download start....", Toast.LENGTH_SHORT).show();

                                    break;
                                case R.id.c_delete:
                                    References.CONTACT_Reference.child(user_id).child(main.getContact_Id()).setValue(null);
                                    Toast.makeText(view.getContext(), "Delete "+ main.getContact_Name(), Toast.LENGTH_SHORT).show();
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
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {

        //LayoutAdBinding binding;
        FrameLayout adLayout;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            //binding = LayoutAdBinding.bind(itemView);
            adLayout = itemView.findViewById(R.id.adLayout);
        }

        private void bindAdData() {
            AdLoader.Builder builder = new AdLoader.Builder(activity, "ca-app-pub-3940256099942544/2247696110")
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                            NativeAdView nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.layout_native_ad, null);
                            populateNativeADView(nativeAd, nativeAdView);
                            adLayout.removeAllViews();
                            adLayout.addView(nativeAdView);
                        }
                    });

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    Toast.makeText(activity, loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).build();

            adLoader.loadAd(new AdRequest.Builder().build());
        }
    }
}
