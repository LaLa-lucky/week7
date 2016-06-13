package com.example.week7.activity;

import java.util.ArrayList;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.week7.R;
import com.example.week7.adapter.CommentListViewAdapter;
import com.example.week7.dao.ParkDao;
import com.example.week7.domain.ParkComment;
import com.example.week7.utils.ToastUtils;
import com.example.week7.view.RefreshListView;

/**
 * ΢�����۽���
 * 
 * @author Administrator
 * 
 */
public class ParkCommentActivity extends BaseActivity implements
		OnClickListener {
	private static final int COMMENT_SUCCESS = 0;// ���۳ɹ�
	private static final int COMMENT_ERROR = 1;// ����ʧ��
	private static final int LOAD_COMMENT_SUCCESS = 2;// �������۳ɹ�
	private static final int LOAD_COMMENT_ERROR = 3;// ��������ʧ��
	private static final int EMPTY_DATA = 4;// ��������Ϊ��
	private Button btnOk;
	private EditText etContent;
	private ListView lvComment;
	private String pid;
	private ArrayList<ParkComment> commentList;
	private Handler handler = new Handler() {

		private CommentListViewAdapter adapter;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// ���۳ɹ�
			case COMMENT_SUCCESS:
				etContent.setText("");
				ToastUtils.showToast(ParkCommentActivity.this, "���۳ɹ�");
				initData();
				break;
			// ����ʧ��
			case COMMENT_ERROR:
				ToastUtils.showToast(ParkCommentActivity.this, "����ʧ��");
				break;
			// ���������б�ɹ�
			case LOAD_COMMENT_SUCCESS:
				adapter = new CommentListViewAdapter(ParkCommentActivity.this,
						commentList);
				lvComment.setAdapter(adapter);
				break;
			// ���������б�ʧ��
			case LOAD_COMMENT_ERROR:
				ToastUtils.showToast(ParkCommentActivity.this, "��������ʧ��");
				break;
			// ��������Ϊ��
			case EMPTY_DATA:
				ToastUtils.showToast(ParkCommentActivity.this, "��ǰ΢����ʱû���κ�����");
				break;
			default:
				ToastUtils.showToast(ParkCommentActivity.this, "�������");
				break;
			}
		}

	};
	private int comment_number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		comment_number = Integer.parseInt(getIntent().getStringExtra(
				"comment_number"));
		initView();
		initData();
		initListener();
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		pid = getIntent().getStringExtra("pid");
		new Thread() {

			public void run() {
				commentList = new ParkDao().getCommentList(pid, 0,
						Integer.MAX_VALUE);
				updateCommentList(commentList);
			};
		}.start();
	}

	@Override
	public void onBackPressed() {
		back();
	}

	@Override
	public void onBack(View view) {
		back();
	}

	/**
	 * ����parkfragmentʱ������ˢ������
	 */
	private void back() {
		Intent data = new Intent();
		data.putExtra("comment_number", comment_number);
		data.putExtra("position", getIntent().getIntExtra("position", 0));
		setResult(RESULT_OK, data);
		finish();
	}

	/**
	 * ���������б�
	 * 
	 * @param commentList
	 */
	protected void updateCommentList(ArrayList<ParkComment> commentList) {
		Message message = handler.obtainMessage();
		if (commentList != null) {
			if (commentList.size() > 0) {
				message.what = LOAD_COMMENT_SUCCESS;
			} else {
				message.what = EMPTY_DATA;
			}
		} else {
			message.what = LOAD_COMMENT_ERROR;
		}
		handler.sendMessage(message);
	}

	/**
	 * ��ʼ��������
	 */
	private void initListener() {
		btnOk.setOnClickListener(this);
	}

	/**
	 * ��ʼ��UI
	 */
	private void initView() {
		setContentView(R.layout.activity_park_comment);
		btnOk = (Button) findViewById(R.id.btn_ok);
		etContent = (EditText) findViewById(R.id.et_content);
		lvComment = (ListView) findViewById(R.id.lv_comment);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ȷ����ť
		case R.id.btn_ok:
			final String content = etContent.getText().toString();
			if (checkData(content)) {
				new Thread() {
					public void run() {
						Map<String, Object> returnMap = new ParkDao()
								.writeComment(phone, pid, content);
						updateUI(returnMap);
					};
				}.start();
			}
			break;
		}
	}

	/**
	 * ˢ��UI
	 * 
	 * @param returnMap
	 */
	protected void updateUI(Map<String, Object> returnMap) {
		Message message = handler.obtainMessage();
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			if (respCode == 1) {
				message.what = COMMENT_SUCCESS;
				comment_number++;
			} else {
				message.what = COMMENT_ERROR;
			}
		} else {
			message.what = COMMENT_ERROR;
		}
		handler.sendMessage(message);
	}

	/**
	 * �����������ĺϷ���
	 * 
	 * @param content
	 * @return
	 */
	private boolean checkData(String content) {
		if (TextUtils.isEmpty(content)) {
			ToastUtils.showToast(this, "����˵�㶫����~");
			return false;
		}
		if (content.length() > 144) {
			ToastUtils.showToast(this, "������������144����");
			return false;
		}
		return true;
	}
}
