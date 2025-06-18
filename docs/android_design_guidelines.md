# Android Design Guidelines

This document summarises how the Android version should mimic the behaviour and appearance of the iOS app found in `WikiArt3.0`.

## Layout and Navigation

- Use a bottom navigation bar with the same sections as iOS: **Paintings**, **Artists**, **Search** and **Support**.
- Present filter options and layout choices in a bottom sheet similar to `OptionsBottomSheet.swift`.
- Support three painting list layouts: **list**, **grid** and **sheet**. The grid layout should keep each image's original aspect ratio.
- Painting lists must scroll infinitely, loading additional pages once the user nears the end.

## Screens

- **Painting Detail** should show favourite, share and buy actions plus related artworks.
- **Artist Detail** displays the biography and list of paintings.
- **Search Results** provide a search bar with autocomplete and sections for paintings and artists.
- **Support** allows feedback and donations.

Follow Material guidelines for components while keeping the structure as close as possible to the iOS implementation.
