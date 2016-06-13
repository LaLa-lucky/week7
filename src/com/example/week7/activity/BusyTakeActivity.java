package com.example.week7.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.week7.R;
import com.example.week7.dao.ExpressDao;
import com.example.week7.dao.SchoolDao;
import com.example.week7.domain.Express;
import com.example.week7.domain.School;
import com.example.week7.utils.ToastUtils;
import com.example.week7.utils.VerificationFormat;

/**
 * �ؿ�Ӽ�������
 * 
 * @author Administrator
 * 
 */
public class BusyTakeActivity extends BaseActivity implements
		OnItemSelectedListener, OnClickListener {

	private static final int LODING_AREA_DATA_SUCCESS = 0;// �������ݼ��سɹ�
	private static final int TAKE_EXPRESS_SUCCESS = 1;// �µ��ɹ�
	private static final int TAKE_EXPRESS_FAIL = 2;// �µ�ʧ��
	private static final int REQUEST_ERROR = 3;// �������
	private EditText etName;
	private EditText etPhone;
	private EditText etRoomId;
	private Spinner spArea;
	private Spinner spExpressShop;
	private Spinner spTime;
	private EditText etExpressNo;
	private EditText etRemark;
	private Spinner spExpressWeight;
	private Button btnOrder;
	private TextView tvExpressWeighDdesc;
	private Button btnTake;
	private ArrayAdapter<String> spAreaAdapter;// ����Spinner������������
	private ArrayAdapter<String> spTimeAdapter;// ����ʱ��Spinner������������
	private ArrayList<School> schoolLists;// �û�����ѧУ����������
	private String mName;// ����
	private String mTel;// �绰
	private String mRoomId;// ���Һ�
	private String mExpressNo;// ȡ����
	private String mRemark;// ��ע
	private int mAid;// ����id
	private String mCompany;// ��ݹ�˾
	private int mPayStyle = 2;// �Ӽ��Ϳ�ݷ�ʽ
	private String mArriveTime;// ����ʱ��
	private int mLarge;// �����С
	private String mPhone;// �û���
	private int mMoney;// ���
	private ImageView ivBack;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// ��ʾ������سɹ�
			case LODING_AREA_DATA_SUCCESS:
				spArea.setAdapter(spAreaAdapter);
				break;
			// �µ��ɹ�
			case TAKE_EXPRESS_SUCCESS:
				ToastUtils.showToast(BusyTakeActivity.this, (String) msg.obj);
				Intent intent = new Intent(BusyTakeActivity.this,
						OrderListActivity.class);
				startActivity(intent);
				break;
			// �µ�ʧ��
			case TAKE_EXPRESS_FAIL:
				ToastUtils.showToast(BusyTakeActivity.this, (String) msg.obj);
				break;
			// �������
			case REQUEST_ERROR:
				ToastUtils.showToast(BusyTakeActivity.this, "�������");
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
	 * ��ʼ������
	 */
	private void initData() {
		// ��sp�ж�ȡ������û���Ϣ
		SharedPreferences sp = getSharedPreferences("userinfo", MODE_PRIVATE);
		mPhone = sp.getString("phone", "");
		// ��ȡ��У���͵�����
		new Thread() {
			public void run() {
				SchoolDao schoolDao = new SchoolDao(BusyTakeActivity.this);
				Map<String, Object> returnMap = schoolDao.getArea(mPhone);
				updateUI(returnMap, "get_area");
			};
		}.start();
		// ��������ʱ������
		String times[] = initTimeData();
		spTimeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, times);
		spTimeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spTime.setAdapter(spTimeAdapter);
	}

	/**
	 * ��ʼ������
	 */
	private void initView() {
		setContentView(R.layout.activity_busy_take);
		etName = (EditText) findViewById(R.id.et_name);
		etPhone = (EditText) findViewById(R.id.et_phone);
		etRoomId = (EditText) findViewById(R.id.et_room_id);
		spArea = (Spinner) findViewById(R.id.sp_area);
		spExpressShop = (Spinner) findViewById(R.id.sp_express_shop);
		spTime = (Spinner) findViewById(R.id.sp_time);
		etExpressNo = (EditText) findViewById(R.id.et_express_no);
		etRemark = (EditText) findViewById(R.id.et_remark);
		spExpressWeight = (Spinner) findViewById(R.id.sp_express_weight);
		btnOrder = (Button) findViewById(R.id.btn_order);
		btnTake = (Button) findViewById(R.id.btn_take);
		tvExpressWeighDdesc = (TextView) findViewById(R.id.tv_express_weight_desc);
		ivBack = (ImageView) findViewById(R.id.iv_back);
	}

	/**
	 * �󶨼�����
	 */
	private void initListener() {
		btnOrder.setOnClickListener(this);
		btnTake.setOnClickListener(this);
		spArea.setOnItemSelectedListener(this);
		spExpressShop.setOnItemSelectedListener(this);
		spTime.setOnItemSelectedListener(this);
		spExpressWeight.setOnItemSelectedListener(this);
		ivBack.setOnClickListener(this);
	}

	/**
	 * ���ݼ��غõ����ݽ���ˢ��UI
	 * 
	 * @param returnMap���ص�����
	 * @param type���������
	 */
	protected void updateUI(Map<String, Object> returnMap, String type) {
		Message message = handler.obtainMessage();
		if (returnMap != null) {
			if (type.equals("get_area")) {
				int respCode = (Integer) returnMap.get("respCode");
				if (respCode == 1) {
					schoolLists = (ArrayList<School>) returnMap
							.get("schoolList");
					String[] areaItems = new String[schoolLists.size()];
					if (schoolLists.size() > 0 && schoolLists != null) {
						for (int i = 0; i < schoolLists.size(); i++) {
							areaItems[i] = schoolLists.get(i).getName();
						}
						spAreaAdapter = new ArrayAdapter<String>(this,
								android.R.layout.simple_spinner_item, areaItems);
						spAreaAdapter
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						handler.sendEmptyMessage(LODING_AREA_DATA_SUCCESS);
					}
				}
			} else if (type.equals("take_express")) {
				int respCode = (Integer) returnMap.get("respCode");
				String respMsg = (String) returnMap.get("respMsg");
				if (respCode == 1) {
					Express express = (Express) returnMap.get("data");
					message.what = TAKE_EXPRESS_SUCCESS;
					message.obj = respMsg;
					handler.sendMessage(message);
				} else {
					message.what = TAKE_EXPRESS_FAIL;
					message.obj = respMsg;
					handler.sendMessage(message);
				}
			}
		} else {
			message.what = REQUEST_ERROR;
			handler.sendMessage(message);
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.sp_area:
			mAid = schoolLists.get(position).getId();
			break;
		case R.id.sp_express_shop:
			mCompany = (String) spExpressShop.getAdapter().getItem(position);
			break;
		case R.id.sp_express_weight:
			switch (position) {
			case 0:
				mLarge = 1;
				mMoney = 1;
				tvExpressWeighDdesc.setText("С��Ϊ�����20cmX10cmX14cm����");
				break;
			case 1:
				mLarge = 2;
				mMoney = 2;
				tvExpressWeighDdesc.setText("�м�Ϊ�����23cmX13cmX16cm����");
				break;
			case 2:
				mLarge = 3;
				mMoney = 3;
				tvExpressWeighDdesc.setText("���Ϊ�����43cmX23cmX27cm����");
				break;
			case 3:
				mLarge = 4;
				mMoney = 5;
				tvExpressWeighDdesc.setText("�������С��Ҫ������");
				break;
			}
			break;
		case R.id.sp_time:
			mArriveTime = (String) spTime.getAdapter().getItem(position);
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ���������ť
		case R.id.btn_order:
			startActivity(new Intent(this, OrderListActivity.class));
			break;
		// �ύ��ť
		case R.id.btn_take:
			getInputData();
			if (checkData()) {
				final ExpressDao expressDao = new ExpressDao();
				new Thread() {
					public void run() {
						Map<String, Object> returnMap = expressDao.getExpress(
								mPhone, mName, mTel, mExpressNo, mArriveTime,
								mCompany, mAid, mPayStyle, mLarge, mMoney,
								mRemark, mRoomId);
						updateUI(returnMap, "take_express");
					};
				}.start();
			}
			break;
		// ���˰�ť
		case R.id.iv_back:
			finish();
			break;
		}
	}

	/**
	 * ��ȡ���������
	 */
	private void getInputData() {
		mName = etName.getText().toString().trim();
		mTel = etPhone.getText().toString().trim();
		mRoomId = etRoomId.getText().toString().trim();
		mExpressNo = etExpressNo.getText().toString().trim();
		mRemark = etRemark.getText().toString().trim();
	}

	/**
	 * �����������ݵĺϷ���
	 * 
	 * @return
	 */
	private boolean checkData() {
		if (TextUtils.isEmpty(mName)) {
			ToastUtils.showToast(this, "��������Ϊ��");
			return false;
		}
		if (TextUtils.isEmpty(mTel)) {
			ToastUtils.showToast(this, "�绰����Ϊ��");
			return false;
		}
		if (!VerificationFormat.isMobile(mTel)) {
			ToastUtils.showToast(this, "�ֻ��Ÿ�ʽ����ȷ");
			return false;
		}
		if (TextUtils.isEmpty(mRoomId)) {
			ToastUtils.showToast(this, "���ҺŲ���Ϊ��");
			return false;
		}
		if (mAid == 0) {
			ToastUtils.showToast(this, "����������Ϊ��");
			return false;
		}
		if (TextUtils.isEmpty(mCompany)) {
			ToastUtils.showToast(this, "��ݹ�˾����Ϊ��");
			return false;
		}
		if (TextUtils.isEmpty(mArriveTime)) {
			ToastUtils.showToast(this, "����ʱ�䲻��Ϊ��");
			return false;
		}
		if (mLarge == 0) {
			ToastUtils.showToast(this, "�����С����Ϊ��");
			return false;
		}
		return true;
	}

	/**
	 * ���ɵ���ʱ��
	 * 
	 * @return
	 */
	private String[] initTimeData() {
		String times[] = new String[5];
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int today = calendar.get(Calendar.DAY_OF_MONTH);
		String format = String.format("%d.%d.%d", year, month, today);
		times[0] = format + "AM";
		times[1] = format + "PM";
		format = String.format("%d.%d.%d", year, month, today - 1);
		times[2] = format;
		format = String.format("%d.%d.%d", year, month, today - 2);
		times[3] = format;
		format = String.format("%d.%d.%d", year, month, today - 3);
		times[4] = format;
		Log.d("TAG", calendar.toString());
		return times;
	}

}
