# WikiArt Android Port

This is an Android port of WikiArt. The project now includes a RecyclerView
based main activity that fetches a list of featured paintings from the
WikiArt API using OkHttp and coroutines. Scrolling now loads additional
pages automatically using the Paging library.

To build the project, open the `android` directory in Android Studio or run
`gradle assembleDebug` from this folder (ensure the Android SDK and Kotlin
plugin are installed).
