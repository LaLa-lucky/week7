package com.example.week7.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.widget.TextView;

/**
 * �ҵļ�ְ����
 * 
 * @author Administrator
 * 
 */
public class MyParttimeActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		TextView textView = new TextView(this);
		textView.setText("�����ҵļ�ְ����");
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(Color.RED);
		setContentView(textView);
	}

}
