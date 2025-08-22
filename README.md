# Curiate App

## About the App
* Curiate is a mobile content curator that lets you save links from any app into a single, organized library.
* It also features an Explore screen where you can discover and search for new articles directly from the internet. The app automatically fetches rich previews for all links and makes your saved content available for offline reading.

## Architecture
* The app is built using Clean Architecture, which separates the code into three distinct layers: UI, Domain, and Data.
* It uses the MVVM pattern in the UI layer, UseCases for business logic in the Domain layer, and a Repository in the Data layer to manage fetching content from the network (Retrofit) and caching it in a local database (Room).
