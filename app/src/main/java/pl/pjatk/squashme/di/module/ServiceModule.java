package pl.pjatk.squashme.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.pjatk.squashme.dao.MatchDao;
import pl.pjatk.squashme.dao.ResultDao;
import pl.pjatk.squashme.service.MatchService;
import pl.pjatk.squashme.service.MatchServiceImpl;
import pl.pjatk.squashme.service.ResultService;
import pl.pjatk.squashme.service.ResultServiceImpl;

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
}
