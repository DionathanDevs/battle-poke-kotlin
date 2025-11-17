package com.example.apppoketeam.ui.screens.player
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope;
import com.example.apppoketeam.data.remote.PokemonNameUrl;
import com.example.apppoketeam.data.repository.AppRepository;
import kotlinx.coroutines.flow.*;
import kotlinx.coroutines.launch
data class PokedexUiState(val query: String = "", val allPokemon: List<PokemonNameUrl> = emptyList(), val filteredPokemon: List<PokemonNameUrl> = emptyList(), val isLoading: Boolean = false, val errorMessage: String? = null)
class PokedexViewModel(private val repository: AppRepository) : ViewModel() { private val _uiState = MutableStateFlow(PokedexUiState());
  val uiState: StateFlow<PokedexUiState> = _uiState.asStateFlow();
  init { loadPokemonList() };
  private fun loadPokemonList() { _uiState.update { it.copy(isLoading = true, errorMessage = null) };
    viewModelScope.launch { val result = repository.getPokemonList();
      result.fold(onSuccess = { response -> _uiState.update { it.copy(isLoading = false, allPokemon = response.results, filteredPokemon = response.results) } }, onFailure = { error -> _uiState.update { it.copy(isLoading = false, errorMessage = "Falha ao carregar Pokédex: ${error.message}") } }) } };

  fun onQueryChange(query: String) { _uiState.update { it.copy(query = query) }; filterPokemon(query) };
  private fun filterPokemon(query: String) {
    val filteredList = if (query.isBlank()) {
      _uiState.value.allPokemon } else {
        _uiState.value.allPokemon.filter {
          it.name.contains(query, ignoreCase = true) } };
    _uiState.update { it.copy(filteredPokemon = filteredList) } } }
