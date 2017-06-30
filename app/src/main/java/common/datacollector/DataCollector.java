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

import java.io.File;
import java.io.IOException;
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

        pushLog(logData, true);
    }

    public static void onResume(Activity activity) {
        LogData logData = new LogData();
        logData.pageName = activity.getPackageName() + "." + activity.getLocalClassName();
        logData.eventId = "onResume";

        pushLog(logData, true);
    }

    public static void onPause(Activity activity) {
        LogData logData = new LogData();
        logData.pageName = activity.getPackageName() + "." + activity.getLocalClassName();
        logData.eventId = "onPause";

        pushLog(logData, true);
    }

    public static void onDestroy(Activity activity) {
        LogData logData = new LogData();
        logData.pageName = activity.getPackageName() + "." + activity.getLocalClassName();
        logData.eventId = "onDestroy";

        pushLog(logData, true);
    }

    public static void onClick(Activity activity, View view) {
        LogData logData = new LogData();
        logData.pageName = String.format("page:%s,viewId:%s,viewidName:%s",
                activity.getPackageName() + "." + activity.getLocalClassName()
                , view.getId() + "", activity.getResources().getResourceName(view.getId()));

        logData.eventId = "onClick";

        pushLog(logData, true);
    }

    public static void onError(Throwable throwable) {
        LogData logData = new LogData();
        logData.pageName = throwable.getMessage();

        logData.eventId = "onClick";

        pushLog(logData, true);
    }

    public static void pushLog(LogData logData, boolean isAppend) {
        logData.logTime = System.currentTimeMillis();
        sLogs.push(logData);

        Message message = Message.obtain();
        message.obj = logData;
        message.arg1 = isAppend ? 1 : 2;
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
//                        BufferedWriter out = null;
//                        try {
//                            File logFile = createFile(sApplication, "aa111", "hbb", msg.arg1 == 1);
//
//                            out = new BufferedWriter(new OutputStreamWriter(
//                                    new FileOutputStream(logFile, true)));
//                            out.write(msg.obj.toString());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        } finally {
//                            try {
//                                out.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
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


    public static File createFile(Context context, String filePath, String fileName, boolean isAppend) {
        String state = Environment.getExternalStorageState();
        File file;
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            file = context.getExternalCacheDir();
        } else {
            file = context.getCacheDir();
        }

        File folder = new File(file.getAbsolutePath() + filePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        int newFileCount = 0;
        File newFile;
        File existingFile = null;

        newFile = new File(folder, String.format("%s_%s.log", fileName, newFileCount));
        while (newFile.exists()) {
            existingFile = newFile;
            newFileCount++;
            newFile = new File(folder, String.format("%s_%s.log", fileName, newFileCount));
        }

        if (isAppend) {
            try {
                existingFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return existingFile;
        } else {
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newFile;
        }
    }
}
