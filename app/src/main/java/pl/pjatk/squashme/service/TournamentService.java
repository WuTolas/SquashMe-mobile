package pl.pjatk.squashme.service;

import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.core.Single;
import pl.pjatk.squashme.model.Tournament;
import pl.pjatk.squashme.model.custom.TournamentHistory;
import pl.pjatk.squashme.model.custom.TournamentResults;

public interface TournamentService {

    Optional<Tournament> getCurrentTournament();

    Tournament save(Tournament tournament);

    void generateRoundRobinMatches(long tournamentId, List<String> players);

    void endTournament(long tournamentId);

    Single<List<TournamentResults>> searchTournamentResults(long tournamentId);

    void removeTournament(long tournamentId);

    Single<List<TournamentHistory>> searchTournamentHistory();
}
