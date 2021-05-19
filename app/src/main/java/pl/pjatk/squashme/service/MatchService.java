package pl.pjatk.squashme.service;

import java.util.Optional;

import pl.pjatk.squashme.model.Match;
import pl.pjatk.squashme.model.MatchWithPlayers;

public interface MatchService {

    Optional<MatchWithPlayers> getCurrentQuickMatchWithPlayers();

    MatchWithPlayers saveWithPlayersReturn(Match match);

    void saveMatch(Match match);

    void updateMatch(Match match);
}
