package in.apra.apraclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class OpeningScreen extends AppCompatActivity {

    Button btnSleepnow;
    Button BtnSleep;
    Button BtnCustomAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);
        btnSleepnow = (Button) findViewById(R.id.btnSleepnow);
        BtnSleep = (Button) findViewById(R.id.BtnSleep);
        BtnCustomAlarm = (Button) findViewById(R.id.BtnCustomAlarm);
    }

    public void changeScreen(View view) {
        Log.d("OpeningScreen"  , "change screen clicked");
        Intent intent = new Intent(this, AlarmActivity.class);
        startActivity(intent);

    }
}

