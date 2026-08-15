package com.ibnuananta.openstream.playback

/**
 * Provider boundary for Spotify playback.
 *
 * Spotify's Android App Remote SDK is intentionally kept behind this interface so
 * the rest of the app is not coupled to provider-specific playback APIs.
 */
interface SpotifyPlaybackController {
    suspend fun connect(): Result<Unit>
    suspend fun play(uri: String): Result<Unit>
    suspend fun pause(): Result<Unit>
    suspend fun resume(): Result<Unit>
    suspend fun skipNext(): Result<Unit>
    suspend fun skipPrevious(): Result<Unit>
    suspend fun seekTo(positionMs: Long): Result<Unit>
    suspend fun disconnect()
}

class UnconfiguredSpotifyPlaybackController : SpotifyPlaybackController {
    private fun unavailable(): Result<Unit> = Result.failure(
        IllegalStateException("Spotify playback is not configured yet.")
    )

    override suspend fun connect() = unavailable()
    override suspend fun play(uri: String) = unavailable()
    override suspend fun pause() = unavailable()
    override suspend fun resume() = unavailable()
    override suspend fun skipNext() = unavailable()
    override suspend fun skipPrevious() = unavailable()
    override suspend fun seekTo(positionMs: Long) = unavailable()
    override suspend fun disconnect() = Unit
}
