package com.example.lli5.sqlitestress;

import android.content.Context;
import android.util.Log;

/**
 * Created by lli5 on 11/18/15.
 */
public class SQLiteStressThread extends Thread {
    private static final String TAGS = "SQLiteStress";
    private final Context context;
    private final int id;
    private final int interval;
    public boolean running;

    public SQLiteStressThread(Context context, int id, int interval) {
        this.context = context;
        this.id = id;
        this.interval = interval;
        running = false;
    }

    @Override
    public void run() {
        running = true;
        SQLiteStressHelper db = new SQLiteStressHelper(context, id);
        db.openDatabase(false);
        int count = db.getCount();
        db.closeDatabase();

        db.openDatabase(true);
        while (running) {
            db.setCount(++count);
            try {
                sleep(interval);
            } catch (Exception e) {
                Log.w(TAGS, "#" + id + " failed to sleep for + " + interval + " ms");
            }
        }
        db.closeDatabase();

        db.openDatabase(false);
        count = db.getCount();
        db.closeDatabase();
        Log.i(TAGS, "#" + id + " stopped at " + count);
    }
}
