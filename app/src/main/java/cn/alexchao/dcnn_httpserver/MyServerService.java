package cn.alexchao.dcnn_httpserver;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

public class MyServerService extends Service {
    private static final String TAG = "MyServerService";
    private static final int ID = 110;
    private MyServer mServer;
    private boolean mIsRunning = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mIsRunning == false) {
            Log.d(TAG, "Start Service ...");
            this.mIsRunning = true;
        } else {
            Log.d(TAG, "The Service is already running ...");
            return super.onStartCommand(intent, flags, startId);
        }
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
        Intent notificationIntent = new Intent(this, MainActivity.class);

        builder.setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent, 0))
                .setContentTitle("HTTP Server")    // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.ic_launcher)    // 设置状态栏内的小图标
                .setContentText("HTTP Server is running")          // 设置上下文内容
                .setWhen(System.currentTimeMillis());  // 设置该通知发生的时间

        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音

        startForeground(ID, notification);
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
            stopForeground(true);
            this.mIsRunning = false;
            Log.d(TAG, "Stop Server");
        }
    }
}
