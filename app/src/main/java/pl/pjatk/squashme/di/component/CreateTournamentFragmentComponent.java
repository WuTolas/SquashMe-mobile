package pl.pjatk.squashme.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pl.pjatk.squashme.di.module.ServiceModule;
import pl.pjatk.squashme.fragment.tournament.CreateTournamentFragment;
import pl.pjatk.squashme.service.TournamentService;

/**
 * Handles services injection in CreateTournamentFragment.
 */
@Singleton
@Component(modules = {ServiceModule.class})
public interface CreateTournamentFragmentComponent {

    void inject(CreateTournamentFragment createTournamentFragment);

    TournamentService tournamentService();
}
