# WikiArt Mobile

This repository contains the legacy iOS project (`WikiArt3.0`) and a new Android app.
The Android code lives under the `WikiArt/` directory and now implements the
paintings list with category selection and multiple layouts. The iOS project
remains feature complete and serves as a reference for the Android port.

## Current Android status

- Bottom navigation with Paintings, Artists, Search and Support sections.
- Painting list screen with infinite scrolling and categories such as Featured,
  Popular and more.
- Layout selector supporting list, grid and sheet presentations via a bottom
  sheet.
- Retrofit based API layer and ViewModel driven UI using LiveData.

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


This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.
