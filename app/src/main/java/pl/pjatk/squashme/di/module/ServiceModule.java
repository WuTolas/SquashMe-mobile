package pl.pjatk.squashme.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.pjatk.squashme.dao.MatchDao;
import pl.pjatk.squashme.dao.PlayerTournamentDao;
import pl.pjatk.squashme.dao.ResultDao;
import pl.pjatk.squashme.dao.PlayerDao;
import pl.pjatk.squashme.dao.TournamentDao;
import pl.pjatk.squashme.service.MatchService;
import pl.pjatk.squashme.service.implementation.MatchServiceImpl;
import pl.pjatk.squashme.service.ResultService;
import pl.pjatk.squashme.service.implementation.ResultServiceImpl;
import pl.pjatk.squashme.service.PlayerService;
import pl.pjatk.squashme.service.implementation.PlayerServiceImpl;
import pl.pjatk.squashme.service.TournamentService;
import pl.pjatk.squashme.service.implementation.TournamentServiceImpl;

/**
 * Handles constructor injection in services.
 */
@Module(includes = {RoomModule.class})
public class ServiceModule {

    @Singleton
    @Provides
    public MatchService providesMatchService(MatchDao matchDao) {
        return new MatchServiceImpl(matchDao);
    }

    @Singleton
    @Provides
    public ResultService providesResultService(ResultDao resultDao) {
        return new ResultServiceImpl(resultDao);
    }

    @Singleton
    @Provides
    public PlayerService providesPlayerService(PlayerDao playerDao) {
        return new PlayerServiceImpl(playerDao);
    }

    @Singleton
    @Provides
    public TournamentService providesTournamentService(
            TournamentDao tournamentDao,
            PlayerTournamentDao playerTournamentDao,
            PlayerService playerService,
            MatchService matchService) {
        return new TournamentServiceImpl(tournamentDao, playerTournamentDao, playerService, matchService);
    }
}
