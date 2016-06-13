package com.example.week7.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.week7.R;
import com.example.week7.adapter.TabViewPagerAdapter;
import com.example.week7.fragment.HomeFragment;
import com.example.week7.fragment.ParkFragment;
import com.example.week7.fragment.PersonFragment;

/**
 * MainActivity����
 * 
 * @author Administrator
 * 
 */
public class MainActivity extends BaseActivity implements OnClickListener {
	private ViewPager vpMain;// �������ViewPager����������3��Fragment
	private LinearLayout llTabHome;// ��ҳtab��ǩ
	private LinearLayout llTabPark;// �㳡tab��ǩ
	private LinearLayout llTabPerson;// ����tab��ǩ
	private ImageView ivTabHome;
	private ImageView ivTabPark;
	private ImageView ivTabPerson;
	private ArrayList<Fragment> fragmentLists = new ArrayList<Fragment>();
	private String tag = "TAG";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initData();
		initListener();
	}

	/**
	 * ��ʼ��������
	 */
	private void initListener() {
		// ��ViewPager����ҳ���л�������
		vpMain.setOnPageChangeListener(new SimpleOnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				showLightTab(position);
			}
		});
		// ��3��tab��ǩ���õ���¼�
		llTabHome.setOnClickListener(this);
		llTabPark.setOnClickListener(this);
		llTabPerson.setOnClickListener(this);
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		// �������������
		fragmentLists.add(new HomeFragment());
		fragmentLists.add(new ParkFragment());
		fragmentLists.add(new PersonFragment());
		// ����ViewPager������������
		vpMain.setAdapter(new TabViewPagerAdapter(getSupportFragmentManager(),
				fragmentLists, user));
	}

	/**
	 * ��ʼ��UI
	 */
	private void initView() {
		setContentView(R.layout.activity_main);
		vpMain = (ViewPager) findViewById(R.id.vp_main);
		llTabHome = (LinearLayout) findViewById(R.id.ll_tab_home);
		llTabPark = (LinearLayout) findViewById(R.id.ll_tab_park);
		llTabPerson = (LinearLayout) findViewById(R.id.ll_tab_person);
		ivTabHome = (ImageView) findViewById(R.id.ivTabHome);
		ivTabPark = (ImageView) findViewById(R.id.ivTabPark);
		ivTabPerson = (ImageView) findViewById(R.id.ivTabPerson);
		showLightTab(0);// ����һ��ҳǩѡ��
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_tab_home:
			vpMain.setCurrentItem(0);
			showLightTab(0);
			break;
		case R.id.ll_tab_park:
			vpMain.setCurrentItem(1);
			showLightTab(1);
			break;
		case R.id.ll_tab_person:
			vpMain.setCurrentItem(2);
			showLightTab(2);
			break;
		}
	}

	/**
	 * ���ݵ�ǰViewPager��ʾ��ҳ����������ʾtab����
	 * 
	 * @param position��ǰҳ���λ��
	 */
	private void showLightTab(int position) {
		ivTabHome.setEnabled(false);
		ivTabPark.setEnabled(false);
		ivTabPerson.setEnabled(false);
		switch (position) {
		case 0:
			ivTabHome.setEnabled(true);
			break;
		case 1:
			ivTabPark.setEnabled(true);
			break;
		case 2:
			ivTabPerson.setEnabled(true);
			break;
		}
	}
}
