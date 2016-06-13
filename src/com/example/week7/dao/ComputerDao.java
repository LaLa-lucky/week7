package com.example.week7.dao;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.example.week7.constant.Config;
import com.example.week7.http.HttpUtils;
import com.example.week7.utils.DesTools;

/**
 * ����ά��ҵ���
 * 
 * @author Administrator
 * 
 */
public class ComputerDao {

	/**
	 * д�����ά����Ϣ
	 * 
	 * @param phone�û��˻�
	 * @param telҪά���˵绰
	 * @param nameҪά��������
	 * @param roomid���Һ�
	 * @param remark���������������Ϣ
	 * @return
	 */
	public Map<String, Object> write(String phone, String tel, String name,
			String roomid, String remark) {
		DesTools desTools = new DesTools(Config.DES_KEY);
		try {
			String address = Config.SERVER_URL
					+ "?c=Computer&a=write_computer&phone="
					+ desTools.encrypt(phone) + "&tel="
					+ URLEncoder.encode(tel) + "&name="
					+ URLEncoder.encode(name) + "&roomid="
					+ URLEncoder.encode(roomid) + "&desc="
					+ URLEncoder.encode(remark);
			String result = HttpUtils.sendHttpRequest(address);
			if (!TextUtils.isEmpty(result)) {
				Map<String, Object> returnMap = parseData(result);
				return returnMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��������
	 * 
	 * @param result
	 * @return
	 */
	private Map<String, Object> parseData(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject respJson = jsonObject.getJSONObject("resp");
			int respCode = respJson.getInt("respCode");
			String respMsg = respJson.getString("respMsg");
			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("respCode", respCode);
			returnMap.put("respMsg", respMsg);
			return returnMap;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
