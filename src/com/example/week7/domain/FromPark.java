package com.example.week7.domain;

/**
 * ԭ��΢������
 * 
 * @author Administrator
 * 
 */
public class FromPark {

	public FromPark(String content, String userphone, String username) {
		super();
		this.content = content;
		this.userphone = userphone;
		this.username = username;
	}

	private String content;// ��ת����΢������
	private String userphone;// ��ת����΢���û��绰
	private String username;// ��ת����΢���û�����

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserphone() {
		return userphone;
	}

	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
