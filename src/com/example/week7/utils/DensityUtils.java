package com.example.week7.utils;

import android.content.Context;

/**
 * �ߴ����乤����
 * 
 * @author Administrator
 * 
 */
public class DensityUtils {

	public static int dp2px(Context context, float dp) {
		// ��ȡ��Ļ���ܶ�
		float density = context.getResources().getDisplayMetrics().density;
		// dp��px�Ĺ�ϵ��dp = px / ��Ļ�ܶ�
		int px = (int) (dp * density + 0.5f);// ������������
		return px;
	}

	public static float px2dp(Context context, int px) {
		// ��ȡ��Ļ���ܶ�
		float density = context.getResources().getDisplayMetrics().density;
		// dp��px�Ĺ�ϵ��dp = px / ��Ļ�ܶ�
		float dp = px / density;
		return dp;
	}
}
