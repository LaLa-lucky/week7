package com.example.week7.activity;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View; 
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.week7.R;
import com.example.week7.dao.UserDao;
import com.example.week7.utils.ToastUtils;
import com.example.week7.utils.VerificationFormat;

/**
 * �����������
 * 
 * @author Administrator
 * 
 */
public class ForgetPasswordActivity extends BaseActivity implements OnClickListener {
	private static final int REQUEST_ERROR = 0;// �������
	private static final int REQUEST_FAIL = 1;// ����ʧ��
	private static final int REQUEST_SUCCESS = 3;// ����ɹ�
	private EditText etPhone;
	private EditText etVcode;
	private EditText etPassword;
	private EditText etRpassword;
	private Button btnEditPassword;
	private TextView tvGetVcode;
	private ImageView ivBack;

	private String mPhone;// ����绰
	private String mPassword;// ���������
	private String mRpassword;// �����ȷ������
	private String mVcode;// �������֤��
	private ProgressDialog progressDialog;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REQUEST_ERROR:
				break;
			case REQUEST_FAIL:
				ToastUtils.showToast(ForgetPasswordActivity.this,
						(String) msg.obj);
				break;
			case REQUEST_SUCCESS:
				ToastUtils.showToast(ForgetPasswordActivity.this,
						(String) msg.obj);
				Intent intent = new Intent(ForgetPasswordActivity.this,
						LoginActivity.class);
				startActivity(intent);
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
		initListener();
	}

	/**
	 * �󶨼�����
	 */
	private void initListener() {
		tvGetVcode.setOnClickListener(this);
		btnEditPassword.setOnClickListener(this);
		ivBack.setOnClickListener(this);
	}

	/**
	 * ��ʼ��UI
	 */
	private void initView() {
		setContentView(R.layout.activity_forgetpassword);
		etPhone = (EditText) findViewById(R.id.et_phone);
		etVcode = (EditText) findViewById(R.id.et_vcode);
		etPassword = (EditText) findViewById(R.id.et_password);
		etRpassword = (EditText) findViewById(R.id.et_rpassword);
		btnEditPassword = (Button) findViewById(R.id.btn_edit_password);
		tvGetVcode = (TextView) findViewById(R.id.tv_get_vcode);
		ivBack = (ImageView) findViewById(R.id.iv_back);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ����������ϵķ��ذ�ť
		case R.id.iv_back:
			finish();
			break;
		// ��ȡ��֤��
		case R.id.tv_get_vcode:
			break;
		// �޸�����
		case R.id.btn_edit_password:
			getInputData();
			if (checkData()) {
				// ��dao���æ��������
				final UserDao userDao = new UserDao(this);
				showProgressDialog();
				new Thread() {
					public void run() {
						SystemClock.sleep(2000);
						HashMap<String, Object> returnMap = userDao
								.editPassword(mPhone, mPassword);
						closeProgressDialog();
						updateUI(returnMap);
					};
				}.start();
			}
			break;
		}
	}

	/**
	 * ���ݽ���������ݽ���ˢ��UI
	 * 
	 * @param returnMap
	 */
	protected void updateUI(HashMap<String, Object> returnMap) {
		Message message = handler.obtainMessage();
		if (returnMap == null) {
			message.what = REQUEST_ERROR;
			message.obj = "�������";
		} else {
			int respCode = (Integer) returnMap.get("respCode");
			// �޸�����ʧ��
			if (respCode == 0) {
				message.what = REQUEST_FAIL;
				message.obj = returnMap.get("respMsg");
			} else {
				// �޸�����ɹ�
				message.what = REQUEST_SUCCESS;
				message.obj = returnMap.get("respMsg");
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
		if (TextUtils.isEmpty(mPhone)) {
			ToastUtils.showToast(this, "�ֻ��Ų���Ϊ��");
			return false;
		}
		if (!VerificationFormat.isMobile(mPhone)) {
			ToastUtils.showToast(this, "�ֻ��Ÿ�ʽ����ȷ");
			return false;
		}
		if (TextUtils.isEmpty(mPassword)) {
			ToastUtils.showToast(this, "�����벻��Ϊ��");
			return false;
		}
		if (TextUtils.isEmpty(mRpassword)) {
			ToastUtils.showToast(this, "ȷ�����벻��Ϊ��");
			return false;
		}
		if (!mPassword.equals(mRpassword)) {
			ToastUtils.showToast(this, "�������벻һ��");
			return false;
		}
		return true;
	}

	/**
	 * ��ʾ���صĶԻ���
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ�����...");
			progressDialog.show();
		}
	}

	/**
	 * �رնԻ���
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	/**
	 * ��ȡ���������
	 */
	private void getInputData() {
		mPhone = etPhone.getText().toString().trim();
		mVcode = etVcode.getText().toString().trim();
		mPassword = etPassword.getText().toString().trim();
		mRpassword = etRpassword.getText().toString().trim();
	}
}
