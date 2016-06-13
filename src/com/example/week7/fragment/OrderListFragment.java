package com.example.week7.fragment;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.week7.R;
import com.example.week7.activity.OrderDetailActivity;
import com.example.week7.activity.OrderListActivity;
import com.example.week7.adapter.OrderListAdapter;
import com.example.week7.dao.ExpressDao;
import com.example.week7.domain.Express;
import com.example.week7.utils.ToastUtils;

/**
 * �����б��Fragment
 * 
 * @author Administrator
 * 
 */
public class OrderListFragment extends Fragment {
	private static final int REQUEST_SUCCESS = 0;// ����ɹ�
	private static final int REQUEST_FAIL = 1;// ����ʧ��
	private static final int REQUEST_ERROR = 2;// �������
	private static final int DATA_EMPTY = 4;// ���������Ϊ��
	private ListView lvOrder;// ����ListView
	private OrderListAdapter adapter;// ListView������������

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REQUEST_SUCCESS:
				llProgress.setVisibility(View.GONE);
				adapter = new OrderListAdapter(getActivity(), expressLists);
				lvOrder.setAdapter(adapter);
				lvOrder.setOnItemClickListener(adapter);
				break;
			case REQUEST_FAIL:
			case REQUEST_ERROR:
			case DATA_EMPTY:
				llProgress.setVisibility(View.GONE);
				ToastUtils.showToast(getActivity(), (String) msg.obj);
				break;
			}
		}

	};
	private ArrayList<Express> expressLists;
	private LinearLayout llProgress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView(inflater);
		return view;
	}

	/**
	 * ��ʼ��UI
	 * 
	 * @param inflater
	 */
	private View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_order_tab, null);
		lvOrder = (ListView) view.findViewById(R.id.lv_order);
		llProgress = (LinearLayout) view.findViewById(R.id.ll_progress);
		return view;
	}

	/**
	 * ������ʵ��Fragment���ݵĻ�����.
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			onVisible();
		} else {
			onInvisible();
		}
	}

	/**
	 * Fragment�����ص�ʱ�����
	 */
	private void onInvisible() {

	}

	/**
	 * Fragment��ʾ��ʱ�����
	 */
	private void onVisible() {
		initData();
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		// ��ȡҪ�����״̬
		Bundle arguments = getArguments();
		final int state = arguments.getInt("args");// position��������״̬
		// ��ȡ�û���Ϣ
		SharedPreferences sp = getActivity().getSharedPreferences("userinfo",
				Context.MODE_PRIVATE);
		final String phone = sp.getString("phone", "");

		// ��ȡ���еĶ�����Ϣ
		new Thread() {

			public void run() {
				SystemClock.sleep(2000);
				Map<String, Object> returnMap = new ExpressDao().getAllOrder(
						phone, state);
				updataUI(returnMap);
			};
		}.start();
	}

	/**
	 * ���½���
	 * 
	 * @param returnMap�����������������
	 */
	protected void updataUI(Map<String, Object> returnMap) {
		Message message = handler.obtainMessage();
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			if (respCode == 1) {
				expressLists = (ArrayList<Express>) returnMap.get("data");
				if (expressLists.size() > 0) {
					message.what = REQUEST_SUCCESS;
				} else {
					message.what = DATA_EMPTY;
					message.obj = "����Ϊ��";
				}
			} else {
				message.what = REQUEST_FAIL;
				message.obj = returnMap.get("resgMsg");
			}
		} else {
			message.what = REQUEST_ERROR;
			message.obj = "�������";
		}
		handler.sendMessage(message);
	}

	/**
	 * ��������ʾFragment��ʱ��ˢ��ListView������
	 */
	@Override
	public void onStart() {
		super.onStart();
		if (adapter != null && !OrderDetailActivity.isPressBack) {
			// ɾ����Ŀ��ˢ��UI
			adapter.deleteItem();
			adapter.notifyDataSetChanged();
		}
	}
}
