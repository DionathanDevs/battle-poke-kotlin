package com.example.apppoketeam.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apppoketeam.data.local.Ranking
import com.example.apppoketeam.data.repository.AppRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AdminRankingUiState(
  val ranking: List<Ranking> = emptyList(),
  val nome: String = "",
  val pontuacao: String = "",
  val rankingEmEdicao: Ranking? = null,
  val textoBotao: String = "Adicionar Ranking"
)

class AdminRankingViewModel(private val repository: AppRepository) : ViewModel() {
  private val _uiState = MutableStateFlow(AdminRankingUiState());
  val uiState: StateFlow<AdminRankingUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      repository.getRanking()
        .map { list -> list.sortedByDescending { it.pontuacao } }
        .collect { rankingList -> _uiState.update { it.copy(ranking = rankingList) }
        }
    }
  }

  fun onNomeChange(nome: String) { _uiState.update { it.copy(nome = nome) } }
  fun onPontuacaoChange(pontuacao: String) { _uiState.update { it.copy(pontuacao = pontuacao) } }

  fun onEdit(ranking: Ranking) {
    _uiState.update {
      it.copy(
        rankingEmEdicao = ranking,
        nome = ranking.nomeJogador,
        pontuacao = ranking.pontuacao.toString(),
        textoBotao = "Atualizar Ranking"
      )
    }
  }

  fun onSave() {
    val state = _uiState.value
    val pontuacaoInt = state.pontuacao.toIntOrNull()
    if (state.nome.isNotBlank() && pontuacaoInt != null) {
      viewModelScope.launch {
        if (state.rankingEmEdicao == null) {
          repository.inserirRanking(Ranking(nomeJogador = state.nome, pontuacao = pontuacaoInt))
        } else {
          repository.atualizarRanking(state.rankingEmEdicao.copy(nomeJogador = state.nome, pontuacao = pontuacaoInt))
        }
        _uiState.update { it.copy(rankingEmEdicao = null, nome = "", pontuacao = "", textoBotao = "Adicionar Ranking") }
      }
    }
  }

  fun onDelete(ranking: Ranking) {
    viewModelScope.launch {
      repository.deletarRanking(ranking)
    }
  }
}
