package com.example.week7.adapter;

import com.example.week7.fragment.OrderListFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * �����б��Fragment����������
 * 
 * @author Administrator
 * 
 */
public class OrderTabPagerAdapter extends FragmentPagerAdapter {

	private String[] tabs;

	public OrderTabPagerAdapter(FragmentManager fm, String[] tabs) {
		super(fm);
		this.tabs = tabs;
	}

	@Override
	public Fragment getItem(int position) {
		// �½�һ��Fragment��չʾViewPager item�����ݣ������ݲ���(����Ϊfragment��λ��)
		Fragment fragment = new OrderListFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("args", position);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public int getCount() {
		return tabs.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return tabs[position % tabs.length];
	}

}
