package com.example.week7.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.week7.activity.RegisterActivity;
import com.example.week7.constant.Config;
import com.example.week7.domain.School;
import com.example.week7.http.HttpUtils;
import com.example.week7.utils.DesTools;

/**
 * ѧУҵ���
 * 
 * @author Administrator
 * 
 */
public class SchoolDao {

	private Context context;// ������
	private Map<String, Object> returnMap = new HashMap<String, Object>();// �����������

	public SchoolDao(Context context) {
		this.context = context;
	}

	/**
	 * ��ȡѧУ
	 * 
	 * @return�������з��ص�����
	 */
	public Map<String, Object> getSchool() {
		String address = Config.SERVER_URL + "?c=Login&a=get_school";
		String result = HttpUtils.sendHttpRequest(address);
		if (result == null) {
			return null;
		}
		parseData(result);
		return returnMap;
	}

	/**
	 * �����������ϻ�ȡ��ѧУ����
	 * 
	 * @param resultJson����
	 */
	protected void parseData(String result) {
		List<School> schoolList = new ArrayList<School>();
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject respJson = jsonObject.getJSONObject("resp");
			int respCode = Integer.parseInt(respJson.getString("respCode"));
			String respMsg = respJson.getString("respMsg");
			JSONArray dataJson = jsonObject.getJSONArray("data");
			for (int i = 0; i < dataJson.length(); i++) {
				JSONObject jsonObject2 = dataJson.getJSONObject(i);
				int id = Integer.parseInt(jsonObject2.getString("id"));
				String name = jsonObject2.getString("name");
				String code = jsonObject2.getString("code");
				int sid = Integer.parseInt(jsonObject2.getString("sid"));
				School school = new School(id, name, code, sid);
				schoolList.add(school);
			}
			returnMap.put("respCode", respCode);
			returnMap.put("respMsg", respMsg);
			returnMap.put("schoolList", schoolList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ͨ���ֻ�������ѯ��ѧУ��������������
	 * 
	 * @param phone
	 * @return
	 */
	public Map<String, Object> getArea(String phone) {
		// http://www.e8net.cn/Week7/and10381.php?c=Express&a=get_roomid_area&phone=Cs9SN0vGFVc65d3HTUFczg==
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			phone = crypt.encrypt(phone);
			String address = Config.SERVER_URL
					+ "?c=Express&a=get_roomid_area&phone=" + phone;
			String result = HttpUtils.sendHttpRequest(address);
			if (TextUtils.isEmpty(result)) {
				return null;
			}
			parseData(result);
			return returnMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
