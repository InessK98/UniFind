package com.example.unifind;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.unifind.models.User;
import com.example.unifind.models.message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ChatActivity extends AppCompatActivity {

    private LinearLayout chatMessagesContainer;
    private NestedScrollView chatScrollView;
    private EditText chatInput;
    private Realm realm;
    private SharedPreferences sharedPreferences;
    
    private String currentEmail;
    private String receiverEmail;
    private String listingId;
    private String listingTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        realm = Realm.getDefaultInstance();
        sharedPreferences = getSharedPreferences("UniFindPrefs", Context.MODE_PRIVATE);
        
        currentEmail = sharedPreferences.getString("loggedInEmail", null);
        receiverEmail = getIntent().getStringExtra("receiverEmail");
        listingId = getIntent().getStringExtra("listingId");
        listingTitle = getIntent().getStringExtra("listingTitle");

        if (currentEmail == null || receiverEmail == null) {
            finish();
            return;
        }

        chatMessagesContainer = findViewById(R.id.chatMessagesContainer);
        chatScrollView = findViewById(R.id.chatScrollView);
        chatInput = findViewById(R.id.chatInput);

        TextView nameText = findViewById(R.id.chatReceiverName);
        TextView titleText = findViewById(R.id.chatListingTitle);

        User receiver = realm.where(User.class).equalTo("email", receiverEmail).findFirst();
        if (receiver != null) {
            nameText.setText(receiver.getName());
        } else {
            nameText.setText(receiverEmail);
        }
        titleText.setText(listingTitle);

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        findViewById(R.id.sendButton).setOnClickListener(v -> sendMessage());

        loadMessages();
    }

    private void loadMessages() {
        chatMessagesContainer.removeAllViews();
        
        // Query messages between these two users
        RealmResults<message> messages = realm.where(message.class)
                .beginGroup()
                    .equalTo("senderId", currentEmail).equalTo("receiverId", receiverEmail)
                .endGroup()
                .or()
                .beginGroup()
                    .equalTo("senderId", receiverEmail).equalTo("receiverId", currentEmail)
                .endGroup()
                .findAll()
                .sort("sentAt", Sort.ASCENDING);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        for (message msg : messages) {
            addMessageToUI(msg, sdf);
        }
        
        scrollToBottom();
    }

    private void addMessageToUI(message msg, SimpleDateFormat sdf) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_chat_message, chatMessagesContainer, false);
        
        boolean isSentByMe = msg.getSenderId().equals(currentEmail);
        
        LinearLayout sentLayout = view.findViewById(R.id.sentMessageLayout);
        LinearLayout receivedLayout = view.findViewById(R.id.receivedMessageLayout);
        
        if (isSentByMe) {
            sentLayout.setVisibility(View.VISIBLE);
            receivedLayout.setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.sentText)).setText(msg.getContent());
            ((TextView) view.findViewById(R.id.sentTime)).setText(sdf.format(new Date(msg.getSentAt())));
        } else {
            sentLayout.setVisibility(View.GONE);
            receivedLayout.setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.receivedText)).setText(msg.getContent());
            ((TextView) view.findViewById(R.id.receivedTime)).setText(sdf.format(new Date(msg.getSentAt())));
        }
        
        chatMessagesContainer.addView(view);
    }

    private void sendMessage() {
        String content = chatInput.getText().toString().trim();
        if (content.isEmpty()) return;

        realm.executeTransaction(r -> {
            message msg = r.createObject(message.class, UUID.randomUUID().toString());
            msg.setContent(content);
            msg.setSenderId(currentEmail);
            msg.setReceiverId(receiverEmail);
            msg.setListingId(listingId);
            msg.setSentAt(System.currentTimeMillis());
            msg.setRead(false);
        });

        chatInput.setText("");
        loadMessages();
    }

    private void scrollToBottom() {
        chatScrollView.post(() -> chatScrollView.fullScroll(View.FOCUS_DOWN));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) realm.close();
    }
}