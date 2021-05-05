package pl.pjatk.squashme.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Optional;

import io.reactivex.rxjava3.core.Maybe;
import pl.pjatk.squashme.model.Match;

@Dao
public interface MatchDao {

    @Query("SELECT * FROM `Match` WHERE finished = 0 ORDER BY id LIMIT 1")
    Optional<Match> getCurrentQuickMatch();

    @Query("SELECT * FROM `Match` WHERE id = :id")
    Match getMatchById(Long id);

    @Insert
    long insert(Match match);

    @Delete
    void delete(Match match);

    @Update
    void update(Match match);
}
