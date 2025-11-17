package com.example.apppoketeam.data.local
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TimePokemonDAO{
  @Insert
  suspend fun inserir(time: TimePokemon)

  @Update
  suspend fun atualizar(time: TimePokemon)

  @Delete
  suspend fun deletar(time: TimePokemon)

  @Query("SELECT * FROM times_pokemon ORDER BY nomeTime ASC")
  fun buscarTodos(): Flow<List<TimePokemon>>

  @Query("SELECT * FROM times_pokemon WHERE id = :id")
  suspend fun buscarTimePorId(id: Int): TimePokemon?
}
