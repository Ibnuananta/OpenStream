package com.ibnuananta.openstream

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Album
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.QueueMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.icons.outlined.SkipPrevious
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Sky100 = Color(0xFFE3F2FD)
private val Sky300 = Color(0xFF90CAF9)
private val Blue500 = Color(0xFF2196F3)
private val Blue700 = Color(0xFF1669C4)
private val Blue900 = Color(0xFF0D47A1)
private val Ink = Color(0xFF071B34)
private val Paper = Color(0xFFFAFDFF)
private val Muted = Color(0xFF5C7391)
private val NavMuted = Color(0xFF8BA3C2)
private val Mono = FontFamily.Monospace
private val Display = FontFamily.SansSerif
private val Body = FontFamily.SansSerif

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { OpenStreamApp() }
    }
}

@Composable
private fun OpenStreamApp() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showPlayer by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(true) }

    MaterialTheme {
        Surface(Modifier.fillMaxSize(), color = Sky100) {
            if (showPlayer) {
                NowPlayingScreen(isPlaying, { isPlaying = !isPlaying }) { showPlayer = false }
            } else {
                Scaffold(
                    containerColor = Sky100,
                    bottomBar = { OpenStreamBottomBar(selectedTab) { selectedTab = it } }
                ) { padding ->
                    when (selectedTab) {
                        0 -> HomeScreen(Modifier.padding(padding), { showPlayer = true }, isPlaying) { isPlaying = !isPlaying }
                        1 -> SearchScreen(Modifier.padding(padding))
                        2 -> LibraryScreen(Modifier.padding(padding))
                        else -> ProfileScreen(Modifier.padding(padding))
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(modifier: Modifier, onOpenPlayer: () -> Unit, isPlaying: Boolean, onPlayPause: () -> Unit) {
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Box(Modifier.fillMaxWidth().height(250.dp)) {
            Box(Modifier.size(260.dp).align(Alignment.TopEnd).clip(CircleShape).background(Brush.linearGradient(listOf(Sky300, Blue500, Blue900))))
            Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 34.dp)) {
                MonoLabel("OPENSTREAM · YOUR DAILY MIX")
                Spacer(Modifier.height(14.dp))
                Text("Good morning,\nIbnu.", fontFamily = Display, fontWeight = FontWeight.ExtraBold, fontSize = 34.sp, lineHeight = 38.sp, color = Ink)
                Spacer(Modifier.height(12.dp))
                Text("Pick up where you left off.", fontFamily = Body, fontSize = 14.sp, color = Blue700)
            }
        }
        SectionHeader("Recently played")
        LazyRow(contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 24.dp, end = 24.dp), horizontalArrangement = Arrangement.spacedBy(18.dp)) {
            items(listOf("Midnight Drive", "Blue Hour", "Ocean Eyes", "Night Shift")) { RecentCard(it) }
        }
        Spacer(Modifier.height(30.dp))
        SectionHeader("Made for you")
        MadeForYouCard(onOpenPlayer, "OpenStream Daily Mix", "42 TRACKS", "2H 48M")
        Spacer(Modifier.height(28.dp))
        SectionHeader("Quick library")
        Row(Modifier.padding(horizontal = 24.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickTile("Liked songs", Icons.Outlined.FavoriteBorder)
            QuickTile("Albums", Icons.Outlined.Album)
        }
        Spacer(Modifier.height(112.dp))
        MiniPlayer(Modifier.padding(horizontal = 14.dp, vertical = 12.dp), isPlaying, onOpenPlayer, onPlayPause)
    }
}

@Composable
private fun RecentCard(title: String) {
    Column(Modifier.width(126.dp)) {
        AlbumArt(Modifier.size(126.dp), listOf(Sky300, Blue700, Blue900))
        Spacer(Modifier.height(9.dp))
        Text(title, fontFamily = Display, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Ink)
        Text("Spotify session", fontFamily = Body, fontSize = 11.sp, color = Muted)
    }
}

@Composable
private fun MadeForYouCard(onClick: () -> Unit, title: String, count: String, duration: String) {
    Row(Modifier.padding(horizontal = 24.dp).fillMaxWidth().clip(RoundedCornerShape(26.dp)).background(Paper).clickable(onClick = onClick).padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
        AlbumArt(Modifier.size(92.dp), listOf(Sky100, Blue500, Blue900))
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            MonoLabel("$count · $duration")
            Spacer(Modifier.height(7.dp))
            Text(title, fontFamily = Display, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
            Text("Your personalized rotation", fontFamily = Body, fontSize = 12.sp, color = Muted)
        }
        PlayFab(onClick)
    }
}

@Composable
private fun QuickTile(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(Modifier.clip(RoundedCornerShape(22.dp)).background(Sky300.copy(alpha = .52f)).padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Blue700, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(9.dp))
        Text(title, fontFamily = Body, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Ink)
    }
}

@Composable
private fun SearchScreen(modifier: Modifier) {
    Column(modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 30.dp)) {
        MonoLabel("DISCOVER")
        Spacer(Modifier.height(10.dp))
        Text("Find your next track.", fontFamily = Display, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, color = Ink)
        Spacer(Modifier.height(22.dp))
        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(999.dp)).background(Paper).padding(horizontal = 18.dp, vertical = 15.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Search, null, tint = Blue700)
            Spacer(Modifier.width(12.dp))
            Text("Tracks, artists, albums, playlists", fontFamily = Body, color = Muted, fontSize = 14.sp)
        }
        Spacer(Modifier.height(30.dp))
        SectionHeader("Browse")
        listOf("Tracks", "Artists", "Albums", "Playlists").forEach { item ->
            Row(Modifier.fillMaxWidth().padding(vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(44.dp).clip(RoundedCornerShape(16.dp)).background(Sky300))
                Spacer(Modifier.width(14.dp))
                Text(item, fontFamily = Display, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Ink)
            }
        }
    }
}

