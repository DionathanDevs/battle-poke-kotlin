package com.example.apppoketeam.data.local
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RankingDAO{
  @Insert
  suspend fun inserir(ranking: Ranking)

  @Update
  suspend fun atualizar(ranking: Ranking)

  @Delete
  suspend fun deletar(ranking: Ranking)

  @Query("SELECT * FROM ranking ORDER BY pontuacao DESC")
  fun buscarRanking(): Flow<List<Ranking>>

  @Query("SELECT * FROM ranking WHERE nomeJogador = :nome LIMIT 1")
  suspend fun buscarPorNome(nome: String): Ranking?
}
