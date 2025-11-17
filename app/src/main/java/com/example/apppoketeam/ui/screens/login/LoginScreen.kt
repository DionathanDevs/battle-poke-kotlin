package com.example.apppoketeam.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.apppoketeam.ui.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, factory: ViewModelFactory) {
  val viewModel: LoginViewModel = viewModel(factory = factory)
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(uiState.loginSuccess) {
    uiState.loginSuccess?.let { user ->
      val route = if (user.isAdmin) "admin_dashboard" else "player_dashboard"
      navController.navigate(route) { popUpTo("login") { inclusive = true } }
    }
  }

  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(32.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {

      AsyncImage(
           model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/94.png",
        contentDescription = "Gengar Login Art",
        modifier = Modifier
          .size(200.dp)
          .padding(bottom = 16.dp),
        contentScale = ContentScale.Fit
      )

      Text(
        "PokeBattle",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(bottom = 24.dp)
      )

      Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          TextField(
            value = uiState.username,
            onValueChange = viewModel::onUsernameChange,
            label = { Text("Usuário (ex: admin ou player)") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(8.dp))
          TextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Senha (ex: 123)") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
          )

          uiState.loginError?.let {
            Text(
              it,
              color = MaterialTheme.colorScheme.error,
              modifier = Modifier.padding(top = 8.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Button(
        onClick = viewModel::login,
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
      ) {
        Text("Entrar")
      }
    }
  }
}
