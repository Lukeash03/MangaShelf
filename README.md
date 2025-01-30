# MangaShelf App
## Overview
<p>MangaShelf is an Android application that allows users to explore and track their favorite manga. The app retrieves manga data from an API, stores it in a local database, and provides functionalities such as sorting, filtering, favoriting, and marking manga as read. The UI is responsive and user-friendly, ensuring a seamless experience.</p>

## Features
### 1. Manga List Page (Home Screen)

Displays a list of mangas with:
- Title, Cover Image, Score, Popularity, Year of Publication.

Sorting & Filtering:
- Manga are grouped into tabs sorted by ascending publication year.
- Tabs are scrollable and auto-select based on scroll position.
- Sorting options:
  - Score (ascending/descending)
  - Popularity (ascending/descending)

Favorite Button:
- Users can mark/unmark manga as a favorite.
- Favorites are persisted across sessions using a local database.

Error Handling:
- Displays appropriate messages for network failures and empty data.

### 2. Manga Detail Page
Displays detailed information:
- Title, Cover Image, Score, Popularity, Published Date, Category.
Favoriting Feature:
- Users can mark/unmark manga as favorite.
- Mark as Read Feature:

## **Tech Stack**
- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Dagger-Hilt for cleaner architecture and easier testing
- **Network**: Retrofit
- **Local Storage**: Room Database
- **UI Components**: Jetpack Compose, LazyColum, ScrollableTabRow, Material Design Components
- **Coroutine Support**: Used for asynchronous tasks

## Installation
- Clone the repository: git clone ```https://github.com/yourusername/mangashelf.git```
- Open the project in Android Studio.
- Build and run the application on an emulator or physical device.

## API Integration
- Manga data is fetched from: ```https://jsonkeeper.com/b/KEJO```
- The app checks the database first. If no data is found, it fetches data from the API.
- Additionally, a refresh button on the home screen allows users to force-fetch data from the API.

## Author
Luqman Ashraf
