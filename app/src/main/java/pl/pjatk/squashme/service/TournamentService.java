package pl.pjatk.squashme.service;

import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.core.Single;
import pl.pjatk.squashme.model.Tournament;
import pl.pjatk.squashme.model.custom.TournamentHistory;
import pl.pjatk.squashme.model.custom.TournamentResults;

public interface TournamentService {

    /**
     * Gets current active tournament.
     *
     * @return Tournament
     */
    Optional<Tournament> getCurrentTournament();

    /**
     * Saves tournament in the database.
     *
     * @param tournament - tournament to be saved
     * @return Tournament which has id set
     */
    Tournament save(Tournament tournament);

    /**
     * Generates round robin matches and saves them to the database.
     *
     * @param tournamentId - id of the tournament
     * @param players - players list
     */
    void generateRoundRobinMatches(long tournamentId, List<String> players);

    /**
     * Finishes tournament for provided id - tournament status is set to FINISHED.
     *
     * @param tournamentId - id of the tournament to end
     */
    void endTournament(long tournamentId);

    /**
     * Gets current tournament results.
     *
     * @param tournamentId - id of the tournament
     * @return list containing data needed to generate leaderboard
     */
    Single<List<TournamentResults>> searchTournamentResults(long tournamentId);

    /**
     * Removes the tournament from the database.
     *
     * @param tournamentId - id of the tournament
     */
    void removeTournament(long tournamentId);

    /**
     * Gets tournament history list.
     *
     * @return list containing basic data for tournament history list view.
     */
    Single<List<TournamentHistory>> searchTournamentHistory();
}
