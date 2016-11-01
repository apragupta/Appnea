package in.apra.apraclock;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;

import in.apra.apraclock.tasks.TaskProgress;

public class RandomTaskActivity extends AppCompatActivity {
    TaskProgress myTaskProgress;
    RatingBar rbProgress;
    TextView tvQuestion;
    EditText etAnswer;
    Chronometer chTicker;
    int allowSeconds=60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myTaskProgress = new TaskProgress(5);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_task);
        rbProgress=(RatingBar) findViewById(R.id.rbProgress);
        tvQuestion=(TextView) findViewById(R.id.tvQuestion);
        etAnswer = (EditText) findViewById(R.id.etAnswer) ;
        chTicker = (Chronometer) findViewById(R.id.chTicker) ;
        init();
    }
    void init(){
        rbProgress.setNumStars(myTaskProgress.getGoal());
        moveToNext();
        //set it to tick for next "allowSeconds" duration
        chTicker.setBase(SystemClock.elapsedRealtime()+allowSeconds*1000);
        chTicker.start();
    }

    private void moveToNext() {
        tvQuestion.setText(myTaskProgress.ask());
        etAnswer.setText("");
        etAnswer.setTextColor(Color.BLUE);
        rbProgress.setRating(myTaskProgress.getCorrects());

    }

    public void check(View v){
        boolean result= myTaskProgress.check(Integer.parseInt(etAnswer.getText().toString()));
        if(result){
            moveToNext();
        }
        else{
            etAnswer.setTextColor(Color.RED);
        }
    }
}
