# iOS Feature Overview

This document lists the view controllers and related data fetchers in the `WikiArt3.0` project. It helps when porting features to Android.

## Screens
- **PaintingCollectionViewController** – infinite list of paintings with category filter and layout switcher.
- **PaintingDetailViewController** – details for a painting with favourite, share, buy merch and related artworks.
- **ArtistsViewController** – list of artists with category/section filters.
- **ArtistDetailViewController** – artist biography and paintings.
- **AllPaintingCollectionViewController** – shows all paintings for a specific artist.
- **SearchResultsViewController** – search screen with autocomplete, painting and artist result rows.
- **SupportViewController** – feedback and donation options.

## Data operations
All networking is performed via `Service` which implements `ServiceType`. Common fetchers wrap API calls:
- `PaintingListFetcher` – paged lists of paintings by category.
- `PaintingDetailsFetcher` – single painting details.
- `RelatedPaintingsFetcher` – list of related artworks.
- `ArtistDetailsFetcher` – artist biography and info.
- `ArtistsListFectcher` – paged artists for a category/section.
- `CategorySectionsFetcher` and `ArtistsCategorySectionsFetcher` – load section lists for categories.
- `PaintingSearchResultsFetcher`, `ArtistSearchResultsFetcher` and `AutoCompleteSearchResultsFetcher` – search APIs.
- Favourites are stored locally via `Favorites.swift`.

