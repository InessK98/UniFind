package com.example.unifind;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unifind.models.Listing;
import com.example.unifind.models.LostFoundItem;

import java.util.UUID;

import io.realm.Realm;

public class PostFragment extends Fragment {

    private EditText editTitre, editPrix, editDescription;
    private Spinner spinnerCategorie, spinnerEtat, spinnerLFType;
    private RadioGroup radioGroupSection;
    private RadioButton radioMarket, radioLostFound;
    private LinearLayout layoutMarketFields, layoutLostFoundFields;
    private Button btnPublier;
    private Realm realm;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        
        realm = Realm.getDefaultInstance();
        sharedPreferences = getActivity().getSharedPreferences("UniFindPrefs", Context.MODE_PRIVATE);
        
        radioGroupSection = view.findViewById(R.id.radioGroupSection);
        radioMarket = view.findViewById(R.id.radioMarket);
        radioLostFound = view.findViewById(R.id.radioLostFound);
        layoutMarketFields = view.findViewById(R.id.layoutMarketFields);
        layoutLostFoundFields = view.findViewById(R.id.layoutLostFoundFields);
        
        editTitre = view.findViewById(R.id.editTitre);
        editPrix = view.findViewById(R.id.editPrix);
        editDescription = view.findViewById(R.id.editDescription);
        spinnerCategorie = view.findViewById(R.id.spinnerCategorie);
        spinnerEtat = view.findViewById(R.id.spinnerEtat);
        spinnerLFType = view.findViewById(R.id.spinnerLFType);
        btnPublier = view.findViewById(R.id.btnPublier);

        setupSpinners();
        
        radioGroupSection.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioMarket) {
                layoutMarketFields.setVisibility(View.VISIBLE);
                layoutLostFoundFields.setVisibility(View.GONE);
            } else {
                layoutMarketFields.setVisibility(View.GONE);
                layoutLostFoundFields.setVisibility(View.VISIBLE);
            }
        });
        
        btnPublier.setOnClickListener(v -> publier());

        return view;
    }

    private void setupSpinners() {
        String[] categories = {"Vêtements", "Livres", "Électronique", "Meubles", "Sport", "Autre"};
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(catAdapter);

        String[] etats = {"Neuf", "Comme neuf", "Bon état", "État correct"};
        ArrayAdapter<String> etatAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, etats);
        etatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEtat.setAdapter(etatAdapter);

        String[] lfTypes = {"Perdu", "Trouvé"};
        ArrayAdapter<String> lfAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lfTypes);
        lfAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLFType.setAdapter(lfAdapter);
    }

    private void publier() {
        String titre = editTitre.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String categorie = spinnerCategorie.getSelectedItem().toString();

        if (titre.isEmpty()) {
            Toast.makeText(getContext(), "Veuillez entrer un titre", Toast.LENGTH_SHORT).show();
            return;
        }

        if (radioMarket.isChecked()) {
            publierMarket(titre, description, categorie);
        } else {
            publierLostFound(titre, description, categorie);
        }
    }

    private void publierMarket(String titre, String description, String categorie) {
        String prixStr = editPrix.getText().toString().trim();
        String etat = spinnerEtat.getSelectedItem().toString().toUpperCase();

        if (prixStr.isEmpty()) {
            Toast.makeText(getContext(), "Veuillez entrer un prix", Toast.LENGTH_SHORT).show();
            return;
        }

        double prix = Double.parseDouble(prixStr);
        String currentEmail = sharedPreferences.getString("loggedInEmail", "anonymous");

        realm.executeTransaction(r -> {
            Listing listing = r.createObject(Listing.class, UUID.randomUUID().toString());
            listing.setTitle(titre);
            listing.setPrice(prix);
            listing.setDescription(description);
            listing.setCategory(categorie);
            listing.setCondition(etat);
            listing.setLocation("Campus");
            listing.setCreatedAt(System.currentTimeMillis());
            listing.setStatus("available");
            listing.setSellerId(currentEmail); // Store who posted it
        });

        Toast.makeText(getContext(), "Article publié dans le Marketplace !", Toast.LENGTH_SHORT).show();
        clearFields();
    }

    private void publierLostFound(String titre, String description, String categorie) {
        String type = spinnerLFType.getSelectedItem().toString().toLowerCase();
        String currentEmail = sharedPreferences.getString("loggedInEmail", "anonymous");

        realm.executeTransaction(r -> {
            LostFoundItem item = r.createObject(LostFoundItem.class, UUID.randomUUID().toString());
            item.setTitle(titre);
            item.setDescription(description);
            item.setType(type); // "perdu" or "trouvé"
            item.setLocation("Campus");
            item.setDateLostOrFound(System.currentTimeMillis());
            item.setCreatedAt(System.currentTimeMillis());
            item.setResolved(false);
            item.setReportedBy(currentEmail); // Store who reported it
        });

        Toast.makeText(getContext(), "Signalement publié dans Objets Trouvés !", Toast.LENGTH_SHORT).show();
        clearFields();
    }

    private void clearFields() {
        editTitre.setText("");
        editPrix.setText("");
        editDescription.setText("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }
}