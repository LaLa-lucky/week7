package com.example.week7.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.example.week7.R;
import com.example.week7.activity.NewsDetailActivity;
import com.example.week7.adapter.NewsListViewAdapter;
import com.example.week7.dao.NewsDao;
import com.example.week7.domain.News;
import com.example.week7.utils.ToastUtils;
import com.example.week7.view.RefreshListView;
import com.example.week7.view.RefreshListView.onRefreshListener;

/**
 * ���������б��Fragment
 * 
 * @author Administrator
 * 
 */
public class NewsFragment extends Fragment implements OnItemClickListener {
	private static final int LOAD_FAIL = 0;// ����ʧ��
	private static final int EMPTY_DATA = 1;// ����Ϊ��
	private static final int LOAD_SUCCESS = 2;// ���سɹ�
	private static final int LOAD_MORE_SUCCESS = 3;// ���ظ���ɹ�
	private int page = 1;// ��ǰ��������ҳ����Ĭ���ǵ�һҳ
	private RefreshListView lvNews;
	private LinearLayout llProgress;
	private ArrayList<News> newsList;// ����������������ݼ���
	private int position;// ��ǰviewPager�л���Fragment��ҳ��
	private NewsListViewAdapter adapter;// ListView������������
	private String reuestActivity;// �Ǹ�Activity���������Fragment
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// ����ʧ��
			case LOAD_FAIL:
				// ����Ϊ��
			case EMPTY_DATA:
				lvNews.onRefreshComplete(false);
				llProgress.setVisibility(View.GONE);
				ToastUtils.showToast(getActivity(), (String) msg.obj);
				break;
			// ���سɹ�
			case LOAD_SUCCESS:
				lvNews.onRefreshComplete(true);
				llProgress.setVisibility(View.GONE);
				adapter = new NewsListViewAdapter(getActivity(), newsList,
						reuestActivity);
				lvNews.setAdapter(adapter);
				break;
			// ���ظ���ɹ�
			case LOAD_MORE_SUCCESS:
				if (adapter != null) {
					lvNews.onRefreshComplete(true);
					adapter.notifyDataSetChanged();
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
		View view = initView(inflater);
		initListener();
		return view;
	}

	/**
	 * ��ʼ��������
	 */
	private void initListener() {
		lvNews.setOnRefreshListener(new onRefreshListener() {

			@Override
			public void onRefresh() {
				refresh();
			}

			@Override
			public void onLoadMore() {
				loadMore();
			}
		});
	}

	/**
	 * ���ظ�������
	 */
	private void loadMore() {
		new Thread() {
			public void run() {
				SystemClock.sleep(2000);
				page++;// ������һҳ
				ArrayList<News> newNewsList = null;
				if (reuestActivity.equals("edu")) {
					newNewsList = new NewsDao().getEduNews(page, position);
				} else if (reuestActivity.equals("job")) {
					newNewsList = new NewsDao().getJobNews(page, position);
				}
				Message message = handler.obtainMessage();
				if (newNewsList == null || newNewsList.size() <= 0) {
					message.what = EMPTY_DATA;
					message.obj = "û�и���������";
				} else {
					message.what = LOAD_MORE_SUCCESS;
				}
				handler.sendMessage(message);
			};
		}.start();
	}

	/**
	 * ����ˢ��
	 */
	private void refresh() {
		initData();
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		new Thread() {
			public void run() {
				SystemClock.sleep(2000);
				// ������ˢ��ʱ�������������ҳ��Ϊ��һҳ
				page = 1;
				if (reuestActivity.equals("edu")) {
					newsList = new NewsDao().getEduNews(page, position);
				} else if (reuestActivity.equals("job")) {
					newsList = new NewsDao().getJobNews(page, position);
				}
				updateUI(newsList);
			};
		}.start();
	}

	/**
	 * ��������֮��ˢ��UI
	 * 
	 * @param newsList��һ�������󵽵�����
	 * 
	 * @param typeˢ�½��������
	 * 
	 * @param newsList
	 */
	protected void updateUI(ArrayList<News> newsList) {
		Message message = handler.obtainMessage();
		if (newsList != null) {
			if (newsList.size() > 0) {
				message.what = LOAD_SUCCESS;
				message.obj = newsList;
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
	 * ��ʼ��UI
	 * 
	 * @param inflater
	 * 
	 * @return
	 */
	private View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_news, null);
		lvNews = (RefreshListView) view.findViewById(R.id.lv_news);
		llProgress = (LinearLayout) view.findViewById(R.id.ll_progress);
		lvNews.setOnItemClickListener(this);
		// ��ȡ��ǰ�л������Ǹ�����
		Bundle bundle = getArguments();
		position = bundle.getInt("position");
		reuestActivity = bundle.getString("reuestActivity");

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

	// ListView����Ŀ����¼�
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("url", newsList.get(position).getUrl());
		bundle.putString("title", newsList.get(position).getTitle());
		bundle.putString("number", newsList.get(position).getNumber());
		bundle.putString("date", newsList.get(position).getDate());
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
