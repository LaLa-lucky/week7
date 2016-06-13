package com.example.week7.dao;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

import com.example.week7.constant.Config;
import com.example.week7.domain.User;
import com.example.week7.http.HttpUtils;
import com.example.week7.utils.DesTools;

/**
 * �û�ҵ���
 * 
 * @author Administrator
 * 
 */
public class UserDao {

	private SharedPreferences sp;// �����û���Ϣ��sp����
	private Context context;// �����Ķ���
	private HashMap<String, Object> loginReturnMap;// �����¼���ص�����
	private HashMap<String, Object> regReturnMap;// ����ѧУ���ص�����

	/**
	 * ���캯��
	 * 
	 * @param user
	 */
	public UserDao(Context context) {
		this.context = context;
	}

	/**
	 * �����ֻ��ź�������е�¼����
	 * 
	 * @param mPassword����
	 * @param mPhone�ֻ���
	 * 
	 * @return��¼�������صĽ��
	 */
	public HashMap<String, Object> login(String mPhone, String mPassword) {
		String loginUrl = getLoginUrl(mPhone, mPassword);
		String result = HttpUtils.sendHttpRequest(loginUrl);
		if (result == null) {
			return null;
		}
		parseLoginData(result);
		return loginReturnMap;
	}

	/**
	 * ƴ�Ӳ���ȡ��¼������ӿ�
	 * 
	 * @param mPassword����
	 * @param mPhone�ֻ���
	 */
	private String getLoginUrl(String mPhone, String mPassword) {
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			String phone = crypt.encrypt(mPhone);
			String password = crypt.encrypt(mPassword);
			String url = Config.SERVER_URL + "?c=Login&a=login&phone=" + phone
					+ "&password=" + password + "&version=1.3";
			return url;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * �����ӵ�¼�ӿڻ�ȡ������
	 */
	protected void parseLoginData(String result) {
		loginReturnMap = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject respJson = jsonObject.getJSONObject("resp");
			// �����ظ�����
			int respCode = respJson.getInt("respCode");
			String respMsg = respJson.getString("respMsg");
			User newUser = new User();
			if (respCode == 1) {
				// ��������װ�û���Ϣ
				JSONObject dataJson = jsonObject.getJSONObject("data");
				newUser.setId(dataJson.getInt("id"));
				newUser.setPhone(dataJson.getString("phone"));
				newUser.setPassword(dataJson.getString("password"));
				newUser.setEmail(dataJson.getString("email"));
				newUser.setNickname(dataJson.getString("nickname"));
				newUser.setPhoto(dataJson.getString("photo"));
				newUser.setScore(dataJson.getString("score"));
				newUser.setRtime(dataJson.getString("rtime"));
				newUser.setLtime(dataJson.getString("ltime"));
				if (dataJson.getString("state").equals("1")) {
					newUser.setUseful(true);
				} else {
					newUser.setUseful(false);
				}
				newUser.setSex(dataJson.getString("sex"));
				newUser.setRoom_id(dataJson.getString("room_id"));
				newUser.setProvince(dataJson.getString("province"));
				newUser.setCity(dataJson.getString("city"));
				newUser.setAddress(dataJson.getString("address"));

				if (dataJson.getString("isqiandao").equals("1")) {
					newUser.setQiandao(true);
				} else {
					newUser.setQiandao(false);
				}
				newUser.setVersion(dataJson.getString("version"));
				newUser.setSid(Integer.parseInt(dataJson.getString("sid")));
			}
			// ��װ��������
			loginReturnMap.put("respCode", respCode);
			loginReturnMap.put("respMsg", respMsg);
			loginReturnMap.put("user", newUser);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �����û���Ϣ��sp��
	 */
	public void saveUserInfo(User newUser) {
		sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("isLogin", true);
		editor.putString("nickname", newUser.getNickname());
		editor.putString("score", newUser.getScore());
		editor.putString("phone", newUser.getPhone());
		editor.putString("sex", newUser.getSex());
		editor.putString("province", newUser.getProvince());
		editor.putString("city", newUser.getCity());
		editor.putString("address", newUser.getAddress());
		editor.putString("photo", newUser.getPhoto());
		editor.commit();
	}

	/**
	 * ����ע�����
	 * 
	 * @param mSidѧУid
	 * @param mPassword����
	 * @param mPhone�绰����
	 */
	public Map<String, Object> register(String mPhone, String mPassword,
			int mSid) {
		String address = getRegisterUrl(mPhone, mPassword, mSid);
		String result = HttpUtils.sendHttpRequest(address);
		if (result == null) {
			return null;
		}
		parseRegData(result);
		return regReturnMap;
	}

	/**
	 * ����ע��(�һ�����)�ӿڷ��ص�����
	 * 
	 * @param result
	 */
	private void parseRegData(String result) {
		regReturnMap = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject respJson = jsonObject.getJSONObject("resp");
			int respCode = Integer.parseInt(respJson.getString("respCode"));
			String respMsg = respJson.getString("respMsg");
			regReturnMap.put("respCode", respCode);
			regReturnMap.put("respMsg", respMsg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ƴ��ע��ӿ�����ĵ�ַ
	 * 
	 * @param mPhone�绰
	 * @param mPassword����
	 * @param mSid�û�����ѧУ��id
	 * @return
	 */
	private String getRegisterUrl(String mPhone, String mPassword, int mSid) {
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			String phone = crypt.encrypt(mPhone);
			String password = crypt.encrypt(mPassword);
			String url = Config.SERVER_URL + "?c=Register&a=register&phone="
					+ phone + "&password=" + password + "&sid" + mSid;
			return url;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * �޸�����
	 * 
	 * @param mPhone�ֻ���
	 * @param mPassword������
	 * @return�ӽӿ��н������������
	 */
	public HashMap<String, Object> editPassword(String mPhone, String mPassword) {
		String editPasswordUrl = getEditPasswordUrl(mPhone, mPassword);
		String result = HttpUtils.sendHttpRequest(editPasswordUrl);
		// ���������������Ϊ�գ���ֱ�ӷ���null
		if (result == null) {
			return null;
		}
		parseRegData(result);
		return regReturnMap;
	}

	/**
	 * ƴ���ַ����������޸�����Ľӿڵ�ַ
	 * 
	 * @param mPassword�ֻ���
	 * @param mPhone������
	 * 
	 * @return�޸�����ӿڵ�ַ
	 */
	private String getEditPasswordUrl(String mPhone, String mPassword) {
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			String phone = crypt.encrypt(mPhone);
			String url = Config.SERVER_URL + "?c=User&a=find_password&phone="
					+ phone + "&password=" + mPassword;
			return url;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ǩ��
	 * 
	 * @param phone
	 * @return
	 */
	public HashMap<String, Object> qiaodao(String phone) {
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			phone = crypt.encrypt(phone);
			String address = Config.SERVER_URL + "?c=User&a=qiandao&phone="
					+ phone;
			String result = HttpUtils.sendHttpRequest(address);
			if (!TextUtils.isEmpty(result)) {
				parseQiandaoData(result);
				return regReturnMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ����ǩ���ӿ�����
	 * 
	 * @param result
	 */
	private void parseQiandaoData(String result) {
		regReturnMap = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject respJson = jsonObject.getJSONObject("resp");
			int respCode = respJson.getInt("respCode");
			String respMsg = respJson.getString("respMsg");
			if (respCode == 1) {
				int score = jsonObject.getInt("data");
				regReturnMap.put("score", score);
			}
			regReturnMap.put("respCode", respCode);
			regReturnMap.put("respMsg", respMsg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �޸��ǳ�
	 * 
	 * @param phone
	 * @param nickname
	 * @return
	 */
	public HashMap<String, Object> editNickname(String phone, String nickname) {
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			phone = crypt.encrypt(phone);
			String address = Config.SERVER_URL
					+ "?c=User&a=write_nickname&phone=" + phone + "&nickname="
					+ URLEncoder.encode(nickname);
			String result = HttpUtils.sendHttpRequest(address);
			if (!TextUtils.isEmpty(result)) {
				parseRegData(result);
				return regReturnMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * �޸��Ա�
	 * 
	 * @param phone
	 * @param sex
	 * @return
	 */
	public HashMap<String, Object> editSex(String phone, String sex) {
		String address = Config.SERVER_URL + "?c=User&a=write_sex&sex="
				+ URLEncoder.encode(sex) + "&phone=" + URLEncoder.encode(phone);
		String result = HttpUtils.sendHttpRequest(address);
		if (!TextUtils.isEmpty(result)) {
			parseRegData(result);
			return regReturnMap;
		}
		return null;
	}

	/**
	 * �޸��û�ͷ��
	 * 
	 * @param phone
	 * @param path
	 * @return
	 */
	public Map<String, Object> writeImg(String phone, String path) {
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			phone = crypt.encrypt(phone);
			String address = Config.SERVER_URL + "?c=User&a=write_img&phone="
					+ phone + "&path=" + URLEncoder.encode(path);
			String result = HttpUtils.sendHttpRequest(address);
			if (!TextUtils.isEmpty(result)) {
				parseRegData(result);
				return regReturnMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * �޸�λ����Ϣ
	 * 
	 * @param province
	 * @param city
	 * @param address
	 * @param string
	 */
	public void editAddress(String phone, String province, String city,
			String address) {
		String url = Config.SERVER_URL + "?c=User&a=write_adress&phone="
				+ phone + "&province=" + URLEncoder.encode(province) + "&city="
				+ URLEncoder.encode(city) + "&address="
				+ URLEncoder.encode(address);
		HttpUtils.sendHttpRequest(url);
	}
}
