package com.example.week7.activity;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.week7.R;
import com.example.week7.constant.Config;
import com.example.week7.dao.ParttimeDao;
import com.example.week7.utils.ToastUtils;
import com.example.week7.utils.VerificationFormat;

/**
 * ��ְ��ϸ����
 * 
 * @author Administrator
 * 
 */
public class ParttimeDetailActivity extends BaseActivity implements OnClickListener {
	private static final int ISPAST = 0;// ��ְ�ѹ���
	private static final int ISFULL = 1;// ������������
	private static final int ISAPPLY = 2;// �ѱ���
	private static final int CANAPPLY = 3;// �ɱ���
	private static final int APPLY_SUCCESS = 4;// �����ɹ�
	private static final int APPLY_FAIL = 5;// ����ʧ��
	private static final int APPLY_ERROR = 6;// ��������
	private TextView tvTitle;
	private WebView webView;
	private Button btnApply;
	private int id;// ��ǰ��ְ�����ݿ��е�id
	private LinearLayout llProgress;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// ������ֹ
			case ISPAST:
				btnApply.setVisibility(View.VISIBLE);
				btnApply.setEnabled(false);
				btnApply.setBackgroundColor(getResources().getColor(
						R.color.disable_button));
				btnApply.setText("�����ѽ�ֹ");
				break;
			// ��������
			case ISFULL:
				btnApply.setVisibility(View.VISIBLE);
				btnApply.setEnabled(false);
				btnApply.setBackgroundColor(getResources().getColor(
						R.color.disable_button));
				btnApply.setText("��������");
				break;
			// �ѱ���
			case ISAPPLY:
				btnApply.setVisibility(View.VISIBLE);
				btnApply.setEnabled(false);
				btnApply.setBackgroundColor(getResources().getColor(
						R.color.disable_button));
				btnApply.setText("�ѱ���");
				break;
			// ���Ա���
			case CANAPPLY:
				btnApply.setVisibility(View.VISIBLE);
				btnApply.setEnabled(true);
				int remainperson = (Integer) msg.obj;
				btnApply.setText("���ұ�����ʣ��" + remainperson + "�ˣ�");
				break;
			// �����ɹ�
			case APPLY_SUCCESS:
				ToastUtils.showToast(ParttimeDetailActivity.this,
						(String) msg.obj);
				dialog.dismiss();
				handler.sendEmptyMessage(ISAPPLY);// �����ѱ�����Ϣ
				break;
			// ����ʧ��
			case APPLY_FAIL:
				ToastUtils.showToast(ParttimeDetailActivity.this,
						(String) msg.obj);
				dialog.dismiss();
				break;
			// ��������
			case APPLY_ERROR:
				ToastUtils.showToast(ParttimeDetailActivity.this,
						(String) msg.obj);
				dialog.dismiss();
				break;
			default:
				ToastUtils.showToast(ParttimeDetailActivity.this, "���ش���");
				break;
			}
		}

	};
	private ImageView ivBack;
	private AlertDialog dialog;
	private EditText etPhone;
	private EditText etSid;
	private EditText etRoomid;
	private EditText etName;
	private String tel;
	private String sid;
	private String roomid;
	private String name;
	private String phone;

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
		btnApply.setOnClickListener(this);
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		id = (Integer) bundle.get("id");
		String title = (String) bundle.get("title");
		tvTitle.setText(title);
		SharedPreferences sp = getSharedPreferences("userinfo", MODE_PRIVATE);
		phone = sp.getString("phone", "");
		initWebView();// ��ʼ����̬����
		initApplyInfo();// ��ʼ����������
	}

	/**
	 * ��ʼ��������Ϣ
	 */
	private void initApplyInfo() {
		new Thread() {
			public void run() {
				Map<String, Integer> returnMap = new ParttimeDao()
						.getApplyInfo(id, phone);
				updateUI(returnMap);
			};
		}.start();
	}

	/**
	 * ����ǲ鿴���������ˢ��UI
	 * 
	 * @param returnMap
	 * @param type
	 */
	protected void updateUI(Map<String, Integer> returnMap) {
		if (returnMap != null) {
			int respCode = returnMap.get("respCode");
			if (respCode == 1) {
				int ispast = returnMap.get("ispast");
				int isfull = returnMap.get("isfull");
				int isapply = returnMap.get("isapply");
				int remainperson = returnMap.get("remainperson");
				// �жϼ�ְ�Ƿ����
				if (ispast == 1) {
					handler.sendEmptyMessage(ISPAST);
					return;
				}
				// �жϱ��������Ƿ�����
				if (isfull == 1) {
					handler.sendEmptyMessage(ISFULL);
					return;
				}
				// �ж��Ƿ��ѱ���
				if (isapply == 1) {
					handler.sendEmptyMessage(ISAPPLY);
					return;
				}
				// �ɱ���
				if (remainperson > 0) {
					Message message = handler.obtainMessage();
					message.what = CANAPPLY;
					message.obj = remainperson;
					handler.sendMessage(message);
					return;
				}
			}
		}
	}

	/**
	 * ��ʼ��webview
	 */
	private void initWebView() {
		String url = Config.SERVER_URL + "?c=Parttime&a=get_ptjob_by_id&id="
				+ id;
		webView.loadUrl(url);
		// ����javascript֧��
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				llProgress.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				llProgress.setVisibility(View.GONE);
			}

		});
	}

	/**
	 * ��ʼ��UI
	 */
	private void initView() {
		setContentView(R.layout.activity_parttime_detail);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		llProgress = (LinearLayout) findViewById(R.id.ll_progress);
		webView = (WebView) findViewById(R.id.webView);
		btnApply = (Button) findViewById(R.id.btn_apply);
		ivBack = (ImageView) findViewById(R.id.iv_back);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ���������˳���ť
		case R.id.iv_back:
			finish();
			break;
		// ������ť
		case R.id.btn_apply:
			showApplyDialog();
			break;
		// ȡ��������ť
		case R.id.btn_cancel:
			if (dialog != null) {
				dialog.dismiss();
			}
			break;
		// ȷ�ϱ�����ť
		case R.id.btn_ok:
			if (checkData()) {
				// �������д�뱨����Ϣ
				new Thread() {
					public void run() {
						Map<String, Object> returnMap = new ParttimeDao()
								.writeParttimeJob(id, phone, tel, sid, roomid,
										name);
						updateApplyUI(returnMap);
					};
				}.start();
			}
			break;
		}
	}

	/**
	 * ����֮��ˢ��UI
	 * 
	 * @param returnMap
	 */
	private void updateApplyUI(Map<String, Object> returnMap) {
		Message message = handler.obtainMessage();
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			String respMsg = (String) returnMap.get("respMsg");
			if (respCode == 1) {
				int isapply = (Integer) returnMap.get("isapply");
				if (isapply == 1) {
					message.what = APPLY_SUCCESS;
					message.obj = returnMap.get("respMsg");
					handler.sendMessage(message);
				} else {
					message.what = APPLY_FAIL;
					message.obj = returnMap.get("respMsg");
					handler.sendMessage(message);
				}
			} else {
				message.what = APPLY_FAIL;
				message.obj = returnMap.get("respMsg");
				handler.sendMessage(message);
			}
		} else {
			message.what = APPLY_ERROR;
			message.obj = "��������";
			handler.sendMessage(message);
		}
	}

	/**
	 * ����������Ϣ
	 * 
	 * @return
	 */
	private boolean checkData() {
		tel = etPhone.getText().toString().trim();
		sid = etSid.getText().toString().trim();
		roomid = etRoomid.getText().toString().trim();
		name = etName.getText().toString().trim();
		if (TextUtils.isEmpty(tel)) {
			ToastUtils.showToast(this, "�ֻ��Ų���Ϊ��");
			return false;
		}
		if (!VerificationFormat.isMobile(tel)) {
			ToastUtils.showToast(this, "�ֻ��Ÿ�ʽ����ȷ");
			return false;
		}
		if (TextUtils.isEmpty(sid)) {
			ToastUtils.showToast(this, "ѧ�Ų���Ϊ��");
			return false;
		}
		if (TextUtils.isEmpty(roomid)) {
			ToastUtils.showToast(this, "���ҺŲ���Ϊ��");
			return false;
		}
		if (TextUtils.isEmpty(name)) {
			ToastUtils.showToast(this, "��ʵ��������Ϊ��");
			return false;
		}
		return true;
	}

	/**
	 * ��ʾ������д��Ϣ�Ի���
	 */
	private void showApplyDialog() {
		AlertDialog.Builder builder = new Builder(ParttimeDetailActivity.this);
		dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_write_parttime, null);
		etPhone = (EditText) view.findViewById(R.id.et_phone);
		etSid = (EditText) view.findViewById(R.id.et_sid);
		etRoomid = (EditText) view.findViewById(R.id.et_roomid);
		etName = (EditText) view.findViewById(R.id.et_name);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		dialog.setView(view);
		dialog.show();
	}
}
