package pl.pjatk.squashme.service;

import java.util.List;
import java.util.Optional;

import pl.pjatk.squashme.model.Tournament;

public interface TournamentService {

    Optional<Tournament> getCurrentTournament();

    Tournament save(Tournament tournament);

    void savePlayers(long tournamentId, List<String> players);

    void generateRoundRobinMatches();
}
