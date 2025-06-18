# WikiArt Mobile

This repository contains the legacy iOS project (`WikiArt3.0`) and a new Android app.
The Android code lives under the `WikiArt/` directory and currently provides only
an initial skeleton while the iOS implementation is feature complete.

## Current Android status

- Basic bottom navigation with three placeholder screens (Home, Dashboard and
  Notifications).
- Retrofit based API layer with calls for popular paintings and painting details.
- View binding is enabled but no UI logic beyond the template screens.

See `docs/android_design_guidelines.md` for the design approach that will bring
parity with the iOS interface.

## Planned features

The Android version will gradually adopt the functionality already available on
iOS:

- Infinite scrolling painting lists with category and layout selection via a
  bottom sheet.
- List, grid and sheet layouts matching the iOS app.
- Painting and artist detail screens, search with autocomplete and a Support
  screen for feedback and donations.
- Material 3 styling and dynamic colour on Android 12+.

## iOS reference

The iOS sources remain for guidance when porting features. A summary of the
existing iOS screens and fetchers is available in
[`docs/ios_features.md`](docs/ios_features.md).
