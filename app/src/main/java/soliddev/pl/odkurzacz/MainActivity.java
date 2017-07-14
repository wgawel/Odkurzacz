package soliddev.pl.odkurzacz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnStart;
    private Button btnStop;
    private Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnClose = (Button) findViewById(R.id.btnClose);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnClose.setEnabled(true);
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
        }
    }
}
