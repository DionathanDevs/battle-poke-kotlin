package com.example.apppoketeam.ui.screens.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.apppoketeam.data.remote.PokemonNameUrl
import com.example.apppoketeam.ui.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexScreen(
  navController: NavController,
  factory: ViewModelFactory,

  timeId: Int?,
  slotId: Int?

) {
  val viewModel: PokedexViewModel = viewModel(factory = factory)
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  val isSelectingMode = timeId != null && slotId != null

  Column(Modifier.padding(16.dp)) {
    if (isSelectingMode) {
      Text(
        "Selecione um Pokémon para o Time (Slot $slotId)",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
      )
    }

    TextField(value = uiState.query, onValueChange = viewModel::onQueryChange, label = { Text("Buscar Pokémon...") }, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(16.dp))

    if (uiState.isLoading) {
      Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
    } else if (uiState.errorMessage != null) {
      Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error) }
    } else {
      LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(uiState.filteredPokemon) { pokemon ->
          PokemonGridItem(
            pokemon = pokemon,
            onClick = {
              val route: String
              if (isSelectingMode) {
                route = "detalhes_pokemon/${pokemon.name}?timeId=$timeId&slotId=$slotId"
              } else {
                route = "detalhes_pokemon/${pokemon.name}"
              }
              navController.navigate(route)
            }
          )
        }
      }
    }
  }
}

@Composable
fun PokemonGridItem(pokemon: PokemonNameUrl, onClick: () -> Unit) {
  val id = pokemon.url.split("/").filter { it.isNotEmpty() }.last()
  val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"

  Card(modifier = Modifier.clickable(onClick = onClick)) {
    Column(
      Modifier.padding(8.dp).fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      AsyncImage(
        model = imageUrl,
        contentDescription = pokemon.name,
        modifier = Modifier.size(90.dp),
        contentScale = ContentScale.Fit
      )
      Text(pokemon.name.replaceFirstChar { it.titlecase() })
    }
  }
}
