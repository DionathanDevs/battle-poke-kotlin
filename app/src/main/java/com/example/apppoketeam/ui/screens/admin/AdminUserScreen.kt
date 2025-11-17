package com.example.apppoketeam.ui.screens.admin
import androidx.compose.foundation.layout.*; import androidx.compose.foundation.lazy.LazyColumn; import androidx.compose.foundation.lazy.items; import androidx.compose.material3.*; import androidx.compose.runtime.*; import androidx.compose.ui.Alignment; import androidx.compose.ui.Modifier; import androidx.compose.ui.platform.LocalContext; import androidx.compose.ui.unit.dp; import androidx.lifecycle.compose.collectAsStateWithLifecycle; import androidx.lifecycle.viewmodel.compose.viewModel; import com.example.apppoketeam.ui.ViewModelFactory
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUserScreen(factory: ViewModelFactory) {
  val viewModel: AdminUserViewModel = viewModel(factory = factory)
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  Column(Modifier.padding(16.dp)) {
    Text("Gerenciar Usuários (CRUD 1)", style = MaterialTheme.typography.headlineSmall)
    TextField(value = uiState.username, onValueChange = viewModel::onUsernameChange, label = { Text("Username") }, modifier = Modifier.fillMaxWidth())
    TextField(value = uiState.password, onValueChange = viewModel::onPasswordChange, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
    Row(verticalAlignment = Alignment.CenterVertically) { Checkbox(checked = uiState.isAdmin, onCheckedChange = viewModel::onIsAdminChange); Text("É Administrador?") }
    Button(onClick = viewModel::onSave, modifier = Modifier.fillMaxWidth()) { Text(if (uiState.userEmEdicao == null) "Criar Usuário" else "Atualizar Usuário") }
    LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
      items(uiState.users) { user ->
        Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
          Text(text = "${user.username} (Admin: ${user.isAdmin})", modifier = Modifier.weight(1f))
          Button(onClick = { viewModel.onEdit(user) }) { Text("Editar") }
          Button(onClick = { viewModel.onDelete(user) }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) { Text("X") }
        }
      }
    }
  }
}
