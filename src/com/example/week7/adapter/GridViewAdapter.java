package com.example.week7.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.week7.R;

/**
 * ����GridView������������
 * 
 * @author Administrator
 * 
 */
public class GridViewAdapter extends BaseAdapter {
	private String[] items = new String[] { "����֪ͨ", "��ҵ��Ϣ", "�α��ѯ", "ʧ������",
			"У԰С��", "����ά��" };
	private int[] resIds = new int[] { R.drawable.jiaowu, R.drawable.jiuye,
			R.drawable.lesson, R.drawable.shiwu, R.drawable.shop,
			R.drawable.home_computer_fix };
	private Context context;

	public GridViewAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return items.length;
	}

	@Override
	public Object getItem(int position) {
		return resIds[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(context, R.layout.gridview_item, null);
		ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
		TextView tvIconInfo = (TextView) view.findViewById(R.id.tv_icon_info);
		int resId = (Integer) getItem(position);
		ivIcon.setImageResource(resId);
		tvIconInfo.setText(items[position]);
		return view;
	}
}
