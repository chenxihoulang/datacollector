package common.datacollector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

public class BaseActivity extends AppCompatActivity {
    public static final String EXTRA_HANDLING_NOTIFICATION = "Notification.EXTRA_HANDLING_NOTIFICATION";

    public enum LaunchMechanism {
        DIRECT(1),

        NOTIFICATION(2),

        URL(3);

        public int launchType;

        LaunchMechanism(int launchType) {
            this.launchType = launchType;
        }
    }

    private static LaunchMechanism mLaunchMechanism = LaunchMechanism.DIRECT;

    public void setLaunchMechanism(LaunchMechanism launchMechanism) {
        mLaunchMechanism = launchMechanism;
    }

    public LaunchMechanism getLaunchMechanism() {
        return mLaunchMechanism;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            // 判断 Activity 是否由用户点击推送启动
            if (intent.getExtras().getBoolean(EXTRA_HANDLING_NOTIFICATION, false)) {
                // 发出“应用通过用户点击推送启动”的通知
                setLaunchMechanism(LaunchMechanism.NOTIFICATION);
            }
        }

        DataCollector.onCreate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppForegroundStateManager.getInstance().onActivityVisible(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("chwwwww111", this.getClass().getSimpleName() + "----onResume");
        DataCollector.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("chwwwww111", this.getClass().getSimpleName() + "----onPause");
        DataCollector.onPause(this);
    }

    @Override
    protected void onStop() {
        AppForegroundStateManager.getInstance().onActivityNotVisible(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        DataCollector.onDestroy(this);
        DataCollector.printLogs();

        Log.d("chwwwww111", this.getClass().getSimpleName() + "----onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                WeLifecycleCallBacks.getInstance().touchAnyView(ev);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
