package pl.pjatk.squashme.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import pl.pjatk.squashme.dao.MatchDao;
import pl.pjatk.squashme.model.Match;
import pl.pjatk.squashme.model.MatchWithPlayers;
import pl.pjatk.squashme.model.custom.MatchHistory;
import pl.pjatk.squashme.model.custom.TournamentMatchSimple;

public class MatchServiceImpl implements MatchService {

    private final MatchDao matchDao;

    @Inject
    public MatchServiceImpl(MatchDao matchDao) {
        this.matchDao = matchDao;
    }

    @Override
    public Optional<MatchWithPlayers> getCurrentQuickMatchWithPlayers() {
        return matchDao.getCurrentQuickMatchWithPlayers();
    }

    @Override
    public MatchWithPlayers saveWithPlayersReturn(Match match) {
        matchDao.insert(match);
        return getCurrentQuickMatchWithPlayers().orElseThrow(() -> new RuntimeException("Match not found"));
    }

    @Override
    public void saveMatch(Match match) {
        matchDao.insert(match);
    }

    @Override
    public void updateMatch(Match match) {
        matchDao.update(match);
    }

    @Override
    public Single<List<TournamentMatchSimple>> searchTournamentMatches(Long tournamentId) {
        return matchDao.searchTournamentMatches(tournamentId);
    }

    @Override
    public void updateRefereeMode(long matchId, boolean refereeMode) {
        matchDao.updateRefereeMode(refereeMode, matchId);
    }

    @Override
    public MatchWithPlayers getMatchWithResults(long id) {
        return matchDao.getMatchWithResults(id).orElseGet(null);
    }

    @Override
    public Single<List<MatchHistory>> searchMatchHistory() {
        return matchDao.searchMatchHistory();
    }
}
