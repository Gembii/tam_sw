package com.efgie.tam1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.viewModels
import com.efgie.tam1.ui.theme.TAM1Theme
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.efgie.tam1.repository.model.PlanetResponse
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getData()
        setContent {
            MainContent(viewModel = viewModel, onClick = { id -> navigateToDetailsActivity(id)})
        }
    }

    private fun navigateToDetailsActivity(id: String) {
        val detailsIntent = Intent(this, DetailsActivity::class.java)
        detailsIntent.putExtra("CUSTOM_ID", id)
        startActivity(detailsIntent)
    }
}

@Composable
fun MainContent(viewModel: MainViewModel, onClick: (String) -> Unit) {
    TAM1Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val uiState by viewModel.immutablePlanetsData.observeAsState(UIState(isLoading = true))
            Showcase(uiState = uiState, modifier = Modifier.fillMaxSize(), onClick = { id -> onClick.invoke(id)})
        }
    }
}

@Composable
fun Showcase(uiState: UIState<List<PlanetResponse>>, modifier: Modifier = Modifier, onClick: (String) -> Unit) {
    when {
        uiState.isLoading -> ShowLoadingIndicator()
        uiState.error != null -> ShowErrorMessage(uiState.error.toString())
        else -> uiState.data?.let { ShowPlanetsData(it, modifier, onClick = { id -> onClick.invoke(id)}) }
    }
}

@Composable
fun ShowPlanetsData(planets: List<PlanetResponse>?, modifier: Modifier, onClick: (String) -> Unit) {
    if (planets?.isNotEmpty() == true) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
        ) {
            items(planets) { planet ->
                PlanetCard(planet = planet, onClick = { id -> onClick.invoke(id)})
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    } else {
        Text(
            text = "No planets data available.",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PlanetCard(planet: PlanetResponse, onClick: (String) -> Unit) {
    Log.d("PlanetCard", "${planet.name} | ${planet.diameter} | ${planet.population} | ${planet.climate} | ${planet.terrain}")

    val imageResource = getImageResource(planet.climate)

    val planetId = getPlanetIdFromUrl(planet.url)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick.invoke(planetId) },
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .clip(RoundedCornerShape(8.dp))
        ) {
            PlanetCardBackground(imageResource = imageResource)
            PlanetCardContent(planet = planet)
        }
    }
}

@Composable
fun PlanetDetail(detailName: String, detailValue: String) {
    Row(
        verticalAlignment = Alignment.Top
    ) {
        PlanetDetailHeader(detailName)
        PlanetDetailValue(detailValue)
    }
}

@Composable
fun PlanetDetailHeader(detailName: String) {
        Text(
            text = "$detailName: ",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.White
        )
}

@Composable
fun PlanetDetailValue(detailValue: String) {
    Text(
        text = detailValue,
        fontSize = 16.sp,
        color = Color.White
    )
}

@Composable
fun PlanetCardBackground(imageResource: Int) {
    Image(
        painter = painterResource(id = imageResource),
        contentDescription = "Climate Image",
        modifier = Modifier
            .fillMaxSize()
            .height(200.dp),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
fun PlanetCardContent(planet: PlanetResponse) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Transparent),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = planet.name,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        PlanetDetail("Population", planet.population)
        PlanetDetail("Climate",  planet.climate)
        PlanetDetail("Terrain", planet.terrain)
        PlanetDetail("Diameter", planet.diameter)
        PlanetDetail("Gravity", planet.gravity)
    }
}

fun getImageResource(climate: String): Int {
    return when {
        climate.contains("tropical") -> R.drawable.tropical
        climate.contains("arid") -> R.drawable.arid
        climate.contains("temperate") -> R.drawable.temperate
        climate.contains("frozen") -> R.drawable.frozen
        climate.contains("murky") -> R.drawable.murky
        else -> R.drawable.def
    }
}

fun getPlanetIdFromUrl(url: String): String {
    val parts = url.trimEnd('/').split('/')
    return parts[parts.size - 1]
}

@Composable
fun ShowLoadingIndicator() {
    StarWarsLoadingIndicator()
}

@Composable
fun StarWarsLoadingIndicator() {
    val infiniteTransition = rememberInfiniteTransition("SW Loading Animation")
    val angle = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SW Animation"
    )

    Image(
        painter = painterResource(id = R.drawable.helmet),
        contentDescription = "Spinning Stormtrooper Helmet",
        modifier = Modifier
            .graphicsLayer {
                rotationZ = angle.value
            }
    )
}

@Composable
fun ShowErrorMessage(error: String?) {
    if (error != null) {
        Text(
            text = "Error: $error",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color = Color.Red,
            textAlign = TextAlign.Center
        )
    }
}