package com.example.mallapplication.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.Nullable;

import com.example.mallapplication.GroceryItem;
import com.example.mallapplication.Utils;

import java.io.Console;

public class TrackUserTime extends Service {

    private int seconds = 0;
    private boolean isFinish = false;
    private GroceryItem item;

    private IBinder binder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        TrackTime();
        return binder;
    }

    public class LocalBinder extends Binder {
        public TrackUserTime getService() {
            return TrackUserTime.this;
        }
    }

    private void TrackTime() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isFinish) {
                    try {
                        Thread.sleep(1000);
                        seconds++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public void setItem(GroceryItem item) {
        this.item = item;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFinish = true;
        int minutes = seconds / 60;
        if (minutes > 0) {
            if (null != item) {
                Utils.changeUserPoint(this, item, minutes);
                Log.d(null, "Adding " + minutes);
            }
        }
    }
}
