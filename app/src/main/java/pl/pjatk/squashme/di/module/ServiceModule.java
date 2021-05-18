package pl.pjatk.squashme.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.pjatk.squashme.dao.MatchDao;
import pl.pjatk.squashme.dao.PlayerDao;
import pl.pjatk.squashme.dao.TournamentDao;
import pl.pjatk.squashme.service.MatchService;
import pl.pjatk.squashme.service.MatchServiceImpl;
import pl.pjatk.squashme.service.PlayerService;
import pl.pjatk.squashme.service.PlayerServiceImpl;
import pl.pjatk.squashme.service.TournamentService;
import pl.pjatk.squashme.service.TournamentServiceImpl;

@Module(includes = {RoomModule.class})
public class ServiceModule {

    @Singleton
    @Provides
    public MatchService providesMatchService(MatchDao matchDao) {
        return new MatchServiceImpl(matchDao);
    }

    @Singleton
    @Provides
    public PlayerService providesPlayerService(PlayerDao playerDao) {
        return new PlayerServiceImpl(playerDao);
    }

    @Singleton
    @Provides
    public TournamentService providesTournamentService(TournamentDao tournamentDao) {
        return new TournamentServiceImpl(tournamentDao);
    }
}
