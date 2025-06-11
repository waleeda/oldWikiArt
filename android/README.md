# WikiArt Android Port

This is an Android port of WikiArt. The project now includes a RecyclerView
based main activity that fetches paintings from the WikiArt API using OkHttp
and coroutines. Scrolling now loads additional pages automatically using the
Paging library. Each item displays the painting image using Coil and tapping a
painting opens a detail screen showing a larger image and the title. A spinner
at the top allows choosing a painting category and the list updates
accordingly.

To build the project, open the `android` directory in Android Studio or run
`gradle assembleDebug` from this folder (ensure the Android SDK and Kotlin
plugin are installed).
