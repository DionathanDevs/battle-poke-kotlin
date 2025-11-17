package com.example.apppoketeam.data.local
import androidx.room.Entity; import androidx.room.PrimaryKey
@Entity(tableName = "ranking")
data class Ranking(@PrimaryKey(autoGenerate = true) val id: Int = 0, val nomeJogador: String, val pontuacao: Int)
