package pl.pjatk.squashme.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Result(
        @PrimaryKey(autoGenerate = true) val id: Long,
        val player: Int,
        val side: Char,
        val point: Int,
        val set: Int,
        val match_id: Long
)

@Entity(primaryKeys = ["match_id", "result_id"])
data class MatchResultCrossRef(
        val match_id: Long,
        val result_id: Long
)
