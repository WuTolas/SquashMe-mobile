package pl.pjatk.squashme.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import pl.pjatk.squashme.dao.PlayerTournamentDao;
import pl.pjatk.squashme.dao.TournamentDao;
import pl.pjatk.squashme.model.Match;
import pl.pjatk.squashme.model.PlayerTournament;
import pl.pjatk.squashme.model.Tournament;
import pl.pjatk.squashme.model.TournamentStatus;
import pl.pjatk.squashme.model.custom.TournamentHistory;
import pl.pjatk.squashme.model.custom.TournamentResults;
import pl.pjatk.squashme.service.MatchService;
import pl.pjatk.squashme.service.PlayerService;
import pl.pjatk.squashme.service.TournamentService;

/**
 * Implementation class for TournamentService interface.
 */
public class TournamentServiceImpl implements TournamentService {

    private final TournamentDao tournamentDao;
    private final PlayerTournamentDao playerTournamentDao;
    private final PlayerService playerService;
    private final MatchService matchService;

    @Inject
    public TournamentServiceImpl(
            TournamentDao tournamentDao,
            PlayerTournamentDao playerTournamentDao,
            PlayerService playerService,
            MatchService matchService) {
        this.tournamentDao = tournamentDao;
        this.playerTournamentDao = playerTournamentDao;
        this.playerService = playerService;
        this.matchService = matchService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Tournament> getCurrentTournament() {
        return tournamentDao.getCurrentTournament();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tournament save(Tournament tournament) {
        long id = tournamentDao.insert(tournament);
        tournament.setId(id);
        return tournament;
    }

    /**
     * {@inheritDoc}
     *
     * Algorithm for generating round robin matches is based on Circle method:
     * https://en.wikipedia.org/wiki/Round-robin_tournament#Scheduling_algorithm
     */
    @Override
    public void generateRoundRobinMatches(long tournamentId, List<String> players) {
        Tournament tournament = tournamentDao.getTournament(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        List<Long> playerIds = savePlayers(tournamentId, players);

        if (playerIds.size() % 2 != 0) {
            playerIds.add(0L);
        }

        int rounds = playerIds.size() - 1;

        for (int i = 1; i <= rounds; i++) {
            generateOneRobinRound(tournament, i, playerIds);
            playerIds.add(1, playerIds.get(playerIds.size() - 1));
            playerIds.remove(playerIds.size() - 1);
        }

        tournament.setStatus(TournamentStatus.ONGOING);
        tournamentDao.update(tournament);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endTournament(long tournamentId) {
        tournamentDao.finishTournament(tournamentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<List<TournamentResults>> searchTournamentResults(long tournamentId) {
        return tournamentDao.searchTournamentResults(tournamentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeTournament(long tournamentId) {
        tournamentDao.deleteTournament(tournamentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<List<TournamentHistory>> searchTournamentHistory() {
        return tournamentDao.searchTournamentHistory();
    }

    private void generateOneRobinRound(Tournament tournament, int round, List<Long> playerIds) {
        for (int i = 0; i < playerIds.size() / 2; i++) {
            int secondHalfPos = playerIds.size() - 1 - i;
            Long firstPlayerId = playerIds.get(i);
            Long secondPlayerId = playerIds.get(secondHalfPos);

            if (firstPlayerId == 0 || secondPlayerId == 0) {
                continue;
            }

            Match match = new Match();
            match.setTournamentId(tournament.getId());
            match.setTournamentRound(round);
            match.setTwoPointsAdvantage(tournament.isTwoPointsAdvantage());
            match.setBestOf(tournament.getBestOf());
            match.setPlayer1Id(firstPlayerId);
            match.setPlayer2Id(secondPlayerId);
            matchService.saveMatch(match);
        }
    }

    private List<Long> savePlayers(long tournamentId, List<String> players) {
        List<Long> ids = new ArrayList<>();

        players.forEach(playerName -> {
            PlayerTournament pt = new PlayerTournament();
            long playerId = playerService.getIdWithSave(playerName);
            ids.add(playerId);
            pt.setPlayerId(playerId);
            pt.setTournamentId(tournamentId);
            playerTournamentDao.insert(pt);
        });

        return ids;
    }
}
