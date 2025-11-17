package com.example.apppoketeam.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apppoketeam.ui.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRankingScreen(factory: ViewModelFactory) {
  val viewModel: AdminRankingViewModel = viewModel(factory = factory)
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  Column(Modifier.padding(16.dp)) {
    Text("Gerenciar Ranking (CRUD 2)", style = MaterialTheme.typography.headlineSmall)
    TextField(value = uiState.nome, onValueChange = viewModel::onNomeChange, label = { Text("Nome do Jogador") }, modifier = Modifier.fillMaxWidth())
    TextField(value = uiState.pontuacao, onValueChange = viewModel::onPontuacaoChange, label = { Text("Pontuação") }, modifier = Modifier.fillMaxWidth())

    Spacer(Modifier.height(8.dp));
    Button(onClick = viewModel::onSave, modifier = Modifier.fillMaxWidth()) {
      Text(uiState.textoBotao)
    }

    Spacer(Modifier.height(16.dp));
    Text("Ranking Atual", style = MaterialTheme.typography.headlineSmall)

    LazyColumn {
      items(uiState.ranking) { ranking ->
        Row(
          Modifier.fillMaxWidth().padding(8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("${ranking.nomeJogador} - ${ranking.pontuacao} Pts", modifier = Modifier.weight(1f))
          Button(onClick = { viewModel.onEdit(ranking) }) { Text("Editar") }
          Spacer(Modifier.width(8.dp))
          Button(onClick = { viewModel.onDelete(ranking) }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
            Text("X")
          }
        }
      }
    }
  }
}
