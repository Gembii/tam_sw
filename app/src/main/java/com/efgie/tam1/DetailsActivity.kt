package com.efgie.tam1

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.efgie.tam1.repository.model.Planet
import com.efgie.tam1.repository.model.PlanetResponse
import com.efgie.tam1.ui.theme.TAM1Theme

class DetailsActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getStringExtra("CUSTOM_ID")

        id?.let {
            viewModel.getDetailsData(it)
        }
        setContent {
            DetailsContent(viewModel = viewModel)
        }
    }
}

@Composable
fun DetailsContent(viewModel: MainViewModel) {
    TAM1Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val uiState by viewModel.immutablePlanetDetails.observeAsState(UIState(isLoading = true))
            DetailsShowcase(uiState = uiState, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun DetailsShowcase(uiState: UIState<Planet>, modifier: Modifier = Modifier) {
    when {
        uiState.isLoading -> ShowLoadingIndicator()
        uiState.error != null -> ShowErrorMessage(uiState.error.toString())
        else -> uiState.data?.let { ShowPlanetDetailsData(it, modifier) }
    }
}

@Composable
fun ShowPlanetDetailsData(planet: Planet?, modifier: Modifier) {
    if (planet != null) {
        PlanetDetailsCard(planet = planet)
    } else {
        Text(
            text = "No planet Details data available.",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PlanetDetailsImage(imageResource: Int) {
    Image(
        painter = painterResource(id = imageResource),
        contentDescription = "Climate Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
fun PlanetDetailsCard(planet: Planet) {
    val imageRes = getImageResource(planet.climate)

    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PlanetDetailsImage(imageResource = imageRes)
            Spacer(modifier = Modifier.height(16.dp))

            PlanetDetail("Name", planet.name)
            PlanetDetail("Climate", planet.climate)
            PlanetDetail("Terrain", planet.terrain)
            PlanetDetail("Population", planet.population)
            PlanetDetail("Diameter", planet.diameter)
            PlanetDetail("Gravity", planet.gravity)

            Spacer(modifier = Modifier.height(16.dp))

            PlanetDetailHeader("Films")
            if (planet.films.isNullOrEmpty()) {
                Text("No films available", fontSize = 16.sp)
            } else {
                planet.films.forEach { film ->
                    PlanetDetailValue("${film?.title}")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            PlanetDetailHeader("Residents")
            if (planet.residents.isNullOrEmpty()) {
                Text("No residents available", fontSize = 16.sp)
            } else {
                planet.residents.forEach { resident ->
                    PlanetDetailValue("${resident?.name}")
                }
            }
        }
    }
}