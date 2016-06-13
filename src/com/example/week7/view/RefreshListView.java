package com.example.week7.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.week7.R;

/**
 * ����ˢ�µ�ListView
 * 
 * @author Administrator
 * 
 */
public class RefreshListView extends ListView implements OnScrollListener,
		android.widget.AdapterView.OnItemClickListener {
	int startY = -1;// �û�������Ļʱ��y����
	private int measuredHeight;// ͷ���ֵĸ߶�
	private View headerView;// ͷ����view
	private ImageView iv_arr;// ����ˢ�µļ�ͷ
	private ProgressBar pb_refresh;// ����ˢ�µĽ�����
	private TextView tv_refresh_info;// ˢ��״̬������
	private TextView tv_time;// ���ˢ��ʱ��
	public static final int STATE_PULL_REFRESH = 0;// ����ˢ��
	public static final int STATE_RELEASE_REFRESH = 1;// �ɿ�ˢ��
	public static final int STATE_REFRESHING = 2;// ����ˢ��
	private int currentState = STATE_PULL_REFRESH;// ��ǰ��ˢ��״̬
	private RotateAnimation upAnimation;
	private RotateAnimation downAnimation;

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context) {
		super(context);
		initHeaderView();
		initFooterView();
	}

	/**
	 * ��ʼ��ͷ����
	 */
	private void initHeaderView() {
		headerView = View.inflate(getContext(),
				R.layout.listview_header_refresh, null);
		iv_arr = (ImageView) headerView.findViewById(R.id.iv_arr);
		pb_refresh = (ProgressBar) headerView.findViewById(R.id.pb_refresh);
		tv_time = (TextView) headerView.findViewById(R.id.tv_time);
		tv_refresh_info = (TextView) headerView
				.findViewById(R.id.tv_refresh_info);
		headerView.measure(0, 0);
		measuredHeight = headerView.getMeasuredHeight();
		headerView.setPadding(0, -measuredHeight, 0, 0);
		addHeaderView(headerView);
		initAnimation();
		tv_time.setText("���ʱ�䣺" + getCurrentTime());
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			// ȷ��startY����ֵ
			if (startY == -1) {
				startY = (int) ev.getRawY();
			}
			if (currentState == STATE_REFRESHING) {
				break;
			}

			int endY = (int) ev.getRawY();
			int dy = endY - startY;
			// ȷ����ָ��������������ʾ���ǵ�һ��view
			if (dy > 0 && getFirstVisiblePosition() == 0) {
				int padding = dy - measuredHeight;
				headerView.setPadding(0, padding, 0, 0);
				// �����ǰ״̬�����ɿ�ˢ�²���padding����0
				if (padding > 0 && currentState != STATE_RELEASE_REFRESH) {
					currentState = STATE_RELEASE_REFRESH;
					refreshState();
					// �����ǰ״̬��������ˢ�²���paddingС��0
				} else if (padding <= 0 && currentState != STATE_PULL_REFRESH) {
					currentState = STATE_PULL_REFRESH;
					refreshState();
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			startY = -1;// ����
			// �����ǰ��״̬���ɿ�ˢ��,����Ϊ����ˢ��
			if (currentState == STATE_RELEASE_REFRESH) {
				currentState = STATE_REFRESHING;
				headerView.setPadding(0, 0, 0, 0);
				refreshState();
			} else if (currentState == STATE_PULL_REFRESH) {
				headerView.setPadding(0, -measuredHeight, 0, 0);
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * ˢ������ˢ�µ�״̬
	 */
	private void refreshState() {
		switch (currentState) {
		// ����ˢ��
		case STATE_PULL_REFRESH:
			tv_refresh_info.setText("����ˢ��");
			iv_arr.setVisibility(View.VISIBLE);
			iv_arr.startAnimation(downAnimation);
			pb_refresh.setVisibility(View.INVISIBLE);
			break;
		// �ɿ�ˢ��
		case STATE_RELEASE_REFRESH:
			tv_refresh_info.setText("�ɿ�ˢ��");
			iv_arr.setVisibility(View.VISIBLE);
			iv_arr.startAnimation(upAnimation);
			pb_refresh.setVisibility(View.INVISIBLE);
			break;
		// ����ˢ��
		case STATE_REFRESHING:
			tv_refresh_info.setText("����ˢ��...");
			iv_arr.clearAnimation();// ������֮ǰ���������ٶ���
			iv_arr.setVisibility(View.INVISIBLE);
			pb_refresh.setVisibility(View.VISIBLE);
			// ���ؽӿ��е�ˢ�·���
			if (listener != null) {
				listener.onRefresh();
			}
			break;

		}
	}

	/**
	 * ��ʼ������
	 */
	private void initAnimation() {
		upAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		upAnimation.setDuration(300);
		upAnimation.setFillAfter(true);
		downAnimation = new RotateAnimation(-180, -360,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		upAnimation.setDuration(300);
		upAnimation.setFillAfter(true);

	}

	/**
	 * ˢ����ɵķ���
	 * 
	 * @param b
	 * 
	 */
	public void onRefreshComplete(boolean success) {
		if (isLoadMore) {
			isLoadMore = false;
			footerView.setPadding(0, -footerViewHeight, 0, 0);
		} else {
			currentState = STATE_PULL_REFRESH;
			tv_refresh_info.setText("����ˢ��");
			iv_arr.setVisibility(View.VISIBLE);
			iv_arr.startAnimation(downAnimation);
			pb_refresh.setVisibility(View.INVISIBLE);
			headerView.setPadding(0, -measuredHeight, 0, 0);
			if (success) {
				tv_time.setText("���ʱ�䣺" + getCurrentTime());
			}
		}
	}

	public void setOnRefreshListener(onRefreshListener listener) {
		this.listener = listener;
	}

	private onRefreshListener listener;
	private View footerView;

	public interface onRefreshListener {
		public void onRefresh();

		public void onLoadMore();
	}

	/**
	 * ��ȡ��ǰʱ��
	 * 
	 * @return
	 */
	private String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	/**
	 * ��ʼ���Ų���
	 */
	private void initFooterView() {
		footerView = View.inflate(getContext(),
				R.layout.listview_footer_refresh, null);
		this.addFooterView(footerView);
		footerView.measure(0, 0);
		footerViewHeight = footerView.getMeasuredHeight();
		footerView.setPadding(0, -footerViewHeight, 0, 0);
		this.setOnScrollListener(this);
	}

	private boolean isLoadMore;
	private int footerViewHeight;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				|| scrollState == OnScrollListener.SCROLL_STATE_FLING) {
			if (getLastVisiblePosition() == getCount() - 1 && !isLoadMore) {
				// ��ʾ�Ų���
				Log.d("TAG", "������...");
				footerView.setPadding(0, 0, 0, 0);
				setSelection(getCount());// ����ListView��ʾ���һ��
				isLoadMore = true;

				// ���ýӿ��еļ��ظ������ݷ���
				if (listener != null) {
					listener.onLoadMore();
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	private OnItemClickListener onItemClickListener;

	// ��дListView��setOnItemClickListener����
	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		super.setOnItemClickListener(this);
		onItemClickListener = listener;
	}

	// ��дonItemClick����
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent.getAdapter().getCount() - 1 == position)
			return;
		onItemClickListener.onItemClick(parent, view, position
				- getHeaderViewsCount(), id);
	}

}
