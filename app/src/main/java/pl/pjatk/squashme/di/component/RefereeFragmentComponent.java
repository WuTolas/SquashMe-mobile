package pl.pjatk.squashme.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pl.pjatk.squashme.di.module.ServiceModule;
import pl.pjatk.squashme.fragment.RefereeModeFragment;
import pl.pjatk.squashme.service.MatchService;

@Singleton
@Component(modules = {ServiceModule.class})
public interface RefereeFragmentComponent {

    void inject(RefereeModeFragment refereeFragment);

    MatchService matchService();
}
