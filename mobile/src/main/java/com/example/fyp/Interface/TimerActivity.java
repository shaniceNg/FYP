package com.example.fyp.Interface;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fyp.ActivityInsert;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimerActivity extends AppCompatActivity {
    private Chronometer chronometer;
    private boolean running,pressed;
    private long pauseOffset;
    private FirebaseAuth firebaseAuth;
    Calendar calForDate;
    String usern,saveCurrentDate,saveCurrentTime,duration,activity,others;
    ImageButton playbtn;
    DatabaseReference reff;
    ActivityInsert activityInsert;
    EditText actname;
    ImageView imageAct;
    Button back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        playbtn= (ImageButton)findViewById(R.id.playbtn);
        actname = findViewById(R.id.etActivity);
        actname.setVisibility(View.GONE);
        activity = getIntent().getStringExtra("NAME");
        imageAct =findViewById(R.id.ivActivity);

        int image = getIntent().getIntExtra("image",R.drawable.ic_icon_awesome_walking);
        imageAct.setImageResource(image);

        if(activity.matches("Others")){
            actname.setVisibility(View.VISIBLE);
        }

        back =findViewById(R.id.backbutton2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        usern = firebaseAuth.getInstance().getCurrentUser().getUid();
        calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("ddMMyyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());


        playbtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        chronometer=findViewById(R.id.chronometer);


        activityInsert = new ActivityInsert();
        reff= FirebaseDatabase.getInstance().getReference().child("Activity Tracker").child(usern).child(saveCurrentDate);

    }

    public void StartPauseChronometer(View v){
        if(!running){
            chronometer.setBase(SystemClock.elapsedRealtime()- pauseOffset);

            chronometer.start();
            running = true;
            playbtn.setImageResource(R.drawable.ic_pause_black_24dp);
        }
        else if(running){
            chronometer.stop();
            pauseOffset=SystemClock.elapsedRealtime()-chronometer.getBase();
            running=false;
            playbtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }


    }

    public void pauseChronometer(View v){
        if(running){
            chronometer.stop();
            pauseOffset=SystemClock.elapsedRealtime()-chronometer.getBase();
            running=false;
        }

    }
    public void resetChronometer(View v){

        if (getIntent().getStringExtra("NAME").matches("Others")) {

            activity = actname.getText().toString();

            if (activity.matches("")) {
                activity = null;
            } else {
                activity = actname.getText().toString();
            }
        }

        duration = getSecondsFromDurationString(String.valueOf(chronometer.getText()));

        if(activity!=null && duration!=null) {

            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset=0;
            pauseChronometer(v);
            playbtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);

            SimpleDateFormat currentTime = new SimpleDateFormat("KK:mm:ss a");
            saveCurrentTime = currentTime.format(calForDate.getTime());

            activityInsert.setActivity(activity);
            activityInsert.setcDuration(duration);
            activityInsert.setcTime(saveCurrentTime);
            reff.push().setValue(activityInsert);
            Toast.makeText(TimerActivity.this, "Session has been recorded successfully", Toast.LENGTH_LONG).show();
            actname.setText("");
            super.onBackPressed();
        }
        else if(activity==null){
            Toast.makeText(TimerActivity.this, "Please enter activity name before ending session", Toast.LENGTH_LONG).show();
            //pauseChronometer(v);
        }
        else if (duration==null){
            Toast.makeText(TimerActivity.this, "Session duration is too short", Toast.LENGTH_LONG).show();
        }
    }

    public String getSecondsFromDurationString(String value){

        String [] parts = value.split(":");

        // Wrong format, no value for you.
        if(parts.length < 2 || parts.length > 3)
            return null;

        int seconds = 0, minutes = 0, hours = 0;

        if(parts.length == 2){
            seconds = Integer.parseInt(parts[1]);
            minutes = Integer.parseInt(parts[0]);
        }
        else if(parts.length == 3){
            seconds = Integer.parseInt(parts[2]);
            minutes = Integer.parseInt(parts[1]);
            hours = Integer.parseInt(parts[0]);
        }

        seconds = seconds + (minutes*60) + (hours*3600);
        if(seconds <5){
            return null;
        }
        else {
            return (String.valueOf(hours) + ":" + String.valueOf(minutes) + ":" + String.valueOf(seconds));
        }
    }
}