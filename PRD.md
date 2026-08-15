# PRD: OpenStream — Personal Music Streaming App

**Author:** Solo project  
**Status:** Draft  
**Format:** Lean PRD  
**Last updated:** August 16, 2026

## 1. Executive Summary

OpenStream is a mobile app for Android that lets a user search, browse, and control playback of music using the Spotify Web API and Android App Remote SDK, without requiring a paid backend. The goal is a personal-use or portfolio-quality custom music player layered on top of Spotify's catalog and playback infrastructure.

## 2. Critical Technical Constraint

Spotify's Web API does not provide raw audio bytes for an independent streaming client. Playback through Spotify's supported mobile integration requires Spotify Premium and an active Spotify Connect device. Non-Premium users can use catalog/search functionality but cannot use this playback flow. OpenStream is therefore a custom Spotify client, not an independent music-streaming service.

## 3. Goals

- Ship a working native Android app in Kotlin.
- Search Spotify's catalog and play tracks for Premium users.
- Use Spotify's free developer tier with no custom backend for MVP.
- Keep the first release solo-buildable and focused.
- Provide a distinctive, clean UI rather than cloning Spotify.

## 4. MVP Scope

- Spotify OAuth login using Authorization Code with PKCE.
- Search tracks, albums, artists, and playlists.
- Artist, album, and playlist detail screens.
- Playback control: play, pause, skip, seek, and queue.
- Now Playing screen with album art, track/artist metadata, and progress.
- User's existing Spotify playlists.
- Basic saved-track and saved-album library.
- Loading, empty, authentication, rate-limit, and no-active-device states.

## 5. Out of Scope for v1

- Hosting or uploading audio.
- Non-Premium playback.
- Offline downloads.
- Social features.
- Desktop/web client.
- Custom recommendation engine.

## 6. Future Considerations

- Deezer/Jamendo fallback catalog.
- Lightweight custom backend for custom playlists.
- Widgets and lock-screen controls.
- Social/collaborative playlists.

## 7. Architecture

- Native Android, Kotlin, Jetpack Compose.
- ViewModel + StateFlow for presentation state.
- Retrofit/OkHttp for Spotify Web API access.
- Kotlin serialization for API models.
- DataStore for local auth/session state.
- OAuth PKCE with no client secret in the Android app.
- A playback abstraction isolates Spotify App Remote SDK integration from UI and domain code.

## 8. Acceptance Criteria

### Authentication

- PKCE login completes and tokens are stored securely enough for the MVP session model.
- Expired/invalid authentication is handled with a re-login path.
- Login state survives app restarts.

### Search

- Search supports tracks, artists, albums, and playlists.
- Results are categorized and open to detail screens.
- Search has loading/error states and avoids unnecessary API requests through debouncing/caching.

### Playback

- Playback commands work when Spotify Premium and an active Spotify Connect device are available.
- The app explains how to recover when no active device exists.
- Now Playing progress updates while playback is active.

### Library

- Existing playlists and saved tracks/albums can be listed.
- Playlist tracks can be opened for playback.

## 9. Risks

- Spotify API/SDK changes: isolate provider-specific code.
- Rate limits: debounce searches and cache recent results.
- Premium dependency: clearly communicate playback requirements.
- Solo scope creep: keep v1 limited to the MVP list.
