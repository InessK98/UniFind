# UniFind 📦🔍

> A campus marketplace and lost-and-found Android app built for UIR students.

![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android)
![Language](https://img.shields.io/badge/Language-Java-ED8B00?style=flat-square&logo=java)
![Database](https://img.shields.io/badge/Database-Realm-39477F?style=flat-square)
![Status](https://img.shields.io/badge/Status-Academic%20Project-lightgrey?style=flat-square)

---

## What is UniFind?

UniFind is an Android application designed for the UIR student community. It combines two features in one app:

- **Marketplace** — buy and sell second-hand items between students (books, electronics, clothes, etc.)
- **Lost & Found** — report lost items on campus or claim something you found

---

## Features

| Feature | Status |
| :------ | :----- |
| Marketplace listings — post items with title, price, category | ✅ Done |
| Lost & Found board — report or claim lost items | ✅ Done |
| In-app messaging between users (local) | ✅ Done |
| Favorites — save listings you're interested in | ✅ Done |
| User profiles — view your active listings | ✅ Done |
| Category browsing and filtering | ✅ Done |
| UIR email verification (@uir.ac.ma only) | ⏳ Planned |
| Microsoft SSO login | ⏳ Planned |
| Camera access for product photos | ⏳ Planned |
| Shared backend for real multi-user support | ⏳ Planned |

---

## ⚠️ Known Limitations

This is an academic project developed as part of the Mobile Development course at UIR. The following limitations are known:

- **Authentication is local only** — UIR email validation and Microsoft SSO login are not yet implemented
- **Data is stored locally via Realm** — no shared backend, meaning users on different devices cannot interact with each other
- **Messaging works within the same device only** — due to local Realm storage
- **Camera integration is not yet implemented** — product images currently use static assets

---

## Tech Stack

| Layer | Technology |
| :---- | :--------- |
| Language | Java |
| IDE | Android Studio |
| Database | Realm 10.15.0 (local) |
| Build System | Gradle 8.5 / AGP 8.2.2 |
| Min SDK | Android 8.0 (API 26) |
| Architecture | Multi-Activity with Fragments |

---

## Project Structure

```
UniFind/
├── app/
│   ├── src/main/java/com/example/unifind/
│   │   ├── models/              # Realm models: User, Listing, LostFoundItem, Message, Category, Report, SavedListing
│   │   ├── MainActivity.java    # Entry point, bottom navigation
│   │   ├── LoginActivity.java   # Login screen
│   │   ├── MarketFragment.java  # Marketplace feed
│   │   ├── LostFoundFragment.java
│   │   ├── PostFragment.java    # Create a new listing
│   │   ├── ProfileFragment.java
│   │   ├── ChatActivity.java    # Chat screen
│   │   ├── MessagesActivity.java # Inbox
│   │   ├── ProductDetailActivity.java
│   │   ├── FavoritesActivity.java
│   │   └── MyListingsActivity.java
│   └── res/
│       ├── layout/              # XML layouts for all screens
│       └── drawable/            # Icons and assets
└── build.gradle
```

---

## Getting Started

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 17
- Android device or emulator (API 26+)

### Run locally
```bash
git clone https://github.com/InessK98/UniFind.git
cd UniFind
# Open in Android Studio → Sync Gradle → Run
```

---

## Screenshots

> *Coming soon*

---

## Author

**Iness K** — Computer Engineering Student @ UIR Rabat  
🔗 [GitHub](https://github.com/InessK98)

---

## License

Developed as part of the Mobile Development course at UIR – Université Internationale de Rabat.
