package pl.pjatk.squashme.service;

import java.util.Optional;

import javax.inject.Inject;

import pl.pjatk.squashme.dao.MatchDao;
import pl.pjatk.squashme.model.Match;
import pl.pjatk.squashme.model.MatchWithPlayers;

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
    public void updateMatch(Match match) {
        matchDao.update(match);
    }
}
