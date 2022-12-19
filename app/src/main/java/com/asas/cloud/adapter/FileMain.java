package com.asas.cloud.adapter;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.util.Log;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.asas.cloud.Model.FileModel;
import com.asas.cloud.R;
import com.asas.cloud.classes.References;
import com.asas.cloud.classes.Uttilties;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;

public class FileMain extends  RecyclerView.Adapter<RecyclerView.ViewHolder>
    {
        private static final int ITEM_VIEW = 0;
        private static final int AD_VIEW = 1;
        private static final int ITEM_FEED_COUNT = 5;
        private final Activity activity;
        private final List<FileModel> mainList;

        public FileMain(Activity activity, List<FileModel> mainList) {
            this.activity = activity;
            this.mainList = mainList;
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            if (viewType == ITEM_VIEW) {
                View view = layoutInflater.inflate(R.layout.video_item, parent, false);
                return new FileMain.ImageViewHolder(view);
            } else if (viewType == AD_VIEW) {
                View view = layoutInflater.inflate(R.layout.layout_ad, parent, false);
                return new FileMain.AdViewHolder(view);
            } else {
                return null;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder.getItemViewType() == ITEM_VIEW) {
                int pos = position - Math.round(position / ITEM_FEED_COUNT);
                ((FileMain.ImageViewHolder) holder).bindData(mainList.get(pos));
            } else if (holder.getItemViewType() == AD_VIEW) {
                ((FileMain.AdViewHolder) holder).bindAdData();
            }

        }

        @Override
    public int getItemViewType(int position) {
        if ((position + 1) % ITEM_FEED_COUNT == 0) {
            return AD_VIEW;
        }
        return ITEM_VIEW;
    }

        @Override
        public int getItemCount() {
            if (mainList.size() > 0) {
                return mainList.size() + Math.round(mainList.size() / ITEM_FEED_COUNT);
            }
            return mainList.size();

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

        //LayoutMainItemBinding binding;

        //MainVideoBinding
        ImageView img;
        TextView filename, data;
        ConstraintLayout layout;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            //binding = LayoutMainItemBinding.bind(itemView);
            img = itemView.findViewById(R.id.image11);
            filename = itemView.findViewById(R.id.name11);
            data = itemView.findViewById(R.id.data11);
            layout = itemView.findViewById(R.id.l_view11);
        }


        private void bindData(FileModel main) {
            //Bitmap bitmap = ImageResize.stringtobitmap(main.getThambnel());
            //img.setImageBitmap(bitmap);
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user=auth.getCurrentUser();
            String user_id=user.getUid();
            img.setImageResource(R.drawable.pdf);
            filename.setText(main.getFile_Name());
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            String date = format.format(main.getData());
            data.setText(date);
            layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popup = new PopupMenu(layout.getContext(), layout);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.file_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.f_download:
                                    Uttilties.downloadManager(layout.getContext(), main.getFile_Path(), main.getFile_Name());
                                    Toast.makeText(layout.getContext(), "Download start....", Toast.LENGTH_SHORT).show();

                                    break;
                                case R.id.f_delete:
                                    FirebaseStorage storage=FirebaseStorage.getInstance();
                                    StorageReference photoRef = storage.getReferenceFromUrl(main.getFile_Path());

                                    photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // File deleted successfully
                                            References.File_Reference.child(user_id).child(main.getId()).setValue(null);
                                            Toast.makeText(layout.getContext(), "Delete "+ main.getFile_Name(), Toast.LENGTH_SHORT).show();

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
