package com.example.week7.domain;

import java.util.ArrayList;

/**
 * ΢��ʵ����
 * 
 * @author Administrator
 * 
 */
public class Park {
	private String id; // ���ݿ�����
	private String user_id;// �û�id
	private String content;// ����
	private String addtime;// ʱ��
	private String zhuanfa;// ת����
	private String comment_count;// ������
	private String zan;// �޵�����
	private String page_view;// �����
	private String type;// ����
	private String pid;// ת��ԭ����΢��id
	private String userphone;// �����˵绰
	private String userimg;// ������ͷ��
	private String username;// �������ǳ�
	private String sex;// �������Ա�
	private String time;// ������ʱ��
	private String level;// �����˵ȼ�
	private int isZan;// �Ƿ�����

	public Park(String id, String user_id, String content, String addtime,
			String zhuanfa, String comment_count, String zan, String page_view,
			String type, String pid, String userphone, String userimg,
			String username, String sex, String time, String level,
			int isZan, ArrayList<ParkImg> parkimg, FromPark from) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.content = content;
		this.addtime = addtime;
		this.zhuanfa = zhuanfa;
		this.comment_count = comment_count;
		this.zan = zan;
		this.page_view = page_view;
		this.type = type;
		this.pid = pid;
		this.userphone = userphone;
		this.userimg = userimg;
		this.username = username;
		this.sex = sex;
		this.time = time;
		this.level = level;
		this.isZan = isZan;
		this.parkimg = parkimg;
		this.from = from;
	}

	public int getIsZan() {
		return isZan;
	}

	public void setIsZan(int isZan) {
		this.isZan = isZan;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	private ArrayList<ParkImg> parkimg;// ����ͼƬ
	private FromPark from;// ԭ��΢������

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getZhuanfa() {
		return zhuanfa;
	}

	public void setZhuanfa(String zhuanfa) {
		this.zhuanfa = zhuanfa;
	}

	public String getComment_count() {
		return comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

	public String getZan() {
		return zan;
	}

	public void setZan(String zan) {
		this.zan = zan;
	}

	public String getPage_view() {
		return page_view;
	}

	public void setPage_view(String page_view) {
		this.page_view = page_view;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getUserphone() {
		return userphone;
	}

	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}

	public String getUserimg() {
		return userimg;
	}

	public void setUserimg(String userimg) {
		this.userimg = userimg;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public ArrayList<ParkImg> getParkimg() {
		return parkimg;
	}

	public void setParkimg(ArrayList<ParkImg> parkimg) {
		this.parkimg = parkimg;
	}

	public FromPark getFrom() {
		return from;
	}

	public void setFrom(FromPark from) {
		this.from = from;
	}

}
