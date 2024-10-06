package com.vnhanh.androiddemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vnhanh.androiddemo.common.theme.AndroidDemoTheme
import kotlinx.coroutines.delay
import timber.log.Timber

// https://dribbble.com/shots/20741494--Tanam-Plant-Shop-Mobile-App
// https://dribbble.com/shots/18661272-Plant-Shop-App
// https://www.youtube.com/watch?v=RERkUPqh71Y&list=PLvG2mD7Ba5Syp4vhNSpZfQ8H63xT9qeOr&index=10
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
private fun MainScreen(
    modifier: Modifier = Modifier,
    links: List<String> = flowers,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(32.dp))
        Conveyor(
//            modifier = Modifier
//                .fillMaxWidth(),
            links = links
        )
    }
}

@Composable
private fun Conveyor(
    modifier: Modifier = Modifier,
    links: List<String> = flowers,
) {
    val density = LocalDensity.current
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        val translateX = with(density) { 1.dp.toPx() }

        while (true) {
            scrollState.animateScrollBy(translateX)
        }
    }

    SideEffect {
        Log.d("TestAlan", "test")
    }
    MeasureWidth(
        content = {
            Row(
                modifier = modifier
                    .wrapContentWidth()
                    .horizontalScroll(scrollState)
            ) {
                for (flower in links) {
                    FlowerPhoto(url = flower)
                }
            }
        }
    )
}

@Composable
private fun MeasureWidth(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    SubcomposeLayout(modifier = modifier) { constraints ->
        val placeables = subcompose("measure") {
            content()
        }.map {
            it.measure(constraints)
        }

        val maxSize: IntSize = placeables.fold(IntSize.Zero) { currentSize, placeable ->
            IntSize(
                maxOf(currentSize.width, placeable.width),
                maxOf(currentSize.height, placeable.height),
            )
        }
        Log.d("TestAlan", "maxSize: $maxSize")
        Log.d("TestAlan", "configuration: ${configuration.screenWidthDp * density.density}")

        layout(maxSize.width, maxSize.height) {
            placeables.forEach {
                it.placeRelative(0, 0)
            }
        }
    }
}

@Composable
private fun FlowerPhoto(
    modifier: Modifier = Modifier,
    url: String,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .build(),
        modifier = modifier.size(width = 120.dp, height = 80.dp),
        contentScale = ContentScale.Crop,
        contentDescription = ""
    )
}

private val flowers: List<String> = listOf(
    "https://as2.ftcdn.net/v2/jpg/07/14/81/81/1000_F_714818198_jsgyKDbdGOOQnKDzgXDgOC5Gfo9lMucf.jpg",
    "https://flowers.vi/gallery/yachts/yachts1.jpg",
    "https://flowers.vi/gallery/yachts/yachts9.jpg",
    "https://flowers.vi/gallery/yachts/yachts4.jpg",
    "https://flowers.vi/gallery/yachts/yachts11.jpg",
    "https://img.goodfon.com/wallpaper/nbig/3/8f/love-flowers-lyubov-cvety.webp",
    "https://t3.ftcdn.net/jpg/07/64/11/16/240_F_764111633_6zxPLPbNU8sLsdnJi3bpHlSnftv5fbqg.jpg",
    "https://t4.ftcdn.net/jpg/07/14/81/81/240_F_714818198_jsgyKDbdGOOQnKDzgXDgOC5Gfo9lMucf.jpg",
    "https://as2.ftcdn.net/v2/jpg/07/14/33/59/1000_F_714335948_LloRWA7VYOuFbTVcD7Xf7a9tUsIMEADr.jpg",
)
