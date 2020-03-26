package soliddev.pl.odkurzacz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String BTN_STATE = "BTN_STATE";
    private Button btnStart;
    private Button btnStop;
    private Button btnDelayedStop;
    private Button btnDelayedStopDecrease;
    private Button btnDelayedStopIncrease;
    private Button btnClose;
    private Button btnBatterySettings;
    private Switch switchNightMode;
    private View generalLayout;
    private TextView titleLabel;
    private TextView downloadLatestLabel;
    private int delayedStopTimerDefaultValue = 15*60000;
    private int delayedStopTimer = delayedStopTimerDefaultValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnDelayedStop = (Button) findViewById(R.id.btnDelayedStop);
        btnDelayedStopDecrease = (Button) findViewById(R.id.btnDelayedStopDecrease);
        btnDelayedStopIncrease = (Button) findViewById(R.id.btnDelayedStopIncrease);
        btnClose = (Button) findViewById(R.id.btnClose);
        btnBatterySettings = (Button) findViewById(R.id.btnBatterySettings);
        switchNightMode = (Switch) findViewById(R.id.switchNightMode);
        generalLayout = (View) findViewById(R.id.generalLayout);
        titleLabel = (TextView) findViewById(R.id.titleLabel);
        downloadLatestLabel = (TextView) findViewById(R.id.downloadLatestLabel);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnDelayedStop.setOnClickListener(this);
        btnDelayedStopDecrease.setOnClickListener(this);
        btnDelayedStopIncrease.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnBatterySettings.setOnClickListener(this);
        switchNightMode.setOnCheckedChangeListener(this);
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnClose.setEnabled(true);

        delayedStopTimer = delayedStopTimerDefaultValue;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        boolean[] btnState = savedInstanceState.getBooleanArray(BTN_STATE);
        btnStart.setEnabled(btnState[0]);
        btnStop.setEnabled(btnState[1]);
        btnDelayedStop.setEnabled(btnState[2]);
        btnDelayedStopDecrease.setEnabled(btnState[3]);
        btnDelayedStopIncrease.setEnabled(btnState[4]);
        btnClose.setEnabled(btnState[5]);

        boolean nightMode = btnState[6];
        switchNightMode.setChecked(nightMode);
        if (nightMode) {
            switchToNightMode();
        } else {
            switchToNotNightMode();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        boolean[] btnState = {
                btnStart.isEnabled(),
                btnStop.isEnabled(),
                btnDelayedStop.isEnabled(),
                btnDelayedStopDecrease.isEnabled(),
                btnDelayedStopIncrease.isEnabled(),
                btnClose.isEnabled(),
                switchNightMode.isChecked()
        };
        outState.putBooleanArray(BTN_STATE, btnState);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onClick(View view) {
        if (view == btnStart) {
            //starting service
            startService(new Intent(this, MusicService.class));
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);
        } else if (view == btnStop) {
            stopAction();
        } else if (view == btnClose) {
            //stopping service
            stopService(new Intent(this, MusicService.class));
            // close app
            moveTaskToBack(true);
            finish();
        } else if (view == btnDelayedStop) {
            delayedStopAction();
        } else if (view == btnDelayedStopDecrease) {
            delayedStopTimer -= 60000;
        } else if (view == btnDelayedStopIncrease) {
            delayedStopTimer += 60000;
        } else if (view == btnBatterySettings) {
            startActivity(new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS));
            Toast.makeText(this,
                    "Wyświetl wszystkie aplikacje, znajdź Odkurzacz i wyłącz optymalizację baterii.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void stopAction() {
        //stopping service
        stopService(new Intent(this, MusicService.class));
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
    }

    private void delayedStopAction() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                delayedStopTimer -= 1000;

                int delayedStopTimerInSeconds = delayedStopTimer/1000;

                long minutes = TimeUnit.SECONDS.toMinutes(delayedStopTimerInSeconds);
                long seconds = TimeUnit.SECONDS.toSeconds(delayedStopTimerInSeconds) - (TimeUnit.SECONDS.toMinutes(delayedStopTimerInSeconds) *60);

                String label = "Wyłączenie za "+String.format("%02d:%02d", minutes, seconds);

                btnDelayedStop.setText(label);

                if ( delayedStopTimer <= 0 ) {
                    stopAction();
                    delayedStopTimer = delayedStopTimerDefaultValue;
                } else {
                    delayedStopAction();
                }
            }
        }, 1000);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switchToNightMode();
        } else {
            switchToNotNightMode();
        }
    }

    private void switchToNightMode() {
        generalLayout.setBackgroundColor(getResources().getColor(R.color.colorBackgroundNightMode));
        switchNightMode.setTextColor(getResources().getColor(R.color.colorControllerTextNightMode));
        titleLabel.setTextColor(getResources().getColor(R.color.colorControllerTextNightMode));
        downloadLatestLabel.setTextColor(getResources().getColor(R.color.colorControllerTextNightMode));
    }

    private void switchToNotNightMode() {
        generalLayout.setBackgroundColor(getResources().getColor(R.color.colorBackgroundNotNightMode));
        switchNightMode.setTextColor(getResources().getColor(R.color.colorControllerTextNotNightMode));
        titleLabel.setTextColor(getResources().getColor(R.color.colorControllerTextNotNightMode));
        downloadLatestLabel.setTextColor(getResources().getColor(R.color.colorControllerTextNotNightMode));
    }
}
