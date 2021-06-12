package pl.pjatk.squashme.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pl.pjatk.squashme.di.module.ServiceModule;
import pl.pjatk.squashme.fragment.TournamentOptionsFragment;
import pl.pjatk.squashme.service.TournamentService;

/**
 * Handles services injection in TournamentOptionsFragment.
 */
@Singleton
@Component(modules = {ServiceModule.class})
public interface TournamentOptionsFragmentComponent {

    void inject (TournamentOptionsFragment tournamentOptionsFragment);

    TournamentService tournamentService();
}
