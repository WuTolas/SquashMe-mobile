package pl.pjatk.squashme.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pl.pjatk.squashme.di.module.ServiceModule;
import pl.pjatk.squashme.fragment.QuickMatchHistoryFragment;
import pl.pjatk.squashme.service.MatchService;

/**
 * Handles services injection in QuickMatchHistoryFragment.
 */
@Singleton
@Component(modules = {ServiceModule.class})
public interface QuickMatchHistoryFragmentComponent {

    void inject(QuickMatchHistoryFragment quickMatchHistoryFragment);

    MatchService matchService();
}
