package com.example.unifind;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unifind.models.Listing;
import com.example.unifind.models.User;
import com.example.unifind.models.message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MessagesActivity extends AppCompatActivity {

    private LinearLayout messagesContainer;
    private Realm realm;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        realm = Realm.getDefaultInstance();
        sharedPreferences = getSharedPreferences("UniFindPrefs", Context.MODE_PRIVATE);
        messagesContainer = findViewById(R.id.messagesContainer);

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        loadMessages();
    }

    private void loadMessages() {
        messagesContainer.removeAllViews();
        String currentEmail = sharedPreferences.getString("loggedInEmail", null);

        if (currentEmail == null) return;

        // Find messages where user is sender or receiver
        RealmResults<message> results = realm.where(message.class)
                .equalTo("receiverId", currentEmail)
                .or()
                .equalTo("senderId", currentEmail)
                .findAll()
                .sort("sentAt", Sort.DESCENDING);

        if (results.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("Aucun message pour le moment");
            emptyText.setGravity(View.TEXT_ALIGNMENT_CENTER);
            emptyText.setPadding(0, 100, 0, 0);
            messagesContainer.addView(emptyText);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        for (message msg : results) {
            View msgView = LayoutInflater.from(this).inflate(R.layout.item_message_preview, messagesContainer, false);
            
            TextView senderText = msgView.findViewById(R.id.msgSender);
            TextView contentText = msgView.findViewById(R.id.msgContent);
            TextView timeText = msgView.findViewById(R.id.msgTime);
            TextView avatarText = msgView.findViewById(R.id.msgAvatar);

            String otherParty = msg.getSenderId().equals(currentEmail) ? msg.getReceiverId() : msg.getSenderId();
            
            // Try to find the name of the other party
            User otherUser = realm.where(User.class).equalTo("email", otherParty).findFirst();
            String displayName = (otherUser != null) ? otherUser.getName() : otherParty;
            
            senderText.setText(displayName);
            contentText.setText(msg.getContent());
            timeText.setText(sdf.format(new Date(msg.getSentAt())));
            avatarText.setText(displayName.substring(0, 1).toUpperCase());

            msgView.setOnClickListener(v -> {
                Intent intent = new Intent(MessagesActivity.this, ChatActivity.class);
                intent.putExtra("receiverEmail", otherParty);
                intent.putExtra("listingId", msg.getListingId());
                
                Listing listing = realm.where(Listing.class).equalTo("id", msg.getListingId()).findFirst();
                if (listing != null) {
                    intent.putExtra("listingTitle", listing.getTitle());
                }
                
                startActivity(intent);
            });

            messagesContainer.addView(msgView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) realm.close();
    }
}