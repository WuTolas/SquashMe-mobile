package pl.pjatk.squashme.service;

import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.core.Single;
import pl.pjatk.squashme.model.Match;
import pl.pjatk.squashme.model.custom.MatchWithPlayers;
import pl.pjatk.squashme.model.custom.MatchHistory;
import pl.pjatk.squashme.model.custom.TournamentMatchSimple;

public interface MatchService {

    /**
     * Gets current quick match.
     *
     * Current quick match:
     * -> finished == 0
     * -> tournament_id == null
     *
     * @return current quick match with players info
     */
    Optional<MatchWithPlayers> getCurrentQuickMatchWithPlayers();

    /**
     * Saves match and returns current data with generated id.
     *
     * @param match - match to be saved
     * @return saved match with players info
     */
    MatchWithPlayers saveWithPlayersReturn(Match match);

    /**
     * Saves match.
     *
     * @param match - match to be saved
     */
    void saveMatch(Match match);

    /**
     * Updates match.
     *
     * @param match - match to be updated
     */
    void updateMatch(Match match);

    /**
     * Get tournament matches for the specific id.
     *
     * @param tournamentId - tournament id for which we want to get matches
     * @return list of the specific tournament matches
     */
    Single<List<TournamentMatchSimple>> searchTournamentMatches(Long tournamentId);

    /**
     * Updates referee mode flag for the provided match id.
     *
     * @param matchId - id of the match where we want to set referee mode
     * @param refereeMode - is referee mode
     */
    void updateRefereeMode(long matchId, boolean refereeMode);

    /**
     * Gets match details containing scores data.
     *
     * @param id - id of the match
     * @return saved match with players and results info
     */
    MatchWithPlayers getMatchWithResults(long id);

    /**
     * Gets quick match history.
     *
     * @return list containing finished quick matches
     */
    Single<List<MatchHistory>> searchMatchHistory();
}
