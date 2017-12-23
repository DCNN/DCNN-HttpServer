package cn.alexchao.dcnn_httpserver;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import static cn.alexchao.dcnn_httpserver.Util.verifyStoragePermissions;

public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        String ipStr = "Local IP: " + Util.getLocalIpStr(this);
        ((TextView) findViewById(R.id.local_ip_tv)).setText(ipStr);

        findViewById(R.id.start_server_btn).setOnClickListener(this);
        findViewById(R.id.stop_server_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.stop_server_btn) {
            stopService(new Intent(this, MyServerService.class));
        } else if (view.getId() == R.id.start_server_btn) {
            startService(new Intent(this, MyServerService.class));
        }
    }

    // kill the server process when exit the app
    @Override
    public void onDestroy() {
        stopService(new Intent(this, MyServerService.class));
        super.onDestroy();
    }
}