package com.example.week7.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.example.week7.domain.User;
import com.example.week7.utils.ActivityCollector;

/**
 * ����Activity�Ļ���
 * 
 * @author Administrator
 * 
 */
public class BaseActivity extends FragmentActivity {
	private SharedPreferences sp;
	protected String phone;// �绰���û��˺ţ�
	protected String nickname;// �û��ǳ�
	protected String sex;// �û��Ա�
	protected String address;// �û���ַ
	protected String photo;// �û�ͷ��
	protected String province;// ʡ
	protected String city;// ��
	protected String score;// ����
	protected User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
		sp = getSharedPreferences("userinfo", MODE_PRIVATE);
		phone = sp.getString("phone", "");// ��ȡ�û���
		nickname = sp.getString("nickname", "");
		sex = sp.getString("sex", "");
		province = sp.getString("province", "");
		city = sp.getString("city", "");
		address = sp.getString("address", "");
		photo = sp.getString("photo", "");
		score = sp.getString("score", "");
		user = new User();
		user.setPhone(phone);
		user.setNickname(nickname);
		user.setSex(sex);
		user.setAddress(address);
		user.setPhoto(photo);
		user.setProvince(province);
		user.setCity(city);
		user.setScore(score);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	/**
	 * ���������˳���ť��Ӧ�¼�
	 */
	public void onBack(View view) {
		finish();
	}

}
