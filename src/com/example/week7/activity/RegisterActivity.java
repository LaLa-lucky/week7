package com.example.week7.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.example.week7.dao.SchoolDao;
import com.example.week7.dao.UserDao;
import com.example.week7.domain.School;
import com.example.week7.utils.ToastUtils;
import com.example.week7.utils.VerificationFormat;

/**
 * ע�����
 * 
 * @author Administrator
 * 
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {
	protected static final int SHOW_DIALOG = 0;// ѡ��ѧУ�Ի���
	private static final int EMPTY_DATA = 1;// ��������Ϊ��
	private static final int REQUEST_FAIL = 2;// ����ʧ��
	private static final int REQUEST_ERROR = 3;// �������
	protected static final String tag = "TAG";
	private static final int REG_FAIL = 4;// ע��ʧ��
	private static final int REG_SUCCESS = 5;// ע��ɹ�
	private EditText etPhone;
	private EditText etVcode;
	private EditText etPassword;
	private EditText etRpassword;
	private TextView tvGetVcode;
	private TextView tvGetSchool;
	private ImageView ivBack;
	private Button btnReg;
	private String mPhone;
	private String mVcode;
	private String mPassword;
	private String mRpassword;
	private UserDao userDao;
	private ProgressDialog progressDialog;
	private TextView tvSchool;

	private int mChoosedItem = 0;// ��ǰ��ѡ�Ի���ѡ�е�ѧУ��Ŀ
	private int mSid;// ��ǰѡ�е�ѧУ�����ݿ��е�id

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_DIALOG:
				showChooseSchoolDialog((List<School>) msg.obj);// ��ʾѡ��ѧУ�ĶԻ���
				break;
			case EMPTY_DATA:
			case REQUEST_FAIL:
			case REQUEST_ERROR:
			case REG_FAIL:
				ToastUtils.showToast(RegisterActivity.this, (String) msg.obj);
				break;
			case REG_SUCCESS:
				ToastUtils.showToast(RegisterActivity.this, (String) msg.obj);
				// �򿪵�¼����
				Intent intent = new Intent(RegisterActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();// ����ע�����
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
		btnReg.setOnClickListener(this);
		tvGetVcode.setOnClickListener(this);
		tvGetSchool.setOnClickListener(this);
		ivBack.setOnClickListener(this);
	}

	/**
	 * ��ʼ��UI
	 */
	private void initView() {
		setContentView(R.layout.activity_reg);
		etPhone = (EditText) findViewById(R.id.et_phone);
		etVcode = (EditText) findViewById(R.id.et_vcode);
		etPassword = (EditText) findViewById(R.id.et_password);
		etRpassword = (EditText) findViewById(R.id.et_rpassword);
		tvGetVcode = (TextView) findViewById(R.id.tv_get_vcode);
		tvGetSchool = (TextView) findViewById(R.id.tv_get_school);
		btnReg = (Button) findViewById(R.id.btn_reg);
		tvSchool = (TextView) findViewById(R.id.tv_school);
		ivBack = (ImageView) findViewById(R.id.iv_back);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ע�ᰴť
		case R.id.btn_reg:
			getInputData();
			if (checkData()) {
				userDao = new UserDao(this);
				showProgressDialog();
				new Thread() {
					public void run() {
						SystemClock.sleep(2000);
						Map<String, Object> regReturnMap = userDao.register(
								mPhone, mPassword, mSid);
						closeProgressDialog();
						updateUI(regReturnMap, "reg");
					};
				}.start();

			}
			break;
		// ������֤��
		case R.id.tv_get_vcode:
			break;
		// ��ȡѧУ
		case R.id.tv_get_school:
			showProgressDialog();
			new Thread() {
				public void run() {
					Map<String, Object> schoolReturnMap = new SchoolDao(
							RegisterActivity.this).getSchool();
					closeProgressDialog();
					updateUI(schoolReturnMap, "getSchool");
				};
			}.start();
			break;
		// ����������ķ��ذ�ť
		case R.id.iv_back:
			finish();
			break;
		}
	}

	/**
	 * ���ݻ�ȡ������ˢ��UI
	 * 
	 * @param returnMap���ص�����
	 * @param string����
	 */
	protected void updateUI(Map<String, Object> returnMap, String type) {
		Message message = handler.obtainMessage();
		// �������
		if (returnMap == null) {
			message.what = REQUEST_ERROR;
			message.obj = "�������";
			handler.sendMessage(message);
			return;
		}
		if (type.equals("getSchool")) {
			int respCode = (Integer) returnMap.get("respCode");
			ArrayList<School> schoolList = (ArrayList<School>) returnMap
					.get("schoolList");
			// ����ɹ�
			if (respCode == 1) {
				// ���ݲ�Ϊ��
				if (schoolList.size() > 0) {
					message.what = SHOW_DIALOG;
					message.obj = schoolList;
					handler.sendMessage(message);
				} else {
					// ����Ϊ��
					message.what = EMPTY_DATA;
					message.obj = "ѧУ����Ϊ��";
					handler.sendMessage(message);
				}
			} else {
				// ����ʧ��
				message.obj = returnMap.get("respMsg");
				message.what = REQUEST_FAIL;
				handler.sendMessage(message);
			}
		} else if (type.equals("reg")) {
			int respCode = (Integer) returnMap.get("respCode");
			// ע��ɹ�
			if (respCode == 1) {
				message.obj = returnMap.get("respMsg");
				message.what = REG_SUCCESS;
				handler.sendMessage(message);
			} else {
				// ע��ʧ��
				message.obj = returnMap.get("respMsg");
				message.what = REG_FAIL;
				handler.sendMessage(message);
			}
		}

	}

	/**
	 * ������ݵĺϷ���
	 * 
	 * @return
	 */
	private boolean checkData() {
		if (TextUtils.isEmpty(mPhone)) {
			ToastUtils.showToast(this, "�ֻ��Ų���Ϊ��");
			return false;
		}
		if (TextUtils.isEmpty(mPassword)) {
			ToastUtils.showToast(this, "���벻��Ϊ��");
			return false;
		}
		if (TextUtils.isEmpty(mRpassword)) {
			ToastUtils.showToast(this, "ȷ�����벻��Ϊ��");
			return false;
		}
		if (!VerificationFormat.isMobile(mPhone)) {
			ToastUtils.showToast(this, "�ֻ��Ÿ�ʽ����ȷ");
			return false;
		}
		if (!mPassword.equals(mRpassword)) {
			ToastUtils.showToast(this, "�����������벻һ��");
			return false;
		}
		if (mSid == 0) {
			ToastUtils.showToast(this, "��ѡ��ѧУ");
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
	 * ��ʾѡ��ѧУ�ĶԻ���
	 * 
	 * @param obj
	 */
	private void showChooseSchoolDialog(final List<School> schoolList) {
		AlertDialog.Builder builder = new Builder(this);
		final String items[] = new String[schoolList.size()];
		for (int i = 0; i < schoolList.size(); i++) {
			items[i] = schoolList.get(i).getName();
		}
		builder.setTitle("��ѡ��ѧУ");
		builder.setSingleChoiceItems(items, mChoosedItem,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						tvSchool.setText(items[which]);// ��ʾѡ�е�ѧУ
						mSid = schoolList.get(which).getId();// ����Ҫע��ѧУ��id
						mChoosedItem = which;// ���õ����ѧУ����Ŀ���Ա��´�ֱ��ѡ��
						dialog.dismiss();// �رնԻ���
					}
				});
		builder.setCancelable(false);
		builder.show();
	}
}
