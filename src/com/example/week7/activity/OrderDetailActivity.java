package com.example.week7.activity;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.week7.R;
import com.example.week7.dao.ExpressDao;
import com.example.week7.domain.Deliveryman;
import com.example.week7.domain.Express;
import com.example.week7.utils.ToastUtils;

/**
 * �����������
 * 
 * @author Administrator
 * 
 */
public class OrderDetailActivity extends BaseActivity implements OnClickListener {
	private static final int REQUEST_ERROR = 0;// �������
	private static final int REQUEST_SUCCESS = 1;// ����ɹ�
	private static final int REQUEST_FAIL = 2;// ����ʧ��
	public static boolean isPressBack = true;
	private ImageView ivBack;
	private TextView tvState;
	private TextView tvEsttime;
	private TextView tvOrdercode;
	private TextView tvName;
	private TextView tvRoomid;
	private TextView tvPhone;
	private TextView tvCompany;
	private TextView tvOrdertime;
	private ImageView ivPhoto;
	private TextView tvSendman;
	private Button btnCancel;
	private LinearLayout ll_bottom;
	private Deliveryman sendman;// ����Ա
	private ImageView ivSms;
	private ImageView ivCall;
	private Express express;// ����
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REQUEST_ERROR:
			case REQUEST_FAIL:
				ToastUtils
						.showToast(OrderDetailActivity.this, (String) msg.obj);
				break;
			case REQUEST_SUCCESS:
				isPressBack = false;
				finish();
				break;
			}
		}

	};

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
		ivSms.setOnClickListener(this);
		ivCall.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		Intent intent = getIntent();
		express = (Express) intent.getSerializableExtra("args");
		if (express.getState() == 0) {
			tvState.setText("�����");
			btnCancel.setVisibility(View.GONE);
		} else if (express.getState() == 1) {
			tvState.setText("�������");
			btnCancel.setVisibility(View.VISIBLE);
		} else if (express.getState() == 2) {
			tvState.setText("����ȡ��");
			btnCancel.setVisibility(View.GONE);
		} else if (express.getState() == 3) {
			tvState.setText("�����ɼ�");
			btnCancel.setVisibility(View.GONE);
		} else if (express.getState() == 4) {
			tvState.setText("��ȡ��");
			btnCancel.setVisibility(View.GONE);
		}
		if (express.getPaystyle() == 1 || express.getPaystyle() == 2) {
			tvEsttime.setText(express.getEsttime());
		} else if (express.getPaystyle() == 3) {
			tvEsttime.setText("��лʹ�������߿��");
		}
		tvOrdercode.setText("�����ţ�" + express.getOrder_code());
		tvName.setText("�ռ��ˣ�" + express.getName());
		tvRoomid.setText("�ռ������ң�" + express.getAddress());
		tvPhone.setText("�ռ����ֻ���" + express.getPhone());
		tvCompany.setText("��ݹ�˾��" + express.getCompany());
		tvOrdertime.setText("�µ�ʱ�䣺" + express.getStime());

		sendman = express.getDeliveryman();
		if (sendman != null) {
			ll_bottom.setVisibility(View.VISIBLE);
			Glide.with(this).load(sendman.getPhoto()).into(ivPhoto);
			tvSendman.setText(sendman.getName());
		} else {
			ll_bottom.setVisibility(View.GONE);
		}
	}

	/**
	 * ��ʼ������
	 */
	private void initView() {
		setContentView(R.layout.activity_order_detail);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		tvState = (TextView) findViewById(R.id.tv_state);
		tvEsttime = (TextView) findViewById(R.id.tv_esttime);
		tvOrdercode = (TextView) findViewById(R.id.tv_ordercode);
		tvName = (TextView) findViewById(R.id.tv_name);
		tvRoomid = (TextView) findViewById(R.id.tv_roomid);
		tvPhone = (TextView) findViewById(R.id.tv_phone);
		tvCompany = (TextView) findViewById(R.id.tv_company);
		tvOrdertime = (TextView) findViewById(R.id.tv_ordertime);
		ivPhoto = (ImageView) findViewById(R.id.iv_photo);
		ivSms = (ImageView) findViewById(R.id.iv_sms);
		ivCall = (ImageView) findViewById(R.id.iv_call);
		tvSendman = (TextView) findViewById(R.id.tv_sendman);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			// ���ذ�ť
			finish();
			break;
		case R.id.iv_sms:
			// ���з����Ų���
			Uri smsToUri = Uri.parse("smsto:" + sendman.getPhone());
			Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
			intent.putExtra("sms_body", "");
			startActivity(intent);
			break;
		case R.id.iv_call:
			// ���д�绰����
			Intent i = new Intent(Intent.ACTION_DIAL);
			i.setData(Uri.parse("tel:" + sendman.getPhone()));
			startActivity(i);
			break;
		case R.id.btn_cancel:
			// ȡ������
			showConfirmDialog();
			break;
		}
	}

	/**
	 * ��ʾȡ�������Ի���
	 */
	private void showConfirmDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("ȡ������");
		builder.setMessage("�������ȡ��������С�ܻ���Ϊ�������أ�");
		builder.setPositiveButton("�����ȴ�", null);
		builder.setNegativeButton("����ȡ��",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferences sp = getSharedPreferences("userinfo",
								MODE_PRIVATE);
						final String phone = sp.getString("phone", "");
						new Thread() {
							public void run() {
								Map<String, Object> returnMap = new ExpressDao()
										.cancelOrder(express.getId(), phone);
								updateUI(returnMap);
							};
						}.start();
						dialog.dismiss();
					}
				});
		builder.show();
	}

	/**
	 * ȡ������֮����½���
	 * 
	 * @param returnMap
	 */
	protected void updateUI(Map<String, Object> returnMap) {
		Message message = handler.obtainMessage();
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			if (respCode == 1) {
				message.obj = returnMap.get("respMsg");
				message.what = REQUEST_SUCCESS;
			} else {
				message.obj = returnMap.get("respMsg");
				message.what = REQUEST_FAIL;
			}
		} else {
			message.obj = "�������";
			message.what = REQUEST_ERROR;
		}
		handler.sendMessage(message);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		isPressBack = true;
	}
}
