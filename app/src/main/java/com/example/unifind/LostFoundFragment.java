package com.example.unifind;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unifind.models.LostFoundItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class LostFoundFragment extends Fragment {

    private LinearLayout lostFoundContainer;
    private EditText searchInput;
    private TextView chipTout, chipPerdu, chipTrouve;
    private Realm realm;
    private String currentFilter = "Tout";
    private String searchQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lost_found, container, false);
        lostFoundContainer = view.findViewById(R.id.lostFoundContainer);
        searchInput = view.findViewById(R.id.searchInput);
        chipTout = view.findViewById(R.id.chipTout);
        chipPerdu = view.findViewById(R.id.chipPerdu);
        chipTrouve = view.findViewById(R.id.chipTrouve);

        realm = Realm.getDefaultInstance();

        setupSearch();
        setupFilters();
        loadItems();

        return view;
    }

    private void setupSearch() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString();
                loadItems();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupFilters() {
        View.OnClickListener filterListener = v -> {
            resetChips();
            v.setBackgroundResource(R.drawable.category_chip_selected);
            ((TextView) v).setTextColor(getResources().getColor(android.R.color.white));
            currentFilter = ((TextView) v).getText().toString();
            loadItems();
        };

        chipTout.setOnClickListener(filterListener);
        chipPerdu.setOnClickListener(filterListener);
        chipTrouve.setOnClickListener(filterListener);
    }

    private void resetChips() {
        int gray = getResources().getColor(R.color.text_gray);
        chipTout.setBackgroundResource(R.drawable.category_chip_unselected);
        chipTout.setTextColor(gray);
        chipPerdu.setBackgroundResource(R.drawable.category_chip_unselected);
        chipPerdu.setTextColor(gray);
        chipTrouve.setBackgroundResource(R.drawable.category_chip_unselected);
        chipTrouve.setTextColor(gray);
    }

    private void loadItems() {
        lostFoundContainer.removeAllViews();
        RealmQuery<LostFoundItem> query = realm.where(LostFoundItem.class);

        if ("Perdu".equals(currentFilter)) {
            query.equalTo("type", "lost");
        } else if ("Trouvé".equals(currentFilter)) {
            query.equalTo("type", "found");
        }

        if (!searchQuery.isEmpty()) {
            query.contains("title", searchQuery, Case.INSENSITIVE);
        }

        RealmResults<LostFoundItem> items = query.findAll();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.FRANCE);

        for (LostFoundItem item : items) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_lost_found, lostFoundContainer, false);

            LinearLayout card = itemView.findViewById(R.id.cardContainer);
            TextView status = itemView.findViewById(R.id.statusBadge);
            TextView title = itemView.findViewById(R.id.itemName);
            TextView location = itemView.findViewById(R.id.itemLocation);
            TextView date = itemView.findViewById(R.id.itemDate);
            TextView description = itemView.findViewById(R.id.itemDescription);
            Button action = itemView.findViewById(R.id.actionButton);

            title.setText(item.getTitle());
            location.setText(item.getLocation());
            date.setText(sdf.format(new Date(item.getDateLostOrFound())));
            description.setText(item.getDescription());

            if ("lost".equals(item.getType())) {
                card.setBackgroundResource(R.drawable.bg_lost_card);
                status.setText("PERDU");
                status.setBackgroundResource(R.drawable.bg_badge_lost);
                action.setText("Contacter");
            } else {
                card.setBackgroundResource(R.drawable.bg_found_card);
                status.setText("TROUVÉ");
                status.setBackgroundResource(R.drawable.bg_badge_found);
                action.setText("C'est le mien !");
            }

            action.setOnClickListener(v -> {
                if (item.getReportedBy() != null) {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("receiverEmail", item.getReportedBy());
                    intent.putExtra("listingId", item.getId());
                    intent.putExtra("listingTitle", item.getTitle());
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Impossible de contacter le membre", Toast.LENGTH_SHORT).show();
                }
            });

            lostFoundContainer.addView(itemView);
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
