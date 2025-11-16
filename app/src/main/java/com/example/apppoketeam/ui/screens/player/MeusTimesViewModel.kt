package com.example.apppoketeam.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apppoketeam.data.local.TimePokemon
import com.example.apppoketeam.data.repository.AppRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MeusTimesUiState(
  val times: List<TimePokemon> = emptyList(),
  val nomeTime: String = "",
  val timeEmEdicao: TimePokemon? = null
)

class MeusTimesViewModel(private val repository: AppRepository) : ViewModel() {

  private val _uiState = MutableStateFlow(MeusTimesUiState())
  val uiState: StateFlow<MeusTimesUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      repository.getTodosTimes().collect { times ->
        _uiState.update { it.copy(times = times) }
      }
    }
  }


  fun onNomeTimeChange(nome: String) {
    if (nome.length <= 16) {
      _uiState.update { it.copy(nomeTime = nome) }
    }
  }

  fun onEdit(time: TimePokemon) {
    _uiState.update {
      it.copy(
        timeEmEdicao = time,
        nomeTime = time.nomeTime
      )
    }
  }

  private fun limparCampos() {
    _uiState.update { it.copy(timeEmEdicao = null, nomeTime = "") }
  }

  fun onSave() {
    val s = _uiState.value
    if (s.nomeTime.isBlank()) return

    viewModelScope.launch {
      if (s.timeEmEdicao == null) {
        repository.inserirTime(TimePokemon(
          nomeTime = s.nomeTime,
          p1_id = null, p1_name = null,
          p2_id = null, p2_name = null,
          p3_id = null, p3_name = null,
          p4_id = null, p4_name = null,
          p5_id = null, p5_name = null,
          p6_id = null, p6_name = null
        ))
      } else {
        repository.atualizarTime(s.timeEmEdicao.copy(nomeTime = s.nomeTime))
      }
      limparCampos()
    }
  }

  fun onDelete(time: TimePokemon) {
    viewModelScope.launch {
      repository.deletarTime(time)
    }
  }
}
