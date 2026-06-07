package com.example.unifind;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unifind.models.Listing;
import com.example.unifind.models.User;

import io.realm.Realm;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "extra_product_id";
    private Realm realm;
    private String productId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        realm = Realm.getDefaultInstance();
        sharedPreferences = getSharedPreferences("UniFindPrefs", Context.MODE_PRIVATE);
        productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);

        if (productId != null) {
            Listing listing = realm.where(Listing.class).equalTo("id", productId).findFirst();
            if (listing != null) {
                populateUI(listing);
                setupFavoriteButton();
                setupContactButton(listing);
            }
        }

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    private void setupContactButton(Listing listing) {
        findViewById(R.id.contactButton).setOnClickListener(v -> {
            String currentEmail = sharedPreferences.getString("loggedInEmail", null);
            if (currentEmail == null) {
                Toast.makeText(this, "Veuillez vous connecter pour contacter le vendeur", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentEmail.equals(listing.getSellerId())) {
                Toast.makeText(this, "C'est votre propre annonce !", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("receiverEmail", listing.getSellerId());
            intent.putExtra("listingId", listing.getId());
            intent.putExtra("listingTitle", listing.getTitle());
            startActivity(intent);
        });
    }

    private void setupFavoriteButton() {
        ImageButton favoriteButton = findViewById(R.id.favoriteButton);
        String currentEmail = sharedPreferences.getString("loggedInEmail", null);

        if (currentEmail == null) {
            favoriteButton.setOnClickListener(v -> 
                Toast.makeText(this, "Veuillez vous connecter pour ajouter aux favoris", Toast.LENGTH_SHORT).show()
            );
            return;
        }

        User user = realm.where(User.class).equalTo("email", currentEmail).findFirst();
        if (user == null) return;

        // Initial state
        updateFavoriteIcon(favoriteButton, user.getFavoriteListingIds().contains(productId));

        favoriteButton.setOnClickListener(v -> {
            realm.executeTransaction(r -> {
                User currentUser = r.where(User.class).equalTo("email", currentEmail).findFirst();
                if (currentUser != null) {
                    if (currentUser.getFavoriteListingIds().contains(productId)) {
                        currentUser.getFavoriteListingIds().remove(productId);
                        updateFavoriteIcon(favoriteButton, false);
                        Toast.makeText(this, "Retiré des favoris", Toast.LENGTH_SHORT).show();
                    } else {
                        currentUser.getFavoriteListingIds().add(productId);
                        updateFavoriteIcon(favoriteButton, true);
                        Toast.makeText(this, "Ajouté aux favoris", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

    private void updateFavoriteIcon(ImageButton button, boolean isFavorite) {
        if (isFavorite) {
            button.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            button.setImageResource(android.R.drawable.btn_star_big_off);
        }
    }

    private void populateUI(Listing listing) {
        TextView title = findViewById(R.id.detailTitle);
        TextView price = findViewById(R.id.detailPrice);
        TextView condition = findViewById(R.id.detailCondition);
        TextView location = findViewById(R.id.detailLocation);
        TextView description = findViewById(R.id.detailDescription);
        ImageView image = findViewById(R.id.detailImage);

        title.setText(listing.getTitle());
        price.setText(listing.getPrice() == 0 ? "GRATUIT" : listing.getPrice() + " DH");
        condition.setText("✨ " + listing.getCondition());
        location.setText("📍 " + listing.getLocation());

        if (listing.getPhotos() != null && !listing.getPhotos().isEmpty()) {
            String imgName = listing.getPhotos().get(0);
            int resId = getResources().getIdentifier(imgName, "drawable", getPackageName());
            if (resId != 0) {
                image.setImageResource(resId);
            }
        }
        
        if (listing.getDescription() != null && !listing.getDescription().isEmpty()) {
            description.setText(listing.getDescription());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }
}