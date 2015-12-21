package com.example.lli5.sqlitestress;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAGS = "SQLiteStress";
    private static final String sConf = "stress.conf";
    private int nThreads = 0;
    private int nDelay = 0;
    private ArrayList<SQLiteStressThread> threads = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readConf();
        startStress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopStress();
    }

    public void stopStress() {
        if (threads == null)
            return;

        Log.i(TAGS, "stopStress");

        for(int i=0; i<threads.size(); i++) {
            threads.get(i).running = false;
        }
        threads = null;
    }

    public void startStress() {
        if (threads != null) {
            stopStress();
        }

        if (nThreads == 0) {
            return;
        }

        Log.i(TAGS, "startStress");

        threads = new ArrayList<SQLiteStressThread>(nThreads);
        for(int i=0; i<nThreads; i++) {
            SQLiteStressThread thread = new SQLiteStressThread(this, i, (i+1)*nDelay);
            threads.add(thread);
            thread.start();
        }
    }

    public void readConf() {
        int n = 0;
        int delay = 0;

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("threads")) {
                n = Integer.valueOf(extras.getString("threads"));
                Log.d(TAGS, "Intent configuration threads=" + n);
            }

            if (extras.containsKey("delay")) {
                delay = Integer.valueOf(extras.getString("delay"));
                Log.d(TAGS, "Intent configuration delay=" + delay + " ms");
            }
        }

        if (n < 0 || n > 255) {
            Log.d(TAGS, "Invalid threads number: " + n + ", reset thread=0");
            n = 0;
        }

        if (delay < 0 || delay > 10000) {
            Log.d(TAGS, "Invalid delay (ms): " + delay + ", reset delay=0");
            delay = 0;
        }

        nThreads = n;
        nDelay = delay;
    }
}
