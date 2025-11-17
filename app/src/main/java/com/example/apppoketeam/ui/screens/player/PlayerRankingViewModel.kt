package com.example.apppoketeam.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apppoketeam.data.local.Ranking
import com.example.apppoketeam.data.repository.AppRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class PlayerRankingUiState(
  val ranking: List<Ranking> = emptyList()
)

class PlayerRankingViewModel(private val repository: AppRepository) : ViewModel() {
  private val _uiState = MutableStateFlow(PlayerRankingUiState())
  val uiState: StateFlow<PlayerRankingUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      repository.getRanking()

        .map { list -> list.sortedByDescending { it.pontuacao } }
        .collect { rankingList ->
          _uiState.update { it.copy(ranking = rankingList) }
        }
    }
  }
}
