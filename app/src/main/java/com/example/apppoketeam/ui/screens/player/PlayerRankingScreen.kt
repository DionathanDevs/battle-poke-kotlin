package com.example.apppoketeam.ui.screens.player

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apppoketeam.ui.ViewModelFactory

@Composable
fun PlayerRankingScreen(factory: ViewModelFactory) {
  val viewModel: PlayerRankingViewModel = viewModel(factory = factory)
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  Column(Modifier.padding(16.dp)) {
    Text(
      "Ranking de Batalhas",
      style = MaterialTheme.typography.headlineSmall,
      modifier = Modifier.padding(bottom = 16.dp)
    )

    if (uiState.ranking.isEmpty()) {
      Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Nenhuma pontuação registrada ainda.")
      }
    } else {
      LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        itemsIndexed(uiState.ranking) { index, item ->
          RankingItemCard(rank = index + 1, item = item)
        }
      }
    }
  }
}


@Composable
fun RankingItemCard(rank: Int, item: com.example.apppoketeam.data.local.Ranking) {
  Card(modifier = Modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {

      Text(
        text = "#$rank: ${item.nomeJogador}",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.weight(1f)
      )


      Spacer(modifier = Modifier.width(16.dp))

      Card(
        colors = CardDefaults.cardColors(

          containerColor = MaterialTheme.colorScheme.primaryContainer
        )
      ) {
        Text(
          text = "${item.pontuacao} Pts",
          style = MaterialTheme.typography.titleMedium,
          // Cor do texto que contrasta com o "primaryContainer"
          color = MaterialTheme.colorScheme.onPrimaryContainer,
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}
