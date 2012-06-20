package so.mp3.app;

import java.util.Observable;
import java.util.Observer;

import so.mp3.http.SougouClient;
import so.mp3.http.parser.DownloadLinkParser;
import so.mp3.http.parser.SearchParser;
import so.mp3.http.request.DownloadLinkRequest;
import so.mp3.http.request.SearchRequest;
import so.mp3.http.response.SearchResponse;
import so.mp3.player.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

public class Mp3playerActivity extends Activity implements Observer {
    /** Called when the activity is first created. */
	private MusicPlayer mp;
	private SeekBar progress;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        progress = (SeekBar) findViewById(R.id.progress);
        mp = new MusicPlayer();
        mp.addObserver(this);
        
        test();
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mp.release();
	}

	public void clickHandler(View v) {
    	switch(v.getId()) {
    	case R.id.play:
    		mp.play("http://slides.sitewelder.com/users/edwinwilliams1357/audio/adele%20-%20chasing%20pavements.mp3");
    		break;
    	case R.id.stop:
    		mp.stop();
    		break;
    	case R.id.pause:
    		if(mp.isPlaying()) {
    			mp.pause();
    		} else {
    			mp.start();
    		}
    		break;
    	}
    }
    
    private void resetProgress(SeekBar progress) {
    	progress.setProgress(0);
    }
    
    private void updateProgress(SeekBar progress, int current, int bufferPercent, int duration) {
    	progress.setSecondaryProgress(bufferPercent);
        int currentPercent = progress.getMax() * current / duration;
        progress.setProgress(currentPercent);
    }

	@Override
	public void update(Observable observable, Object data) {
		Bundle b = (Bundle) data;
		updateProgress(progress, b.getInt("current"), b.getInt("bufferPercent"), b.getInt("duration"));
	}
	 
    private void test() {
		new Thread() {

			@Override
			public void run() {
				SearchRequest sr = new SearchRequest();
				sr.setQuery("王力宏");
				SougouClient sc = new SougouClient();
				SearchResponse resp = (SearchResponse) sc.excute(sr, new SearchParser());
				
				DownloadLinkRequest dlr = new DownloadLinkRequest();
				dlr.setLink(resp.getMp3s().get(0).getLink());
				sc.excute(dlr, new DownloadLinkParser());
				
			}
			
			
		}.start();
	}
    
}