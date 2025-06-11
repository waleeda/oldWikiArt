# WikiArt Mobile

This repository contains the original iOS project and an Android port.

* `WikiArt3.0` – iOS application written in Swift.
* `android` – Android port written in Kotlin.

The Android port now displays paintings retrieved from the WikiArt API with
endless scrolling support powered by the Paging library. A category spinner
lets you filter paintings (e.g. Featured or Popular). List items load images
using Coil, and tapping an item opens a detail screen. You can search with
autocomplete suggestions, mark paintings as favourites, view artist details and
share or buy prints of a painting. Images can also be viewed full screen with
pinch‑to‑zoom and a Support screen lets you send feedback or donate via
Google Play in‑app purchases. Section titles are localized using your device
language with a fallback to English when needed.
