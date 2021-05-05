package pl.pjatk.squashme.service;

import java.util.Optional;

import javax.inject.Inject;

import pl.pjatk.squashme.dao.MatchDao;
import pl.pjatk.squashme.model.Match;

public class MatchServiceImpl implements MatchService {

    private final MatchDao matchDao;

    @Inject
    public MatchServiceImpl(MatchDao matchDao) {
        this.matchDao = matchDao;
    }

    @Override
    public Optional<Match> getCurrentActiveQuickMatch() {
        return matchDao.getCurrentQuickMatch();
    }

    @Override
    public Match saveMatch(Match match) {
        long id = matchDao.insert(match);
        match.setId(id);
        return match;
    }
}
