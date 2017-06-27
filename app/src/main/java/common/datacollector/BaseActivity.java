package common.datacollector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataCollector.onCreate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataCollector.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DataCollector.onPause(this);
    }

    @Override
    protected void onDestroy() {
        DataCollector.onDestroy(this);
        DataCollector.printLogs();
        super.onDestroy();
    }
}
