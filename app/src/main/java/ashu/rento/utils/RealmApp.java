package ashu.rento.utils;

import android.app.Application;

import ashu.rento.model.MovieDTO;
import ashu.rento.model.MoviesResponse;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.annotations.RealmModule;

/**
 * Created by apple on 03/05/18.
 */

public class RealmApp extends Application {

    @RealmModule(classes = {MovieDTO.class})
    public class SimpleRealmModule {}
    public static RealmApp instance;
    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration.Builder(this).name("Movies.DB").build();
        Realm.setDefaultConfiguration(config);
    }
}
