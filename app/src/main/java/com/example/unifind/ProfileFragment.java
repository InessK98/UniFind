package com.example.unifind;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unifind.models.User;

import io.realm.Realm;

public class ProfileFragment extends Fragment {

    private TextView profileName, profileEmail, profileAvatar, profileTotalListings, profileSoldListings, profileRating;
    private Realm realm;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        realm = Realm.getDefaultInstance();
        sharedPreferences = getActivity().getSharedPreferences("UniFindPrefs", Context.MODE_PRIVATE);

        // Find views
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileAvatar = view.findViewById(R.id.profileAvatar);
        profileTotalListings = view.findViewById(R.id.profileTotalListings);
        profileSoldListings = view.findViewById(R.id.profileSoldListings);
        profileRating = view.findViewById(R.id.profileRating);

        loadUserProfile();
        setupClickListeners(view);

        return view;
    }

    private void loadUserProfile() {
        String loggedInEmail = sharedPreferences.getString("loggedInEmail", null);

        if (loggedInEmail != null) {
            User user = realm.where(User.class).equalTo("email", loggedInEmail).findFirst();
            if (user != null) {
                profileName.setText(user.getName());
                profileEmail.setText(user.getEmail());
                profileTotalListings.setText(String.valueOf(user.getTotalListings()));
                profileSoldListings.setText(String.valueOf(user.getSoldListings()));
                profileRating.setText(String.valueOf(user.getRating()));

                // Set avatar to first letter of name
                if (user.getName() != null && !user.getName().isEmpty()) {
                    profileAvatar.setText(user.getName().substring(0, 1).toUpperCase());
                }
            }
        }
    }

    private void setupClickListeners(View view) {
        view.findViewById(R.id.optAnnonces).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MyListingsActivity.class);
            startActivity(intent);
        });
        
        view.findViewById(R.id.optFavoris).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FavoritesActivity.class);
            startActivity(intent);
        });

        view.findViewById(R.id.optMessages).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MessagesActivity.class);
            startActivity(intent);
        });
        
        view.findViewById(R.id.optLogout).setOnClickListener(v -> {
            // Clear preferences
            sharedPreferences.edit().remove("loggedInEmail").apply();
            
            // Go back to login
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }
}