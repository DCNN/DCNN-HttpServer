package cn.alexchao.dcnn_httpserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

public class MyServerService extends Service {
    private static final String TAG = "MyServerService";
    private MyServer mServer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "The Service is running ...");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * No Binding Accepted
     *
     * @param intent intent
     * @return null
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        try {
            mServer = new MyServer();
            Log.d(TAG, "The HTTP Server is running ...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Shutdown Service");
        if (mServer != null) {
            mServer.stop();
            Log.d(TAG, "Stop Server");
        }
    }
}
