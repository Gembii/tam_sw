package com.efgie.tam1

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
import com.efgie.tam1.repository.model.Planet

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getData()
        setContent {
            MainContent(viewModel)
        }
    }
}

@Composable
fun MainContent(viewModel: MainViewModel) {
    TAM1Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val uiState by viewModel.immutablePlanetsData.observeAsState(UIState(isLoading = true))
            Showcase(uiState = uiState, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun Showcase(uiState: UIState<List<Planet>>, modifier: Modifier = Modifier) {
    when {
        uiState.isLoading -> ShowLoadingIndicator()
        uiState.error != null -> ShowErrorMessage(uiState.error.toString())
        else -> uiState.data?.let { ShowPlanetsData(it, modifier) }
    }
}

@Composable
fun ShowPlanetsData(planets: List<Planet>?, modifier: Modifier) {
    if (planets?.isNotEmpty() == true) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
        ) {
            items(planets) { planet ->
                PlanetCard(planet = planet)
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
fun PlanetCard(planet: Planet) {
    Log.d("PlanetCard", "${planet.name} | ${planet.diameter} | ${planet.population} | ${planet.climate} | ${planet.terrain}")

    val imageResource = getImageResource(planet)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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
        Text(
            text = "$detailName: ",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.White
        )
        Text(
            text = detailValue,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

@Composable
fun PlanetCardBackground(imageResource: Int) {
    Image(
        painter = painterResource(id = imageResource),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .height(200.dp),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
fun PlanetCardContent(planet: Planet) {
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

fun getImageResource(planet: Planet): Int {
    return when {
        planet.climate.contains("tropical") -> R.drawable.tropical
        planet.climate.contains("arid") -> R.drawable.arid
        planet.climate.contains("temperate") -> R.drawable.temperate
        planet.climate.contains("frozen") -> R.drawable.frozen
        planet.climate.contains("murky") -> R.drawable.murky
        else -> R.drawable.def
    }
}

@Composable
fun ShowLoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
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