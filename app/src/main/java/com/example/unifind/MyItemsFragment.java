package com.example.unifind;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unifind.models.Listing;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyItemsFragment extends Fragment {

    private GridLayout myItemsGrid;
    private TextView activeCountText, soldCountText;
    private Realm realm;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_items, container, false);
        
        myItemsGrid = view.findViewById(R.id.myItemsGrid);
        activeCountText = view.findViewById(R.id.activeCountText);
        soldCountText = view.findViewById(R.id.soldCountText);
        
        realm = Realm.getDefaultInstance();
        sharedPreferences = getActivity().getSharedPreferences("UniFindPrefs", Context.MODE_PRIVATE);
        
        loadMyItems();
        
        return view;
    }

    private void loadMyItems() {
        myItemsGrid.removeAllViews();
        
        String currentEmail = sharedPreferences.getString("loggedInEmail", null);
        
        if (currentEmail == null) {
            activeCountText.setText("0");
            soldCountText.setText("0");
            return;
        }

        // Filter by the current user's email (stored as sellerId)
        RealmResults<Listing> listings = realm.where(Listing.class)
                .equalTo("sellerId", currentEmail)
                .findAll();
        
        long activeCount = realm.where(Listing.class)
                .equalTo("sellerId", currentEmail)
                .equalTo("status", "available")
                .count();
                
        long soldCount = realm.where(Listing.class)
                .equalTo("sellerId", currentEmail)
                .equalTo("status", "sold")
                .count();
        
        activeCountText.setText(String.valueOf(activeCount));
        soldCountText.setText(String.valueOf(soldCount));

        for (Listing listing : listings) {
            View productView = LayoutInflater.from(getContext()).inflate(R.layout.item_product_card, myItemsGrid, false);
            
            TextView title = productView.findViewById(R.id.productTitle);
            TextView price = productView.findViewById(R.id.productPrice);
            TextView location = productView.findViewById(R.id.productLocation);
            TextView condition = productView.findViewById(R.id.conditionBadge);
            ImageView image = productView.findViewById(R.id.productImage);
            
            title.setText(listing.getTitle());
            price.setText(listing.getPrice() == 0 ? "GRATUIT" : listing.getPrice() + " DH");
            location.setText(listing.getLocation());
            condition.setText(listing.getCondition());

            // Handle Dynamic Image Loading
            if (listing.getPhotos() != null && !listing.getPhotos().isEmpty()) {
                String imgName = listing.getPhotos().get(0);
                int resId = getResources().getIdentifier(imgName, "drawable", getContext().getPackageName());
                if (resId != 0) {
                    image.setImageResource(resId);
                }
            }

            // Click listener to open detail
            String productId = listing.getId();
            productView.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, productId);
                startActivity(intent);
            });

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            productView.setLayoutParams(params);

            myItemsGrid.addView(productView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }
}