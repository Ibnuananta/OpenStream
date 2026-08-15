# OpenStream

OpenStream is a native Android music client built with Kotlin and Jetpack Compose. It uses Spotify's catalog and playback infrastructure rather than hosting audio.

> **Important:** Spotify playback requires a Spotify Premium account and an active Spotify Connect device. OpenStream does not download, host, or stream raw Spotify audio.

## MVP

- Spotify OAuth Authorization Code with PKCE
- Search tracks, artists, albums, and playlists
- Browse result details
- Personal playlists and saved tracks
- Now Playing UI and playback controls
- Clean, custom Jetpack Compose UI

## Setup

1. Create a Spotify Developer application.
2. Add an Android redirect URI for your package/application setup.
3. Put your Spotify client ID in `local.properties` as `SPOTIFY_CLIENT_ID=...`.
4. Open the project in Android Studio and run the `app` configuration.
5. Complete Spotify login. Playback integration is isolated behind `SpotifyPlaybackController` so the Spotify App Remote SDK can be wired in without coupling the UI to it.

The initial repository intentionally contains no client secret. Mobile authentication uses PKCE.

## Architecture

- **UI:** Jetpack Compose + Material 3
- **State:** ViewModel + StateFlow
- **Networking:** Retrofit + OkHttp + Kotlin serialization
- **Auth:** Spotify Authorization Code + PKCE
- **Playback:** `SpotifyPlaybackController` abstraction, ready for Spotify Android App Remote integration
- **Persistence:** DataStore for auth/session state

See `PRD.md` for the product scope and constraints.
