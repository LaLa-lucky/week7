package com.example.week7.adapter;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.week7.R;
import com.example.week7.activity.ParkCommentActivity;
import com.example.week7.activity.ParkZhuanfaActivity;
import com.example.week7.dao.ParkDao;
import com.example.week7.domain.Park;
import com.example.week7.fragment.ParkFragment;
import com.example.week7.utils.ToastUtils;
import com.example.week7.view.PictureGridView;

/**
 * �㳡�б�ListView������������
 * 
 * @author Administrator
 * 
 */
public class ParkListViewAdapter extends BaseAdapter {
	private static final int ZAN_SUCCESS = 0;// ���޳ɹ�
	private static final int ZAN_ERROR = 1;// ����ʧ��
	private static final int CANCEL_ZAN_SUCCESS = 2;// ȡ���޳ɹ�
	private static final int CANCEL_ZAN_ERROR = 3;// ȡ����ʧ��
	private Context context;// ������
	private ArrayList<Park> parkList;// ΢���б���Ϣ
	private ParkFragment fragment;// fragment����
	private String phone;// �û��˻�
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// ���޳ɹ�
			case ZAN_SUCCESS:
				ToastUtils.showToast(context, "���޳ɹ�");
				Park park = (Park) msg.obj;
				park.setIsZan(1);// �����ѱ���
				park.setZan(Integer.parseInt(park.getZan()) + 1 + "");// �����޵�����+1
				notifyDataSetChanged();// ˢ�½���
				break;
			// ����ʧ��
			case ZAN_ERROR:
				ToastUtils.showToast(context, "����ʧ��");
				break;
			// ȡ����ʧ��
			case CANCEL_ZAN_ERROR:
				ToastUtils.showToast(context, "ȡ��ʧ��");
				break;
			// ȡ���޳ɹ�
			case CANCEL_ZAN_SUCCESS:
				ToastUtils.showToast(context, "ȡ���ɹ�");
				Park p = (Park) msg.obj;
				p.setIsZan(0);// �����ѱ���
				p.setZan(Integer.parseInt(p.getZan()) - 1 + "");// �����޵�����+1
				notifyDataSetChanged();// ˢ�½���
				break;
			default:
				ToastUtils.showToast(context, "�������");
				break;
			}
		}

	};

	public ParkListViewAdapter(ParkFragment fragment, Context context,
			ArrayList<Park> parkList, String phone) {
		this.fragment = fragment;
		this.context = context;
		this.parkList = parkList;
		this.phone = phone;
	}

	@Override
	public int getCount() {
		return parkList.size();
	}

	@Override
	public Object getItem(int position) {
		return parkList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final View view;
		final ViewHolder holder;
		if (convertView == null) {
			view = View.inflate(context, R.layout.listview_item_park, null);
			holder = new ViewHolder();
			holder.tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
			holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			holder.tv_level = (TextView) view.findViewById(R.id.tv_level);
			holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
			holder.iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
			holder.iv_zan = (ImageView) view.findViewById(R.id.iv_zan);
			holder.iv_sex = (ImageView) view.findViewById(R.id.iv_sex);
			holder.gv_park_img = (PictureGridView) view
					.findViewById(R.id.gv_park_img);
			holder.gv_zhuanfa_park_img = (PictureGridView) view
					.findViewById(R.id.gv_zhuanfa_park_img);
			holder.ll_zhuanfa_park = (LinearLayout) view
					.findViewById(R.id.ll_zhuanfa_park);
			holder.tv_zhuanfa_content = (TextView) view
					.findViewById(R.id.tv_zhuanfa_content);
			holder.ll_park_zhuanfa = (LinearLayout) view
					.findViewById(R.id.ll_park_zhuanfa);
			holder.ll_park_comment = (LinearLayout) view
					.findViewById(R.id.ll_park_comment);
			holder.ll_park_zan = (LinearLayout) view
					.findViewById(R.id.ll_park_zan);
			holder.tv_zhuanfa_number = (TextView) view
					.findViewById(R.id.tv_zhuanfa_number);
			holder.tv_comment_number = (TextView) view
					.findViewById(R.id.tv_comment_number);
			holder.tv_zan_number = (TextView) view
					.findViewById(R.id.tv_zan_number);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_nickname.setText(parkList.get(position).getUsername());
		holder.tv_time.setText(parkList.get(position).getTime());
		holder.tv_content.setText(parkList.get(position).getContent());
		holder.tv_level.setText(parkList.get(position).getLevel());
		// �ж��Ƿ���ת����΢��
		if (parkList.get(position).getType().equals("1")) {
			// ��ʾ��ת��������
			holder.ll_zhuanfa_park.setVisibility(View.VISIBLE);
			holder.tv_zhuanfa_content.setText(parkList.get(position).getFrom()
					.getUsername()
					+ "�� " + parkList.get(position).getFrom().getContent());
			int num = parkList.get(position).getParkimg().size();// ��ȡ��ǰ��ͼƬ��Ŀ
			int col = 1;// Ĭ������
			Log.i("tag", "num" + num);
			if (num == 1) {
				holder.gv_park_img.setNumColumns(1);
				col = 1;
			} else if (num == 2 || num == 4) {
				holder.gv_park_img.setNumColumns(2);
				col = 2;
			} else {
				holder.gv_park_img.setNumColumns(3);
				col = 3;
			}
			holder.gv_zhuanfa_park_img.setAdapter(new ParkGridViewAdapter(
					context, parkList.get(position).getParkimg(), col));// ��ʾGridView������������
		} else {
			holder.ll_zhuanfa_park.setVisibility(View.GONE);// ����ת��ģ��
			int num = parkList.get(position).getParkimg().size();// ��ȡ��ǰ��ͼƬ��Ŀ
			int col = 1;// Ĭ������
			Log.i("tag", "num" + num);
			if (num == 1) {
				holder.gv_park_img.setNumColumns(1);
				col = 1;
			} else if (num == 2 || num == 4) {
				holder.gv_park_img.setNumColumns(2);
				col = 2;
			} else {
				holder.gv_park_img.setNumColumns(3);
				col = 3;
			}
			holder.gv_park_img.setAdapter(new ParkGridViewAdapter(context,
					parkList.get(position).getParkimg(), col));// ��ʾGridView������������
		}
		// ����ͷ��
		Glide.with(context).load(parkList.get(position).getUserimg())
				.into(holder.iv_photo);
		// ��ʾ�Ա�ͼ��
		if (parkList.get(position).getSex().equals("��")) {
			holder.iv_sex.setImageResource(R.drawable.sexmale);
		} else if (parkList.get(position).getSex().equals("Ů")) {
			holder.iv_sex.setImageResource(R.drawable.sexfemale);
		}

		// ��ʾ�޵�״̬
		if (parkList.get(position).getIsZan() == 1) {
			holder.iv_zan.setImageResource(R.drawable.park_zaned);
		} else if (parkList.get(position).getIsZan() == 0) {
			holder.iv_zan.setImageResource(R.drawable.park_zan_normal);
		}
		// ��ʾת����������
		holder.tv_zhuanfa_number.setText(parkList.get(position).getZhuanfa());
		// ��ʾ���۵�������
		holder.tv_comment_number.setText(parkList.get(position)
				.getComment_count());
		// ��ʾ�޵�������
		holder.tv_zan_number.setText(parkList.get(position).getZan());
		// ת����ť
		holder.ll_park_zhuanfa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ParkZhuanfaActivity.class);
				intent.putExtra("pid", parkList.get(position).getId());
				fragment.startActivityForResult(intent,
						Activity.RESULT_FIRST_USER);
			}
		});
		// ���۰�ť
		holder.ll_park_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ParkCommentActivity.class);
				intent.putExtra("pid", parkList.get(position).getId());
				intent.putExtra("position", position);
				intent.putExtra("comment_number", parkList.get(position)
						.getComment_count());
				fragment.startActivityForResult(intent, 1000);
			}
		});
		// ���֮ǰû�е���
		if (parkList.get(position).getIsZan() == 0) {
			// ���ް�ť��ʱ���Ǽ��޵Ĺ���
			holder.ll_park_zan.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// �����������磬���޵���Ϣд�������
					new Thread() {
						public void run() {
							Map<String, Object> returnMap = new ParkDao()
									.writeZan(phone, parkList.get(position)
											.getId());
							// Ȼ����½���
							updateAddZanUI(returnMap, parkList.get(position));
						}

					}.start();
				}
			});
		} else {
			// ��ʱ����ȡ���޵Ĺ���
			holder.ll_park_zan.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// �����������磬��ȡ���޵���Ϣд�������
					new Thread() {
						public void run() {
							Map<String, Object> returnMap = new ParkDao()
									.cancelZan(phone, parkList.get(position)
											.getId());
							// Ȼ����½���
							updateCancelZanUI(returnMap, parkList.get(position));
						}

					}.start();
				}
			});
		}
		return view;
	}

	/**
	 * ȡ����֮�󣬸���UI
	 * 
	 * @param returnMap
	 * @param park
	 * @param iv_zan
	 */
	private void updateCancelZanUI(Map<String, Object> returnMap, Park park) {
		Message message = handler.obtainMessage();
		message.obj = park;
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			if (respCode == 1) {
				message.what = CANCEL_ZAN_SUCCESS;
			} else {
				message.what = CANCEL_ZAN_ERROR;
			}
		} else {
			message.what = CANCEL_ZAN_ERROR;
		}
		handler.sendMessage(message);
	}

	/**
	 * ����֮�󣬸���UI
	 * 
	 * @param returnMap
	 * @param park
	 * @param iv_zan
	 */
	private void updateAddZanUI(Map<String, Object> returnMap, Park park) {
		Message message = handler.obtainMessage();
		message.obj = park;
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			if (respCode == 1) {
				message.what = ZAN_SUCCESS;
			} else {
				message.what = ZAN_ERROR;
			}
		} else {
			message.what = ZAN_ERROR;
		}
		handler.sendMessage(message);
	}

	class ViewHolder {
		TextView tv_nickname;
		TextView tv_time;
		TextView tv_content;
		TextView tv_level;
		ImageView iv_photo;
		ImageView iv_zan;
		ImageView iv_sex;
		LinearLayout ll_park_zhuanfa;
		LinearLayout ll_park_zan;
		LinearLayout ll_park_comment;
		LinearLayout ll_zhuanfa_park;
		TextView tv_zhuanfa_content;
		TextView tv_zhuanfa_number;
		TextView tv_comment_number;
		TextView tv_zan_number;
		PictureGridView gv_park_img;
		PictureGridView gv_zhuanfa_park_img;
	}

}
