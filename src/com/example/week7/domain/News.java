package com.example.week7.domain;

/**
 * ����ʵ����
 * 
 * @author Administrator
 * 
 */
public class News {
	private String title;// ���ű���
	private String url;// ��������
	private String number;// ���ŵ����
	private String date;// ���ŷ�������

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public News(String title, String url, String number, String date) {
		super();
		this.title = title;
		this.url = url;
		this.number = number;
		this.date = date;
	}

}
