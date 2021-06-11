package pl.pjatk.squashme.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.core.Single;
import pl.pjatk.squashme.model.Tournament;
import pl.pjatk.squashme.model.complex.TournamentResults;

@Dao
public interface TournamentDao extends BaseDao<Tournament> {

    @Query("SELECT * FROM `Tournament` WHERE tournament_status != 'FINISHED'")
    Optional<Tournament> getCurrentTournament();

    @Query("SELECT * FROM `Tournament` WHERE id = :id")
    Optional<Tournament> getTournament(long id);

    @Query("UPDATE `Tournament` SET tournament_status = 'FINISHED' WHERE id = :tournamentId")
    void finishTournament(long tournamentId);

    @Query("DELETE FROM `Tournament` WHERE id = :tournamentId")
    void deleteTournament(long tournamentId);

    @Transaction
    @Query("SELECT " +
            "  p.id AS playerId " +
            "  , p.name AS name " +
            "  , COALESCE(SUM(scores.winning), 0) AS winning " +
            "  , COALESCE(SUM(scores.losing), 0) AS losing " +
            "  , COALESCE(SUM(scores.sets_winning), 0) AS setsWinning " +
            "  , COALESCE(SUM(scores.sets_losing), 0) AS setsLosing " +
            "  , COALESCE(SUM(scores.points_winning), 0) AS pointsWinning " +
            "  , COALESCE(SUM(scores.points_losing), 0) AS pointsLosing " +
            "FROM `PlayerTournament` pt " +
            "INNER JOIN Player p ON p.id = pt.player_id " +
            "LEFT JOIN ( " +
            "  SELECT " +
            "   m.player1 as player_id " +
            "    , finalScore.winning " +
            "    , finalScore.losing " +
            "    , finalScore.sets_winning " +
            "    , finalScore.sets_losing " +
            "    , m.id " +
            "    , matchResults.points_winning " +
            "    , matchResults.points_losing " +
            "FROM Result r " +
            "INNER JOIN `Match` m ON m.id = r.match_id " +
            "INNER JOIN ( " +
            "    SELECT  " +
            "            mi1.id as match_id " +
            "            , CASE WHEN ri1.playerOneScore > ri1.playerTwoScore THEN 1 ELSE 0 END AS winning " +
            "            , CASE WHEN ri1.playerOneScore < ri1.playerTwoScore THEN 1 ELSE 0 END AS losing " +
            "            , CASE WHEN ri1.playerOneScore > ri1.playerTwoScore THEN ri1.playerOneSet + 1 ELSE ri1.playerOneSet END AS sets_winning " +
            "            , CASE WHEN ri1.playerOneScore < ri1.playerTwoScore THEN ri1.playerTwoSet + 1 ELSE ri1.playerTwoSet END AS sets_losing " +
            "    FROM Result ri1 " +
            "    INNER JOIN `Match` mi1 ON mi1.id = ri1.match_id " +
            "    WHERE mi1.tournament_id =:tournamentId " +
            "    AND ri1.id IN ( " +
            "        SELECT MAX(ID) FROM Result WHERE tournament_id =:tournamentId GROUP BY match_id " +
            "    ) " +
            ") as finalScore ON finalScore.match_id = m.id " +
            "INNER JOIN ( " +
            "    SELECT  " +
            "            mi2.id as match_id " +
            "            , SUM(ri2.playerOneScore) as points_winning " +
            "            , SUM(ri2.playerTwoScore) as points_losing " +
            "        FROM Result ri2 " +
            "        INNER JOIN `Match` mi2 ON mi2.id = ri2.match_id " +
            "        WHERE mi2.tournament_id =:tournamentId " +
            "        AND ri2.id IN ( " +
            "            SELECT MAX(ID) FROM Result WHERE mi2.tournament_id =:tournamentId GROUP BY match_id, playerOneSet, playerTwoSet " +
            "        )  " +
            "        GROUP BY mi2.id, mi2.player1 " +
            ") as matchResults ON matchResults.match_id = m.id " +
            "WHERE " +
            "    m.tournament_id =:tournamentId " +
            "    AND finished = 1 " +
            "GROUP BY m.id " +
            "UNION " +
            "SELECT " +
            "   m.player2 as player_id " +
            "    , finalScore.winning " +
            "    , finalScore.losing " +
            "    , finalScore.sets_winning " +
            "    , finalScore.sets_losing " +
            "    , m.id " +
            "    , matchResults.points_winning " +
            "    , matchResults.points_losing " +
            "FROM Result r " +
            "INNER JOIN `Match` m ON m.id = r.match_id " +
            "INNER JOIN ( " +
            "    SELECT  " +
            "            mi1.id as match_id " +
            "            , CASE WHEN ri1.playerOneScore < ri1.playerTwoScore THEN 1 ELSE 0 END AS winning " +
            "            , CASE WHEN ri1.playerOneScore > ri1.playerTwoScore THEN 1 ELSE 0 END AS losing " +
            "            , CASE WHEN ri1.playerOneScore < ri1.playerTwoScore THEN ri1.playerTwoSet + 1 ELSE ri1.playerTwoSet END AS sets_winning " +
            "            , CASE WHEN ri1.playerOneScore > ri1.playerTwoScore THEN ri1.playerOneSet + 1 ELSE ri1.playerOneSet END AS sets_losing " +
            "    FROM Result ri1 " +
            "    INNER JOIN `Match` mi1 ON mi1.id = ri1.match_id " +
            "    WHERE mi1.tournament_id =:tournamentId " +
            "    AND ri1.id IN ( " +
            "        SELECT MAX(ID) FROM Result WHERE tournament_id =:tournamentId GROUP BY match_id " +
            "    ) " +
            ") as finalScore ON finalScore.match_id = m.id " +
            "INNER JOIN ( " +
            "    SELECT  " +
            "            mi2.id as match_id " +
            "            , SUM(ri2.playerTwoScore) as points_winning " +
            "            , SUM(ri2.playerOneScore) as points_losing " +
            "        FROM Result ri2 " +
            "        INNER JOIN `Match` mi2 ON mi2.id = ri2.match_id " +
            "        WHERE mi2.tournament_id =:tournamentId " +
            "        AND ri2.id IN ( " +
            "            SELECT MAX(ID) FROM Result WHERE mi2.tournament_id =:tournamentId GROUP BY match_id, playerOneSet, playerTwoSet " +
            "        )  " +
            "        GROUP BY mi2.id, mi2.player1 " +
            ") as matchResults ON matchResults.match_id = m.id " +
            "WHERE " +
            "    m.tournament_id =:tournamentId " +
            "    AND finished = 1 " +
            "GROUP BY m.id " +
            ") scores ON scores.player_id = pt.player_id " +
            "WHERE pt.tournament_id =:tournamentId " +
            "GROUP BY p.id " +
            "ORDER BY winning DESC, sets_winning DESC, points_winning DESC, losing ASC, sets_losing ASC, points_losing ASC")
    Single<List<TournamentResults>> searchTournamentResults(long tournamentId);
}
