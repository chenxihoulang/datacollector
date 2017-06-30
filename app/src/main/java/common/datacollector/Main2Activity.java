package common.datacollector;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

public class Main2Activity extends BaseActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                try {
                //                    String string = null;
                //                    string.substring(1);
                //                } catch (Exception ex) {
                //                    DataCollector.onError(ex);
                //                }

                JSONObject jsonObject = new BuildCollector().collectBuild();

                Log.d("chwwww", jsonObject.toString());



                try{
                    //提交请求
                    //....
                    //处理响应结果
                }catch (Exception ex){
                    //出现异常,在这里捕获

                }
            }
        });
    }
}
