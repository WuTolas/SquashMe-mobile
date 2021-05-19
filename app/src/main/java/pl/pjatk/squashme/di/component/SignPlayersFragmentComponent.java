package pl.pjatk.squashme.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pl.pjatk.squashme.di.module.ServiceModule;
import pl.pjatk.squashme.fragment.SignPlayersFragment;
import pl.pjatk.squashme.service.TournamentService;

@Singleton
@Component(modules = {ServiceModule.class})
public interface SignPlayersFragmentComponent {

    void inject(SignPlayersFragment signPlayersFragment);

    TournamentService tournamentService();
}
