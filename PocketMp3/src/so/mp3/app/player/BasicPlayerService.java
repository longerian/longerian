package so.mp3.app.player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import so.mp3.app.IndexActivity;
import so.mp3.player.R;
import so.mp3.type.Tracker;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

abstract public class BasicPlayerService extends Service {
	
	public static final String TAG = "BasicPlayerService";
    public static final int STOPED = -1, PAUSED = 0, PLAYING = 1;
    protected MediaPlayer mediaPlayer;
    protected ArrayList<? extends Tracker> trackList;
    protected int status, currentTrackPosition;
    protected boolean taken;
    protected IBinder playerBinder;
    protected Handler mHandler = new Handler();
    protected PlayerListener playerListener;
    protected IndicatorListener indicatorListener;
    protected Notification notification;
    
    abstract protected int getNotificationId();
    abstract protected String getNotificationAction();
    abstract public void addTracks(ArrayList<? extends Tracker> tracks);
    abstract public void playTrack(int position);

    public interface PlayerListener {
    	public void onPlay(int position, Tracker track);
    	public void onPause(int position);
    	public void onStop(int position);
    	public void onProgress(int max, int current);
    	public void onError();
    }
    
    public interface IndicatorListener {
    	public void onPlay(int position);
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        currentTrackPosition = -1;
        setStatus(STOPED);
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer arg0) {
                if (currentTrackPosition == trackList.size()-1) {
                    stop();
                } else {
                    nextTrack();
                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return playerBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
    	Log.d(TAG, "service unbind, intent is: " + intent.getAction() + "/" + intent.getComponent());
        return super.onUnbind(intent);
    }
    
    @Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "service destroyed");
		release();
	}

	public void setPlayerListener(PlayerListener listener) {
		this.playerListener = listener;
	}

	public void setIndicatorListener(IndicatorListener indicatorListener) {
		this.indicatorListener = indicatorListener;
	}

	public void take() {
        taken = true;
    }

    protected void untake() {
        synchronized (this) {
            taken = false;
            notifyAll();
        }
    }

    public boolean isTaken() {
        return taken;
    }

    protected void setStatus(int s) {
        status = s;
    }

    public int getStatus() {
        return status;
    }

    public ArrayList<? extends Tracker> getTracklist() {
        return trackList;
    }

    public Tracker getTrack(int pos) {
        return trackList.get(pos);
    }

    public Tracker getCurrentTrack() {
        if (currentTrackPosition < 0) {
            return null;
        } else {
            return trackList.get(currentTrackPosition);
        }
    }

    public int getCurrentTrackPosition() {
        return currentTrackPosition;
    }

    public void removeTrack(int pos) {
        if (pos == currentTrackPosition) {
            stop();
        }
        if (pos < currentTrackPosition) {
            currentTrackPosition--;
        }
        trackList.remove(pos);
        untake();
    }

    public void clearTracklist() {
        if (status > STOPED) {
            stop();
        }
        trackList.clear();
        untake();
    }

    public void play() {
        switch (status) {
        case STOPED:
            if (!trackList.isEmpty()) {
                playTrack(0);
            }
        break;
        case PLAYING:
            pause();
        break;
        case PAUSED:
            start();
        break;
        }
        untake();
    }
    
    public void start() {
    	mediaPlayer.start();
        setStatus(PLAYING);
        startForeground(getNotificationId(), notification);
		if(playerListener != null) playerListener.onPlay(currentTrackPosition, trackList.get(currentTrackPosition));
		mHandler.post(mRunProgress);
		untake();
    }
    
    public void pause() {
        mediaPlayer.pause();
        setStatus(PAUSED);
        stopForeground(true);
        if(playerListener != null) playerListener.onPause(currentTrackPosition);
        mHandler.removeCallbacks(mRunProgress);
        untake();
    }

    public void stop() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        currentTrackPosition = -1;
        setStatus(STOPED);
        stopForeground(true);
        if(playerListener != null) playerListener.onStop(currentTrackPosition);
        mHandler.removeCallbacks(mRunProgress);
        untake();
    }
    
    public void release() {
    	stop();
    	mediaPlayer.release();
    }
    
    public boolean isPlaying() {
    	return mediaPlayer.isPlaying();
    }
    
    public boolean isActive() {
    	return status > STOPED;
    }

    public void nextTrack() {
    	Log.d(TAG, "next: " + (currentTrackPosition + 1));
        if (currentTrackPosition < trackList.size()-1) {
            playTrack(currentTrackPosition+1);
        }
    }

    public void prevTrack() {
        if (currentTrackPosition > 0) {
            playTrack(currentTrackPosition-1);
        }
    }

    public int getCurrentTrackProgress() {
        if (status > STOPED) {
            return mediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    public int getCurrentTrackDuration() {
        if (status > STOPED) {
            return trackList.get(currentTrackPosition).getDuration();
        } else {
            return 0;
        }
    }

    public void seekTrack(int p) {
        if (status > STOPED) {
            mediaPlayer.seekTo(p);
            untake();
        }
    }
    
    protected Runnable mRunProgress = new Runnable() {
		
		@Override
		public void run() {
			if(playerListener != null && mediaPlayer.isPlaying()) {
				playerListener.onProgress(mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition());
			}
			mHandler.postDelayed(mRunProgress, 1000);
		}
	};
    
}
