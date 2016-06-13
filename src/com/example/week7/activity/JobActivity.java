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
 * ��ҵ��Ϣҳ��
 * 
 * @author Administrator
 * 
 */
public class JobActivity extends BaseActivity implements OnClickListener {
	private String[] data = { "У����Ƹ", "У����Ƹ", "������Ϣ", "֪ͨ����", "��ҵָ��", "��ҵ����",
			"�γ̽���" };
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
		setContentView(R.layout.activity_job);
		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		ViewPager viewPager = (ViewPager) findViewById(R.id.vp_news);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		viewPager.setAdapter(new NewsPagerAdapter(getSupportFragmentManager(),
				data, "job"));
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
