package com.example.week7.dao;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.example.week7.constant.Config;
import com.example.week7.domain.FromPark;
import com.example.week7.domain.Park;
import com.example.week7.domain.ParkComment;
import com.example.week7.domain.ParkImg;
import com.example.week7.http.HttpUtils;
import com.example.week7.utils.DesTools;

/**
 * �㳡��ҵ����
 * 
 * @author Administrator
 * 
 */
public class ParkDao {

	/**
	 * ��ȡ����΢����Ϣ
	 * 
	 * @param string
	 * 
	 * @param start
	 * @param count
	 * @return
	 */
	public ArrayList<Park> getPark(String phone, int start, int count) {
		String address = Config.SERVER_URL + "?c=Park&a=get_all_park&phone="
				+ phone + "&start=" + start + "&count=" + count;
		String result = HttpUtils.sendHttpRequest(address);
		if (!TextUtils.isEmpty(result)) {
			ArrayList<Park> parkList = parseParkData(result);
			return parkList;
		}
		return null;
	}

	/**
	 * ������΢���Ľӿ��л�ȡ������
	 * 
	 * @param result
	 * @return
	 */
	private ArrayList<Park> parseParkData(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONArray dataJson = jsonObject.getJSONArray("data");
			ArrayList<Park> parkList = new ArrayList<Park>();
			for (int i = 0; i < dataJson.length(); i++) {
				JSONObject jsonObject2 = dataJson.getJSONObject(i);
				String id = jsonObject2.getString("id");
				String user_id = jsonObject2.getString("user_id");
				String content = jsonObject2.getString("content");
				String addtime = jsonObject2.getString("addtime");
				String zhuanfa = jsonObject2.getString("zhuanfa");
				String comment_count = jsonObject2.getString("comment_count");
				String zan = jsonObject2.getString("zan");
				String page_view = jsonObject2.getString("page_view");
				String type = jsonObject2.getString("type");
				String pid = jsonObject2.getString("pid");
				String userphone = jsonObject2.getString("userphone");
				String userimg = jsonObject2.getString("userimg");
				int isZan = jsonObject2.getInt("isZan");
				String username = jsonObject2.getString("username");
				String sex = jsonObject2.getString("sex");
				String time = jsonObject2.getString("time");
				String level = jsonObject2.getString("level");
				JSONArray parkimgJson = jsonObject2.getJSONArray("parkimg");
				ArrayList<ParkImg> parkimgList = new ArrayList<ParkImg>();
				for (int j = 0; j < parkimgJson.length(); j++) {
					JSONObject jsonObject3 = parkimgJson.getJSONObject(j);
					String image_id = jsonObject3.getString("id");
					String park_id = jsonObject3.getString("park_id");
					String img_url = jsonObject3.getString("img_url");
					ParkImg parkimg = new ParkImg(image_id, park_id, img_url);
					parkimgList.add(parkimg);
				}
				FromPark from = null;
				if (type.equals("1")) {
					JSONObject fromJson = jsonObject2.getJSONObject("from");
					String zfcontent = fromJson.getString("content");
					String zfuserphone = fromJson.getString("userphone");
					String zfusername = fromJson.getString("username");
					from = new FromPark(zfcontent, zfuserphone, zfusername);
				}
				Park park = new Park(id, user_id, content, addtime, zhuanfa,
						comment_count, zan, page_view, type, pid, userphone,
						userimg, username, sex, time, level, isZan,
						parkimgList, from);
				parkList.add(park);
			}
			return parkList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * �����µ�΢��
	 * 
	 * @param phone�û���
	 * @param content����
	 * @param pathͼƬ·��
	 * @return
	 */
	public Map<String, Object> writePark(String phone, String content,
			String path) {
		DesTools desTools = new DesTools(Config.DES_KEY);
		try {
			phone = desTools.encrypt(phone);
			String address = Config.SERVER_URL + "?c=Park&a=write_park&phone="
					+ URLEncoder.encode(phone) + "&content="
					+ URLEncoder.encode(content);
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

	/**
	 * ת��΢��
	 * 
	 * @param phone
	 * @param pid
	 * @param content
	 * @return
	 */
	public Map<String, Object> zhuanfa(String phone, String pid, String content) {
		DesTools desTools = new DesTools(Config.DES_KEY);
		try {
			phone = desTools.encrypt(phone);
			String address = Config.SERVER_URL
					+ "?c=Park&a=write_zhuanfa_park&phone="
					+ URLEncoder.encode(phone) + "&pid="
					+ URLEncoder.encode(pid) + "&content="
					+ URLEncoder.encode(content);
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
	 * д����
	 * 
	 * @param phone
	 * @param pid
	 * @param content
	 * @return
	 */
	public Map<String, Object> writeComment(String phone, String pid,
			String content) {
		DesTools desTools = new DesTools(Config.DES_KEY);
		try {
			phone = desTools.encrypt(phone);
			String address = Config.SERVER_URL
					+ "?c=Park&a=write_comment&phone="
					+ URLEncoder.encode(phone) + "&pid="
					+ URLEncoder.encode(pid) + "&content="
					+ URLEncoder.encode(content);
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
	 * ����΢��id����ȡ�����б�
	 * 
	 * @param pid
	 * @param start
	 * @param count
	 * @return
	 */
	public ArrayList<ParkComment> getCommentList(String pid, int start,
			int count) {
		String address = Config.SERVER_URL + "?c=Park&a=get_comment_by_id&pid="
				+ pid + "&start=" + start + "&count=" + count;
		String result = HttpUtils.sendHttpRequest(address);
		if (!TextUtils.isEmpty(result)) {
			ArrayList<ParkComment> commentList = parseGetCommentData(result);
			return commentList;
		}
		return null;
	}

	/**
	 * ���������۽ӿڻ�ȡ������
	 * 
	 * @param result
	 * @return
	 */
	private ArrayList<ParkComment> parseGetCommentData(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONArray dataJson = jsonObject.getJSONArray("data");
			ArrayList<ParkComment> commentList = new ArrayList<ParkComment>();
			for (int i = 0; i < dataJson.length(); i++) {
				JSONObject jsonObject2 = dataJson.getJSONObject(i);
				String user_id = jsonObject2.getString("user_id");
				String park_id = jsonObject2.getString("park_id");
				String content = jsonObject2.getString("content");
				String addtime = jsonObject2.getString("addtime");
				String photo = jsonObject2.getString("photo");
				String nickname = jsonObject2.getString("nickname");
				ParkComment comment = new ParkComment(user_id, park_id,
						content, addtime, photo, nickname);
				commentList.add(comment);
			}
			return commentList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * д����
	 * 
	 * @param phone
	 * @param id
	 * @return
	 */
	public Map<String, Object> writeZan(String phone, String pid) {
		DesTools desTools = new DesTools(Config.DES_KEY);
		try {
			phone = desTools.encrypt(phone);
			String address = Config.SERVER_URL + "?c=Park&a=write_zan&phone="
					+ URLEncoder.encode(phone) + "&pid=" + pid;
			String result = HttpUtils.sendHttpRequest(address);
			if (!TextUtils.isEmpty(result)) {
				Map<String, Object> returnMap = parseData(result);
				return returnMap;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * ȡ����
	 * 
	 * @param phone
	 * @param id
	 * @return
	 */
	public Map<String, Object> cancelZan(String phone, String pid) {
		try {
			String address = Config.SERVER_URL + "?c=Park&a=cancel_zan&phone="
					+ URLEncoder.encode(phone) + "&pid=" + pid;
			String result = HttpUtils.sendHttpRequest(address);
			if (!TextUtils.isEmpty(result)) {
				Map<String, Object> returnMap = parseData(result);
				return returnMap;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