@Composable
private fun LibraryScreen(modifier: Modifier) {
    Column(modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 30.dp)) {
        MonoLabel("YOUR LIBRARY")
        Spacer(Modifier.height(10.dp))
        Text("Your collection.", fontFamily = Display, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, color = Ink)
        Spacer(Modifier.height(24.dp))
        listOf("Liked songs", "Your playlists", "Saved albums").forEachIndexed { index, item ->
            Row(Modifier.fillMaxWidth().padding(vertical = 9.dp), verticalAlignment = Alignment.CenterVertically) {
                AlbumArt(Modifier.size(58.dp), listOf(Sky300, Blue500, Blue900), index)
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(item, fontFamily = Display, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Ink)
                    Text("Spotify library", fontFamily = Body, fontSize = 12.sp, color = Muted)
                }
                Icon(Icons.Outlined.MoreHoriz, null, tint = Muted)
            }
        }
    }
}

@Composable
private fun ProfileScreen(modifier: Modifier) {
    Column(modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(88.dp).clip(CircleShape).background(Blue900), contentAlignment = Alignment.Center) { Text("I", color = Paper, fontFamily = Display, fontWeight = FontWeight.ExtraBold, fontSize = 32.sp) }
        Spacer(Modifier.height(18.dp))
        Text("Spotify account", fontFamily = Display, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Ink)
        Text("Premium playback enabled when connected", fontFamily = Body, fontSize = 12.sp, color = Muted)
    }
}

