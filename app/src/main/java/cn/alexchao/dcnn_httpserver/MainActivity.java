package cn.alexchao.dcnn_httpserver;

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


    private MyServer server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        String ipStr = "Local IP: " + Util.getLocalIpStr(this);
        ((TextView) findViewById(R.id.local_ip_tv)).setText(ipStr);

        Log.d("MainActivity", Environment.getExternalStorageDirectory().toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        LinkedList<File> fileList = new LinkedList<>();
        fileList.add(new File(Util.ROOT_PATH + "/multicnn/netfiles/AlexNet_def.txt"));
        try {
            server = new MyServer(fileList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(server != null) {
            server.stop();
        }
    }


}