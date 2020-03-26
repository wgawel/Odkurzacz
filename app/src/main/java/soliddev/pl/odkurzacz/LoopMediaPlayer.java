package soliddev.pl.odkurzacz;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * from: https://stackoverflow.com/a/38487142
 * and https://stackoverflow.com/questions/6884590/android-how-to-create-fade-in-fade-out-sound-effects-for-any-music-file-that-my/20898962
 */

class LoopMediaPlayer {

    private static final String TAG = LoopMediaPlayer.class.getSimpleName();

    private Context mContext = null;
    private int mResId   = 0;
    private int mCounter = 1;

    private int iVolume;

    private final static int INT_VOLUME_MAX = 100;
    private final static int INT_VOLUME_MIN = 0;
    private final static float FLOAT_VOLUME_MAX = 1;
    private final static float FLOAT_VOLUME_MIN = 0;

    private MediaPlayer mCurrentPlayer = null;
    private MediaPlayer mNextPlayer    = null;

    public static LoopMediaPlayer create(Context context, int resId) {
        return new LoopMediaPlayer(context, resId);
    }

    private LoopMediaPlayer(Context context, int resId) {
        mContext = context;
        mResId   = resId;

        mCurrentPlayer = MediaPlayer.create(mContext, mResId);
        mCurrentPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mCurrentPlayer.start();
            }
        });
        createNextMediaPlayer();
    }

    private void createNextMediaPlayer() {
        mNextPlayer = MediaPlayer.create(mContext, mResId);
        mCurrentPlayer.setNextMediaPlayer(mNextPlayer);
        mCurrentPlayer.setOnCompletionListener(onCompletionListener);
    }

    private final MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.release();
            mCurrentPlayer = mNextPlayer;
            createNextMediaPlayer();
            Log.d(TAG, String.format("Loop #%d", ++mCounter));
        }
    };
    // code-read additions:
    public boolean isPlaying() throws IllegalStateException {
        return mCurrentPlayer.isPlaying();
    }

    public void setVolume(float leftVolume, float rightVolume) {
        mCurrentPlayer.setVolume(leftVolume, rightVolume);
    }

    public void start() throws IllegalStateException {
        mCurrentPlayer.start();
    }

    public void stop() throws IllegalStateException {
        mCurrentPlayer.stop();
    }

    public void pause() throws IllegalStateException {
        mCurrentPlayer.pause();
    }

    public void release() {
        mCurrentPlayer.release();
        mNextPlayer.release();
    }

    public void reset() {
        mCurrentPlayer.reset();
    }

    public void stop(int fadeDuration)
    {
        try {
            // Set current volume, depending on fade or not
            if (fadeDuration > 0)
                iVolume = INT_VOLUME_MAX;
            else
                iVolume = INT_VOLUME_MIN;

            updateVolume(0);

            // Start increasing volume in increments
            if (fadeDuration > 0)
            {
                final Timer timer = new Timer(true);
                TimerTask timerTask = new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        updateVolume(-1);
                        if (iVolume == INT_VOLUME_MIN)
                        {
                            // Pause music
                            mCurrentPlayer.stop();
                            timer.cancel();
                            timer.purge();
                        }
                    }
                };

                // calculate delay, cannot be zero, set to 1 if zero
                int delay = fadeDuration / INT_VOLUME_MAX;
                if (delay == 0)
                    delay = 1;

                timer.schedule(timerTask, delay, delay);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateVolume(int change) {
        // increment or decrement depending on type of fade
        iVolume = iVolume + change;

        // ensure iVolume within boundaries
        if (iVolume < INT_VOLUME_MIN)
            iVolume = INT_VOLUME_MIN;
        else if (iVolume > INT_VOLUME_MAX)
            iVolume = INT_VOLUME_MAX;

        // convert to float value
        float fVolume = 1 - ((float) Math.log(INT_VOLUME_MAX - iVolume) / (float) Math.log(INT_VOLUME_MAX));

        // ensure fVolume within boundaries
        if (fVolume < FLOAT_VOLUME_MIN)
            fVolume = FLOAT_VOLUME_MIN;
        else if (fVolume > FLOAT_VOLUME_MAX)
            fVolume = FLOAT_VOLUME_MAX;

        mCurrentPlayer.setVolume(fVolume, fVolume);
    }
}
