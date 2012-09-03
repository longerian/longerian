package com.tencent.phonemgr.filetype;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

public class ImageFile implements FileItem {

	public static final String JPG_FILE = ".jpg";
	public static final String PNG_FILE = ".png";
	
	private File file;
	private BitmapDrawable thumbnail;
	
	public ImageFile(File file) {
		this.file = file;
	}

	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public Drawable getLogo(Context context) {
		if(thumbnail == null) {
			Options options = new Options();
			options.outHeight = 40;
			options.outWidth = 40;
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
			Bitmap thumbnailBitmap = Bitmap.createScaledBitmap(bitmap, 40, 40, true);
			bitmap.recycle();
			Log.d(file.getName(), thumbnailBitmap.getHeight() + "/" + thumbnailBitmap.getWidth());
			thumbnail = new BitmapDrawable(context.getResources(), thumbnailBitmap);
		}
		return thumbnail;
	}

	@Override
	public void open(Activity activity) {
	     Intent intent = new Intent(Intent.ACTION_VIEW);
	     intent.setDataAndType(Uri.fromFile(file), "image/*");
	     activity.startActivity(intent);     
	}

	@Override
	public void close() {
		if(thumbnail != null) {
			if(thumbnail.getBitmap() != null) {
				thumbnail.getBitmap().recycle();
			}
		}
	}

	@Override
	public void setOnLoadListener(OnLoadListener listener) {
		
	}
	
	@Override
	public File getParentDir() {
		return file.getParentFile();
	}

}