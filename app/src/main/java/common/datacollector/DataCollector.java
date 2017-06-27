package common.datacollector;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Stack;

/**
 * Created by ChaiHongwei on 2017/6/26 13:55.
 */

public class DataCollector {
    private static final String TAG = DataCollector.class.getSimpleName();
    private static Stack<LogData> sLogs = new Stack<>();

    private static Application sApplication;
    private static LogThread sLogThread;


    public static void start(Application application) {
        sApplication = application;
        sLogThread = new LogThread();
        sLogThread.start();
    }

    public static void onCreate(Activity activity) {
        LogData logData = new LogData();
        logData.pageName = activity.getPackageName() + "." + activity.getLocalClassName();
        logData.eventId = "onCreate";

        pushLog(logData);
    }

    public static void onResume(Activity activity) {
        LogData logData = new LogData();
        logData.pageName = activity.getPackageName() + "." + activity.getLocalClassName();
        logData.eventId = "onResume";

        pushLog(logData);
    }

    public static void onPause(Activity activity) {
        LogData logData = new LogData();
        logData.pageName = activity.getPackageName() + "." + activity.getLocalClassName();
        logData.eventId = "onPause";

        pushLog(logData);
    }

    public static void onDestroy(Activity activity) {
        LogData logData = new LogData();
        logData.pageName = activity.getPackageName() + "." + activity.getLocalClassName();
        logData.eventId = "onDestroy";

        pushLog(logData);
    }

    public static void onClick(Activity activity, View view) {
        LogData logData = new LogData();
        logData.pageName = String.format("page:%s,viewId:%s,viewidName:%s",
                activity.getPackageName() + "." + activity.getLocalClassName()
                , view.getId() + "", activity.getResources().getResourceName(view.getId()));

        logData.eventId = "onClick";

        pushLog(logData);
    }

    public static void pushLog(LogData logData) {
        logData.logTime = System.currentTimeMillis();
        sLogs.push(logData);

        Message message = Message.obtain();
        message.obj = logData;
        sLogThread.getHandler().sendMessage(message);
    }

    public static void printLogs() {
        while (sLogs.size() > 0) {
            Log.d(TAG, sLogs.pop().toString());
        }
    }

    private static void saveToFile() {
        /**
         * fdsjalfkjsdakl
         */
    }

    private static class LogThread extends Thread {
        private Handler mHandler;
        private final Object mSync = new Object();

        public void run() {
            Looper.prepare();
            synchronized (mSync) {
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        BufferedWriter out = null;
                        try {
                            File logFile = createFile(sApplication, "aa111", "hbb.log");
                            out = new BufferedWriter(new OutputStreamWriter(
                                    new FileOutputStream(logFile, true)));
                            out.write(msg.obj.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                mSync.notifyAll();
            }
            Looper.loop();
        }

        public Handler getHandler() {
            synchronized (mSync) {
                if (mHandler == null) {
                    try {
                        mSync.wait();
                    } catch (InterruptedException e) {
                    }
                }
                return mHandler;
            }
        }

        public void exit() {
            getHandler().post(new Runnable() {
                public void run() {
                    Looper.myLooper().quit();
                }
            });
        }
    }


    public static File createFile(Context context, String filePath, String fileName) {
        String state = Environment.getExternalStorageState();
        File file;
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            file = context.getExternalCacheDir();
        } else {
            file = context.getCacheDir();
        }

        File newFile = new File(file.getAbsolutePath() + filePath);
        if (!newFile.exists()) {
            newFile.mkdirs();
        }

        File tmpFile = new File(newFile, fileName);
        return tmpFile;
    }
}
