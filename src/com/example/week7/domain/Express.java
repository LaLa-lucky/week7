package com.example.week7.domain;

import java.io.Serializable;

/**
 * ���ʵ����
 * 
 * @author Administrator
 * 
 */
public class Express implements Serializable {
	private int id;// id
	private int uid;// �û�id
	private int did;// ����Աid
	private String order_code;// �������
	private String name;// �µ�������
	private String phone;// �µ��˵绰
	private String address;// �µ�����ϸ��ַ
	private int aid;// �µ�����������
	private double money;// ���
	private String stime;// �����ύʱ��
	private String etime;// �������ʱ��
	private int state;// ����״̬
	private String company;// ��ݹ�˾
	private String getnum;// ȡ����
	private String arrivetime;// ����ʱ��
	private int paystyle;// ֧����ʽ
	private int sid;// ѧУid
	private String desc;// ��ע
	private String esttime;// Ԥ���ʹ�ʱ��
	private int large;// �����С
	private Deliveryman deliveryman;// ����Ա

	public Deliveryman getDeliveryman() {
		return deliveryman;
	}

	public void setDeliveryman(Deliveryman deliveryman) {
		this.deliveryman = deliveryman;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getGetnum() {
		return getnum;
	}

	public void setGetnum(String getnum) {
		this.getnum = getnum;
	}

	public String getArrivetime() {
		return arrivetime;
	}

	public void setArrivetime(String arrivetime) {
		this.arrivetime = arrivetime;
	}

	public int getPaystyle() {
		return paystyle;
	}

	public void setPaystyle(int paystyle) {
		this.paystyle = paystyle;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getEsttime() {
		return esttime;
	}

	public void setEsttime(String esttime) {
		this.esttime = esttime;
	}

	public int getLarge() {
		return large;
	}

	public void setLarge(int large) {
		this.large = large;
	}

}
