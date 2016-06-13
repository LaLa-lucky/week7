package com.example.week7.activity;

import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.week7.R;
import com.example.week7.dao.ComputerDao;
import com.example.week7.utils.ToastUtils;
import com.example.week7.utils.VerificationFormat;

/**
 * ����ά�޽���
 * 
 * @author Administrator
 * 
 */
public class ComputerActivity extends BaseActivity implements OnClickListener {
	private static final int REQUEST_ERROR = 0;// �������
	private static final int REQUEST_SUCCESS = 1;// ����ɹ�
	private static final int REQUEST_FAIL = 2;// ����ʧ��
	private EditText etName;
	private EditText etTel;
	private EditText etRoomId;
	private EditText etRemark;
	private Button ivBack;
	private Button btnOk;
	private String name;
	private String tel;
	private String roomid;
	private String remark;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REQUEST_ERROR:
			case REQUEST_FAIL:
				ToastUtils.showToast(ComputerActivity.this, (String) msg.obj);
				break;
			case REQUEST_SUCCESS:
				finish();
				ToastUtils.showToast(ComputerActivity.this, (String) msg.obj);
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initListener();
	}

	/**
	 * ��ʼ��������
	 */
	private void initListener() {
		ivBack.setOnClickListener(this);
		btnOk.setOnClickListener(this);
	}

	/**
	 * ��ʼ��UI
	 */
	private void initView() {
		setContentView(R.layout.activity_computer);
		ivBack = (Button) findViewById(R.id.iv_back);
		btnOk = (Button) findViewById(R.id.btn_ok);
		etName = (EditText) findViewById(R.id.et_name);
		etTel = (EditText) findViewById(R.id.et_tel);
		etRoomId = (EditText) findViewById(R.id.et_roomid);
		etRemark = (EditText) findViewById(R.id.et_remark);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ���������˳���ť
		case R.id.iv_back:
			finish();
			break;
		// ȷ�ϰ�ť
		case R.id.btn_ok:
			getInputData();
			if (checkData()) {
				new Thread() {
					public void run() {
						Map<String, Object> returnMap = new ComputerDao()
								.write(phone, tel, name, roomid, remark);
						updateUI(returnMap);
					};
				}.start();
			}
			break;
		}
	}

	/**
	 * ˢ�½���
	 * 
	 * @param returnMap
	 */
	protected void updateUI(Map<String, Object> returnMap) {
		Message message = handler.obtainMessage();
		if (returnMap == null) {
			message.obj = "�������";
			message.what = REQUEST_ERROR;
		} else {
			int respCode = (Integer) returnMap.get("respCode");
			if (respCode == 1) {
				message.what = REQUEST_SUCCESS;
				message.obj = "�ύ�ɹ�";
			} else {
				message.obj = returnMap.get("respMsg");
				message.what = REQUEST_FAIL;
			}
		}
		handler.sendMessage(message);
	}

	/**
	 * �����������ĺϷ���
	 * 
	 * @return
	 */
	private boolean checkData() {
		if (TextUtils.isEmpty(name)) {
			ToastUtils.showToast(this, "��������Ϊ��");
			return false;
		}
		if (TextUtils.isEmpty(tel)) {
			ToastUtils.showToast(this, "�ֻ��Ų���Ϊ��");
			return false;
		}
		if (!VerificationFormat.isMobile(tel)) {
			ToastUtils.showToast(this, "�ֻ��Ÿ�ʽ����ȷ");
			return false;
		}
		if (TextUtils.isEmpty(roomid)) {
			ToastUtils.showToast(this, "���ҺŲ���Ϊ��");
			return false;
		}
		if (TextUtils.isEmpty(remark)) {
			ToastUtils.showToast(this, "�������ݲ���Ϊ��");
			return false;
		}
		return true;
	}

	/**
	 * ��ȡ���������
	 */
	private void getInputData() {
		name = etName.getText().toString().trim();
		tel = etTel.getText().toString().trim();
		roomid = etRoomId.getText().toString().trim();
		remark = etRemark.getText().toString().trim();
	}
}
