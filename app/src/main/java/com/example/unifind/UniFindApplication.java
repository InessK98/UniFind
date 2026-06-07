package com.example.unifind;

import android.app.Application;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class UniFindApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Realm
        Realm.init(this);

        // Configure Realm
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("unifind.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .allowWritesOnUiThread(true) // Added to fix the crash
                .build();

        Realm.setDefaultConfiguration(config);
    }
}