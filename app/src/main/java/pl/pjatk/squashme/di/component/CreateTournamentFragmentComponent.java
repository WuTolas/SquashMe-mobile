package pl.pjatk.squashme.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pl.pjatk.squashme.di.module.ServiceModule;
import pl.pjatk.squashme.fragment.CreateTournamentFragment;
import pl.pjatk.squashme.service.TournamentService;

@Singleton
@Component(modules = {ServiceModule.class})
public interface CreateTournamentFragmentComponent {

    void inject(CreateTournamentFragment createTournamentFragment);

    TournamentService tournamentService();
}
