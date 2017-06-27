package common.datacollector;

import android.app.Application;

/**
 * Created by ChaiHongwei on 2017/6/26 14:52.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        DataCollector.start(this);
    }
}
