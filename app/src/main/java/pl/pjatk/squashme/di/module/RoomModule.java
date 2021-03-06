package pl.pjatk.squashme.di.module;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.pjatk.squashme.dao.MatchDao;
import pl.pjatk.squashme.dao.PlayerTournamentDao;
import pl.pjatk.squashme.dao.ResultDao;
import pl.pjatk.squashme.dao.PlayerDao;
import pl.pjatk.squashme.dao.TournamentDao;
import pl.pjatk.squashme.database.AppDatabase;

/**
 * Handles data access objects injection needed in services layer.
 */
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
    public ResultDao providesResultDao(AppDatabase appDatabase) {
        return appDatabase.getResultDao();
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

    @Singleton
    @Provides
    public PlayerTournamentDao providesPlayerTournamentDao(AppDatabase appDatabase) {
        return appDatabase.getPlayerTournamentDao();
    }
}
