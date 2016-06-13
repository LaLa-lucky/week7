package com.example.week7.activity;

import java.util.ArrayList;

import com.example.week7.R;
import com.example.week7.adapter.ShopListViewAdapter;
import com.example.week7.dao.ShopDao;
import com.example.week7.domain.Shop;
import com.example.week7.utils.ToastUtils;
import com.example.week7.view.RefreshListView;
import com.example.week7.view.RefreshListView.onRefreshListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * У԰С�̽���
 */
public class ShopActivity extends BaseActivity implements OnClickListener,
		onRefreshListener, OnItemClickListener {

	private static final int FRESH_SUCCESS = 0;// ˢ�³ɹ�
	private static final int EMPTY_DATA = 1;// ����Ϊ��
	private static final int LOAD_FAIL = 2;// ����ʧ��
	private static final int LOAD_MORE_SUCCESS = 3;// ���ظ���ɹ�
	private ImageView ivBack;
	private RefreshListView lvShop;
	private int start = 0;
	private int count = 10;
	private ArrayList<Shop> shopList;// ���������
	private ShopListViewAdapter adapter;// ListView������������
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FRESH_SUCCESS:
				llProgress.setVisibility(View.GONE);
				lvShop.onRefreshComplete(true);
				adapter = new ShopListViewAdapter(ShopActivity.this, shopList);
				lvShop.setAdapter(adapter);
				break;
			case EMPTY_DATA:
			case LOAD_FAIL:
				lvShop.onRefreshComplete(false);
				ToastUtils.showToast(ShopActivity.this, (String) msg.obj);
				break;
			case LOAD_MORE_SUCCESS:
				lvShop.onRefreshComplete(true);
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}
				break;
			default:
				ToastUtils.showToast(ShopActivity.this, "�������");
				break;
			}
		}

	};
	private LinearLayout llProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initData();
		initListener();
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		new Thread() {

			public void run() {
				SystemClock.sleep(2000);
				start = 0;
				shopList = new ShopDao().getShop(phone, start, count);
				updateUI(shopList);
			};
		}.start();
	}

	/**
	 * ������������������½���
	 * 
	 * @param newLostfoundList
	 * 
	 * @param typeˢ�½��������
	 * 
	 * @param shopList
	 */
	protected void updateUI(ArrayList<Shop> shopList) {
		Message message = handler.obtainMessage();
		if (shopList != null) {
			if (shopList.size() > 0) {
				message.what = FRESH_SUCCESS;
			} else {
				message.what = EMPTY_DATA;
				message.obj = "����Ϊ��";
			}
		} else {
			message.what = LOAD_FAIL;
			message.obj = "����ʧ��";
		}
		handler.sendMessage(message);
	}

	/**
	 * ��ʼ��������
	 */
	private void initListener() {
		ivBack.setOnClickListener(this);
		lvShop.setOnRefreshListener(this);
		lvShop.setOnItemClickListener(this);
	}

	/**
	 * ��ʼ��UI
	 */
	private void initView() {
		setContentView(R.layout.activity_shop);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		lvShop = (RefreshListView) findViewById(R.id.lv_shop);
		llProgress = (LinearLayout) findViewById(R.id.ll_progress);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ���������˳���ť
		case R.id.iv_back:
			finish();
			break;
		// ������ť
		case R.id.btn_publish:
			Intent intent = new Intent(ShopActivity.this,
					LostfoundPulishActivity.class);
			startActivityForResult(intent, 0);
			break;
		}
	}

	// ˢ��ʱ�ص�
	@Override
	public void onRefresh() {
		initData();
	}

	// ���ظ����ǻص�
	@Override
	public void onLoadMore() {
		initMoreData();
	}

	/**
	 * ���ظ�������
	 */
	private void initMoreData() {
		new Thread() {

			public void run() {
				Message message = handler.obtainMessage();
				SystemClock.sleep(2000);
				start = shopList.size();
				ArrayList<Shop> newShopList = new ShopDao().getShop(phone,
						start, count);
				if (newShopList == null || newShopList.size() <= 0) {
					message.what = EMPTY_DATA;
					message.obj = "û�и���������";
				} else {
					shopList.addAll(newShopList);
					message.what = LOAD_MORE_SUCCESS;
				}
				handler.sendMessage(message);
			};
		}.start();
	}

	// ��Ŀ����¼�
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, ShopDetailActivity.class);
		intent.putExtra("id", shopList.get(position).getId());
		intent.putExtra("tel", shopList.get(position).getTel());
		startActivity(intent);
	}

}
