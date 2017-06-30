package common.datacollector;

import android.app.Application;
import android.util.Log;

/**
 * Created by ChaiHongwei on 2017/6/26 14:52.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(WeLifecycleCallBacks.getInstance());

        DataCollector.start(this);
        Log.d("chwwwww111", "启动app");

        AppForegroundStateManager.getInstance()
                .addListener(new AppForegroundStateManager.OnAppForegroundStateChangeListener() {
                    @Override
                    public void onAppForegroundStateChange(AppForegroundStateManager.AppForegroundState newState) {
                        if (AppForegroundStateManager.AppForegroundState.IN_FOREGROUND == newState) {
                            Log.d("chwwwww111", "进入前台");
                        } else {
                            Log.d("chwwwww111", "进入后台");
                        }
                    }
                });
    }
}
