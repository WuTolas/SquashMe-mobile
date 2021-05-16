package pl.pjatk.squashme.service;

import android.util.Log;

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

    @Override
    public void updateMatch(Match match) {
        matchDao.update(match);
    }

    @Override
    public void addPoint() {
        Log.i("serv","test adding point");
//        return matchDao.getCurrentQuickMatch().get();
    }
}
