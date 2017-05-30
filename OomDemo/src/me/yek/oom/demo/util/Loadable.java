package me.yek.oom.demo.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

//Ϊ��һ��һ����ʾ��ͬImageLoader�Ĺ��ܣ�ʵ�ִ˽ӿڵ���������ظ���
public interface Loadable {

	public Bitmap loadBitmapImage(String path);
	public Drawable loadDrawableImage(String path);
	public void releaseImage(String path);
	
}
