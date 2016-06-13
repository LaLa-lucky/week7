package com.example.week7.fragment;

import java.util.ArrayList;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.example.week7.R;
import com.example.week7.activity.BusyTakeActivity;
import com.example.week7.activity.ComputerActivity;
import com.example.week7.activity.EduActivity;
import com.example.week7.activity.JobActivity;
import com.example.week7.activity.LostFoundActivity;
import com.example.week7.activity.ParttimeActivity;
import com.example.week7.activity.SendExpressActivity;
import com.example.week7.activity.ShopActivity;
import com.example.week7.activity.TakeActivity;
import com.example.week7.adapter.AdViewPagerAdapter;
import com.example.week7.adapter.GridViewAdapter;
import com.example.week7.dao.FocusDao;
import com.example.week7.domain.Focus;
import com.example.week7.view.MyGridView;

/**
 * ��ҳ��Fragment
 * 
 * @author Administrator
 * 
 */
public class HomeFragment extends Fragment implements OnTouchListener,
		OnClickListener, OnItemClickListener {
	private MyGridView gridView;// GridView
	private ViewPager vpFocusAd;// ����ͼ
	private LinearLayout ll_dot;// ������СԲ��ĸ��ؼ�
	private TextView tv_desc;// ����ͼ��ʾ�����ֿؼ�
	private ArrayList<Focus> focusLists;// �������ϻ�ȡ�Ľ���ͼ���󼯺�
	private LinearLayout llTake;
	private LinearLayout llSend;
	private LinearLayout llParttime;
	private LinearLayout llBusyTake;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// ��ʼ����Ĳ�������
				initDots();
				updateDescAndDot();
				// ���ý���ͼ������������
				vpFocusAd.setAdapter(new AdViewPagerAdapter(getActivity(),
						focusLists));
				// ��3���л���һҳ
				handler.sendEmptyMessageDelayed(1, 3000);
				break;
			case 1:
				// �л���һҳ
				int currentItem = vpFocusAd.getCurrentItem();
				vpFocusAd.setCurrentItem((++currentItem) % focusLists.size());
				handler.sendEmptyMessageDelayed(1, 3000);
				break;
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView();
		initData();
		initListener();
		return view;
	}

	/**
	 * ��ʼ��UI
	 * 
	 * @return
	 */
	private View initView() {
		View view = View.inflate(getActivity(), R.layout.fragment_home, null);
		gridView = (MyGridView) view.findViewById(R.id.gridView);
		gridView.setFocusable(false);// ��gridViewĬ�ϲ����ό�㣬ʹScrollView��ͷ����
		vpFocusAd = (ViewPager) view.findViewById(R.id.vp_foucs_ad);
		ll_dot = (LinearLayout) view.findViewById(R.id.ll_dot);
		tv_desc = (TextView) view.findViewById(R.id.tv_desc);
		llTake = (LinearLayout) view.findViewById(R.id.llTake);
		llSend = (LinearLayout) view.findViewById(R.id.llSend);
		llParttime = (LinearLayout) view.findViewById(R.id.llParttime);
		llBusyTake = (LinearLayout) view.findViewById(R.id.llBusyTake);
		return view;
	}

	/**
	 * ��ʼ��������
	 */
	private void initListener() {
		vpFocusAd.setOnTouchListener(this);
		vpFocusAd.setOnPageChangeListener(new SimpleOnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				updateDescAndDot();
			}

		});
		llTake.setOnClickListener(this);
		llSend.setOnClickListener(this);
		llParttime.setOnClickListener(this);
		llBusyTake.setOnClickListener(this);
		gridView.setOnItemClickListener(this);
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		gridView.setAdapter(new GridViewAdapter(getActivity()));
		new Thread() {
			public void run() {
				int start = 0;
				int count = 10;
				Map<String, Object> returnMap = new FocusDao().getFocus(start,
						count);
				updateUI(returnMap);
			};
		}.start();
	}

	/**
	 * ����UI
	 * 
	 * @param returnMap
	 */
	protected void updateUI(Map<String, Object> returnMap) {
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			if (respCode == 1) {
				focusLists = (ArrayList<Focus>) returnMap.get("focusLists");
				if (focusLists != null && focusLists.size() > 0) {
					Message message = handler.obtainMessage();
					message.what = 0;
					handler.sendMessage(message);
				}
			}
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		switch (arg1.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			// ��1����Ϣ���ڷ���
			handler.removeMessages(1);
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			// ���·���1����Ϣ
			handler.sendEmptyMessageDelayed(1, 3000);
			break;
		}
		return false;
	}

	/**
	 * ��ʼ������ͼ�ĵ�
	 */
	private void initDots() {
		for (int i = 0; i < focusLists.size(); i++) {
			View view = new View(getActivity());
			LinearLayout.LayoutParams params = new LayoutParams(20, 20);
			if (i != 0) {
				params.leftMargin = 8;
			}
			view.setLayoutParams(params);
			view.setBackgroundResource(R.drawable.selector_dot);
			ll_dot.addView(view);
		}
	}

	/**
	 * ����ͼƬ˵�������ý����λ��
	 */
	protected void updateDescAndDot() {
		// ��ȡ��ǰviewPager��λ��
		int currentPosition = vpFocusAd.getCurrentItem();
		// ����ͼƬ������
		tv_desc.setText(focusLists.get(currentPosition % focusLists.size())
				.getTitle());
		for (int i = 0; i < ll_dot.getChildCount(); i++) {
			ll_dot.getChildAt(i).setEnabled(
					i == currentPosition % focusLists.size());
		}
	}

	// ����¼�����
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		// ��ݴ���
		case R.id.llTake:
			Intent takeIntent = new Intent(getActivity(), TakeActivity.class);
			startActivity(takeIntent);
			break;
		// �Ŀ��
		case R.id.llSend:
			Intent sendIntent = new Intent(getActivity(),
					SendExpressActivity.class);
			startActivity(sendIntent);
			break;
		// �Ӽ��Ϳ��
		case R.id.llBusyTake:
			Intent busyTakeIntent = new Intent(getActivity(),
					BusyTakeActivity.class);
			startActivity(busyTakeIntent);
			break;
		// ��ְ��Ƹ
		case R.id.llParttime:
			Intent parttimeIntent = new Intent(getActivity(),
					ParttimeActivity.class);
			startActivity(parttimeIntent);
			break;
		}
	}

	// gridView��Ŀ����¼�
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		// ����֪ͨ
		case 0:
			Intent eduIntent = new Intent(getActivity(), EduActivity.class);
			startActivity(eduIntent);
			break;
		// ��ҵ��Ϣ
		case 1:
			Intent jobIntent = new Intent(getActivity(), JobActivity.class);
			startActivity(jobIntent);
			break;
		// �α��ѯ
		case 2:
			break;
		// ʧ������
		case 3:
			Intent lostfoundIntent = new Intent(getActivity(),
					LostFoundActivity.class);
			startActivity(lostfoundIntent);
			break;
		// У԰С��
		case 4:
			Intent shopIntent = new Intent(getActivity(), ShopActivity.class);
			startActivity(shopIntent);
			break;
		// ����ά��
		case 5:
			Intent computerIntent = new Intent(getActivity(),
					ComputerActivity.class);
			startActivity(computerIntent);
			break;
		default:
			break;
		}
	}
}
