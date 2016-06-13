package com.example.week7.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.week7.R;
import com.example.week7.domain.News;

/**
 * �����б����ListView����������
 * 
 * @author Administrator
 * 
 */
public class NewsListViewAdapter extends BaseAdapter {

	private ArrayList<News> newsList;
	private Context context;
	private String reuestActivity;// �������Activity��Activity

	public NewsListViewAdapter(Context context, ArrayList<News> newsList,
			String reuestActivity) {
		this.context = context;
		this.newsList = newsList;
		this.reuestActivity = reuestActivity;
	}

	@Override
	public int getCount() {
		return newsList.size();
	}

	@Override
	public Object getItem(int position) {
		return newsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.listview_item_news, null);
			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			holder.tv_readnumber = (TextView) view
					.findViewById(R.id.tv_readnumber);
			holder.tv_addtime = (TextView) view.findViewById(R.id.tv_addtime);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		// ���ݷ��ʵ�Activity��ͬ����ʾ��ͬ����ʽ
		holder.tv_title.setText(newsList.get(position).getTitle());
		if (reuestActivity.equals("edu")) {
			holder.tv_readnumber.setText(newsList.get(position).getNumber()
					.trim());
			holder.tv_addtime.setText(newsList.get(position).getDate());
		} else if (reuestActivity.equals("job")) {
			holder.tv_readnumber.setText("�Ķ�����"
					+ newsList.get(position).getNumber().trim());
			holder.tv_addtime.setText("��" + newsList.get(position).getDate()
					+ "��");
		}
		return view;
	}

	class ViewHolder {
		TextView tv_title;
		TextView tv_readnumber;
		TextView tv_addtime;
	}
}
