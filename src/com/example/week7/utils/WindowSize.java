package com.example.week7.utils;

import android.content.Context;
import android.view.WindowManager;

/**
 * ��ȡ��Ļ��С
 * 
 * @author Administrator
 * 
 */
public class WindowSize {
	public static int getWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = manager.getDefaultDisplay().getWidth();
		return width;
	}
}
