package com.example.apppoketeam.data.local
import androidx.room.*;
import kotlinx.coroutines.flow.Flow
@Dao interface UserDAO{@Insert suspend fun inserir(user: User);
  @Query("SELECT * FROM usuarios WHERE username = :username") suspend fun buscarPorUsername(username: String): User?;
  @Update suspend fun atualizar(user: User);

  @Delete suspend fun deletar(user: User);
  @Query("SELECT * FROM usuarios ORDER BY username ASC") fun buscarTodos(): Flow<List<User>>}
