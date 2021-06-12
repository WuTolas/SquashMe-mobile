package pl.pjatk.squashme.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.core.Single;
import pl.pjatk.squashme.model.Match;
import pl.pjatk.squashme.model.MatchWithPlayers;
import pl.pjatk.squashme.model.custom.MatchHistory;
import pl.pjatk.squashme.model.custom.TournamentMatchSimple;

@Dao
public interface MatchDao extends BaseDao<Match> {

    @Transaction
    @Query("SELECT * FROM `Match` WHERE finished = 0 AND tournament_id IS NULL ORDER BY id LIMIT 1")
    Optional<MatchWithPlayers> getCurrentQuickMatchWithPlayers();

    @Transaction
    @Query("SELECT * FROM `Match` WHERE id = :id")
    Optional<MatchWithPlayers> getMatchWithResults(long id);

    @Transaction
    @Query("SELECT " +
            "    m.id                   AS matchId " +
            "   , m.finished            AS finished " +
            "   , m.referee_mode        AS refereeMode " +
            "   , m.tournament_round    AS tournamentRound " +
            "   , p1.name               AS player1 " +
            "   , p2.name               AS player2 " +
            "   , COALESCE(r.sets1, 0)  AS sets1 " +
            "   , COALESCE(r.sets2, 0)  AS sets2 " +
            "FROM `Match` m " +
            "INNER JOIN Player p1 ON p1.id = m.player1 " +
            "INNER JOIN Player p2 ON p2.id = m.player2 " +
            "LEFT JOIN ( " +
            "                SELECT " +
            "                    mi1.id    as matchId  " +
            "                    , CASE WHEN ri1.playerOneScore > ri1.playerTwoScore THEN ri1.playerOneSet + 1 ELSE ri1.playerOneSet END AS sets1  " +
            "                    , CASE WHEN ri1.playerOneScore < ri1.playerTwoScore THEN ri1.playerTwoSet + 1 ELSE ri1.playerTwoSet END AS sets2  " +
            "                FROM `Match` mi1 " +
            "                INNER JOIN Result ri1 ON ri1.match_id = mi1.id " +
            "                WHERE  " +
            "                mi1.tournament_id = :tournamentId " +
            "                AND mi1.finished = 1 " +
            "                AND ri1.id IN (SELECT MAX(ID) FROM Result GROUP BY match_id) " +
            ") as r ON r.matchId = m.id " +
            "WHERE " +
            "   m.tournament_id = :tournamentId " +
            "ORDER BY m.tournament_round ASC")
    Single<List<TournamentMatchSimple>> searchTournamentMatches(Long tournamentId);

    @Transaction
    @Query("UPDATE `Match` SET referee_mode = :refereeMode WHERE id = :id")
    void updateRefereeMode(boolean refereeMode, long id);

    @Transaction
    @Query("SELECT " +
            "    m.id AS id " +
            "    , p1.name AS player1 " +
            "    , p2.name AS player2 " +
            "    , CASE WHEN r.playerOneScore > r.playerTwoScore THEN r.playerOneSet + 1 ELSE r.playerOneSet END AS sets1 " +
            "    , CASE WHEN r.playerOneScore < r.playerTwoScore THEN r.playerTwoSet + 1 ELSE r.playerTwoSet END AS sets2 " +
            "FROM Result r " +
            "INNER JOIN `Match` m ON m.id = r.match_id " +
            "INNER JOIN Player p1 ON p1.id = m.player1 " +
            "INNER JOIN Player p2 ON p2.id = m.player2 " +
            "WHERE " +
            "   m.finished = 1 " +
            "   AND m.tournament_id IS NULL " +
            "   AND r.id IN (SELECT MAX(ID) FROM Result GROUP BY match_id) " +
            "ORDER BY m.id DESC")
    Single<List<MatchHistory>> searchMatchHistory();
}
