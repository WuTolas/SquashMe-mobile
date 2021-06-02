package pl.pjatk.squashme.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pl.pjatk.squashme.di.module.ServiceModule;
import pl.pjatk.squashme.fragment.TournamentMatchesFragment;
import pl.pjatk.squashme.service.MatchService;

@Singleton
@Component(modules = {ServiceModule.class})
public interface TournamentMatchesFragmentComponent {

    void inject(TournamentMatchesFragment tournamentMatchesFragment);

    MatchService matchService();
}