@Composable
private fun NowPlayingScreen(isPlaying: Boolean, onPlayPause: () -> Unit, onClose: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Blue900).padding(horizontal = 24.dp)) {
        Row(Modifier.fillMaxWidth().padding(top = 22.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(onClick = onClose) { Text("⌄", color = Paper, fontSize = 30.sp) }
            MonoLabel("NOW PLAYING", true)
            IconButton(onClick = {}) { Icon(Icons.Outlined.QueueMusic, null, tint = Sky300) }
        }
        Spacer(Modifier.height(24.dp))
        AlbumArt(Modifier.fillMaxWidth().aspectRatio(1f).shadow(24.dp, RoundedCornerShape(32.dp)), listOf(Sky100, Blue500, Blue900), 3)
        Spacer(Modifier.height(28.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Midnight Drive", fontFamily = Display, fontWeight = FontWeight.ExtraBold, fontSize = 23.sp, color = Paper, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(5.dp))
                Text("OpenStream Radio", fontFamily = Body, fontSize = 14.sp, color = Sky300)
            }
            IconButton(onClick = {}) { Icon(Icons.Outlined.FavoriteBorder, null, tint = Paper) }
        }
        Spacer(Modifier.height(24.dp))
        Box(Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(999.dp)).background(Color.White.copy(alpha = .18f))) {
            Box(Modifier.fillMaxWidth(.38f).height(4.dp).clip(RoundedCornerShape(999.dp)).background(Sky300))
        }
        Row(Modifier.fillMaxWidth().padding(top = 7.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("1:42", fontFamily = Mono, fontSize = 11.sp, color = Sky300)
            Text("4:18", fontFamily = Mono, fontSize = 11.sp, color = Sky300)
        }
        Spacer(Modifier.height(20.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
            IconButton(onClick = {}) { Icon(Icons.Outlined.Speed, null, tint = Sky300, modifier = Modifier.size(20.dp)) }
            IconButton(onClick = {}) { Icon(Icons.Outlined.SkipPrevious, null, tint = Paper, modifier = Modifier.size(30.dp)) }
            Box(Modifier.size(66.dp).clip(CircleShape).background(Sky100), contentAlignment = Alignment.Center) {
                IconButton(onClick = onPlayPause) { Icon(if (isPlaying) Icons.Outlined.Pause else Icons.Outlined.PlayArrow, null, tint = Blue900, modifier = Modifier.size(32.dp)) }
            }
            IconButton(onClick = {}) { Icon(Icons.Outlined.SkipNext, null, tint = Paper, modifier = Modifier.size(30.dp)) }
            IconButton(onClick = {}) { Icon(Icons.Outlined.VolumeUp, null, tint = Sky300, modifier = Modifier.size(20.dp)) }
        }
        Spacer(Modifier.weight(1f))
        Row(Modifier.fillMaxWidth().padding(bottom = 24.dp), horizontalArrangement = Arrangement.Center) {
            Text("SPOTIFY CONNECT · PREMIUM", fontFamily = Mono, fontSize = 10.sp, color = Sky300, letterSpacing = 1.2.sp)
        }
    }
}

@Composable
private fun MiniPlayer(modifier: Modifier, isPlaying: Boolean, onOpen: () -> Unit, onPlayPause: () -> Unit) {
    Row(modifier.fillMaxWidth().navigationBarsPadding().shadow(18.dp, RoundedCornerShape(24.dp)).clip(RoundedCornerShape(24.dp)).background(Blue900).clickable(onClick = onOpen).padding(9.dp), verticalAlignment = Alignment.CenterVertically) {
        AlbumArt(Modifier.size(48.dp), listOf(Sky100, Blue500, Blue900), 4)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text("Midnight Drive", fontFamily = Display, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Paper)
            Text("OpenStream Radio", fontFamily = Body, fontSize = 11.sp, color = Sky300)
        }
        IconButton(onClick = onPlayPause) { Icon(if (isPlaying) Icons.Outlined.Pause else Icons.Outlined.PlayArrow, null, tint = Sky100) }
        IconButton(onClick = {}) { Icon(Icons.Outlined.SkipNext, null, tint = Sky300) }
    }
}

@Composable
private fun PlayFab(onClick: () -> Unit) {
    Box(Modifier.size(50.dp).clip(CircleShape).background(Sky100).clickable(onClick = onClick), contentAlignment = Alignment.Center) {
        Icon(Icons.Outlined.PlayArrow, null, tint = Blue900, modifier = Modifier.size(26.dp))
    }
}

@Composable
private fun AlbumArt(modifier: Modifier, colors: List<Color>, seed: Int = 0) {
    Box(modifier.clip(RoundedCornerShape(22.dp)).background(Brush.linearGradient(colors.asReversed()))) {
        Box(Modifier.size((36 + seed * 5).dp).align(Alignment.Center).clip(CircleShape).background(Color.White.copy(alpha = .18f)))
        Box(Modifier.size((12 + seed * 3).dp).align(Alignment.Center).clip(CircleShape).background(Sky100.copy(alpha = .72f)))
    }
}

@Composable
private fun SectionHeader(title: String) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(title, fontFamily = Display, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Ink)
        TextButton(onClick = {}) { Text("See all", color = Blue700, fontFamily = Body, fontSize = 12.sp) }
    }
}

@Composable
private fun MonoLabel(text: String, light: Boolean = false) {
    Text(text, fontFamily = Mono, fontWeight = FontWeight.Medium, fontSize = 10.sp, letterSpacing = 1.3.sp, color = if (light) Sky300 else Blue700)
}

@Composable
private fun OpenStreamBottomBar(selected: Int, onSelected: (Int) -> Unit) {
    val items = listOf("Home" to Icons.Outlined.Home, "Search" to Icons.Outlined.Search, "Library" to Icons.Outlined.LibraryMusic, "Profile" to Icons.Outlined.FavoriteBorder)
    Row(Modifier.fillMaxWidth().background(Sky100).navigationBarsPadding().padding(horizontal = 18.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
        items.forEachIndexed { index, item ->
            val active = selected == index
            Column(Modifier.width(70.dp).clip(RoundedCornerShape(999.dp)).clickable { onSelected(index) }.padding(vertical = 7.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.clip(RoundedCornerShape(999.dp)).background(if (active) Sky300 else Color.Transparent).padding(horizontal = 14.dp, vertical = 5.dp)) {
                    Icon(item.second, null, tint = if (active) Blue700 else NavMuted, modifier = Modifier.size(20.dp))
                }
                Text(item.first, fontFamily = Body, fontSize = 10.5.sp, color = if (active) Blue700 else NavMuted)
            }
        }
    }
}
