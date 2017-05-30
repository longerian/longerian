package cc.icefire.market.util;

import android.util.Log;
import cc.icefire.market.BuildConfig;

public class ILog {

	private ILog() {
		
	}
	
	public static void v(String tag, String msg) {
		if(BuildConfig.DEBUG) {
			Log.v(tag, msg);
		}
	}
	
	public static void d(String tag, String msg) {
		if(BuildConfig.DEBUG) {
			Log.d(tag, msg);
		}
	}
	
	public static void i(String tag, String msg) {
		if(BuildConfig.DEBUG) {
			Log.i(tag, msg);
		}
	}
	
	public static void w(String tag, String msg) {
		if(BuildConfig.DEBUG) {
			Log.w(tag, msg);
		}
	}
	
	public static void e(String tag, String msg) {
		if(BuildConfig.DEBUG) {
			Log.e(tag, msg);
		}
	}
	
}
