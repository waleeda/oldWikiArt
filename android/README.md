# WikiArt Android Port

This is an Android port of WikiArt. The project now includes a RecyclerView
based main activity that fetches paintings from the WikiArt API using OkHttp
and coroutines. Scrolling now loads additional pages automatically using the
Paging library. Each item displays the painting image using Coil and tapping a
painting opens a detail screen showing a larger image and the title. A spinner
at the top allows choosing a painting category and the list updates accordingly.
Additional screens let you search with autocomplete, manage favourites and view
artist information. You can share or buy prints of a painting, view the image in
full screen and visit a Support screen to send feedback or make a donation

using Google Play Billing. Navigation between Paintings, Artists, Search and
Support is now provided by a BottomNavigationView instead of the old options
menu.

using Google Play Billing.
Section titles in category dialogs are shown in your device language when
available, defaulting to English otherwise.

To build the project, open the `android` directory in Android Studio or run
`gradle assembleDebug` from this folder (ensure the Android SDK and Kotlin
plugin are installed). To test the billing flow you must sign in with a Google
Play test account on your device and upload the in-app products in the Play
Console.

## Secrets

Create a `local.properties` file in the `android` directory or define the
following environment variables before building:

```
IS_OSS=false
FIELD_REPORT_EMAIL=<your email>
API_CLIENT_PRODUCTION=<client id>
API_CLIENT_STAGING=<staging client id>
API_ENDPOINT_PRODUCTION=<production endpoint>
API_ENDPOINT_STAGING=<staging endpoint>
BASIC_HTTP_AUTH_USERNAME=<username>
BASIC_HTTP_AUTH_PASSWORD=<password>
CANVAS_POP_ACCESS_KEY=<canvaspop access>
CANVAS_POP_SECRET_KEY=<canvaspop secret>
KITE_SECRET=<kite secret>
KITE_PUBLIC_KEY=<kite public key>
```

Values from `local.properties` take precedence, falling back to the environment
when not present.
