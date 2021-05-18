package pl.pjatk.squashme.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Result(
        val player: Int,
        val side: Char,
        val point: Int,
        val set: Int,
        val match_id: Long,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
)
