package com.example.week7.domain;

import java.io.Serializable;

/**
 * ʧ������ʵ����
 * 
 * @author Administrator
 * 
 */
public class Lostfound implements Serializable {
	private String id;// ����
	private String uid;// �û�id
	private String content;// ����
	private String time;// ����ʱ��
	private String img;// ͼ��
	private String tel;// ��ϵ�绰
	private String name;// ��ϵ������
	private String sid;// ѧУid
	private String photo;// ��������Ƭ
	private String phone;// �������˺�
	private String nickname;// �������ǳ�

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Lostfound(String id, String uid, String content, String time,
			String img, String tel, String name, String sid, String photo,
			String phone, String nickname) {
		super();
		this.id = id;
		this.uid = uid;
		this.content = content;
		this.time = time;
		this.img = img;
		this.tel = tel;
		this.name = name;
		this.sid = sid;
		this.photo = photo;
		this.phone = phone;
		this.nickname = nickname;
	}

}
