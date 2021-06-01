package pl.pjatk.squashme.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.core.Single;
import pl.pjatk.squashme.model.Match;
import pl.pjatk.squashme.model.MatchWithPlayers;
import pl.pjatk.squashme.model.complex.TournamentMatchSimple;

@Dao
public interface MatchDao extends BaseDao<Match> {

    @Transaction
    @Query("SELECT * FROM `Match` WHERE finished = 0 AND tournament_id IS NULL ORDER BY id LIMIT 1")
    Optional<MatchWithPlayers> getCurrentQuickMatchWithPlayers();

    @Transaction
    @Query("SELECT * FROM `Match` WHERE tournament_id = :tournamentId ORDER BY tournament_round ASC")
    Single<List<TournamentMatchSimple>> searchTournamentMatches(Long tournamentId);
}
