# WikiArt Mobile

This repository originally contained both an iOS project and an Android port.
The Android app is actively maintained while the iOS sources remain for
reference.

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

Paintings can be viewed in three layouts—list, grid and sheet—mirroring the
iOS design. The list and grid layouts display the artist name, title and year
beneath each image while the sheet layout shows only the artwork image. The
grid layout now uses a staggered grid so images keep their original aspect
ratio just like on iOS.

The latest update adds Material You dynamic color support on Android 12+
devices, automatically adapting the app theme to the user's wallpaper.

## Future improvements

- Cache paintings for offline viewing
- Port additional iOS features such as artwork collections
- Rewrite the Android interface using Jetpack Compose
