package common.datacollector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseActivity {

    private Button button2;
    private ScreenListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button2 = (Button) findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataCollector.onClick((Activity) view.getContext(),view);
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });

        listener = new ScreenListener(this);
        listener.register(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                Log.d("chwwwww111", "ScreenBroadcastReceiver --> ACTION_SCREEN_ON");
            }

            @Override
            public void onScreenOff() {
                Log.d("chwwwww111", "ScreenBroadcastReceiver --> ACTION_SCREEN_OFF");
            }

            @Override
            public void onUserPresent() {
                Log.d("chwwwww111", "ScreenBroadcastReceiver --> ACTION_USER_PRESENT");
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (listener != null) {
            listener.unregister();
        }

        super.onDestroy();
    }
}
