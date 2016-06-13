package com.example.week7.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.example.week7.R;
import com.example.week7.adapter.OrderTabPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

/**
 * �����б����
 * 
 * @author Administrator
 * 
 */
public class OrderListActivity extends BaseActivity implements
		OnClickListener {
	private ViewPager vpOrderTab;// ������ViewPager
	private ImageView ivBack;// ���˰�ť
	private TabPageIndicator indicator;
	private String[] tabs = new String[] { "�����", "�������", "����ȡ��", "�����ɼ�", "��ȡ��" };
	private OrderTabPagerAdapter orderTabPagerAdapter;// ����������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initListener();
	}

	/**
	 * �󶨼�����
	 */
	private void initListener() {
		ivBack.setOnClickListener(this);
	}

	/**
	 * ��ʼ��UI
	 */
	private void initView() {
		setContentView(R.layout.activity_order_list);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		// ��ʼ��ViewPager����
		vpOrderTab = (ViewPager) findViewById(R.id.vp_order_tab);
		orderTabPagerAdapter = new OrderTabPagerAdapter(
				getSupportFragmentManager(), tabs);
		vpOrderTab.setOffscreenPageLimit(tabs.length);
		vpOrderTab.setAdapter(orderTabPagerAdapter);
		// ��ʼ��tabpageIndicator����
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		// �����߽��й���
		indicator.setViewPager(vpOrderTab);
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
