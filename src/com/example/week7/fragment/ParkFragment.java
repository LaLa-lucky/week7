package com.example.week7.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.week7.R;
import com.example.week7.activity.ParkPublishActivity;
import com.example.week7.adapter.ParkListViewAdapter;
import com.example.week7.dao.ParkDao;
import com.example.week7.domain.Park;
import com.example.week7.utils.ToastUtils;
import com.example.week7.view.RefreshListView;
import com.example.week7.view.RefreshListView.onRefreshListener;

/**
 * �㳡Fragment
 * 
 * @author Administrator
 * 
 */
public class ParkFragment extends Fragment implements OnClickListener {
	private static final int LOAD_FAIL = 0;// ����ʧ��
	private static final int LOAD_SUCCESS = 1;// ���سɹ�
	protected static final int EMPTY_DATA = 2;// ���ظ�������Ϊ��
	protected static final int LOAD_MORE_SUCCESS = 3;// ���ظ���ɹ�
	private RefreshListView lvPark;
	private Button btnPublish;
	private int count = 10;
	private int start = 0;
	private ArrayList<Park> parkList;// �������ϻ�ȡ������
	private ParkListViewAdapter adapter;// ����������

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOAD_FAIL:
				lvPark.onRefreshComplete(false);
				ToastUtils.showToast(getActivity(), (String) msg.obj);
				break;
			case LOAD_SUCCESS:
				lvPark.onRefreshComplete(true);
				adapter = new ParkListViewAdapter(ParkFragment.this,
						getActivity(), parkList, getArguments().getString(
								"phone"));
				lvPark.setAdapter(adapter);
				break;
			case EMPTY_DATA:
				lvPark.onRefreshComplete(false);
				ToastUtils.showToast(getActivity(), (String) msg.obj);
				break;
			case LOAD_MORE_SUCCESS:
				ArrayList<Park> newParkList = (ArrayList<Park>) msg.obj;
				parkList.addAll(newParkList);
				if (adapter != null) {
					adapter.notifyDataSetChanged();
					lvPark.onRefreshComplete(true);
				}
				break;
			default:
				ToastUtils.showToast(getActivity(), "���ش���");
				break;
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView();
		initData(false);
		initListener();
		return view;
	}

	/**
	 * ��ʼ��������
	 */
	private void initListener() {
		btnPublish.setOnClickListener(this);
		lvPark.setOnRefreshListener(new onRefreshListener() {

			@Override
			public void onRefresh() {
				initData(true);
			}

			@Override
			public void onLoadMore() {
				initMoreData();
			}
		});
	}

	/**
	 * ���ظ�������
	 */
	protected void initMoreData() {
		// �������ϻ�ȡ�㳡��Ϣ
		new Thread() {

			public void run() {
				SystemClock.sleep(2000);
				start = parkList.size();
				ArrayList<Park> newParkList = new ParkDao().getPark(
						getArguments().getString("phone"), start, count);
				Message message = handler.obtainMessage();
				if (newParkList == null || newParkList.size() <= 0) {
					message.what = EMPTY_DATA;
					message.obj = "û�и���������";
				} else {
					message.what = LOAD_MORE_SUCCESS;
					message.obj = newParkList;
				}
				handler.sendMessage(message);
			};
		}.start();
	}

	/**
	 * ��������
	 */
	private void initData(final boolean isDelay) {
		// �������ϻ�ȡ�㳡��Ϣ
		new Thread() {

			public void run() {
				if (isDelay) {
					SystemClock.sleep(2000);
				}
				start = 0;
				parkList = new ParkDao().getPark(
						getArguments().getString("phone"), start, count);
				updateUI(parkList);
			};
		}.start();
	}

	/**
	 * ����UI
	 * 
	 * @param parkList
	 */
	protected void updateUI(ArrayList<Park> parkList) {
		Message message = handler.obtainMessage();
		if (parkList == null || parkList.size() <= 0) {
			message.what = LOAD_FAIL;
			message.obj = "����ʧ��";
		} else {
			message.what = LOAD_SUCCESS;
		}
		handler.sendMessage(message);
	}

	/**
	 * ��ʼ��UI
	 * 
	 * @return
	 */
	private View initView() {
		View view = View.inflate(getActivity(), R.layout.fragment_park, null);
		btnPublish = (Button) view.findViewById(R.id.btn_publish);
		lvPark = (RefreshListView) view.findViewById(R.id.lv_park);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ����
		case R.id.btn_publish:
			Intent intent = new Intent(getActivity(), ParkPublishActivity.class);
			startActivityForResult(intent, Activity.RESULT_FIRST_USER);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// ����ӷ���ҳ�淵�أ�������ˢ�½���
		if (resultCode == Activity.RESULT_OK) {
			// ˵���ǽ������۲���
			if (requestCode == 1000) {
				int comment_number = data.getIntExtra("comment_number", 0);
				int position = data.getIntExtra("position", 0);
				parkList.get(position).setComment_count(comment_number + "");
				adapter.notifyDataSetChanged();
				return;
			}
			initData(false);
		}
	}
}
