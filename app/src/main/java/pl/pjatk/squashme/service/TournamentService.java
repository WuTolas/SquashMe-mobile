package pl.pjatk.squashme.service;

import java.util.Optional;

import pl.pjatk.squashme.model.Tournament;

public interface TournamentService {

    Optional<Tournament> getCurrentTournament();
}
