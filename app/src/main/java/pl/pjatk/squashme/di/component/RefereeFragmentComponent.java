package pl.pjatk.squashme.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pl.pjatk.squashme.di.module.ServiceModule;
import pl.pjatk.squashme.fragment.referee.RefereeModeFragment;
import pl.pjatk.squashme.service.MatchService;

/**
 * Handles services injection in RefereeModeFragment.
 */
@Singleton
@Component(modules = {ServiceModule.class})
public interface RefereeFragmentComponent {

    void inject(RefereeModeFragment refereeFragment);

    MatchService matchService();
}
