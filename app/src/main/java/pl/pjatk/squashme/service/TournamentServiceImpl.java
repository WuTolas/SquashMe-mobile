package pl.pjatk.squashme.service;


import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import pl.pjatk.squashme.dao.PlayerTournamentDao;
import pl.pjatk.squashme.dao.TournamentDao;
import pl.pjatk.squashme.model.PlayerTournament;
import pl.pjatk.squashme.model.Tournament;

public class TournamentServiceImpl implements TournamentService {

    private final TournamentDao tournamentDao;
    private final PlayerTournamentDao playerTournamentDao;
    private final PlayerService playerService;

    @Inject
    public TournamentServiceImpl(TournamentDao tournamentDao, PlayerTournamentDao playerTournamentDao, PlayerService playerService) {
        this.tournamentDao = tournamentDao;
        this.playerTournamentDao = playerTournamentDao;
        this.playerService = playerService;
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

    @Override
    public void savePlayers(long tournamentId, List<String> players) {
        players.forEach(playerName -> {
            PlayerTournament pt = new PlayerTournament();
            long playerId = playerService.getIdWithSave(playerName);
            pt.setPlayerId(playerId);
            pt.setTournamentId(tournamentId);
            playerTournamentDao.insert(pt);
        });
    }

    @Override
    public void generateRoundRobinMatches() {

    }
}
