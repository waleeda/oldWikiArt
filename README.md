# WikiArt Mobile

This repository originally contained both an iOS project and an Android port.
The iOS project has been removed, leaving only the Android source code.

* `android` – Android application written in Kotlin.

The Android port now displays paintings retrieved from the WikiArt API with
endless scrolling support powered by the Paging library. Only one page of
results is fetched at a time and the next page loads after most of the current
items have been scrolled through. A category spinner
lets you filter paintings (e.g. Featured or Popular). List items load images
using Coil, and tapping an item opens a detail screen. You can search with
autocomplete suggestions, mark paintings as favourites, view artist details and
share or buy prints of a painting. Images can also be viewed full screen with
pinch‑to‑zoom and a Support screen lets you send feedback or donate via
Google Play in‑app purchases. Section titles are localized using your device
language with a fallback to English when needed.
