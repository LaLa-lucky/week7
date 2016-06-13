package com.example.week7.activity;

import java.io.File;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.week7.R;
import com.example.week7.constant.Config;
import com.example.week7.dao.ParkDao;
import com.example.week7.utils.ToastUtils;
import com.example.week7.utils.UploadUtils;

/**
 * ����΢��ҳ��
 * 
 * @author Administrator
 * 
 */
public class ParkPublishActivity extends BaseActivity implements
		OnClickListener {
	private static final int PUBLISH_SUCCES = 0;
	private static final int PUBLISH_ERROR = 1;
	private static final int CHOOSE_PHOTO = 2;// �����ѡ��ͼƬ
	protected static final int UPLOAD_FILE_ERROR = 3;// �ϴ�ͼƬʧ��
	private EditText etContent;
	private Button btnChoosePic;
	private Button btnOk;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PUBLISH_SUCCES:
				ToastUtils.showToast(ParkPublishActivity.this, "�����ɹ�");
				Intent data = new Intent();
				setResult(RESULT_OK, data);
				finish();
				break;
			case PUBLISH_ERROR:
				ToastUtils.showToast(ParkPublishActivity.this, "����ʧ��");
				break;
			case UPLOAD_FILE_ERROR:
				ToastUtils.showToast(ParkPublishActivity.this, "ͼƬ�ϴ�ʧ��");
				break;
			default:
				ToastUtils.showToast(ParkPublishActivity.this, "�������");
				break;
			}
		}

	};
	private AlertDialog dialog;
	private File file;// ѡ����ļ�����

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
		btnChoosePic.setOnClickListener(this);
		btnOk.setOnClickListener(this);
	}

	/**
	 * ��ʼ��UI
	 */
	private void initView() {
		setContentView(R.layout.activity_park_publish);
		etContent = (EditText) findViewById(R.id.et_content);
		btnChoosePic = (Button) findViewById(R.id.btn_choose_pic);
		btnOk = (Button) findViewById(R.id.btn_ok);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ѡ��ͼƬ��ť
		case R.id.btn_choose_pic:
			showChoosePhotoDialog();
			break;
		// ȷ�ϰ�ť
		case R.id.btn_ok:
			final String content = etContent.getText().toString();
			if (TextUtils.isEmpty(content)) {
				ToastUtils.showToast(this, "���ݲ���Ϊ��");
				break;
			}
			if (content.length() > 144) {
				ToastUtils.showToast(this, "���ܳ���144���ַ�");
				break;
			}
			// ���������������
			new Thread() {
				public void run() {
					// ���ύͼƬ��������
					// �����ϴ�ͼƬ
					if (file != null) {
						String requestUrl = Config.SERVER_URL
								+ "?c=User&a=upload_img";
						String result = UploadUtils
								.uploadFile(file, requestUrl);
						if (!TextUtils.isEmpty(result)) {
							// ���΢��д��������
							Map<String, Object> returnMap = new ParkDao()
									.writePark(phone, content, result);
							updateUI(returnMap);
						} else {
							handler.sendEmptyMessage(UPLOAD_FILE_ERROR);
						}
					} else {
						// ���΢��д��������
						Map<String, Object> returnMap = new ParkDao()
								.writePark(phone, content, null);
						updateUI(returnMap);
					}

				};
			}.start();
			break;
		// �����ѡ��ͼƬ
		case R.id.btn_album:
			Intent intent = new Intent("android.intent.action.GET_CONTENT");
			intent.setType("image/*");
			startActivityForResult(intent, CHOOSE_PHOTO);// �����
			break;
		// ȡ��ѡ��ͼƬ
		case R.id.btn_cancel:
			closeChoosePhotoDialog();
			break;
		}
	}

	/**
	 * ��ʾѡ����Ƭ�ĶԻ���
	 */
	private void showChoosePhotoDialog() {
		AlertDialog.Builder builder = new Builder(this);
		dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_choose_photo, null);
		Button btnAlbum = (Button) view.findViewById(R.id.btn_album);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		Button btnTake = (Button) view.findViewById(R.id.btn_take);
		btnAlbum.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnTake.setOnClickListener(this);
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	/**
	 * �ر�ѡ����Ƭ�ĶԻ���
	 */
	private void closeChoosePhotoDialog() {
		if (dialog != null) {
			dialog.dismiss();
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
				message.what = PUBLISH_SUCCES;
			} else {
				message.what = PUBLISH_ERROR;
			}
		} else {
			message.what = PUBLISH_ERROR;
		}
		handler.sendMessage(message);
	}

	// ������Activity��������ʱ�ص�
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CHOOSE_PHOTO:
			if (resultCode == RESULT_OK) {
				// �ж��ֻ�ϵͳ�汾��
				if (Build.VERSION.SDK_INT >= 19) {
					file = handleImageOnKitKat(data);
				} else {
					file = handleImageBeforeKitKat(data);
				}
				// �жϷ��ص��ļ�����
				if (file != null) {
					btnChoosePic.setText("ͼƬ��ѡ��");
				} else {
					btnChoosePic.setText("���ѡ��ͼƬ");
				}
			}
			break;
		}
		closeChoosePhotoDialog();
	}

	private File handleImageBeforeKitKat(Intent data) {
		Uri uri = data.getData();
		String imagePath = getImagePath(uri, null);
		return returnImage(imagePath);
	}

	@SuppressLint("NewApi")
	private File handleImageOnKitKat(Intent data) {
		String imagePath = null;
		Uri uri = data.getData();
		if (DocumentsContract.isDocumentUri(this, uri)) {
			// �����document���͵�Uri����ͨ��document id����
			String docId = DocumentsContract.getDocumentId(uri);
			if ("com.android.providers.media.documents".equals(uri
					.getAuthority())) {
				String id = docId.split(":")[1];// ���������ָ�ʽ��id
				String selection = MediaStore.Images.Media._ID + "=" + id;
				imagePath = getImagePath(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
			} else if ("com.android.providers.downloads.documents".equals(uri
					.getAuthority())) {
				Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(docId));
				imagePath = getImagePath(contentUri, null);
			} else if ("content".equalsIgnoreCase(uri.getScheme())) {
				// �������document���͵�Uri����ʹ����ͨ��ʽ����
				imagePath = getImagePath(uri, null);
			}
		}
		return returnImage(imagePath);// ����ͼƬ·����ʾͼƬ
	}

	/**
	 * ��ʾͼƬ
	 * 
	 * @param imagePath
	 */
	private File returnImage(String imagePath) {
		if (imagePath != null) {
			File file = new File(imagePath);
			return file;
		} else {
			Toast.makeText(this, "Failed to get image ", 0).show();
		}
		return null;
	}

	private String getImagePath(Uri uri, String selection) {
		String path = null;
		// ͨ����Uri��selection����ȡ��ʵ��ͼƬ·��
		Cursor cursor = getContentResolver().query(uri, null, selection, null,
				null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				path = cursor.getString(cursor.getColumnIndex(Media.DATA));
			}
			cursor.close();
		}
		return path;
	}

}
