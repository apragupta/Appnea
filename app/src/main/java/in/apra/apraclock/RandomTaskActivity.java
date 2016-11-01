package in.apra.apraclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import in.apra.apraclock.tasks.TaskProgress;

public class RandomTaskActivity extends AppCompatActivity implements Chronometer.OnChronometerTickListener,TextWatcher {
    TaskProgress myTaskProgress;
    RatingBar rbProgress;
    TextView tvQuestion;
    EditText etAnswer;
    Chronometer chTicker;
    Button btnSkip;
    int allowSeconds=60;
    MediaPlayer happy_step;
    MediaPlayer happy_end;
    MediaPlayer wrong_step;
    MediaPlayer sad_end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_task);
        rbProgress=(RatingBar) findViewById(R.id.rbProgress);
        tvQuestion=(TextView) findViewById(R.id.tvQuestion);
        etAnswer = (EditText) findViewById(R.id.etAnswer) ;
        chTicker = (Chronometer) findViewById(R.id.chTicker);
        btnSkip = (Button)findViewById(R.id.btnSkip);

        happy_step= MediaPlayer.create(this,R.raw.happy_step);
        happy_end= MediaPlayer.create(this,R.raw.happy_end);
        wrong_step= MediaPlayer.create(this,R.raw.failed);
        sad_end= MediaPlayer.create(this,R.raw.sad_end);

        chTicker.setOnChronometerTickListener(this);
        etAnswer.addTextChangedListener(this);
        init();
    }
    void init(){
        myTaskProgress = new TaskProgress(5);
        rbProgress.setNumStars(myTaskProgress.getGoal());
        //rbProgress.getProgressDrawable().setTint(Color.RED);
        moveToNext();
        //set it to tick for next "allowSeconds" duration
        chTicker.setBase(SystemClock.elapsedRealtime()+allowSeconds*1000);

        chTicker.start();
        btnSkip.setEnabled(true);

    }

    private void moveToNext() {
        tvQuestion.setText(myTaskProgress.ask());
        etAnswer.setText("");
        etAnswer.setTextColor(Color.BLUE);
    }
    private void done(boolean passed)
    {
        chTicker.stop();
        btnSkip.setEnabled(false);
        if(passed)
        {
            happy_end.start();
            rbProgress.getProgressDrawable().setTint(Color.GREEN);
            passed();
        }
        else
        {
            sad_end.start();
            failed();

        }
    }

    public void skip(View v){
        wrong_step.start();
        moveToNext();
    }

    /*public void check(View v){
        afterTextChanged(etAnswer.getText());
    }*/

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length()==0) return;
        try {
            check(Integer.parseInt(s.toString()));
        }
        catch(NumberFormatException nex)
        {
            return;
        }
    }
    private void check(int num){
        boolean result= myTaskProgress.check(num);
        if(result){
            rbProgress.setRating(myTaskProgress.getCorrects());
            if(myTaskProgress.isDone())
            {
                done(true);
            }
            else {
                happy_step.start();
                moveToNext();
            }
        }
        else{
            //wrong_step.start();
            etAnswer.setTextColor(Color.RED);
        }
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        if(SystemClock.elapsedRealtime()>=chronometer.getBase())
        {
            done(false);
        }
    }

    private void failed() {
        Toast.makeText(this, "Keep trying...",Toast.LENGTH_LONG).show();
        init();
    }

    private void passed() {
        Toast.makeText(this, "Not posting to facebook! Enjoy your day",Toast.LENGTH_LONG).show();

        cancelPendingInsult();

        Intent goToNextActivity = new Intent(getApplicationContext(), AlarmActivity.class);
        startActivity(goToNextActivity);
    }

    private void cancelPendingInsult() {
        Intent myIntent = new Intent(this, InsultSender.class);
        PendingIntent p=PendingIntent.getBroadcast(this, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(p);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}


}
