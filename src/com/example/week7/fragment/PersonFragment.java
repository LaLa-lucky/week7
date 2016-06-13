package com.example.week7.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.week7.R;
import com.example.week7.activity.ChooseLocationActivity;
import com.example.week7.activity.MoreActivity;
import com.example.week7.activity.MyParttimeActivity;
import com.example.week7.activity.OrderListActivity;
import com.example.week7.constant.Config;
import com.example.week7.dao.UserDao;
import com.example.week7.domain.Location;
import com.example.week7.domain.User;
import com.example.week7.utils.BitmapUtils;
import com.example.week7.utils.ToastUtils;
import com.example.week7.utils.UploadUtils;
import com.example.week7.view.PullScrollView;
import com.example.week7.view.PullScrollView.OnTurnListener;

/**
 * ���˵�Fragment
 * 
 * @author Administrator
 * 
 */
public class PersonFragment extends Fragment implements OnTurnListener,
		OnClickListener {
	private static final int QIANDAO_SUCCESS = 0;// ǩ���ɹ�
	private static final int QIANDAO_FAIL = 1;// ǩ��ʧ��
	private static final int QIANDAO_ERROR = 2;// ǩ������
	private static final int EDITNICKNAME_SUCCESS = 3;// �޸��ǳƳɹ�
	private static final int EDITNICKNAME_FAIL = 4;// �޸��ǳ�ʧ��
	private static final int EDITNICKNAME_ERROR = 5;// �޸��ǳƴ���
	private static final int EDITSEX_SUCCESS = 6;// �޸��Ա�ɹ�
	private static final int EDITNICKSEX_FAIL = 7;// �޸��Ա�ʧ��
	private static final int EDITNICKSEX_ERROR = 8;// �޸��Ա����
	private static final int CHOOSE_PHOTO = 9;// �����ѡ��ͼƬ
	private static final int TAKE_PHOTO = 10;// ��������
	private static final int CROP_PHOTO = 11;// �ü�ͼƬ
	protected static final int UPLOAD_IMAGE_ERROR = 12;// �ϴ�ͼƬʧ��
	private static final int WRITEIMG_SUCCESS = 13;// ͼƬ�ϴ��ɹ�
	private static final int WRITEIMG_FAIL = 14;// ͼƬ�ϴ�ʧ��
	private static final int WRITEIMG_ERROR = 15;// ͼƬ�ϴ�����
	private static final int REQUEST_LOCATION_CODE = 16;// ����λҳ��ı�־
	private PullScrollView mScrollView;
	private ImageView mHeadImg;
	private ImageView ivPhoto;
	private TextView tvQiandao;
	private TextView tvScore;
	private TextView tvUsername;
	private LinearLayout llParttime;
	private LinearLayout llExpress;
	private LinearLayout llNickname;
	private LinearLayout llLocation;
	private LinearLayout llSex;
	private LinearLayout llMore;
	private TextView tvTel;
	private TextView tvNickname;
	private TextView tvSex;
	private User user;
	private Uri imageUri;
	private AlertDialog dialog;

	/**
	 * Handler
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// ǩ��ʧ��
			case QIANDAO_FAIL:
				// ǩ������
			case QIANDAO_ERROR:
				ToastUtils.showToast(getActivity(), (String) msg.obj);
				break;
			// ǩ���ɹ�
			case QIANDAO_SUCCESS:
				ToastUtils.showToast(getActivity(), "ǩ���ɹ�");
				tvScore.setText("�ҵĻ��֣�" + msg.obj);
				saveInfo("score", msg.obj);
				break;
			// �޸��ǳ�ʧ��
			case EDITNICKNAME_FAIL:
				// �޸��ǳƴ���
			case EDITNICKNAME_ERROR:
				ToastUtils.showToast(getActivity(), (String) msg.obj);
				break;
			// �޸��ǳƳɹ�
			case EDITNICKNAME_SUCCESS:
				ToastUtils.showToast(getActivity(), "�ǳ��޸ĳɹ�");
				tvNickname.setText((String) msg.obj);
				tvUsername.setText((String) msg.obj);
				saveInfo("nickname", msg.obj);
				break;
			// �޸��Ա����
			case EDITNICKSEX_ERROR:
				// �޸��Ա�ʧ��
			case EDITNICKSEX_FAIL:
				ToastUtils.showToast(getActivity(), (String) msg.obj);
				break;
			// �޸��Ա�ɹ�
			case EDITSEX_SUCCESS:
				ToastUtils.showToast(getActivity(), "�Ա��޸ĳɹ�");
				tvSex.setText((String) msg.obj);
				saveInfo("sex", msg.obj);
				break;
			// �޸�ͷ�����
			case WRITEIMG_ERROR:
				// �޸�ͷ��ʧ��
			case WRITEIMG_FAIL:
				ToastUtils.showToast(getActivity(), (String) msg.obj);
				break;
			// �޸�ͷ��ɹ�
			case WRITEIMG_SUCCESS:
				ToastUtils.showToast(getActivity(), "ͷ���ϴ��ɹ�");
				Map<String, Object> map = (Map<String, Object>) msg.obj;
				String path = (String) map.get("path");
				Bitmap bitmap = (Bitmap) map.get("bitmap");
				ivPhoto.setImageBitmap(bitmap);// ��ʾͷ��
				saveInfo("photo", path);
				break;
			default:
				ToastUtils.showToast(getActivity(), "�������");
				break;
			}
		}

	};
	private TextView tvLocation;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView(inflater);
		initData();
		initListener();
		return view;
	}

	/**
	 * ���޸ĺ����Ϣ���浽sp��
	 * 
	 * @param obj
	 */
	protected void saveInfo(String key, Object obj) {
		SharedPreferences sp = getActivity().getSharedPreferences("userinfo",
				Context.MODE_PRIVATE);
		sp.edit().putString(key, obj + "").commit();
	}

	/**
	 * ��ʼ�����ݲ���������
	 */
	private void initData() {
		Bundle arguments = getArguments();
		user = (User) arguments.getSerializable("user");
		Glide.with(getActivity()).load(user.getPhoto()).into(ivPhoto);
		tvScore.setText("�ҵĻ��֣�" + user.getScore());
		tvUsername.setText(user.getNickname());
		tvTel.setText(user.getPhone());
		tvNickname.setText(user.getNickname());
		tvSex.setText(user.getSex());
		tvLocation.setText(user.getProvince() + user.getCity()
				+ user.getAddress());
	}

	/**
	 * ��ʼ��������
	 */
	private void initListener() {
		tvQiandao.setOnClickListener(this);
		llParttime.setOnClickListener(this);
		llExpress.setOnClickListener(this);
		llNickname.setOnClickListener(this);
		llLocation.setOnClickListener(this);
		llSex.setOnClickListener(this);
		llMore.setOnClickListener(this);
		ivPhoto.setOnClickListener(this);
	}

	/**
	 * ��ʼ��UI
	 * 
	 * @param inflater
	 * @return
	 */
	protected View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_person, null);
		mScrollView = (PullScrollView) view.findViewById(R.id.scroll_view);
		mHeadImg = (ImageView) view.findViewById(R.id.background_img);
		ivPhoto = (ImageView) view.findViewById(R.id.iv_photo);
		tvQiandao = (TextView) view.findViewById(R.id.tv_qiandao);
		tvScore = (TextView) view.findViewById(R.id.tv_score);
		tvUsername = (TextView) view.findViewById(R.id.user_name);
		llParttime = (LinearLayout) view.findViewById(R.id.ll_parttime);
		llExpress = (LinearLayout) view.findViewById(R.id.ll_express);
		llNickname = (LinearLayout) view.findViewById(R.id.ll_nickname);
		llLocation = (LinearLayout) view.findViewById(R.id.ll_location);
		tvLocation = (TextView) view.findViewById(R.id.tv_location);
		llSex = (LinearLayout) view.findViewById(R.id.ll_sex);
		llMore = (LinearLayout) view.findViewById(R.id.ll_more);
		tvTel = (TextView) view.findViewById(R.id.tv_tel);
		tvNickname = (TextView) view.findViewById(R.id.tv_nickname);
		tvSex = (TextView) view.findViewById(R.id.tv_sex);

		mScrollView.setHeader(mHeadImg);
		mScrollView.setOnTurnListener(this);
		return view;
	}

	@Override
	public void onTurn() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ǩ����ť
		case R.id.tv_qiandao:
			new Thread() {
				public void run() {
					HashMap<String, Object> returnMap = new UserDao(
							getActivity()).qiaodao(user.getPhone());
					updateQiandaoUI(returnMap);
				};
			}.start();
			break;
		// �ҵļ�ְ
		case R.id.ll_parttime:
			Intent myparttimeIntent = new Intent(getActivity(),
					MyParttimeActivity.class);
			startActivity(myparttimeIntent);
			break;
		// �ҵĿ��
		case R.id.ll_express:
			Intent intent = new Intent(getActivity(), OrderListActivity.class);
			startActivity(intent);
			break;
		// �ǳ�
		case R.id.ll_nickname:
			openEditNicknameDialog();
			break;
		// �Ա�
		case R.id.ll_sex:
			openEditSexDialog();
			break;
		// ����
		case R.id.ll_location:
			// �򿪶�λѡ��
			Intent chooseLocationIntent = new Intent(getActivity(),
					ChooseLocationActivity.class);
			startActivityForResult(chooseLocationIntent, REQUEST_LOCATION_CODE);
			break;
		// ����
		case R.id.ll_more:
			Intent moreIntent = new Intent(getActivity(), MoreActivity.class);
			startActivity(moreIntent);
			break;
		// ͷ��
		case R.id.iv_photo:
			showChoosePhotoDialog();
			break;
		// �����ѡ��ͼƬ
		case R.id.btn_album:
			Intent i = new Intent("android.intent.action.GET_CONTENT");
			i.setType("image/*");
			startActivityForResult(i, CHOOSE_PHOTO);// �����
			break;
		// ȡ��ѡ��ͼƬ
		case R.id.btn_cancel:
			closeChoosePhotoDialog();
			break;
		// ��������
		case R.id.btn_take:
			File file = new File(Environment.getExternalStorageDirectory(),
					"output_image.jpg");
			try {
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
			imageUri = Uri.fromFile(file);
			Intent takePhotoIntent = new Intent();
			takePhotoIntent.setAction("android.media.action.IMAGE_CAPTURE");
			takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(takePhotoIntent, TAKE_PHOTO);
			break;
		}
	}

	/**
	 * ���޸��Ա�ĶԻ���
	 */
	private void openEditSexDialog() {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setTitle("��ѡ���Ա�");
		final String items[] = { "��", "Ů" };
		String sex = getActivity().getSharedPreferences("userinfo",
				Context.MODE_PRIVATE).getString("sex", "");
		int position = 0;
		for (int i = 0; i < items.length; i++) {
			if (sex.equals(items[i]))
				position = i;
		}
		builder.setSingleChoiceItems(items, position,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						final String sex = items[which];
						new Thread() {
							public void run() {
								HashMap<String, Object> returnMap = new UserDao(
										getActivity()).editSex(user.getPhone(),
										sex);
								updateWriteSexUI(returnMap, sex);
							};
						}.start();
						dialog.dismiss();
					}
				});
		builder.show();
	}

	/**
	 * ���޸��ǳƵĶԻ���
	 */
	private void openEditNicknameDialog() {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setTitle("�޸��ǳ�");
		View view = View.inflate(getActivity(), R.layout.dialog_edit_nickname,
				null);
		final EditText etNickname = (EditText) view
				.findViewById(R.id.et_nickname);
		etNickname.setText(getActivity().getSharedPreferences("userinfo",
				Context.MODE_PRIVATE).getString("nickname", ""));
		builder.setView(view);
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// �޸��ǳ�
				new Thread() {
					public void run() {
						String nickname = etNickname.getText().toString()
								.trim();
						HashMap<String, Object> returnMap = new UserDao(
								getActivity()).editNickname(user.getPhone(),
								nickname);
						updateWriteNicknameUI(returnMap, nickname);
					}

				}.start();
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * ˢ��д�Ա�������
	 * 
	 * @param returnMap
	 * @param nickname
	 */
	private void updateWriteSexUI(HashMap<String, Object> returnMap, String sex) {
		Message message = handler.obtainMessage();
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			String respMsg = (String) returnMap.get("respMsg");
			if (respCode == 1) {
				message.obj = sex;
				message.what = EDITSEX_SUCCESS;
			} else {
				message.what = EDITNICKSEX_FAIL;
				message.obj = respMsg;
			}
		} else {
			message.what = EDITNICKSEX_ERROR;
			message.obj = "�������";
		}
		handler.sendMessage(message);
	}

	/**
	 * ˢ��д�ǳƺ������
	 * 
	 * @param returnMap
	 * @param nickname
	 */
	private void updateWriteNicknameUI(HashMap<String, Object> returnMap,
			String nickname) {
		Message message = handler.obtainMessage();
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			String respMsg = (String) returnMap.get("respMsg");
			if (respCode == 1) {
				message.obj = nickname;
				message.what = EDITNICKNAME_SUCCESS;
			} else {
				message.what = EDITNICKNAME_FAIL;
				message.obj = respMsg;
			}
		} else {
			message.what = EDITNICKNAME_ERROR;
			message.obj = "�������";
		}
		handler.sendMessage(message);
	}

	/**
	 * ˢ��ǩ����Ľ���
	 */
	protected void updateQiandaoUI(HashMap<String, Object> returnMap) {
		Message message = handler.obtainMessage();
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			String respMsg = (String) returnMap.get("respMsg");
			if (respCode == 1) {
				int score = (Integer) returnMap.get("score");
				message.obj = score;
				message.what = QIANDAO_SUCCESS;
			} else {
				message.what = QIANDAO_FAIL;
				message.obj = respMsg;
			}
		} else {
			message.what = QIANDAO_ERROR;
			message.obj = "�������";
		}
		handler.sendMessage(message);
	}

	/**
	 * ˢ��ǩ����Ľ���
	 * 
	 * @param bitmap
	 */
	protected void updateWriteImgUI(Map<String, Object> returnMap, String path,
			Bitmap bitmap) {
		Message message = handler.obtainMessage();
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			if (respCode == 1) {
				message.what = WRITEIMG_SUCCESS;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("path", path);
				map.put("bitmap", bitmap);
				message.obj = map;
			} else {
				message.what = WRITEIMG_FAIL;
				message.obj = "ͷ���ϴ�ʧ��";
			}
		} else {
			message.what = WRITEIMG_ERROR;
			message.obj = "�������";
		}
		handler.sendMessage(message);
	}

	/**
	 * ��ʾѡ����Ƭ�ĶԻ���
	 */
	private void showChoosePhotoDialog() {
		AlertDialog.Builder builder = new Builder(getActivity());
		dialog = builder.create();
		View view = View.inflate(getActivity(), R.layout.dialog_choose_photo,
				null);
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
		dialog.dismiss();
	}

	// ������Activity��������ʱ�ص�
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// �Ӷ�λҳ���ȡ����������
		case REQUEST_LOCATION_CODE:
			if (resultCode == Activity.RESULT_OK) {
				final Location location = (Location) data
						.getSerializableExtra("location");
				// ��ʾ��λ��Ϣ
				tvLocation.setText(location.getProvince() + location.getCity()
						+ location.getAddress());
				// �Ѷ�λ��Ϣ�����Ϣд����������
				new Thread() {
					@Override
					public void run() {
						new UserDao(getActivity()).editAddress(user.getPhone(),
								location.getProvince(), location.getCity(),
								location.getAddress());
					}
				}.start();
				// �Ѷ�λ��Ϣд������
				saveInfo("province", location.getProvince());
				saveInfo("city", location.getCity());
				saveInfo("address", location.getAddress());
			}
			break;
		// ����
		case TAKE_PHOTO:
			if (resultCode == Activity.RESULT_OK) {
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);
			}
			closeChoosePhotoDialog();
			break;
		// �ü�ͼƬ
		case CROP_PHOTO:
			if (resultCode == Activity.RESULT_OK) {
				// Bitmap bitmap = BitmapUtils
				// .load(getActivity(),);
				// uploadImage(bitmap);
			}
			closeChoosePhotoDialog();
			break;
		// ѡ��ͼƬ
		case CHOOSE_PHOTO:
			if (resultCode == Activity.RESULT_OK) {
				// �ж��ֻ�ϵͳ�汾��
				if (Build.VERSION.SDK_INT >= 19) {
					handleImageOnKitKat(data);
				} else {
					handleImageBeforeKitKat(data);
				}
			}
			closeChoosePhotoDialog();
			break;
		}
	}

	private void handleImageBeforeKitKat(Intent data) {
		Uri uri = data.getData();
		String imagePath = getImagePath(uri, null);
		displayImage(imagePath);
	}

	@SuppressLint("NewApi")
	private void handleImageOnKitKat(Intent data) {
		String imagePath = null;
		Uri uri = data.getData();
		if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
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
		displayImage(imagePath);// ����ͼƬ·����ʾͼƬ
	}

	/**
	 * ��ʾͼƬ
	 * 
	 * @param imagePath
	 */
	private void displayImage(String imagePath) {
		if (imagePath != null) {
			final Bitmap bitmap = BitmapUtils.load(getActivity(), imagePath);
			File file = new File(imagePath);
			uploadImage(bitmap, file);
		} else {
			Toast.makeText(getActivity(), "Failed to get image ", 0).show();
		}
	}

	private String getImagePath(Uri uri, String selection) {
		String path = null;
		// ͨ����Uri��selection����ȡ��ʵ��ͼƬ·��
		Cursor cursor = getActivity().getContentResolver().query(uri, null,
				selection, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				path = cursor.getString(cursor.getColumnIndex(Media.DATA));
			}
			cursor.close();
		}
		return path;
	}

	/**
	 * �ϴ�ͷ��
	 */
	public void uploadImage(final Bitmap bitmap, final File file) {
		// �ϴ�ͷ��
		new Thread() {
			public void run() {
				// �����ϴ�ͼƬ
				String requestUrl = Config.SERVER_URL + "?c=User&a=upload_img";
				String result = UploadUtils.uploadFile(file, requestUrl);
				// Ȼ���޸��û�ͷ������
				if (!TextUtils.isEmpty(result)) {
					Map<String, Object> returnMap = new UserDao(getActivity())
							.writeImg(user.getPhone(), result);
					updateWriteImgUI(returnMap, result, bitmap);
				} else {
					handler.sendEmptyMessage(UPLOAD_IMAGE_ERROR);
				}
			};
		}.start();
	}
}
