package com.example.unifind;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.unifind.models.Listing;

import io.realm.Realm;
import io.realm.RealmList;

public class MainActivity extends AppCompatActivity {

    private LinearLayout navMarket, navLostFound, navPost, navMyItems, navProfile;
    private TextView navMarketText, navLostFoundText, navPostText, navMyItemsText, navProfileText;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_layout);

        realm = Realm.getDefaultInstance();

        navMarket = findViewById(R.id.navMarket);
        navLostFound = findViewById(R.id.navLostFound);
        navPost = findViewById(R.id.navPost);
        navMyItems = findViewById(R.id.navMyItems);
        navProfile = findViewById(R.id.navProfile);

        navMarketText = findViewById(R.id.navMarketText);
        navLostFoundText = findViewById(R.id.navLostFoundText);
        navPostText = findViewById(R.id.navPostText);
        navMyItemsText = findViewById(R.id.navMyItemsText);
        navProfileText = findViewById(R.id.navProfileText);

        navMarket.setOnClickListener(v -> selectTab(0));
        navLostFound.setOnClickListener(v -> selectTab(1));
        navPost.setOnClickListener(v -> selectTab(2));
        navMyItems.setOnClickListener(v -> selectTab(3));
        navProfile.setOnClickListener(v -> selectTab(4));

        checkAndPopulateDummyData();

        if (savedInstanceState == null) {
            selectTab(0);
        }
    }

    private void checkAndPopulateDummyData() {
        long count = realm.where(Listing.class).count();
        Listing first = realm.where(Listing.class).equalTo("id", "1").findFirst();
        
        // Refresh if empty, if we added new items, or if categories are missing
        if (count < 5 || (first != null && "Tout".equals(first.getCategory()))) {
            realm.executeTransaction(r -> {
                r.where(Listing.class).findAll().deleteAllFromRealm();
                createListing(r, "1", "Veste d'hiver", 150, "NEUF", "Résidences", "Veste chaude bleue.", "veste_img", "vendeur1@uir.ac.ma", "Vêtements");
                createListing(r, "2", "Manuel Physique", 80, "BON ÉTAT", "Bibliothèque", "Manuel 1ère année.", "manuel_img", "vendeur2@uir.ac.ma", "Livres");
                createListing(r, "3", "Calculatrice TI-84", 250, "COMME NEUF", "Cafétéria", "TI-84 Plus graphique.", "calcul_img", "vendeur3@uir.ac.ma", "Électronique");
                createListing(r, "4", "Sac à dos", 0, "GRATUIT", "Bâtiment d'ingénierie", "Sac à dos noir.", "sac_image", "vendeur4@uir.ac.ma", "Vêtements");
                createListing(r, "5", "Jean Évasé", 120, "TRÈS BON ÉTAT", "Résidences", "Jean évasé bleu clair, taille 38.", "jeans_img", "i.k@uir.ac.ma", "Vêtements");
            });
        }
    }

    private void createListing(Realm r, String id, String title, double price, String cond, String loc, String desc, String imgName, String sellerId, String category) {
        Listing l = r.createObject(Listing.class, id);
        l.setTitle(title);
        l.setPrice(price);
        l.setCondition(cond);
        l.setLocation(loc);
        l.setDescription(desc);
        l.setCategory(category);
        l.setPhotos(new RealmList<>(imgName));
        l.setSellerId(sellerId);
    }

    private void selectTab(int tabIndex) {
        resetNavColors();
        Fragment selectedFragment = null;
        switch (tabIndex) {
            case 0: selectedFragment = new MarketFragment(); navMarketText.setTextColor(getColor(R.color.uir_blue)); break;
            case 1: selectedFragment = new LostFoundFragment(); navLostFoundText.setTextColor(getColor(R.color.uir_blue)); break;
            case 2: selectedFragment = new PostFragment(); navPostText.setTextColor(getColor(R.color.uir_blue)); break;
            case 3: selectedFragment = new MyItemsFragment(); navMyItemsText.setTextColor(getColor(R.color.uir_blue)); break;
            case 4: selectedFragment = new ProfileFragment(); navProfileText.setTextColor(getColor(R.color.uir_blue)); break;
        }
        if (selectedFragment != null) replaceFragment(selectedFragment);
    }

    private void resetNavColors() {
        int gray = getColor(R.color.text_gray);
        navMarketText.setTextColor(gray);
        navLostFoundText.setTextColor(gray);
        navPostText.setTextColor(gray);
        navMyItemsText.setTextColor(gray);
        navProfileText.setTextColor(gray);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) realm.close();
    }
}