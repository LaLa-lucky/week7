package com.example.week7.utils;

import java.util.ArrayList;

import android.app.Activity;

/**
 * Activity�Ĺ�����
 * 
 * @author Administrator
 * 
 */
public class ActivityCollector {
	private static ArrayList<Activity> activities = new ArrayList<Activity>();

	/*
	 * ��Activity��ӽ�������
	 */
	public static void addActivity(Activity activity) {
		activities.add(activity);
	}

	/*
	 * �Ӽ������Ƴ�ĳ��ָ����Activity
	 */
	public static void removeActivity(Activity activity) {
		activities.remove(activity);
	}

	/*
	 * �������е�Activity
	 */
	public static void killAll() {
		for (Activity activity : activities) {
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
	}
}
