package com.example.unifind;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unifind.models.Listing;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MarketFragment extends Fragment {

    private GridLayout productsGrid;
    private EditText searchInput;
    private TextView chipTout, chipVetements, chipLivres, chipElec;
    private Realm realm;
    private String currentCategory = "Tout";
    private String searchQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        
        productsGrid = view.findViewById(R.id.productsGrid);
        searchInput = view.findViewById(R.id.searchInput);
        
        // Find Chips
        chipTout = view.findViewById(R.id.chipTout);
        chipVetements = view.findViewById(R.id.chipVetements);
        chipLivres = view.findViewById(R.id.chipLivres);
        chipElec = view.findViewById(R.id.chipElec);

        realm = Realm.getDefaultInstance();
        
        setupSearch();
        setupFilters();
        loadProducts();
        
        return view;
    }

    private void setupSearch() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString();
                loadProducts();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupFilters() {
        View.OnClickListener filterListener = v -> {
            resetChips();
            v.setBackgroundResource(R.drawable.category_chip_selected);
            ((TextView)v).setTextColor(getResources().getColor(android.R.color.white));
            currentCategory = ((TextView)v).getText().toString();
            loadProducts();
        };

        chipTout.setOnClickListener(filterListener);
        chipVetements.setOnClickListener(filterListener);
        chipLivres.setOnClickListener(filterListener);
        chipElec.setOnClickListener(filterListener);
    }

    private void resetChips() {
        int gray = getResources().getColor(R.color.text_gray);
        chipTout.setBackgroundResource(R.drawable.category_chip_unselected);
        chipTout.setTextColor(gray);
        chipVetements.setBackgroundResource(R.drawable.category_chip_unselected);
        chipVetements.setTextColor(gray);
        chipLivres.setBackgroundResource(R.drawable.category_chip_unselected);
        chipLivres.setTextColor(gray);
        chipElec.setBackgroundResource(R.drawable.category_chip_unselected);
        chipElec.setTextColor(gray);
    }

    private void loadProducts() {
        productsGrid.removeAllViews();
        RealmQuery<Listing> query = realm.where(Listing.class);
        
        if (!currentCategory.equals("Tout")) {
            query.equalTo("category", currentCategory, Case.INSENSITIVE);
        }
        if (!searchQuery.isEmpty()) {
            query.contains("title", searchQuery, Case.INSENSITIVE);
        }

        RealmResults<Listing> listings = query.findAll();

        for (Listing listing : listings) {
            View productView = LayoutInflater.from(getContext()).inflate(R.layout.item_product_card, productsGrid, false);
            
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

            productView.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, listing.getId());
                startActivity(intent);
            });

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            productView.setLayoutParams(params);

            productsGrid.addView(productView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null) realm.close();
    }
}