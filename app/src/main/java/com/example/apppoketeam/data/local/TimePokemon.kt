package com.example.apppoketeam.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "times_pokemon")
data class TimePokemon(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  val nomeTime: String,

  val p1_id: Int?,
  val p1_name: String?,

  val p2_id: Int?,
  val p2_name: String?,

  val p3_id: Int?,
  val p3_name: String?,

  val p4_id: Int?,
  val p4_name: String?,

  val p5_id: Int?,
  val p5_name: String?,

  val p6_id: Int?,
  val p6_name: String?
)
