package com.example.week7.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.week7.R;
import com.example.week7.dao.UserDao;
import com.example.week7.domain.User;
import com.example.week7.utils.ToastUtils;
import com.example.week7.utils.VerificationFormat;

/**
 * ��¼����
 * 
 * @author Administrator
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	private EditText etPhone;
	private EditText etPassword;
	private Button btnLogin;
	private TextView tvForget;
	private TextView tvReg;
	private String mPhone;// ����ĵ绰
	private String mPassword;// ���������
	private static final String tag = "TAG";
	private static final int REQUEST_FAIL = 0;// ����ʧ��
	private static final int REQUEST_ERROR = 1;// �������
	private static final int REQUEST_SUCCESS = 2;// ����ɹ�
	private static final int ERROR_USER_STATE = 3;// ������û�״̬
	private ProgressDialog progressDialog;// �����еĶԻ���
	private UserDao userDao;// �û�ҵ������

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ERROR_USER_STATE:
			case REQUEST_ERROR:
			case REQUEST_FAIL:
				// ʧ�ܣ�������˾�Ի���
				ToastUtils.showToast(LoginActivity.this, (String) msg.obj);
				break;
			case REQUEST_SUCCESS:
				// ��¼�ɹ�����תҳ��
				ToastUtils.showToast(LoginActivity.this, "��¼�ɹ�");
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
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
		btnLogin.setOnClickListener(this);
		tvForget.setOnClickListener(this);
		tvReg.setOnClickListener(this);
	}

	/**
	 * ��ʼ��UI
	 */
	private void initView() {
		setContentView(R.layout.activity_login);
		etPhone = (EditText) findViewById(R.id.et_phone);
		etPassword = (EditText) findViewById(R.id.et_password);
		btnLogin = (Button) findViewById(R.id.btn_login);
		tvForget = (TextView) findViewById(R.id.tv_forget);
		tvReg = (TextView) findViewById(R.id.tv_reg);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ��¼
		case R.id.btn_login:
			getInputData();// ��ȡ���������
			// ������ݵĺϷ���
			if (checkData()) {
				userDao = new UserDao(this);
				showProgressDialog();// �������ظ���Ի���
				// �����߳̽��е�¼����
				new Thread() {
					public void run() {
						SystemClock.sleep(2000);
						HashMap<String, Object> returnMap = userDao.login(
								mPhone, mPassword);// ���е�¼����
						closeProgressDialog();// �رռ��ظ���ĶԻ���
						updateUI(returnMap);
					};
				}.start();
			}
			break;
		// ע��
		case R.id.tv_reg:
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			break;
		// ��������
		case R.id.tv_forget:
			Intent i = new Intent(this, ForgetPasswordActivity.class);
			startActivity(i);
			break;
		}
	}

	/**
	 * �����������ĺϷ���
	 * 
	 * @return
	 */
	private boolean checkData() {
		if (TextUtils.isEmpty(mPhone)) {
			ToastUtils.showToast(this, "�û�������Ϊ��");
			return false;
		}
		if (TextUtils.isEmpty(mPassword)) {
			ToastUtils.showToast(this, "���벻��Ϊ��");
			return false;
		}
		if (!VerificationFormat.isMobile(mPhone)) {
			ToastUtils.showToast(this, "�ֻ��Ÿ�ʽ����ȷ");
			return false;
		}
		return true;
	}

	/**
	 * ���ݷ��ص����ݣ�����ˢ��UI
	 * 
	 * @param respMap���ص�����
	 */
	public void updateUI(Map<String, Object> respMap) {
		Message message = handler.obtainMessage();
		if (respMap == null) {
			// �������
			message.what = REQUEST_ERROR;
			message.obj = "����ʧ��";
		} else {
			// ����ɹ�
			if ((Integer) respMap.get("respCode") == 1) {
				User user = (User) respMap.get("user");
				if (!user.isUseful()) {
					// �û�������
					message.what = ERROR_USER_STATE;
					message.obj = "�û�״̬Ϊ�����ã�����ϵ����Ա";
				} else {
					// ��¼�ɹ�
					message.what = REQUEST_SUCCESS;
					message.obj = respMap.get("respMsg");
					userDao.saveUserInfo(user);// ��¼�ɹ��󣬱����û���Ϣ
				}
			} else {
				// ��¼ʧ��
				message.what = REQUEST_FAIL;
				message.obj = respMap.get("respMsg");
			}
		}
		handler.sendMessage(message);
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
		mPassword = etPassword.getText().toString().trim();
	}
}
