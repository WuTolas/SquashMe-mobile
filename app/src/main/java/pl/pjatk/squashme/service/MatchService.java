package pl.pjatk.squashme.service;

import java.util.Optional;

import pl.pjatk.squashme.model.Match;
import pl.pjatk.squashme.model.MatchWithResults;

public interface MatchService {

    Optional<MatchWithResults> getCurrentActiveQuickMatch();

    Match saveMatch(Match match);

    void updateMatch(Match match);
}
