package com.example.apppoketeam.ui.screens.admin

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apppoketeam.ui.ViewModelFactory
import com.example.apppoketeam.ui.components.AdminBottomNav

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(rootNavController: NavHostController) {
  val navController = rememberNavController()
  val factory = ViewModelFactory(LocalContext.current.applicationContext)

  Scaffold(
    topBar = { TopAppBar(title = { Text("Área do Admin") }, actions = {
      IconButton(onClick = { rootNavController.navigate("login") { popUpTo(0) } }) {
        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sair")
      }
    })},
    bottomBar = { AdminBottomNav(navController = navController) }
  ) { padding ->

    NavHost(navController = navController, startDestination = "admin_users", Modifier.padding(padding)) {


      composable("admin_users") {
        AdminUserScreen(factory = factory)
      }

      composable("admin_ranking") {
        AdminRankingScreen(factory = factory)
      }
    }
  }
}
