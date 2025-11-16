package com.example.apppoketeam.ui.screens.player

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.apppoketeam.ui.ViewModelFactory
import com.example.apppoketeam.ui.theme.VictoryGreen // <-- Importamos a nova cor

@Composable
fun BatalhaScreen(timeId: Int, factory: ViewModelFactory, navController: NavController) {
  val viewModel: BatalhaViewModel = viewModel(factory = factory)
  LaunchedEffect(timeId) { viewModel.loadTime(timeId) }
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  Column(
    Modifier.fillMaxSize().padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (uiState.isLoading) { CircularProgressIndicator() }
    else if (uiState.errorMessage != null) { Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error) }
    else if (uiState.time != null) {

      Text(uiState.time!!.nomeTime, style = MaterialTheme.typography.headlineMedium)
      Spacer(Modifier.height(16.dp))

      Row(Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.SpaceAround) {
        TeamDisplay(title = "Seu Time", pokemons = uiState.timeDetails)
        TeamDisplay(title = "Time CPU", pokemons = uiState.cpuTeamDetails)
      }

      Spacer(Modifier.height(16.dp))

      if (uiState.battleMessage != null) {
        val isVictory = uiState.battleScore!! > 0

        // --- LÓGICA DE COR ATUALIZADA ---
        // Agora usa VictoryGreen se 'isVictory' for verdadeiro
        val accentColor = if (isVictory) VictoryGreen else MaterialTheme.colorScheme.error

        val imageUrl = if (isVictory) {
          "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/94.png" // Gengar
        } else {
          "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/54.png" // Psyduck
        }

        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
          Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            AsyncImage(
              model = imageUrl,
              contentDescription = "Resultado da Batalha",
              modifier = Modifier.size(120.dp),
              contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(16.dp))

            Text(
              text = uiState.battleMessage!!,
              style = MaterialTheme.typography.headlineSmall,
              fontWeight = FontWeight.Bold,
              color = accentColor, // <-- A cor verde é aplicada aqui
              textAlign = TextAlign.Center,
              modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Text(
              text = "Pontuação da Batalha: ${uiState.battleScore}",
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = TextAlign.Center,
              modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Divider(Modifier.fillMaxWidth(0.8f), color = MaterialTheme.colorScheme.outlineVariant)

            Spacer(Modifier.height(12.dp))

            Text(
              text = "Pontuação Total: ${uiState.totalScore}",
              style = MaterialTheme.typography.headlineSmall,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.onSurface,
              textAlign = TextAlign.Center,
              modifier = Modifier.fillMaxWidth()
            )
          }
        }

        Spacer(Modifier.height(24.dp))

        Button(
          onClick = { navController.popBackStack() },
          modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
          Text("Voltar aos Times", style = MaterialTheme.typography.titleMedium)
        }

      } else {
        Button(
          onClick = viewModel::startFight,
          modifier = Modifier.fillMaxWidth().height(60.dp),
          enabled = uiState.battleMessage == null,
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Text("FIGHT!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun TeamDisplay(title: String, pokemons: List<com.example.apppoketeam.data.remote.PokemonDTO>) {
  Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(IntrinsicSize.Min)) {
    Text(title, style = MaterialTheme.typography.titleLarge)
    Spacer(Modifier.height(8.dp))
    Column(
      modifier = Modifier.fillMaxHeight().verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      if (pokemons.isEmpty()) {
        Text("Vazio", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
      } else {
        pokemons.forEach { pokemon ->
          Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(7.dp).fillMaxWidth()
            ) {
              AsyncImage(
                model = pokemon.sprites.frontDefault,
                contentDescription = pokemon.name,
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Fit
              )
              Spacer(Modifier.width(6.dp))
              Text(
                pokemon.name.replaceFirstChar { it.titlecase() },
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }
        }
      }
    }
  }
}
