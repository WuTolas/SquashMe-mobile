package pl.pjatk.squashme.service;

import java.util.Optional;

import pl.pjatk.squashme.model.Match;

public interface MatchService {

    Optional<Match> getCurrentActiveQuickMatch();

    Match saveMatch(Match match);
}
