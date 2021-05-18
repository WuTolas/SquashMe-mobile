package pl.pjatk.squashme.di.module;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.pjatk.squashme.dao.MatchDao;
import pl.pjatk.squashme.dao.PlayerDao;
import pl.pjatk.squashme.dao.TournamentDao;
import pl.pjatk.squashme.database.AppDatabase;

@Module
public class RoomModule {

    private final AppDatabase appDatabase;

    public RoomModule(Application application) {
        appDatabase = Room.databaseBuilder(application, AppDatabase.class, AppDatabase.DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    public AppDatabase providesAppDatabase() {
        return appDatabase;
    }

    @Singleton
    @Provides
    public MatchDao providesMatchDao(AppDatabase appDatabase) {
        return appDatabase.getMatchDao();
    }

    @Singleton
    @Provides
    public PlayerDao providesPlayerDao(AppDatabase appDatabase) {
        return appDatabase.getPlayerDao();
    }

    @Singleton
    @Provides
    public TournamentDao providesTournamentDao(AppDatabase appDatabase) {
        return appDatabase.getTournamentDao();
    }
}
