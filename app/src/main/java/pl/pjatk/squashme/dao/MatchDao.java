package pl.pjatk.squashme.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.Optional;

import pl.pjatk.squashme.model.Match;

@Dao
public interface MatchDao extends BaseDao<Match> {

    @Query("SELECT * FROM `Match` WHERE finished = 0 ORDER BY id LIMIT 1")
    Optional<Match> getCurrentQuickMatch();
}
