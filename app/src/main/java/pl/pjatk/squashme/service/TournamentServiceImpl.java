package pl.pjatk.squashme.service;


import java.util.Optional;

import javax.inject.Inject;

import pl.pjatk.squashme.dao.TournamentDao;
import pl.pjatk.squashme.model.Tournament;

public class TournamentServiceImpl implements TournamentService {

    private final TournamentDao tournamentDao;

    @Inject
    public TournamentServiceImpl(TournamentDao tournamentDao) {
        this.tournamentDao = tournamentDao;
    }

    @Override
    public Optional<Tournament> getCurrentTournament() {
        return tournamentDao.getCurrentTournament();
    }

    @Override
    public Tournament save(Tournament tournament) {
        long id = tournamentDao.insert(tournament);
        tournament.setId(id);
        return tournament;
    }
}
