package pl.pjatk.squashme.service;

import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.core.Single;
import pl.pjatk.squashme.model.Match;
import pl.pjatk.squashme.model.MatchWithPlayers;
import pl.pjatk.squashme.model.complex.TournamentMatchSimple;

public interface MatchService {

    Optional<MatchWithPlayers> getCurrentQuickMatchWithPlayers();

    MatchWithPlayers saveWithPlayersReturn(Match match);

    void saveMatch(Match match);

    void updateMatch(Match match);

    Single<List<TournamentMatchSimple>> searchTournamentMatches(Long tournamentId);

    void updateRefereeMode(long matchId, boolean refereeMode);

    MatchWithPlayers getMatchWithResults(long id);
}
