package pl.pjatk.squashme.service.implementation;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import pl.pjatk.squashme.dao.MatchDao;
import pl.pjatk.squashme.model.Match;
import pl.pjatk.squashme.model.custom.MatchWithPlayers;
import pl.pjatk.squashme.model.custom.MatchHistory;
import pl.pjatk.squashme.model.custom.TournamentMatchSimple;
import pl.pjatk.squashme.service.MatchService;

/**
 * Implementation class for MatchService interface.
 */
public class MatchServiceImpl implements MatchService {

    private final MatchDao matchDao;

    @Inject
    public MatchServiceImpl(MatchDao matchDao) {
        this.matchDao = matchDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<MatchWithPlayers> getCurrentQuickMatchWithPlayers() {
        return matchDao.getCurrentQuickMatchWithPlayers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MatchWithPlayers saveWithPlayersReturn(Match match) {
        matchDao.insert(match);
        return getCurrentQuickMatchWithPlayers().orElseThrow(() -> new RuntimeException("Match not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveMatch(Match match) {
        matchDao.insert(match);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMatch(Match match) {
        matchDao.update(match);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<List<TournamentMatchSimple>> searchTournamentMatches(Long tournamentId) {
        return matchDao.searchTournamentMatches(tournamentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateRefereeMode(long matchId, boolean refereeMode) {
        matchDao.updateRefereeMode(refereeMode, matchId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MatchWithPlayers getMatchWithResults(long id) {
        return matchDao.getMatchWithResults(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<List<MatchHistory>> searchMatchHistory() {
        return matchDao.searchMatchHistory();
    }
}
