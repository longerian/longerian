package com.tencent.phonemgr;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.tencent.phonemgr.utils.bitmap.AppIconWorker;
import com.tencent.phonemgr.utils.bitmap.ImageCache;
import com.tencent.phonemgr.utils.bitmap.ImageCache.ImageCacheParams;
import com.tencent.phonemgr.utils.bitmap.ImageWorker.ImageWorkerAdapter;
import com.tencent.phonemgr.utils.bitmap.Utils;

public class AppManagerActivity extends SherlockFragmentActivity {

	private GridView shortcuts;
	private ProgressBar progress;
	private AppLoaderTask loaderTask;
	private AppIconWorker iconWorker;
	private ImageWorkerAdapter iconWorkerAdapter = new ImageWorkerAdapter() {
        
		@Override
		public Object getItem(int num) {
			return shortcuts.getItemAtPosition(num);
		}

		@Override
		public int getSize() {
			return shortcuts.getCount();
		}
		
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImageCacheParams cacheParams = new ImageCacheParams();
        cacheParams.memCacheSize = 1024 * 1024 * Utils.getMemoryClass(this) / 3;
        iconWorker = new AppIconWorker(getApplicationContext(), 40);
        iconWorker.setAdapter(iconWorkerAdapter);
        iconWorker.setImageCache(ImageCache.findOrCreateCache(this, cacheParams));
        
		setContentView(R.layout.activity_shortcut_grid);
		shortcuts = (GridView) findViewById(R.id.shortcuts);
		progress = (ProgressBar) findViewById(R.id.progress);
		loaderTask = new AppLoaderTask();
		loaderTask.execute();
	}
	
	@Override
    public void onResume() {
        super.onResume();
        iconWorker.setExitTasksEarly(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        iconWorker.setExitTasksEarly(true);
    }
    
	@Override
	protected void onStop() {
		super.onStop();
		if(loaderTask != null) {
			loaderTask.cancel(true);
		}
	}

	private class AppLoaderTask extends AsyncTask<Void, Void, List<ResolveInfo>> {

		private List<ResolveInfo> appPackages;
		
		@Override
		protected void onPreExecute() {
			progress.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected List<ResolveInfo> doInBackground(Void... params) {
			Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
	        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	        appPackages = getPackageManager().queryIntentActivities(mainIntent, 0);
			return appPackages;
		}

		@Override
		protected void onPostExecute(List<ResolveInfo> result) {
			progress.setVisibility(View.INVISIBLE);
			shortcuts.setAdapter(new AppsAdapter(getApplicationContext(), R.layout.app_item_in_grid, result));
			shortcuts.setOnItemClickListener(mOnIconClickListener);
		}
		
		private OnItemClickListener mOnIconClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View parent, int position,
					long id) {
				lauchApplication(appPackages.get(position).activityInfo.packageName,
						appPackages.get(position).activityInfo.name);
			}

		};
		
	}
	
	private class AppsAdapter extends ArrayAdapter<ResolveInfo> {
		
		private List<ResolveInfo> nonSystemPackages;
		
        public AppsAdapter(Context context, int resource,
				List<ResolveInfo> resolveInfo) {
        	super(context, resource, resolveInfo);
        	this.nonSystemPackages = resolveInfo;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
        	ViewHolder viewHolder;
            if (convertView == null) {
            	convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.app_item_in_grid, null);
            	viewHolder = new ViewHolder(convertView);
				convertView.setTag(viewHolder);
            }
            viewHolder = (ViewHolder) convertView.getTag();
            ResolveInfo info = nonSystemPackages.get(position);
            iconWorker.loadImage(info, viewHolder.getLogo());
            return convertView;
        }

        private class ViewHolder {
			
			private View base;
			private ImageView logo;
			
			public ViewHolder(View base) {
				super();
				this.base = base;
			}
			
			public ImageView getLogo() {
				if(logo == null) {
					logo = (ImageView) base.findViewById(R.id.app_logo);
				}
				return logo;
			}
			
		}
    }
	
	public void lauchApplication(String packageName, String clsName) {
		ComponentName comp = new ComponentName(packageName, clsName);
		Intent appIntent = new Intent();
		appIntent.setComponent(comp);
		startActivity(appIntent);
	}
	
}
