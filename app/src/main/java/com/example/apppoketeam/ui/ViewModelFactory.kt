package com.example.apppoketeam.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.apppoketeam.data.local.AppDatabase
import com.example.apppoketeam.data.remote.RetrofitClient
import com.example.apppoketeam.data.repository.AppRepository
import com.example.apppoketeam.ui.screens.admin.AdminRankingViewModel
import com.example.apppoketeam.ui.screens.admin.AdminUserViewModel
import com.example.apppoketeam.ui.screens.login.LoginViewModel
import com.example.apppoketeam.ui.screens.player.BatalhaViewModel

import com.example.apppoketeam.ui.screens.player.*

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

  private val database by lazy { AppDatabase.getDatabase(context) }
  private val apiService by lazy { RetrofitClient.instance }
  private val repository by lazy { AppRepository(database, apiService) }

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return when {

      modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository) as T
      modelClass.isAssignableFrom(PokedexViewModel::class.java) -> PokedexViewModel(repository) as T
      modelClass.isAssignableFrom(PokemonDetailViewModel::class.java) -> PokemonDetailViewModel(repository) as T
      modelClass.isAssignableFrom(MeusTimesViewModel::class.java) -> MeusTimesViewModel(repository) as T
      modelClass.isAssignableFrom(BatalhaViewModel::class.java) -> BatalhaViewModel(repository) as T


      modelClass.isAssignableFrom(PlayerRankingViewModel::class.java) -> PlayerRankingViewModel(repository) as T


      modelClass.isAssignableFrom(AdminUserViewModel::class.java) -> AdminUserViewModel(repository) as T


      modelClass.isAssignableFrom(AdminRankingViewModel::class.java) -> AdminRankingViewModel(repository) as T

      else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
  }
}
