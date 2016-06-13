package com.example.week7.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.week7.R;
import com.example.week7.utils.ActivityCollector;
import com.example.week7.utils.DataCleanUitls;

/**
 * �鿴����ҳ��
 * 
 * @author Administrator
 * 
 */
public class MoreActivity extends BaseActivity implements OnClickListener {
	private Button btnLoginout;
	private TextView tvIntroduce;
	private TextView tvUpdate;
	private TextView tvVersion;
	private ImageView ivBack;

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
		tvIntroduce.setOnClickListener(this);
		tvUpdate.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		btnLoginout.setOnClickListener(this);
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		tvVersion.setText("�汾��:" + getVersionName());
	}

	/**
	 * ��ȡ����app�İ汾��
	 * 
	 * @return
	 */
	private String getVersionName() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);// ��ȡ�汾��Ϣ
			int versionCode = packageInfo.versionCode;
			String versionName = packageInfo.versionName;
			System.out.println("versionCode=" + versionCode + ";versionName="
					+ versionName);
			return versionName;
		} catch (NameNotFoundException e) {
			// ��������ȷʱ������쳣
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * ��ȡ����app�İ汾��
	 * 
	 * @return
	 */
	private int getVersionCode() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);// ��ȡ�汾��Ϣ
			int versionCode = packageInfo.versionCode;
			return versionCode;
		} catch (NameNotFoundException e) {
			// ��������ȷʱ������쳣
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * ��ʼ��UI
	 */
	private void initView() {
		setContentView(R.layout.activity_more);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		btnLoginout = (Button) findViewById(R.id.btn_loginout);
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvIntroduce = (TextView) findViewById(R.id.tv_introduce);
		tvUpdate = (TextView) findViewById(R.id.tv_update);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// �������ķ��ذ�ť
		case R.id.iv_back:
			finish();
			break;
		// ����������
		case R.id.tv_introduce:
			Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			break;
		// �汾����
		case R.id.tv_update:
			break;
		// �˳���¼
		case R.id.btn_loginout:
			openConfirmLoginoutDialog();
			break;
		}
	}

	/**
	 * ��ȷ���˳��ĶԻ���
	 */
	private void openConfirmLoginoutDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("ȷ���˳�");
		builder.setMessage("���Ҫ�˳���");
		builder.setNegativeButton("ȡ��", null);
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ���shareprefrece
				emptySp();
				// �ر������Ѵ򿪵�Activity
				ActivityCollector.killAll();
				// �رնԻ���
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * ���sp�е�����
	 */
	protected void emptySp() {
		SharedPreferences sp = getSharedPreferences("userinfo", MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("isLogin", false);
		editor.putString("nickname", "");
		editor.putString("score", "");
		editor.putString("phone", "");
		editor.putString("sex", "");
		editor.putString("province", "");
		editor.putString("city", "");
		editor.putString("address", "");
		editor.putString("photo", "");
		editor.commit();
	}
}
