package com.example.apppoketeam.ui.screens.login
import androidx.lifecycle.ViewModel; import androidx.lifecycle.viewModelScope; import com.example.apppoketeam.data.local.User; import com.example.apppoketeam.data.repository.AppRepository; import kotlinx.coroutines.flow.*; import kotlinx.coroutines.launch
data class LoginUiState(val username: String = "", val password: String = "", val loginError: String? = null, val loginSuccess: User? = null)
class LoginViewModel(private val repository: AppRepository) : ViewModel() { private val _uiState = MutableStateFlow(LoginUiState());
  val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow();
  fun onUsernameChange(username: String) { _uiState.update { it.copy(username = username) } };
  fun onPasswordChange(password: String) { _uiState.update { it.copy(password = password) } };
  fun login() { viewModelScope.launch { val result = repository.checkLogin(_uiState.value.username, _uiState.value.password);
  result.fold(onSuccess = { user -> _uiState.update { it.copy(loginSuccess = user, loginError = null) } }, onFailure = { error -> _uiState.update { it.copy(loginError = error.message) } }) } } }

