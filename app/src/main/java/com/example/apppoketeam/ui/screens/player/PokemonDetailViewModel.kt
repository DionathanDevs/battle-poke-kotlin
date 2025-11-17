package com.example.apppoketeam.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apppoketeam.data.remote.PokemonDTO
import com.example.apppoketeam.data.repository.AppRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DetalhesUiState(
  val pokemon: PokemonDTO? = null,
  val isLoading: Boolean = false,
  val errorMessage: String? = null,
  val pokemonAdicionado: Boolean = false // Novo estado para feedback
)

class PokemonDetailViewModel(private val repository: AppRepository) : ViewModel() {
  private val _uiState = MutableStateFlow(DetalhesUiState())
  val uiState: StateFlow<DetalhesUiState> = _uiState.asStateFlow()

  fun loadPokemonDetails(name: String) {
    _uiState.update { it.copy(isLoading = true, errorMessage = null, pokemonAdicionado = false) }
    viewModelScope.launch {
      val result = repository.getPokemonDetails(name)
      result.fold(
        onSuccess = { pokemon ->
          _uiState.update { it.copy(isLoading = false, pokemon = pokemon) }
        },
        onFailure = { error ->
          _uiState.update { it.copy(isLoading = false, errorMessage = "Falha ao carregar detalhes: ${error.message}") }
        }
      )
    }
  }

  // --- NOVA FUNÇÃO ---
  fun addPokemonToTeam(timeId: Int, slotId: Int, pokemonId: Int, pokemonName: String) {
    viewModelScope.launch {
      repository.addPokemonToTeam(timeId, slotId, pokemonId, pokemonName)
      _uiState.update { it.copy(pokemonAdicionado = true) } // Atualiza o estado para a UI reagir
    }
  }
}
