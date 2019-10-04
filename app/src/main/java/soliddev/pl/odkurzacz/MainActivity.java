package soliddev.pl.odkurzacz;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String BTN_STATE = "BTN_STATE";
    private Button btnStart;
    private Button btnStop;
    private Button btnClose;
    private Button btnBatterySettings;
    private Switch switchNightMode;
    private View generalLayout;
    private TextView titleLabel;
    private TextView downloadLatestLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnClose = (Button) findViewById(R.id.btnClose);
        btnBatterySettings = (Button) findViewById(R.id.btnBatterySettings);
        switchNightMode = (Switch) findViewById(R.id.switchNightMode);
        generalLayout = (View) findViewById(R.id.generalLayout);
        titleLabel = (TextView) findViewById(R.id.titleLabel);
        downloadLatestLabel = (TextView) findViewById(R.id.downloadLatestLabel);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnBatterySettings.setOnClickListener(this);
        switchNightMode.setOnCheckedChangeListener(this);
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnClose.setEnabled(true);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        boolean[] btnState = savedInstanceState.getBooleanArray(BTN_STATE);
        btnStart.setEnabled(btnState[0]);
        btnStop.setEnabled(btnState[1]);
        btnClose.setEnabled(btnState[2]);

        boolean nightMode = btnState[3];
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
            //stopping service
            stopService(new Intent(this, MusicService.class));
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
        } else if (view == btnClose) {
            //stopping service
            stopService(new Intent(this, MusicService.class));
            // close app
            moveTaskToBack(true);
            finish();
        } else if (view == btnBatterySettings) {
            startActivity(new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS));
            Toast.makeText(this,
                    "Wyświetl wszystkie aplikacje, znajdź Odkurzacz i wyłącz optymalizację baterii.",
                    Toast.LENGTH_LONG).show();
        }
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
