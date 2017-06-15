package com.clock.performance.tools.block;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.clock.performance.tools.R;
import com.clock.performance.tools.test.BlockLooper;

public class BlockSamplesActivity extends AppCompatActivity implements View.OnClickListener {

    private Object lockObj = new Object();

    private Handler uiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anr_samples);

        findViewById(R.id.btn_start_looper).setOnClickListener(this);
        findViewById(R.id.btn_stop_looper).setOnClickListener(this);
        findViewById(R.id.btn_ui_thread_create_anr).setOnClickListener(this);
        findViewById(R.id.btn_worker_thread_create_anr).setOnClickListener(this);
        findViewById(R.id.btn_send_broadcast).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btn_start_looper) {
            BlockLooper.getBlockLooper().start();

        } else if (viewId == R.id.btn_stop_looper) {
            BlockLooper.getBlockLooper().stop();

        } else if (viewId == R.id.btn_ui_thread_create_anr) {

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else if (viewId == R.id.btn_worker_thread_create_anr) {

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {
                    synchronized (lockObj) {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }
            }.execute();

            uiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    synchronized (lockObj) {
                        Toast.makeText(BlockSamplesActivity.this, "hello anr", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 1000);

        } else if (viewId == R.id.btn_send_broadcast){

            Intent intent = new Intent();
            intent.setAction("com.clock.performance.tools.block");
            sendBroadcast(intent);

        }
    }
}
