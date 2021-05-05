package pl.pjatk.squashme.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pl.pjatk.squashme.activity.QuickMatchActivity;
import pl.pjatk.squashme.di.module.ServiceModule;
import pl.pjatk.squashme.service.MatchService;

@Singleton
@Component(modules = {ServiceModule.class})
public interface QuickMatchActivityComponent {

    void inject(QuickMatchActivity quickMatchActivity);

    MatchService matchService();
}
