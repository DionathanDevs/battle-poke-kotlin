package com.example.apppoketeam.ui.screens.player

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.apppoketeam.ui.ViewModelFactory
import com.example.apppoketeam.ui.components.PlayerBottomNav

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerDashboardScreen(rootNavController: NavHostController) {
  val navController = rememberNavController()

  val factory = ViewModelFactory(LocalContext.current.applicationContext)

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Área do Jogador") },
        actions = {
          IconButton(onClick = { rootNavController.navigate("login") { popUpTo(0) } }) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sair")
          }
        }
      )
    },
    bottomBar = { PlayerBottomNav(navController = navController) }
  ) { padding ->
    NavHost(navController = navController, startDestination = "pokedex", Modifier.padding(padding)) {

      // --- ESTE É O BLOCO CORRIGIDO (Rota da Pokédex) ---
      composable(
        route = "pokedex?timeId={timeId}&slotId={slotId}",
        arguments = listOf(
          navArgument("timeId") { type = NavType.IntType; defaultValue = -1 },
          navArgument("slotId") { type = NavType.IntType; defaultValue = -1 }
        )
      ) { backStackEntry ->
        // Extrai os argumentos da rota
        val timeId = backStackEntry.arguments?.getInt("timeId").let { if (it == -1) null else it }
        val slotId = backStackEntry.arguments?.getInt("slotId").let { if (it == -1) null else it }

        // Passa os argumentos para a tela
        PokedexScreen(
          navController = navController,
          factory = factory,
          timeId = timeId,
          slotId = slotId
        )
      }
      // --- FIM DA CORREÇÃO ---

      composable(
        route = "detalhes_pokemon/{pokemonName}?timeId={timeId}&slotId={slotId}",
        arguments = listOf(
          navArgument("pokemonName") { type = NavType.StringType },
          navArgument("timeId") { type = NavType.IntType; defaultValue = -1 },
          navArgument("slotId") { type = NavType.IntType; defaultValue = -1 }
        )
      ) { backStackEntry ->
        val name = backStackEntry.arguments?.getString("pokemonName") ?: ""
        val timeId = backStackEntry.arguments?.getInt("timeId").let { if (it == -1) null else it }
        val slotId = backStackEntry.arguments?.getInt("slotId").let { if (it == -1) null else it }
        PokemonDetailScreen(
          navController = navController,
          pokemonName = name,
          factory = factory,
          timeId = timeId,
          slotId = slotId
        )
      }

      composable("meus_times") {
        MeusTimesScreen(navController = navController, factory = factory)
      }

      composable(
        route = "batalha/{timeId}",
        arguments = listOf(navArgument("timeId") { type = NavType.IntType })
      ) { backStackEntry ->
        val timeId = backStackEntry.arguments?.getInt("timeId") ?: 0
        BatalhaScreen(timeId = timeId, factory = factory, navController = navController)
      }

      composable("player_ranking") {
        PlayerRankingScreen(factory = factory)
      }
    }
  }
}
