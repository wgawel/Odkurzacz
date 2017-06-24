package soliddev.pl.odkurzacz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnStart;
    private Button btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
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
        }
    }
}
