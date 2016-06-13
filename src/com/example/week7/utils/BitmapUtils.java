package com.example.week7.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.Display;
import android.view.WindowManager;

/**
 * λͼ�ļ�����
 * 
 * @author Administrator
 * 
 */
public class BitmapUtils {
	public static Bitmap load(Context context, String file) {
		// ����options����
		Options opts = new Options();
		// ��������ͼƬ�Ŀ��
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file, opts);
		// ��ȡͼƬ�Ŀ��
		int imageWidth = opts.outWidth;
		int imageHeight = opts.outHeight;
		// ��ȡ�ֻ���Ļ�Ŀ��
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display dp = manager.getDefaultDisplay();
		int screenWidth = dp.getWidth();
		int screenHeight = dp.getHeight();
		// ������ű���
		int scale = 1;
		int widthScale = imageWidth / screenWidth;
		int heightScale = imageHeight / screenHeight;
		if (widthScale >= heightScale && widthScale >= 1) {
			scale = widthScale;
		} else if (widthScale < heightScale && heightScale >= 1) {
			scale = heightScale;
		}
		// ���ü���ͼƬ�����ű���
		opts.inSampleSize = scale;
		// ����opts�����Բ��ǽ�������ͼƬ��С
		opts.inJustDecodeBounds = false;
		// ��ȡλͼ����
		Bitmap bm = BitmapFactory.decodeFile(file, opts);
		return bm;
	}

}
