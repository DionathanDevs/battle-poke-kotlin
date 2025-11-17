package com.example.apppoketeam.data.repository

import com.example.apppoketeam.data.local.AppDatabase
import com.example.apppoketeam.data.local.Ranking
import com.example.apppoketeam.data.local.TimePokemon
import com.example.apppoketeam.data.local.User
import com.example.apppoketeam.data.remote.PokeApiService
import com.example.apppoketeam.data.remote.PokemonDTO
import com.example.apppoketeam.data.remote.PokemonListResponse
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

class AppRepository(
  private val db: AppDatabase,
  private val apiService: PokeApiService
) {
  private val userDAO = db.userDAO()
  private val timeDAO = db.timePokemonDAO()
  private val rankingDAO = db.rankingDAO()


  suspend fun checkLogin(username: String, password: String): Result<User> {
    return try {
      val user = userDAO.buscarPorUsername(username)
      if (user != null && user.password == password) { Result.success(user) }
      else { Result.failure(Exception("Usuário ou senha inválidos.")) }
    } catch (e: Exception) { Result.failure(e) }
  }
  fun getAllUsers(): Flow<List<User>> = userDAO.buscarTodos()
  suspend fun inserirUser(user: User) = userDAO.inserir(user)
  suspend fun atualizarUser(user: User) = userDAO.atualizar(user)
  suspend fun deletarUser(user: User) = userDAO.deletar(user)
  fun getTodosTimes(): Flow<List<TimePokemon>> = timeDAO.buscarTodos()
  suspend fun getTimesPorId(id: Int): TimePokemon? = timeDAO.buscarTimePorId(id)
  suspend fun inserirTime(time: TimePokemon) = timeDAO.inserir(time)
  suspend fun atualizarTime(time: TimePokemon) = timeDAO.atualizar(time)
  suspend fun deletarTime(time: TimePokemon) = timeDAO.deletar(time)
  suspend fun addPokemonToTeam(timeId: Int, slotId: Int, pokemonId: Int, pokemonName: String) {
    val time = timeDAO.buscarTimePorId(timeId)
    if (time != null) {
      val timeAtualizado = when (slotId) {
        1 -> time.copy(p1_id = pokemonId, p1_name = pokemonName)
        2 -> time.copy(p2_id = pokemonId, p2_name = pokemonName)
        3 -> time.copy(p3_id = pokemonId, p3_name = pokemonName)
        4 -> time.copy(p4_id = pokemonId, p4_name = pokemonName)
        5 -> time.copy(p5_id = pokemonId, p5_name = pokemonName)
        6 -> time.copy(p6_id = pokemonId, p6_name = pokemonName)
        else -> time
      }
      timeDAO.atualizar(timeAtualizado)
    }
  }


  fun getRanking(): Flow<List<Ranking>> = rankingDAO.buscarRanking()
  suspend fun inserirRanking(ranking: Ranking) = rankingDAO.inserir(ranking)
  suspend fun atualizarRanking(ranking: Ranking) = rankingDAO.atualizar(ranking)
  suspend fun deletarRanking(ranking: Ranking) = rankingDAO.deletar(ranking)


  suspend fun getRankingPorNome(nome: String): Ranking? = rankingDAO.buscarPorNome(nome)


  suspend fun getPokemonList(): Result<PokemonListResponse> {
    return try { Result.success(apiService.getPokemonList(151)) }
    catch (e: Exception) { Result.failure(e) }
  }
  suspend fun getPokemonDetails(nome: String): Result<PokemonDTO> {
    return try {
      val pokemon = apiService.getPokemonDetails(nome.lowercase().trim())
      Result.success(pokemon)
    } catch (e: Exception) { Result.failure(e) }
  }
  suspend fun getCpuTeam(): Result<List<PokemonDTO>> {
    return try {
      val cpuTeam = mutableListOf<PokemonDTO>()
      for (i in 1..6) {
        val randomId = Random.nextInt(1, 152)
        val pokemon = apiService.getPokemonDetails(randomId.toString())
        cpuTeam.add(pokemon)
      }
      Result.success(cpuTeam)
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}
