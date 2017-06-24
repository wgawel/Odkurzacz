package soliddev.pl.odkurzacz;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class MusicService extends Service {
    private MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int resId = R.raw.odkurzacz_4_16;
        player = MediaPlayer.create(this,
                resId);
        player.setLooping(true);

        //staring the player
        player.start();

        //we have some options for service
        return START_STICKY;
//        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopping the player when service is destroyed
        player.stop();
    }
}
