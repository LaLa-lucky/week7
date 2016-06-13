package com.example.week7.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.week7.R;
import com.example.week7.adapter.ParttimeFocusViewPagerAdapter;
import com.example.week7.adapter.ParttimeListViewAdapter;
import com.example.week7.dao.ParttimeDao;
import com.example.week7.domain.Parttime;
import com.example.week7.utils.ToastUtils;
import com.example.week7.view.RefreshListView;
import com.example.week7.view.RefreshListView.onRefreshListener;

/**
 * ��ְ����
 * 
 * @author Administrator
 * 
 */
public class ParttimeActivity extends BaseActivity implements OnClickListener {
	private static final int REFRESH = 0;// ����ˢ��
	private static final int LOAD_MORE = 1;// ���ظ���
	private static final int EMPTY_DATA = 2;// ��������Ϊ��
	private int currentStartId;// ��ѯ��ʼ�ĽǱ�
	private int count = 4;// ��ѯ��������
	private ViewPager vpJds;
	private RefreshListView lvList;
	private ArrayList<Parttime> parttimeList;
	private ArrayList<Parttime> jdList;
	private ParttimeFocusViewPagerAdapter vpAdapter;
	private ParttimeListViewAdapter lvAdapter;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// ����ˢ��
			case REFRESH:
				lvList.onRefreshComplete(true);// ��������ˢ�¿ؼ�
				llProgress.setVisibility(View.INVISIBLE);
				if (jdList != null) {
					vpAdapter = new ParttimeFocusViewPagerAdapter(
							ParttimeActivity.this, jdList);
					vpJds.setAdapter(vpAdapter);
				}
				if (parttimeList != null) {
					lvAdapter = new ParttimeListViewAdapter(
							ParttimeActivity.this, parttimeList);
					lvList.setAdapter(lvAdapter);
				}
				break;
			// ���ظ���
			case LOAD_MORE:
				if (lvAdapter != null) {
					lvList.onRefreshComplete(true);// ��������ˢ�¿ؼ�
					lvAdapter.notifyDataSetChanged();
				}
				break;
			// ����Ϊ��
			case EMPTY_DATA:
				lvList.onRefreshComplete(false);// ��������ˢ�¿ؼ�
				ToastUtils.showToast(ParttimeActivity.this, "û�и���������");
				break;
			default:
				ToastUtils.showToast(ParttimeActivity.this, "���ش���");
				break;
			}
		}

	};
	private LinearLayout llProgress;
	private ImageView ivBack;

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
		ivBack.setOnClickListener(this);
		// ����ListView��ˢ�½ӿ�
		lvList.setOnRefreshListener(new onRefreshListener() {

			@Override
			public void onRefresh() {
				initData();// ��������
			}

			@Override
			public void onLoadMore() {
				initMoreData();// ���ظ�������
			}
		});
		// ����ListView����Ŀ����¼�
		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ParttimeActivity.this,
						ParttimeDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("title", parttimeList.get(position).getTitle());
				bundle.putInt("id", parttimeList.get(position).getId());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	/**
	 * ���ظ�������
	 */
	protected void initMoreData() {
		new Thread() {
			public void run() {
				SystemClock.sleep(2000);
				Message message = handler.obtainMessage();
				currentStartId = parttimeList.size();
				ArrayList<Parttime> newParttimeList = (ArrayList<Parttime>) new ParttimeDao()
						.getParttimeList(currentStartId, count, "parttime");
				if (newParttimeList == null) {
					message.what = EMPTY_DATA;
				} else {
					message.what = LOAD_MORE;
					parttimeList.addAll(newParttimeList);
				}
				handler.sendMessage(message);
			};
		}.start();
	}

	/**
	 * ��ʼ������
	 * 
	 */
	private void initData() {
		new Thread() {

			public void run() {
				SystemClock.sleep(2000);
				currentStartId = 0;
				parttimeList = (ArrayList<Parttime>) new ParttimeDao()
						.getParttimeList(currentStartId, count, "parttime");
				jdList = (ArrayList<Parttime>) new ParttimeDao()
						.getParttimeList(currentStartId, count, "jd");
				Message message = handler.obtainMessage();
				message.what = REFRESH;
				handler.sendMessage(message);
			};
		}.start();
	}

	/**
	 * ��ʼ������
	 */
	private void initView() {
		setContentView(R.layout.activity_parttime);
		lvList = (RefreshListView) findViewById(R.id.lv_list);
		llProgress = (LinearLayout) findViewById(R.id.ll_progress);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		// ����ListView��ͷ����
		View headerView = View
				.inflate(this, R.layout.list_header_topnews, null);
		vpJds = (ViewPager) headerView.findViewById(R.id.vp_jd);
		lvList.addHeaderView(headerView);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ���������˳���ť
		case R.id.iv_back:
			finish();
			break;
		}
	}

}
