package com.example.unifind;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unifind.models.Listing;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyListingsActivity extends AppCompatActivity {

    private GridLayout myListingsGrid;
    private Realm realm;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);

        realm = Realm.getDefaultInstance();
        sharedPreferences = getSharedPreferences("UniFindPrefs", Context.MODE_PRIVATE);
        myListingsGrid = findViewById(R.id.myListingsGrid);

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        loadMyListings();
    }

    private void loadMyListings() {
        myListingsGrid.removeAllViews();
        String currentEmail = sharedPreferences.getString("loggedInEmail", null);

        if (currentEmail == null) return;

        RealmResults<Listing> results = realm.where(Listing.class)
                .equalTo("sellerId", currentEmail)
                .findAll();

        for (Listing listing : results) {
            View productView = LayoutInflater.from(this).inflate(R.layout.item_product_card, myListingsGrid, false);

            TextView title = productView.findViewById(R.id.productTitle);
            TextView price = productView.findViewById(R.id.productPrice);
            TextView location = productView.findViewById(R.id.productLocation);
            TextView condition = productView.findViewById(R.id.conditionBadge);
            ImageView image = productView.findViewById(R.id.productImage);

            title.setText(listing.getTitle());
            price.setText(listing.getPrice() == 0 ? "GRATUIT" : listing.getPrice() + " DH");
            location.setText(listing.getLocation());
            condition.setText(listing.getCondition());

            if (listing.getPhotos() != null && !listing.getPhotos().isEmpty()) {
                String imgName = listing.getPhotos().get(0);
                int resId = getResources().getIdentifier(imgName, "drawable", getPackageName());
                if (resId != 0) {
                    image.setImageResource(resId);
                }
            }

            productView.setOnClickListener(v -> {
                Intent intent = new Intent(this, ProductDetailActivity.class);
                intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, listing.getId());
                startActivity(intent);
            });

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            productView.setLayoutParams(params);

            myListingsGrid.addView(productView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) realm.close();
    }
}