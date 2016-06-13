package com.example.week7.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.example.week7.R;
import com.example.week7.adapter.NewsPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

/**
 * ����֪ͨҳ��
 * 
 * @author Administrator
 * 
 */
public class EduActivity extends BaseActivity implements OnClickListener {
	private String[] data = { "����֪ͨ", "��������", "�߽�����" };
	private ImageView ivBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initListener();
	}

	private void initListener() {
		ivBack.setOnClickListener(this);
	}

	/**
	 * ��ʼ������
	 */
	private void initView() {
		setContentView(R.layout.activity_edu);
		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		ViewPager viewPager = (ViewPager) findViewById(R.id.vp_news);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		viewPager.setAdapter(new NewsPagerAdapter(getSupportFragmentManager(),
				data, "edu"));
		viewPager.setOffscreenPageLimit(data.length);// ���û���3ҳ����
		indicator.setViewPager(viewPager);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		}
	}

}
