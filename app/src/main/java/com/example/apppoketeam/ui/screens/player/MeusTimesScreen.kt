package com.example.apppoketeam.ui.screens.player

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.apppoketeam.data.local.TimePokemon
import com.example.apppoketeam.ui.ViewModelFactory
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeusTimesScreen(navController: NavController, factory: ViewModelFactory) {
  val viewModel: MeusTimesViewModel = viewModel(factory = factory)
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  Column(Modifier.padding(17.dp).verticalScroll(rememberScrollState())) {
    Text("Gerenciar Time", style = MaterialTheme.typography.headlineSmall)

    TextField(
      value = uiState.nomeTime,
      onValueChange = viewModel::onNomeTimeChange,
      label = { Text("Nome do Time (Max 16 letras)") },
      modifier = Modifier.fillMaxWidth()
    )

    Spacer(Modifier.height(8.dp))
    Button(onClick = viewModel::onSave, modifier = Modifier.fillMaxWidth()) {
      Text(if (uiState.timeEmEdicao == null) "Criar Novo Time" else "Atualizar Nome")
    }

    Spacer(Modifier.height(16.dp))
    Text("Meus Times Salvos", style = MaterialTheme.typography.headlineSmall)

    Column(modifier = Modifier.fillMaxWidth()) {
      uiState.times.forEach { time ->
        TimeItemCard(
          time = time,
          onEdit = { viewModel.onEdit(time) },
          onDelete = { viewModel.onDelete(time) },
          onFight = { navController.navigate("batalha/${time.id}") },
          onSlotClick = { slotId ->
            navController.navigate("pokedex?timeId=${time.id}&slotId=$slotId")
          }
        )
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TimeItemCard(
  time: TimePokemon,
  onEdit: () -> Unit,
  onDelete: () -> Unit,
  onFight: () -> Unit,
  onSlotClick: (Int) -> Unit
) {
  Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {

    Card(modifier = Modifier.fillMaxWidth()) {
      Column(Modifier.padding(16.dp)) {


        Row(
          modifier = Modifier
            .fillMaxWidth()

            .padding(end = 48.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // 1. Nome do Time (com weight para empurrar o lápis)
          Text(
            text = time.nomeTime,
            style = MaterialTheme.typography.titleLarge,

          )

          // 2. Ícone de Editar (Lápis) vem DEPOIS do nome
          IconButton(onClick = onEdit) {
            Icon(Icons.Default.Edit, contentDescription = "Editar Nome")
          }
        }
        // --- FIM DA CORREÇÃO ---

        Spacer(Modifier.height(16.dp))

        FlowRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          PokemonSlotCard(pokemonId = time.p1_id, pokemonName = time.p1_name, onClick = { onSlotClick(1) })
          PokemonSlotCard(pokemonId = time.p2_id, pokemonName = time.p2_name, onClick = { onSlotClick(2) })
          PokemonSlotCard(pokemonId = time.p3_id, pokemonName = time.p3_name, onClick = { onSlotClick(3) })
          PokemonSlotCard(pokemonId = time.p4_id, pokemonName = time.p4_name, onClick = { onSlotClick(4) })
          PokemonSlotCard(pokemonId = time.p5_id, pokemonName = time.p5_name, onClick = { onSlotClick(5) })
          PokemonSlotCard(pokemonId = time.p6_id, pokemonName = time.p6_name, onClick = { onSlotClick(6) })
        }

        Spacer(Modifier.height(16.dp))

        Button(
          onClick = onFight,
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
          Text("Batalhar!", style = MaterialTheme.typography.titleMedium)
        }
      }
    }

    // --- Ícone da Lixeira (Deletar) no Canto ---
    IconButton(
      onClick = onDelete,
      modifier = Modifier.align(Alignment.TopEnd)
    ) {
      Icon(
        Icons.Default.Delete,
        contentDescription = "Deletar Time",
        tint = MaterialTheme.colorScheme.error
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonSlotCard(pokemonId: Int?, pokemonName: String?, onClick: () -> Unit) {
  val imageUrl = if (pokemonId != null) {
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokemonId.png"
  } else {
    null
  }

  Card(
    onClick = onClick,
    modifier = Modifier.size(110.dp),
    border = BorderStroke(1.dp, Color.LightGray)
  ) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      if (imageUrl != null && pokemonName != null) {
        AsyncImage(
          model = imageUrl,
          contentDescription = pokemonName,
          modifier = Modifier.size(90.dp)
        )
      } else {
        Icon(Icons.Default.Add, contentDescription = "Adicionar Pokémon", modifier = Modifier.size(40.dp))
      }
    }
  }
}
