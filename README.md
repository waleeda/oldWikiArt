# WikiArt Mobile

This repository contains the original iOS project and an Android port.

* `WikiArt3.0` – iOS application written in Swift.
* `android` – Android port written in Kotlin.

The Android port now displays paintings retrieved from the WikiArt API with
endless scrolling support powered by the Paging library. A category spinner
lets you filter paintings (e.g. Featured or Popular). List items load images
using Coil, and tapping an item opens a simple detail screen.
