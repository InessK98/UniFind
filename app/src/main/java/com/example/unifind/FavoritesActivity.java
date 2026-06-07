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
import com.example.unifind.models.User;

import io.realm.Realm;
import io.realm.RealmList;

public class FavoritesActivity extends AppCompatActivity {

    private GridLayout favoritesGrid;
    private Realm realm;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        realm = Realm.getDefaultInstance();
        sharedPreferences = getSharedPreferences("UniFindPrefs", Context.MODE_PRIVATE);
        favoritesGrid = findViewById(R.id.favoritesGrid);

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        loadFavorites();
    }

    private void loadFavorites() {
        favoritesGrid.removeAllViews();
        String currentEmail = sharedPreferences.getString("loggedInEmail", null);

        if (currentEmail == null) return;

        User user = realm.where(User.class).equalTo("email", currentEmail).findFirst();
        if (user == null) return;

        RealmList<String> favoriteIds = user.getFavoriteListingIds();

        for (String id : favoriteIds) {
            Listing listing = realm.where(Listing.class).equalTo("id", id).findFirst();
            if (listing != null) {
                View productView = LayoutInflater.from(this).inflate(R.layout.item_product_card, favoritesGrid, false);

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

                favoritesGrid.addView(productView);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload in case some were removed in detail activity
        loadFavorites();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) realm.close();
    }
}