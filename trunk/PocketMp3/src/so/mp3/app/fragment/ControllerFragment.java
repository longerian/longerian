package so.mp3.app.fragment;

import so.mp3.app.Host;
import so.mp3.app.MusicPlayer;
import so.mp3.app.downloader.DownloadService;
import so.mp3.http.SougouClient;
import so.mp3.http.parser.DownloadLinkParser;
import so.mp3.http.request.DownloadLinkRequest;
import so.mp3.http.response.DownloadLinkResponse;
import so.mp3.player.R;
import so.mp3.type.Mp3;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class ControllerFragment extends SherlockFragment {

	private Host host;
	
	private MusicPlayer mp;
	
	private DownloadLinkTask downloadLinkTask;
	private PlayTask playTask;
	
	private TextView title;
	private ImageButton playOrPause;
	private ImageButton download;
	private SeekBar progress;
	
	private Mp3 currentMp3;
	
	private long lastUpdate = 0;
	
	private long enqueue;
    private DownloadManager dm;
    
    private AudioManager am; 
    
    public static ControllerFragment newInstance() {
    	ControllerFragment f = new ControllerFragment();
        return f;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        	host = (Host) activity;
        } catch (ClassCastException e) {
        	throw new ClassCastException(activity.toString() + " must implement Host");
        }
    }
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mp = new MusicPlayer();
		mp.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				complete();
			}
		});
		mp.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Toast.makeText(getActivity(), R.string.can_not_play_the_song, Toast.LENGTH_LONG).show();
				return true;
			}
		});
		mp.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
			
			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				if(mp.isPlaying()) {
					long now = System.currentTimeMillis();
					if(now - lastUpdate > 500) {
						updateMusicProgress(progress, mp.getCurrentPosition(), percent, mp.getDuration());
						lastUpdate = now;
					}
				}
			}
		});
	    dm = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
//	    getActivity().registerReceiver(downloadCompletionReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	    am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
	}
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.controller_layout, null);
    	title = (TextView) view.findViewById(R.id.title);
    	playOrPause = (ImageButton) view.findViewById(R.id.play_or_pause);
    	playOrPause.setEnabled(false);
        playOrPause.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mp.isPlaying()) {
					pause();
				} else {
					start();
				}
			}
		});
        progress = (SeekBar) view.findViewById(R.id.progress);
        progress.setEnabled(false);
        download = (ImageButton) view.findViewById(R.id.download);
        download.setEnabled(false);
        download.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(currentMp3 != null && currentMp3.getMp3Link() != null) {
					download(currentMp3);
				}
			}
		});
		return view;
	}
    
    @Override
	public void onDestroyView() {
		super.onDestroyView();
		if(downloadLinkTask != null) {
			downloadLinkTask.cancel(true);
		}
		if(playTask != null) {
			playTask.cancel(true);
		}
	}
    
    @Override
	public void onDestroy() {
		super.onDestroy();
		mp.release();
//		getActivity().unregisterReceiver(downloadCompletionReceiver);
		am.abandonAudioFocus(afChangeListener);
	}
	
	private class DownloadLinkTask extends AsyncTask<String, Void, DownloadLinkResponse> {

		@Override
		protected void onPreExecute() {
			host.showIndeterminateProgressBar();
		}

		@Override
		protected DownloadLinkResponse doInBackground(String... params) {
			SougouClient sc = new SougouClient();
			DownloadLinkRequest dlr = new DownloadLinkRequest();
			dlr.setLink(params[0]);
			DownloadLinkResponse resp = (DownloadLinkResponse) sc.excute(dlr, new DownloadLinkParser());
			return resp;
		}
		
		@Override
		protected void onPostExecute(DownloadLinkResponse result) {
			host.hideIndeterminateProgressBar();
			if(result.isNetworkException()) {
				Toast.makeText(getActivity(), R.string.network_wonky, Toast.LENGTH_LONG).show();
			} else {
				if(TextUtils.isEmpty(result.getLink())) {
					Toast.makeText(getActivity(), R.string.can_not_find_the_song, Toast.LENGTH_LONG).show();
				} else {
					prepareDownloadAndPlay(result.getLink());
				}
			}
		}
		
	}
	
	private class PlayTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			host.showIndeterminateProgressBar();
		}

		@Override
		protected Void doInBackground(String... params) {
			mp.play(params[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(mp.isPlaying()) {
				playOrPause.setImageResource(R.drawable.av_pause);
				playOrPause.setEnabled(true);
				progress.setEnabled(true);
			} else {
				Toast.makeText(getActivity(), R.string.can_not_play_the_song, Toast.LENGTH_LONG).show();
			}
			host.hideIndeterminateProgressBar();
		}
		
	}
	 
	public void handleNewMp3(Mp3 music) {
		if(mp.isPlaying()) {
			stop();
			download.setEnabled(false);
		}
		int result = requestAudioFocus();
		if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
			currentMp3 = music;
			title.setText(music.getTitle() + " - " + music.getSinger());
			if(TextUtils.isEmpty(music.getMp3Link())) {
				downloadLinkTask = new DownloadLinkTask();
				downloadLinkTask.execute(music.getPlayerLink());
			} else {
				prepareDownloadAndPlay(music.getMp3Link());
			}
		}
	}
	
	private void prepareDownloadAndPlay(String link) {
		currentMp3.setMp3Link(link);
		playOrPause.setImageResource(R.drawable.av_play);
		playOrPause.setEnabled(false);
		progress.setEnabled(false);
		download.setEnabled(true);
		play(link);
	}
	
	private void play(String link) {
		playTask = new PlayTask();
		playTask.execute(link);
	}
	
	private void start() {
		playOrPause.setImageResource(R.drawable.av_pause);
		mp.start();
	}
	
	private void pause() {
		playOrPause.setImageResource(R.drawable.av_play);
		mp.pause();
	}
	
	private void stop() {
		playOrPause.setImageResource(R.drawable.av_play);
		progress.setProgress(0);
		playOrPause.setEnabled(false);
		progress.setEnabled(false);
		mp.stop();
	}
	
	private void complete() {
		playOrPause.setImageResource(R.drawable.av_play);
		progress.setProgress(0);
	}
	
	private void download(Mp3 mp3) {
	    getActivity().startService(new Intent(getActivity(), DownloadService.class)
        	.putExtra(DownloadService.TARGET_URL, mp3.getMp3Link())
        	.putExtra(DownloadService.TARGET_NAME, mp3.getTitle() + "-" + mp3.getSinger() + ".mp3"));
	}
	
	private void updateMusicProgress(SeekBar progress, int current, int bufferPercent, int duration) {
    	progress.setSecondaryProgress(bufferPercent);
        int currentPercent = progress.getMax() * current / duration;
        progress.setProgress(currentPercent);
    }

	private int requestAudioFocus() {
		int result = am.requestAudioFocus(afChangeListener,
		                                 AudioManager.STREAM_MUSIC,
		                                 AudioManager.AUDIOFOCUS_GAIN);
		return result;
	}
	
	private OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {
	    public void onAudioFocusChange(int focusChange) {
	        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
	        	pause();
	        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
	        	start();
	        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
	            am.abandonAudioFocus(afChangeListener);
	            stop();
	        }
	    }
	};
	
}
