package cn.alexchao.dcnn_httpserver;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import static cn.alexchao.dcnn_httpserver.Util.verifyStoragePermissions;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private MyServer server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        String ipStr = "Local IP: " + Util.getLocalIpStr(this);
        ((TextView) findViewById(R.id.local_ip_tv)).setText(ipStr);

        Log.d(TAG, Util.getRootPath());

        startServer();
    }

    private void startServer() {
        Intent intent = new Intent(this, MyServerService.class);
        startService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}