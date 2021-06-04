package pl.pjatk.squashme.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pl.pjatk.squashme.di.module.ServiceModule;
import pl.pjatk.squashme.fragment.TournamentResultsFragment;
import pl.pjatk.squashme.service.TournamentService;

@Singleton
@Component(modules = {ServiceModule.class})
public interface TournamentResultsFragmentComponent {

    void inject(TournamentResultsFragment tournamentResultsFragment);

    TournamentService tournamentService();
}
