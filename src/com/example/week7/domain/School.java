package com.example.week7.domain;

/**
 * ѧУʵ����
 * 
 * @author Administrator
 * 
 */
public class School {
	private int id;// id
	private String name;// ѧУ����
	private String code;// ѧУ����
	private int sid;// ѧУ������������ѧУ����

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public School(int id, String name, String code, int sid) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.sid = sid;
	}

}
