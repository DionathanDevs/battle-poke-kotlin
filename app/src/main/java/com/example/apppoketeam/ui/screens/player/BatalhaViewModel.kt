package com.example.apppoketeam.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apppoketeam.data.local.Ranking
import com.example.apppoketeam.data.local.TimePokemon
import com.example.apppoketeam.data.remote.PokemonDTO
import com.example.apppoketeam.data.repository.AppRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class BatalhaUiState(
  val time: TimePokemon? = null,
  val isLoading: Boolean = false,
  val errorMessage: String? = null,
  val timeDetails: List<PokemonDTO> = emptyList(),
  val cpuTeamDetails: List<PokemonDTO> = emptyList(),
  val battleMessage: String? = null,
  val battleScore: Int? = null,
  val totalScore: Int? = null
)

class BatalhaViewModel(private val repository: AppRepository) : ViewModel() {

  private val _uiState = MutableStateFlow(BatalhaUiState())
  val uiState: StateFlow<BatalhaUiState> = _uiState.asStateFlow()

  fun loadTime(timeId: Int) {
    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true, errorMessage = null, battleMessage = null, battleScore = null, totalScore = null) }

      val time = repository.getTimesPorId(timeId)
      if (time == null) {
        _uiState.update { it.copy(isLoading = false, errorMessage = "Time não encontrado") }
        return@launch
      }
      _uiState.update { it.copy(time = time) }

      val pokemonNames = listOfNotNull(
        time.p1_name,
        time.p2_name,
        time.p3_name,
        time.p4_name,
        time.p5_name,
        time.p6_name
      )

      val detailsList = mutableListOf<PokemonDTO>()
      for (name in pokemonNames) {
        val result = repository.getPokemonDetails(name)
        result.fold(
          onSuccess = { detailsList.add(it) },
          onFailure = {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Erro ao buscar $name") }
            return@launch
          }
        )
      }
      _uiState.update { it.copy(timeDetails = detailsList) }

      val cpuResult = repository.getCpuTeam()
      cpuResult.fold(
        onSuccess = { cpuTeam -> _uiState.update { it.copy(isLoading = false, cpuTeamDetails = cpuTeam) } },
        onFailure = { _uiState.update { it.copy(isLoading = false, errorMessage = "Erro ao montar time da CPU") } }
      )
    }
  }

  fun startFight() {
    val state = _uiState.value
    val timeAtual = state.time

    if (state.timeDetails.isEmpty() || state.cpuTeamDetails.isEmpty() || timeAtual == null) {
      _uiState.update { it.copy(errorMessage = "Erro: Time não carregado.") }
      return
    }

    val playerPower = state.timeDetails.sumOf { p -> p.stats.sumOf { it.baseStat } }
    val cpuPower = state.cpuTeamDetails.sumOf { p -> p.stats.sumOf { it.baseStat } }

    val scoreDaBatalha = playerPower - cpuPower
    val resultText = when {
      scoreDaBatalha > 0 -> "Você Venceu!"
      scoreDaBatalha < 0 -> "Você Perdeu!"
      else -> "Empate!"
    }

    viewModelScope.launch {
      val nomeTime = timeAtual.nomeTime
      val rankingExistente = repository.getRankingPorNome(nomeTime)
      val pontuacaoAtual = rankingExistente?.pontuacao ?: 0
      val novaPontuacaoTotal = pontuacaoAtual + scoreDaBatalha
      val pontuacaoFinal = novaPontuacaoTotal.coerceAtLeast(0)

      if (rankingExistente != null) {
        val rankingAtualizado = rankingExistente.copy(pontuacao = pontuacaoFinal)
        repository.atualizarRanking(rankingAtualizado)
      } else {
        repository.inserirRanking(Ranking(nomeJogador = nomeTime, pontuacao = pontuacaoFinal))
      }

      _uiState.update {
        it.copy(
          battleMessage = resultText,
          battleScore = scoreDaBatalha,
          totalScore = pontuacaoFinal
        )
      }
    }
  }
}
