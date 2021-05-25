package pl.pjatk.squashme.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Result(
        val playerOneScore: Int,
        val playerTwoScore: Int,
        val side: String,
        val serve: Int,
        val playerOneSet: Int,
        val playerTwoSet: Int,
        val match_id: Long,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
)
