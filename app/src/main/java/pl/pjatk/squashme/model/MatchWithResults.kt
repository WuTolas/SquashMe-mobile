package pl.pjatk.squashme.model

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

data class MatchWithResults(
        @Embedded val match: Match,
        @Relation(
                parentColumn = "id",
                entityColumn = "match_id"
        )
        val results: MutableList<Result>
) : Serializable
