package pl.pjatk.squashme.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pl.pjatk.squashme.di.module.ServiceModule;
import pl.pjatk.squashme.fragment.quickMatch.CreateQuickMatchFragment;
import pl.pjatk.squashme.service.MatchService;
import pl.pjatk.squashme.service.PlayerService;

/**
 * Handles services injection in CreateQuickMatchFragment.
 */
@Singleton
@Component(modules = {ServiceModule.class})
public interface CreateQuickMatchFragmentComponent {

    void inject(CreateQuickMatchFragment createQuickMatchFragment);

    MatchService matchService();

    PlayerService playerService();
}
