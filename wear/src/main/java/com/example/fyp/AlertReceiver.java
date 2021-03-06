package com.example.fyp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class AlertReceiver extends BroadcastReceiver {
    private Calendar currentTime;
    private static final String Current_Steps_Now = "CurrentStepsCount";
    private static final String Initial_Count_Key = "FootStepInitialCount";
    private SharedPreferences prefs;
    @Override
    public void onReceive(Context context, Intent intent) {

            prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(Initial_Count_Key, prefs.getInt(Current_Steps_Now, -1));
            editor.putInt("getMaxcurrentHeartRate", 0);
            editor.commit();

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
        Toast.makeText(context, "This is a new day!", Toast.LENGTH_SHORT).show();
    }
}