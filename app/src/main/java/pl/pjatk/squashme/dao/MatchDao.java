package pl.pjatk.squashme.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Optional;

import pl.pjatk.squashme.model.Match;
import pl.pjatk.squashme.model.MatchWithPlayers;

@Dao
public interface MatchDao extends BaseDao<Match> {

    @Transaction
    @Query("SELECT * FROM `Match` WHERE finished = 0 AND tournament_id IS NULL ORDER BY id LIMIT 1")
    Optional<MatchWithPlayers> getCurrentQuickMatchWithPlayers();
}
