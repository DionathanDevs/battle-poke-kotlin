package com.example.apppoketeam.ui.screens.admin
import androidx.lifecycle.ViewModel; import androidx.lifecycle.viewModelScope; import com.example.apppoketeam.data.local.User; import com.example.apppoketeam.data.repository.AppRepository; import kotlinx.coroutines.flow.*; import kotlinx.coroutines.launch
data class AdminUserUiState(val users: List<User> = emptyList(), val username: String = "", val password: String = "", val isAdmin: Boolean = false, val userEmEdicao: User? = null)
class AdminUserViewModel(private val repository: AppRepository) : ViewModel() {
  private val _uiState = MutableStateFlow(AdminUserUiState()); val uiState: StateFlow<AdminUserUiState> = _uiState.asStateFlow()
  init { viewModelScope.launch { repository.getAllUsers().collect { users -> _uiState.update { it.copy(users = users) } } } }
  fun onUsernameChange(u: String) { _uiState.update { it.copy(username = u) } }
  fun onPasswordChange(p: String) { _uiState.update { it.copy(password = p) } }
  fun onIsAdminChange(admin: Boolean) { _uiState.update { it.copy(isAdmin = admin) } }
  fun onEdit(user: User) { _uiState.update { it.copy(userEmEdicao = user, username = user.username, password = user.password, isAdmin = user.isAdmin) } }
  fun onSave() { val s = _uiState.value; if (s.username.isBlank() || s.password.isBlank()) return; viewModelScope.launch { if (s.userEmEdicao == null) { repository.inserirUser(User(username = s.username, password = s.password, isAdmin = s.isAdmin)) } else { repository.atualizarUser(s.userEmEdicao.copy(username = s.username, password = s.password, isAdmin = s.isAdmin)) }; _uiState.update { it.copy(userEmEdicao = null, username = "", password = "", isAdmin = false) } } }
  fun onDelete(user: User) { viewModelScope.launch { repository.deletarUser(user) } }
}
