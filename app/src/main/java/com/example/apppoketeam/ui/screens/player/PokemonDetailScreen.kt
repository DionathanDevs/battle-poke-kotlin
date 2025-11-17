package com.example.apppoketeam.ui.screens.player

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import com.example.apppoketeam.ui.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
  navController: NavController,
  pokemonName: String,
  factory: ViewModelFactory,
  // Recebe os IDs do time e do slot (opcionais)
  timeId: Int?,
  slotId: Int?
) {
  val viewModel: PokemonDetailViewModel = viewModel(factory = factory)
  val context = LocalContext.current

  LaunchedEffect(pokemonName) {
    viewModel.loadPokemonDetails(pokemonName)
  }

  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  // --- LÓGICA DE NAVEGAÇÃO DE RETORNO ---
  LaunchedEffect(uiState.pokemonAdicionado) {
    if (uiState.pokemonAdicionado) {
      Toast.makeText(context, "$pokemonName adicionado ao time!", Toast.LENGTH_SHORT).show()
      // Navega de volta para a tela de times
      navController.navigate("meus_times") {
        // Limpa a pilha de navegação para que o usuário não volte para a Pokédex ou Detalhes
        popUpTo(navController.graph.startDestinationId)
      }
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(pokemonName.replaceFirstChar { it.titlecase() }, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        navigationIcon = {
          IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
          }
        }
      )
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      if (uiState.isLoading) { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
      else if (uiState.errorMessage != null) { Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp)) }
      else if (uiState.pokemon != null) {
        val pokemon = uiState.pokemon!!
        AsyncImage(
          model = pokemon.sprites.frontDefault,
          contentDescription = pokemon.name,
          modifier = Modifier.size(200.dp),
          contentScale = ContentScale.Fit
        )
        Text(pokemon.name.replaceFirstChar { it.titlecase() }, style = MaterialTheme.typography.headlineLarge)

        // --- BOTÃO "ADICIONAR AO TIME" ---
        if (timeId != null && slotId != null) {
          Spacer(Modifier.height(16.dp))
          Button(onClick = {
            viewModel.addPokemonToTeam(timeId, slotId, pokemon.id, pokemon.name)
          }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Text("Adicionar ao Time (Slot $slotId)")
          }
        }

        Spacer(Modifier.height(16.dp))
        Text("Status", style = MaterialTheme.typography.titleMedium); Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) { Column(Modifier.padding(16.dp)) { pokemon.stats.forEach { statSlot -> StatRow(statName = statSlot.stat.name, statValue = statSlot.baseStat) } } }
        Spacer(Modifier.height(16.dp))
        Text("Habilidades", style = MaterialTheme.typography.titleMedium); Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) { Column(Modifier.padding(16.dp)) { pokemon.abilities.forEach { abilitySlot -> Text("• ${abilitySlot.ability.name.replaceFirstChar { it.titlecase() }}") } } }
      }
    }
  }
}

@Composable
fun StatRow(statName: String, statValue: Int) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(statName.replaceFirstChar { it.titlecase() }, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    Text("$statValue", fontSize = 16.sp)
  }
  Divider(modifier = Modifier.padding(vertical = 4.dp))
}
